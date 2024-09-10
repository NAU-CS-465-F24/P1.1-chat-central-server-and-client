package chat;


import java.io.*;
import java.net.*;
import java.util.*;

import utils.*;


public class ChatServer implements Runnable {
    // initialize variables
    public static List<NodeInfo> participants = new ArrayList<>();
    int port = 0;
    ServerSocket serverSocket = null;

    // constructor
    public ChatServer(String propertiesFile) {
        Properties properties = null;

        try {
            // initialize a properties object from the file
            properties = new Properties();
            properties.load(new FileReader(propertiesFile));
        } catch (IOException e) {
            System.err.println("Cannot open properties file");
            System.exit(1);
        }

        try {
            // get the port from the properties file
            port = Integer.parseInt(properties.getProperty("PORT"));
        } catch (NumberFormatException e) {
            System.err.println("Cannot read port");
            System.exit(1);
        }
    }


    public static void main(String[] args) {
        // initialize variables
        String propertiesFile = null;

        try {
            // get the properties file from the args
            propertiesFile = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            // set the default properties file
            propertiesFile = "config/server.properties";
        }

        // start the server
        (new ChatServer(propertiesFile)).run();
    }


    @Override
    public void run() {
        try {
            // create a new server socket from the port
            serverSocket = new ServerSocket(port);
            System.out.println("ChatServer listening on " + NetworkUtilities.getMyIP() + ":" + port);
        } catch (IOException e) {
            System.err.println("Cannot open server socket");
            System.exit(1);
        }

        while (true) {
            try {
                // start the server worker
                (new ChatServerWorker(serverSocket.accept())).start();
            } catch (IOException e) {
                System.out.println("[ChatServer].run Warning: Error accepting client");
            }
        }
    }
}