/**
 * Chat Client Class
 * <p>
 * reads configuration information and starts up the ChatClient
 *
 * @author wolfdieterotte
 */

package chat;

import java.io.*;
import java.net.*;
import java.util.*;

import utils.*;

public class ChatClient implements Runnable {
    public static NodeInfo myNodeInfo = null;
    public static NodeInfo serverNodeInfo = null;
    static Receiver receiver = null;
    static Sender sender = null;

    public ChatClient(String propertiesFile) {
        Properties properties = null;

        try {
            properties = new Properties();
            properties.load(new FileReader(propertiesFile));
        } catch (IOException ex) {
            System.out.println("Could not open properties file");
            System.exit(1);
        }

        int myPort = 0;
        try {
            myPort = Integer.parseInt(properties.getProperty("MY_PORT"));
        } catch (NumberFormatException ex) {
            System.out.println("Could not read receiver port");
            System.exit(1);
        }

        String myName = properties.getProperty("MY_NAME");
        if (myName == null) {
            System.out.println("Could not read my name");
            System.exit(1);

        }

        myNodeInfo = new NodeInfo(NetworkUtilities.getMyIP(), myPort, myName);

        int serverPort = 0;
        try {
            serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        } catch (NumberFormatException ex) {
            System.out.println("Could not read receiver port");

        }

        String serverIP = null;
        serverIP = properties.getProperty("SERVER_IP");
        if (serverIP == null) {
            System.out.println("Could not read receiver port");

        }

        if (serverPort != 0 && serverIP != null) {
            serverNodeInfo = new NodeInfo(serverIP, serverPort);
        }
    }

    public static void main(String[] args) {
        String propertiesFile = null;

        try {
            propertiesFile = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            propertiesFile = "config/client.properties";
        }

        (new ChatClient(propertiesFile)).run();
    }

    @Override
    public void run() {
        (receiver = new Receiver()).start();
        (sender = new Sender()).start();
    }

}