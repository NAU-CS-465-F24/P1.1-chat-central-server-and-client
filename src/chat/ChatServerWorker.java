package chat;

import java.io.*;
import java.net.*;
import java.util.*;

import message.*;

public class ChatServerWorker extends Thread implements MessageTypes {
    Socket chatConnection = null;

    ObjectOutputStream writeToNet = null;
    ObjectInputStream readFromNet = null;

    Message message = null;

    public ChatServerWorker(Socket chatConnection) {
        this.chatConnection = chatConnection;
    }

    @Override
    public void run() {
        NodeInfo participantInfo = null;

        Iterator<NodeInfo> participantsIterator;

        try {
            writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
            readFromNet = new ObjectInputStream(chatConnection.getInputStream());

            message = (Message) readFromNet.readObject();

            chatConnection.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ChatServerWorker.run] Failed to open object stream or message could not be read.");
            System.exit(1);
        }

        switch (message.getType()) {
            case JOIN:
                NodeInfo joiningParticipantNodeInfo = (NodeInfo) message.getContent();

                ChatServer.participants.add(joiningParticipantNodeInfo);

                System.out.print(joiningParticipantNodeInfo.getName() + " joined. All current participants: ");

                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext()) {
                    participantInfo = participantsIterator.next();
                    System.out.print(participantInfo.name + " ");
                }

                System.out.println();

                break;

            case LEAVE:
            case SHUTDOWN:
                NodeInfo leavingParticipantInfo = (NodeInfo) message.getContent();
                if (ChatServer.participants.remove(leavingParticipantInfo)) {
                    System.err.println(leavingParticipantInfo.getName() + "removed");
                } else {
                    System.err.println(leavingParticipantInfo.getName() + "not found");
                }

                System.out.print(leavingParticipantInfo.getName() + "left. Remaining participants: ");

                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext()) {
                    participantInfo = participantsIterator.next();
                    System.out.print(participantInfo.name + " ");
                }

                System.out.println();

                break;

            case SHUTDOWN_ALL:
                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext()) {
                    participantInfo = participantsIterator.next();

                    try {
                        chatConnection = new Socket(participantInfo.address, participantInfo.port);

                        writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
                        readFromNet = new ObjectInputStream(chatConnection.getInputStream());

                        writeToNet.writeObject(new Message(SHUTDOWN, null));

                        chatConnection.close();
                    } catch (IOException e) {
                        System.err.println("[ChatServerWorker].run Could not open socket to client or send a message");
                    }
                }

                System.out.println("Shut down all clients, exiting...");

                System.exit(0);

            case NOTE:
                System.out.println((String) message.getContent());

                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext()) {
                    participantInfo = participantsIterator.next();

                    try {
                        chatConnection = new Socket(participantInfo.address, participantInfo.port);

                        writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
                        readFromNet = new ObjectInputStream(chatConnection.getInputStream());

                        writeToNet.writeObject(message);

                        chatConnection.close();
                    } catch (IOException e) {
                        System.err.println("[ChatServerWorker].run Could not open socket to client or send a message");
                    }
                }

                break;

            default:

        }
    }
}