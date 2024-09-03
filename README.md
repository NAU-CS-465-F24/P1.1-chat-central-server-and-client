# P1.1-chat-central-server-and-client

This is a team project.

In this very first assignment I want to make sure that everybody has a basic understanding of how to program networked applications (specifically connection-oriented applications using TCP/IP) in Java. At the same time, the assignment will thoroughly prepare you for the upcoming - and more challenging - follow-up assignment. Specifically, while you will be developing a server-based chat system in this assignment, the next one is all about a peer-to-peer based chat application.
What to do

You are supposed to design and implement a chat server and client - in Java (and only in Java, as all assignments that follow this one). The server serves as a "central hub", which allows chat clients to:

    Register for participating in the chat (aka JOIN)
    Leave the chat (aka LEAVE)
    Send messages (called "notes") to all registered chat participants.

Similarly, the chat client will be able to:

    JOIN a chat, i.e., register with a known chat server to participate in the chat. The information of the server, i.e., its IP/port, needs to be read from a properties file upon starting up the chat client. Note that a client who has joined before, should not be able to join again.
    LEAVE a chat - while not terminating itself, i.e. it may register again at some later time. Note that leaving a chat anticipates that the client has joined the chat before.
    Send a note to all other chat clients, using the chat server. Note that a client can only send a note, if it has joined the chat before. For simplicity's sake we want to assume that a message is being forwarded by the server to ALL chat participants, including the originator of the note.
    SHUTDOWN, which terminates the chat client. This may involve to first leave the chat, if the client is registered at the server.
    SHUYTDOWN_ALL, which shuts down the whole chat, including the server.
    In order to trigger all above activities/states, the client needs to provide a primitive command-line user interface for the user to use. The interface will recognise the following "commands":
        JOIN IP port
        LEAVE
        SHUTDOWN
        SHUTDOWN_ALL
        any other text, which is interpreted to be a note to be sent to all other registered chat clients. Note, however, that the client has to be a chat participant, before being able to send notes.

Clients have logical, human-readable names, like Paul, Mickey Mouse, Sandy etc. When sending a message, the logical name will be prepended to the message that is supposed to be sent and thus will automatically be displayed on the receiving side, when displaying the message string.

## Hints:

It is very important to make INTENSE use of the OOP power of Java - for efficiency's sake as well as your own sanity.

You will want to make the communication between client and server, i.e., the "network protocol", as efficient as possible, from a design perspective. That is why I ask you to design and implement a class Message. Messages are what you send over the network via object streams. Hence, class Message has to implement the interface java.io.Serializable.

A message has a type and content. There are getter and setter methods and a constructor that takes a message's type and content as arguments - content has to be general enough to accommodate any type of data for a message to carry and for that reason needs to be of type Object.

Message types are defined in an interface called MessageTypes. Each type is realized as a symbolic constant of type int and corresponds to JOIN, LEAVE, NOTE and the two shutdown actions, see above. Understand that each message type suggests a certain type of content, i.e. JOIN  and LEAVE will correspond with connectivity information of the chat client, while NOTE corresponds with String.

I said above that messages are sent over object streams. As you know, there are InputStreams and OutputStreams. You have learned in CS460/560 how to get your hands on those streams, once you have a Socket object. If you work with object streams, all you need to do is to wrap those primitive streams with ObjectInputStream() and ObjectOutputStream() constructors. Once you have those object streams, you can call readObject() and writeObject() upon them. And that is how you send Messages.

There is a caveat, however. You need to be careful with the instantiation of object stream objects, i.e., the order of the instantiation matters. An ObjectInputStream initiated on one side of a network connection needs to match with the instantiation of a corresponding ObjectOutputStream on the other side and vice versa. If you, let's say, instantiate first the ObjectInputStreams on both sides, and then the ObjectOutputStreams on both sides thereafter, you will get weird effects and errors. In summary, the instantiations need to "cross over".

I also ask you to create a class NodeInfo that contains the tuple "IP/port/logical name" of a chat client, making it convenient to send an object of that type in the content field of a message. This is needed e.g. in JOIN and LEAVE messages. Make sure to also have this class implement interface java.io.Serializable as it is going to be sent over the object streams and thus needs to be serializable, just like Message objects.

One last word about java.io.Serializable: this is what is called a "marker interface". A marker interface doesn't require you to actually implement anything. It is just a way of telling the runtime system "Hey, I intend to send objects of this type over the network, please deal with it and serialize the objects for me".

To get you started quickly, I will provide some of the classes described above:

[Message.java](src/message/Message.java)
 
[MessageTypes.java](src/message/MessageTypes.java)
     

## What to submit

This is a team project! Each team of three students will submit exactly one project. These are the parts of the project that I want you to submit on BBLearn:

    In the submission comments, you need to name each member of your team
    Also in the comments, you need to provide your github link
    You need to submit your complete source code, along with a memo
        That code is understood to have been developed by all team members as a collective effort (as opposed to turning a bottle to find a victim who is supposed to do the whole job on his/her own) and each team member needs to fully understand what has been submitted. In order to provide transparency and accountability, I ask you to outline in your memo which team member worked on which part of the project - naming the classes, or, if not applicable, the methods that each team member designed and implemented.
        The code needs to follow Java style guidelines when it comes to naming conventions, indentations, comments etc.
        Your code needs to be commented abundantly.
        Your code needs to be properly structured. The big NO NO is to put all of the classes in the default package. Also, main() just gets things started, you CANNOT put any code other than bootstrapping code into main()
    Prepare a video clip, upload it unto youtube.com and submit its youtube.com link in the submission comments. The video clip demonstrates the flawless function of your server and two clients. Specifically, record the following sequence of actions:
        Start up your chat server in a terminal window
        Start up one chat client in a different terminal window on the same computer
        Start up a second chat client in a terminal window on a DIFFERENT computer and make sure that both screens are shown side-by-side
        In the terminal window of one of the clients, put in the string "AAA". This should trigger a response in the client, saying that the client needs to join a chat, before a message can be sent
        Join the chat with BOTH clients
        Send the message "XXX" from one client and the message "YYY" from the other. Both messages should be visible on both clients
        Have one of the two clients leave the chat
        Have the other client send the message "ZZZ". Shutdown this client.
        Shut down the client that left before.
        Make sure that for each of these actions, there is some debugging information being printed out on the client as well as on the server side that documents what is going on.

## Grading guide

Your work will be graded according to the following rubric:
Aspect 	Points earned  
Abundant comments  
Proper variable naming  
Proper indentation  
Code structure, packages. Red flag: all classes in default package 	This is a baseline expectation, that doesn't earn points
but rather gets points deducted if not honored.  
-20 pts
  	 
JOIN working properly 	20 pts  
LEAVE working properly 	10 pts  
SHUTDOWN working properly 	10 pts  
Sending a NOTE working properly 	20 pts  
SHUTDOWN_ALL working properly 	20 pts  
Correctly handling state information about JOIN status, i.e.
not JOINing, when already JOINed 
not LEAVING when not JOINed before 	20 pts  

Incomplete assignments do not earn any points 
Check for:
- missing parts of the source code
- missing memo with outline of workload distribution
- youtube.com video clip

-100 pts, if ANY part is missing
