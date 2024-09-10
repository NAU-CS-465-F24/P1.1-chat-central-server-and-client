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

import message.*;
import utils.*;

import static message.MessageTypes.NOTE;
import static message.MessageTypes.SHUTDOWN;

public class ReceiverWorker extends Thread {

    Socket serverConnection = null;

    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;

    Message message = null;

    public ReceiverWorker(Socket serverConnection) {
        this.serverConnection = serverConnection;

        try {
            readFromNet = new ObjectInputStream(serverConnection.getInputStream());
            writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
        } catch (IOException e) {
            System.err.println("[ReceiverWorker.java] failed creating worker.");
        }

    }

    @Override
    public void run() {
        try {
            message = (Message) readFromNet.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("[ReceiverWorker.run] run failed.");
            System.exit(1);
        }

        switch (message.getType()) {
            case SHUTDOWN:

                System.out.println("Received Shutdown message from server, exiting");

                try {
                    serverConnection.close();
                } catch (IOException e) {

                }

                System.exit(0);

                break;

            case NOTE:

                System.out.println((String) message.getContent());

                try {
                    serverConnection.close();
                } catch (IOException e) {
                    System.out.println("[ReceiverWorker.run] Could not close the connection.");
                }

                break;

            default:
        }
    }
}

