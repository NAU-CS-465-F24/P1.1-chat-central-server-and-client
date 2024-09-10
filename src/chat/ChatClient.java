package chat;


import java.io.*;
import java.net.*;
import java.util.*;

import utils.*;


public class ChatClient implements Runnable {
    // initialize variables
    public static NodeInfo myNodeInfo = null;
    public static NodeInfo serverNodeInfo = null;
    static Receiver receiver = null;
    static Sender sender = null;

    // constructor
    public ChatClient(String propertiesFile) {
        // initialize variables
        Properties properties = null;

        try {
            // initialize a properties object from the file
            properties = new Properties();
            properties.load(new FileReader(propertiesFile));
        } catch (IOException ex) {
            System.out.println("Could not open properties file");
            System.exit(1);
        }

        int myPort = 0;
        try {
            // get the client port from the properties file
            myPort = Integer.parseInt(properties.getProperty("MY_PORT"));
        } catch (NumberFormatException ex) {
            System.out.println("Could not read receiver port");
            System.exit(1);
        }

        // get the client name from the properties file
        String myName = properties.getProperty("MY_NAME");
        if (myName == null) {
            System.out.println("Could not read my name");
            System.exit(1);
        }

        // create the client node info from the IP, port, and name
        myNodeInfo = new NodeInfo(NetworkUtilities.getMyIP(), myPort, myName);

        int serverPort = 0;
        try {
            // get the server port from the properties file
            serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        } catch (NumberFormatException ex) {
            System.out.println("Could not read receiver port");
        }

        // get the server address from the properties file
        String serverIP = properties.getProperty("SERVER_IP");
        if (serverIP == null) {
            System.out.println("Could not read receiver port");
        }

        // ensure the server port and IP exist
        if (serverPort != 0 && serverIP != null) {
            // create the server node info from the ip and port
            serverNodeInfo = new NodeInfo(serverIP, serverPort);
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
            propertiesFile = "config/client.properties";
        }

        // start the client
        (new ChatClient(propertiesFile)).run();
    }


    @Override
    public void run() {
        // start the receiver and sender
        (receiver = new Receiver()).start();
        (sender = new Sender()).start();
    }
}
