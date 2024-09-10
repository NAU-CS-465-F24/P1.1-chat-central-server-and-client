package chat;


import java.io.*;
import java.net.*;
import java.util.*;

import utils.*;
import message.*;


public class Sender extends Thread implements MessageTypes {
    // initialize variables
    Socket serverConnection = null;
    Scanner userInput = new Scanner(System.in);
    String inputLine = null;
    boolean hasJoined;


    // constructor
    public Sender() {
        // start to read user input
        userInput = new Scanner(System.in);

        // set the bool so the client doesn't think it has joined a server
        hasJoined = false;
    }


    @Override
    public void run() {
        // initialize variables
        ObjectOutputStream writeToNet;
        ObjectInputStream readFromNet;

        while (true) {
            // read the users input
            inputLine = userInput.nextLine();

            // JOIN
            if (inputLine.startsWith("JOIN")) {
                // check client join status
                if (hasJoined == true) {
                    System.err.println("You have already joined a chat...");
                    continue;
                }

                // instantiate the connectivity info of the client
                String[] connectivityInfo = inputLine.split("[ ]+");

                // try to initialize the server info
                try {
                    ChatClient.serverNodeInfo = new NodeInfo(connectivityInfo[1], Integer.parseInt(connectivityInfo[2]));
                } catch (ArrayIndexOutOfBoundsException ex) {
                }

                // check if the server info exists
                if (ChatClient.serverNodeInfo == null) {
                    System.err.println("[Sender].run no server connectivity information provided!");
                    continue;
                }

                try {
                    // create a connection to the server
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                    // create I/O streams from the server connection
                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    // write the JOIN message object to the output stream
                    writeToNet.writeObject(new Message(JOIN, ChatClient.myNodeInfo));

                    // close the connection
                    serverConnection.close();
                } catch (IOException ex) {
                    System.err.println("Error connecting to server.");
                    continue;
                }

                // set the bool so the client knows it has joined
                hasJoined = true;
                System.out.println("Joined chat ...");
            }

            // LEAVE
            else if (inputLine.startsWith("LEAVE")) {
                // check client join status
                if (hasJoined == false) {
                    System.err.println("You have not joined a chat yet...");
                    continue;
                }

                try {
                    // create a connection to the server
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                    // create I/O streams from the server connection
                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    // write the LEAVE message object to the output stream
                    writeToNet.writeObject(new Message(LEAVE, ChatClient.myNodeInfo));

                    // close the connection
                    serverConnection.close();
                } catch (IOException ex) {
                    System.err.println("Error connecting to the server or closing the connection.");
                    continue;
                }

                // set the bool so the client knows it has left
                hasJoined = false;
                System.out.println("Left chat ...");
            }

            // SHUTDOWN_ALL
            else if (inputLine.startsWith("SHUTDOWN_ALL")) {
                // check client join status
                if (hasJoined == false) {
                    System.err.println("To shut down the whole chat, you need to first join it...");
                    continue;
                }

                try {
                    // create a connection to the server
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                    // create I/O streams from the server connection
                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    // write the SHUTDOWN_ALL message object to the output stream
                    writeToNet.writeObject(new Message(SHUTDOWN_ALL, ChatClient.myNodeInfo));

                    // close the connection
                    serverConnection.close();
                } catch (IOException ex) {
                    System.err.println("Error opening the connection or writing the object.");
                }

                System.out.println("Sent shutdown all request...\n");
                System.out.println("Exiting...\n");
                System.exit(0);
            }

            // SHUTDOWN
            else if (inputLine.startsWith("SHUTDOWN")) {
                if (hasJoined == true) {
                    try {
                        // create a connection to the server
                        serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                        // create I/O streams from the server connection
                        readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                        writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                        // write the SHUTDOWN message object to the output stream
                        writeToNet.writeObject(new Message(SHUTDOWN, ChatClient.myNodeInfo));

                        // close the connection
                        serverConnection.close();
                        System.out.println("Left chat...");
                    } catch (IOException ex) {
                        System.err.println("Error opening the connection ");
                    }
                }

                // exit the client
                System.out.println("Exiting...\n");
                System.exit(0);
            }

            // NOTE
            else {
                // check client join status
                if (hasJoined == false) {
                    System.err.println("you need to join the chat first");
                    continue;
                }

                try {
                    // create a connection to the server
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                    // create I/O streams from the server connection
                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    // write the NOTE message object to the output stream, including the client name and user input
                    writeToNet.writeObject(new Message(NOTE, ChatClient.myNodeInfo.getName() + ": " + inputLine));

                    // close the connection
                    serverConnection.close();
                    System.out.println("Message sent...");
                } catch (IOException ex) {
                    System.out.println("Sending message failed!");
                    continue;
                }
            }
        }
    }
}