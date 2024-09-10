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

public class Receiver extends Thread {
    static ServerSocket receiverSocket = null;
    static String userName = null;

    public Receiver() {
        try {
            receiverSocket = new ServerSocket(ChatClient.myNodeInfo.getPort());
            System.out.println("[Receiver.Receiver] receiver socket created, listening on port");
        } catch (IOException e) {
            System.out.println("Creating receiver socket failed");
        }

        System.out.println(ChatClient.myNodeInfo.getName() + "listening on" + ChatClient.myNodeInfo.getAddress() +
                ChatClient.myNodeInfo.getPort());
    }

    @Override
    public void run() {
        while (true) {
            try {
                (new ReceiverWorker(receiverSocket.accept())).start();
            } catch (IOException e) {
                System.err.println("[Receiver.run] Warning: Error accepting client");
            }

        }
    }
}