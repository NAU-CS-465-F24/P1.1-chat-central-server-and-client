package chat;

import java.io.*;
import java.net.*;
import java.util.*;

import utils.*;


public class ChatServer implements Runnable {
    public static List<NodeInfo> participants = new ArrayList<>();

    int port = 0;
    ServerSocket serverSocket = null;

    public ChatServer(String propertiesFile) {
        Properties properties = null;

        try {
            properties = new Properties();
            properties.load(new FileReader(propertiesFile));
        } catch (IOException e) {
            System.err.println("Cannot open properties file");
            System.exit(1);
        }

        try {
            port = Integer.parseInt(properties.getProperty("PORT"));
        } catch (NumberFormatException e) {
            System.err.println("Cannot read port");
            System.exit(1);
        }
    }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("ChatServer listening on " + NetworkUtilities.getMyIP() + ":" + port);
            } catch (IOException e) {
                System.err.println("Cannot open server socket");
                System.exit(1);
            }

            while (true) {
                try {
                    (new ChatServerWorker(serverSocket.accept())).start();
                } catch (IOException e) {
                    System.out.println("[ChatServer].run Warning: Error accepting client");
                }
            }
        }

        public static void main(String[] args) {
            String propertiesFile = null;

            try {
                propertiesFile = args[0];
            } catch (ArrayIndexOutOfBoundsException e) {
                propertiesFile = "config/server.properties";
            }

            (new ChatServer(propertiesFile)).run();
        }
    }