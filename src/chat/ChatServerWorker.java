package chat;


import java.io.*;
import java.net.*;
import java.util.*;

import message.*;


public class ChatServerWorker extends Thread implements MessageTypes {
    // initialize variables
    Socket chatConnection = null;
    ObjectOutputStream writeToNet = null;
    ObjectInputStream readFromNet = null;
    Message message = null;


    // constructor
    public ChatServerWorker(Socket chatConnection) {
        this.chatConnection = chatConnection;
    }


    @Override
    public void run() {
        // initialize variables
        NodeInfo participantInfo = null;
        Iterator<NodeInfo> participantsIterator;

        try {
            // create I/O streams from the chat connection
            writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
            readFromNet = new ObjectInputStream(chatConnection.getInputStream());

            // read a message from the input stream
            message = (Message) readFromNet.readObject();

            // close the connection
            chatConnection.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ChatServerWorker.run] Failed to open object stream or message could not be read.");
            System.exit(1);
        }

        switch (message.getType()) {
            // JOIN
            case JOIN:
                // get the joining clients info
                NodeInfo joiningParticipantNodeInfo = (NodeInfo) message.getContent();

                // add the info to the list of current participants
                ChatServer.participants.add(joiningParticipantNodeInfo);

                // print the joined message and current participants
                System.out.print(joiningParticipantNodeInfo.getName() + " joined. All current participants: ");
                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext()) {
                    participantInfo = participantsIterator.next();
                    System.out.print(participantInfo.name + " ");
                }
                System.out.println();

                break;

            // LEAVE / SHUTDOWN
            case LEAVE:
            case SHUTDOWN:
                // get the leaving clients info
                NodeInfo leavingParticipantInfo = (NodeInfo) message.getContent();

                // check if the leaving client is actually a current participant
                if (ChatServer.participants.remove(leavingParticipantInfo)) {
                    System.err.println(leavingParticipantInfo.getName() + " removed");
                } else {
                    System.err.println(leavingParticipantInfo.getName() + " not found");
                }

                // print the left message and remaining participants
                System.out.print(leavingParticipantInfo.getName() + " left. Remaining participants: ");
                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext()) {
                    participantInfo = participantsIterator.next();
                    System.out.print(participantInfo.name + " ");
                }
                System.out.println();

                break;

            // SHUTDOWN_ALL
            case SHUTDOWN_ALL:
                // iterate through all current participants
                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext()) {
                    participantInfo = participantsIterator.next();

                    try {
                        // open a connection to the client
                        chatConnection = new Socket(participantInfo.address, participantInfo.port);

                        // create I/O streams from the client connection
                        writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
                        readFromNet = new ObjectInputStream(chatConnection.getInputStream());

                        // write the SHUTDOWN message object to the output stream
                        writeToNet.writeObject(new Message(SHUTDOWN, null));

                        // close the connection
                        chatConnection.close();
                    } catch (IOException e) {
                        System.err.println("[ChatServerWorker].run Could not open socket to client or send a message");
                    }
                }

                // print the shutdown all message and exit the server
                System.out.println("Shut down all clients, exiting...");
                System.exit(0);

                // NOTE
            case NOTE:
                // print the NOTE message
                System.out.println((String) message.getContent());

                // iterate through all current participants
                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext()) {
                    participantInfo = participantsIterator.next();

                    try {
                        // open a connection to the client
                        chatConnection = new Socket(participantInfo.address, participantInfo.port);

                        // create I/O streams from the client connection
                        writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
                        readFromNet = new ObjectInputStream(chatConnection.getInputStream());

                        // write the NOTE message to the output stream
                        writeToNet.writeObject(message);

                        // close the connection
                        chatConnection.close();
                    } catch (IOException e) {
                        System.err.println("[ChatServerWorker].run Could not open socket to client or send a message");
                    }
                }

                break;

            default:
                // no default case
        }
    }
}