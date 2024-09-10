package chat;


import java.io.*;
import java.net.*;
import java.util.*;

import utils.*;


public class Receiver extends Thread {
    // initialize variables
    static ServerSocket receiverSocket = null;
    static String userName = null;


    // constructor
    public Receiver() {
        try {
            // create a socket for the server to connect to
            receiverSocket = new ServerSocket(ChatClient.myNodeInfo.getPort());
            System.out.println("[Receiver.Receiver] receiver socket created, listening on port");
        } catch (IOException e) {
            System.out.println("Creating receiver socket failed");
        }

        // print the listening message and the client info
        System.out.println(ChatClient.myNodeInfo.getName() + " listening on " + ChatClient.myNodeInfo.getAddress() + ":" +
                ChatClient.myNodeInfo.getPort());
    }


    @Override
    public void run() {
        while (true) {
            try {
                // accept any new connections to the socket and start the worker
                (new ReceiverWorker(receiverSocket.accept())).start();
            } catch (IOException e) {
                System.err.println("[Receiver.run] Warning: Error accepting client");
            }
        }
    }
}