/** 
 *Chat Client Class

 reads configuration information and starts up the ChatClient

 @author wolfdieterotte
 */

 package chat;

 import java.io.*;
 import java.net.*;
 import java.util.*;
 
 import utils.*;
 import message.*;

 public class Sender extends Thread implements MessageTypes
 {
    Socket serverConnection = null;
    Scanner userInput = new Scanner(System.in);
    String inputLine = null;
    Boolean hasJoined; 

    //constructor
    public Sender()
    {
        userInput = new Scanner(System.in);
        hasJoined = false;
    }

    @Override
    public void run()
    {
        ObjectOutputStream writeToNet;
        ObjectInputStream readFromNet;

        while (true)
        {
            inputLine = userInput.nextLine();

            if (inputLine.startsWith("JOIN"))
            {

                if (hasJoined == true)
                {
                    System.err.println("You have already joined a chat...");
                    continue;
                }

                String[] connectivityInfo = inputLine.split("[]+");

            
                try
                {
                    ChatClient.serverNodeInfo = new NodeInfo(connectivityInfo[1], Integer.parseInt(connectivityInfo[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    continue;

                }

                if (ChatClient.serverNodeInfo == null)
                {
                    System.err.println("[Sender].run no server connectivity information provided!");
                    continue;
                }
                try
                {
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.ServerNodeInfo.getPort());

                    readFromNet = new ObjectINputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Meassage(LEAVE, ChatClient.myNodeInfo));

                    serverConnection.close();
                }
                catch(IOException ex)
                {
                    continue;

                }

                hasJoined = true;

                System.out.println("Joined chat ...");
            }

            else if (inputLine.startsWith("LEAVE"))
            {
                if (hasJoined == false)
                {
                    System.err.println("You have not joined a chat yet...");
                    continue;
                }

                try
                {
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                    readFromNet = new ObjectINputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Meassage(JOIN, ChatClient.myNodeInfo));

                    serverConnection.close();
                }
                catch(IOException ex)
                {

                    continue;

                }

                hasJoined = false;

                System.out.println("Left chat ...");
 
            }

            else if (inputLine.startsWith("SHUTDOWN ALL"))
            {
                if (hasJoined == false)
                {
                    System.err.println("to shut down the chat you must join")
                    continue;
                }

                try
                {
                    serverConnection = new Socket(ChatClient.ServerNodeInfo.getAddress(), ChatClient. serverNodeInfo.getPort());
                    readFromNet = new ObjectINputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Meassage(SHUTDOWN_ALL, ChatClient.myNodeInfo));

                    serverConnection.close();

                }
                catch(IOException ex)
                {
                    


                }

                System.out.println("Sent shutdown all request...\n");
            }
            else if (inputLine.startsWith("SHUTDOWN"))
            {
                if (hasJoined == true)
                {
                    serverConnection = new Socket(ChatClient.ServerNodeInfo.getAddress(), ChatClient. serverNodeInfo.getPort());
                    readFromNet = new ObjectINputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Meassage(SHUTDOWN_ALL, ChatClient.myNodeInfo));

                    serverConnection.close();

                    System.out.println("Left chat...");
                }
                catch(IOException ex)
                {

                }

                System.out.println("Exiting...\n");
                System.exit(0);

            }

            else
            {

                if (hasJoined == false)
                {
                    System.err.println("you need to join the chat first")
                    continue;
                }

                try
                {
                    serverConnection = new Socket(ChatClient.ServerNodeInfo.getAddress(), ChatClient. serverNodeInfo.getPort());
                    readFromNet = new ObjectINputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    writeToNet.writeObject(new Meassage(NOTE, ChatClient.myNodeInfo.getName() + ": "+ inputLine));

                    serverConnection.close();

                    System.out.println("message sent...");

                }
                catch(IOException ex)
                {

                    continue;                


                }

                System.out.println("Sent shutdown all request...\n");

            }

        }

      }
 }