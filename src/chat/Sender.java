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
import message.*;

public class Sender extends Thread implements MessageTypes {
    Socket serverConnection = null;
    Scanner userInput = new Scanner(System.in);
    String inputLine = null;
    boolean hasJoined;

    //constructor
    public Sender() {
        userInput = new Scanner(System.in);
        hasJoined = false;
    }

    @Override
    public void run() {
        ObjectOutputStream writeToNet;
        ObjectInputStream readFromNet;

        while (true) {
            inputLine = userInput.nextLine();

            if (inputLine.startsWith("JOIN")) {
                if (hasJoined == true) {
                    System.err.println("You have already joined a chat...");
                    continue;
                }

                String[] connectivityInfo = inputLine.split("[ ]+");

                try {
                    ChatClient.serverNodeInfo = new NodeInfo(connectivityInfo[1], Integer.parseInt(connectivityInfo[2]));
                } catch (ArrayIndexOutOfBoundsException ex) {
                }

                if (ChatClient.serverNodeInfo == null) {
                    System.err.println("[Sender].run no server connectivity information provided!");
                    continue;
                }

                try {
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Message(JOIN, ChatClient.myNodeInfo));

                    serverConnection.close();
                } catch (IOException ex) {
                    System.err.println("Error connecting to server.");
                    continue;
                }

                hasJoined = true;
                System.out.println("Joined chat ...");

            } else if (inputLine.startsWith("LEAVE")) {
                if (hasJoined == false) {
                    System.err.println("You have not joined a chat yet...");
                    continue;
                }

                try {
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Message(LEAVE, ChatClient.myNodeInfo));

                    serverConnection.close();
                } catch (IOException ex) {
                    System.err.println("Error connecting to the server or closing the connection.");
                    continue;
                }

                hasJoined = false;

                System.out.println("Left chat ...");

            } else if (inputLine.startsWith("SHUTDOWN ALL")) {
                if (hasJoined == false) {
                    System.err.println("To shut down the whole chat, you need to first join it...");
                    continue;
                }

                try {
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Message(SHUTDOWN_ALL, ChatClient.myNodeInfo));

                    serverConnection.close();
                } catch (IOException ex) {
                    System.err.println("Error opening the connection or writing the object.");
                }

                System.out.println("Sent shutdown all request...\n");

            } else if (inputLine.startsWith("SHUTDOWN")) {
                if (hasJoined == true) {
                    try {
                        serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                        readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                        writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                        writeToNet.writeObject(new Message(SHUTDOWN, ChatClient.myNodeInfo));

                        serverConnection.close();

                        System.out.println("Left chat...");
                    } catch (IOException ex) {
                        System.err.println("Error opening the connection ");
                    }
                }

                System.out.println("Exiting...\n");
                System.exit(0);

            } else {

                if (hasJoined == false) {
                    System.err.println("you need to join the chat first");
                    continue;
                }

                try {
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());

                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Message(NOTE, ChatClient.myNodeInfo.getName() + ": " + inputLine));

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