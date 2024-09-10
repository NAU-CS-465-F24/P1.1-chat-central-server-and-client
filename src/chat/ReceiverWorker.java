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
 import static message.MessageTypes.NOTE;
 import static message.MessageTypes.SHUTDOWN;

 public class ReceiverWorker extends Thread
 {

    Socket serverConnection = null;

    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    
    Message message = null;

    public ReceiverWorker(Socket serverConnection)
    {
        this.serverConnection = serverConnection;

        try
        {
            readFromNet = new ObjectInputStream(serverConnection.getInputStream());
            writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
        }

    }

    @Override
    public void run()
    {
        try
        {
            message = (Message) readFromNet.readObject();
        }

        switch (message.getType())
        {
            case SHUTDOWN:

            System.out.println("Received Shutdown message from server, exiting");

            try
            {
                serverConnection.close();
            }

            System.exit(0);

            break;

            case NOTE:

            System.out.println((String) message.getContent());

            try
            {
                ServerConnection.close();
            }

            break;

            default:
            //cant occure
        }
    }
 }

