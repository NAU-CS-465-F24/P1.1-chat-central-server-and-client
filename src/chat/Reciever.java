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

 public class Receiver extends Thread
 {
    static ServerSocket receiverSocket = null;
    static String userName = null;

    public Reciever()
    {
        try
        {
            receiverSocket = new ServerSocket(ChatClient.myNodeInfo.getPort());
            System.out.println("[Receiver.Receiver] reciever socket created, listening on port")
        }
       

        System.out.println(ChatClient.myNodeInfo.getName() + "listening on" +ChatClient.myNodeInfo.getMyIP() + 
            ChatClient.myNodeInfo.getPort());
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                (new ReceiverWorker(receiverSocket.accept())).start();
            }
          
        }
    }
 } 