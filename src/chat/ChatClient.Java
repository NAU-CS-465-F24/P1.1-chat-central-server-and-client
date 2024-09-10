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

public class ChatClient implements Runnable 
{
    static Receiver receiver = null;
    static Sender sender = null;

    public static NodeInfo myNodeInfo = null;
    public static NodeInfo serverNodeInfo = null;

    public ChatClient(string propertiesFile)
    {
        Properties properties = null
        try
        {
            properties = new PropertyHandler(propertiesFile);
        }
        catch (IOException ex)
        {
            logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not open properties file, ex");
            System.exit(1);
        }

        int myPort = 0;
        try
        {
            myPort = Integer.parseInt(properties.getProperty("MY_PORT"));
        }
        catch (NumberFormatException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read reciever port", ex);
            System.exit(1);
        }

        String myName = properties.getProperty("MY_NAME");
        if (nyName == null)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read Name", ex);
            System.exit(1);
            
        }

        myNodeInfo = new NodeInfo(NetworkUtilities.getMyIP(), myPort, myName);

        int serverPort = 0
        try
        {
            serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        }
        catch (NumberFormatException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read reciever port", ex);

        }

        String serverIP = null;
        serverIP = properties.getProperty("SERVER_IP");
        if (serverIP == null)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read reciever port", ex);

        }

        if(serverPort != 0 && serverIP != null)
        {
            serverNodeInfo = new NodeInfo(serverIP, serverPort);
        }
    }

    @Override
    public void run()
    {
        (receiver = new Receiver()).start();
        (sender = new Sender()).start();
    }

    public static void main (String[] args)
    {
        String propertiesFile = null;

        try
        {
            propertiesFile = args[0];
        }
        catch
        {
            propertiesFile = "config/ChatNodeDefaults.properties";
        }

        (new ChatClient(propertiesFile)).run();
    }

}