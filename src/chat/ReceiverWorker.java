package chat;


import java.io.*;
import java.net.*;
import java.util.*;

import message.*;
import utils.*;

import static message.MessageTypes.NOTE;
import static message.MessageTypes.SHUTDOWN;


public class ReceiverWorker extends Thread {
    // initialize variables
    Socket serverConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    Message message = null;


    // constructor
    public ReceiverWorker(Socket serverConnection) {
        this.serverConnection = serverConnection;

        try {
            // create I/O streams from the server connection
            readFromNet = new ObjectInputStream(serverConnection.getInputStream());
            writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
        } catch (IOException e) {
            System.err.println("[ReceiverWorker.java] failed creating worker.");
        }

    }

    
    @Override
    public void run() {
        try {
            // read a message from the input stream
            message = (Message) readFromNet.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("[ReceiverWorker.run] failed.");
            System.exit(1);
        }

        switch (message.getType()) {
            case SHUTDOWN:
                // print the SHUTDOWN message
                System.out.println("Received Shutdown message from server, exiting");

                try {
                    // close the connection to the server
                    serverConnection.close();
                } catch (IOException e) {
                    System.out.println("[ReceiverWorker.run] Could not close the connection.");
                }

                // exit the client
                System.exit(0);
                break;

            case NOTE:
                // print the NOTE
                System.out.println((String) message.getContent());

                try {
                    // close the connection to the server
                    serverConnection.close();
                } catch (IOException e) {
                    System.out.println("[ReceiverWorker.run] Could not close the connection.");
                }

                break;

            default:
                // no default case
        }
    }
}

