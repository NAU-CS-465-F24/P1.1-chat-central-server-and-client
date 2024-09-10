package chat;


import java.io.Serializable;


public class NodeInfo implements Serializable {
    // initialize variables
    String address;
    int port;
    String name = null;


    // constructor (client)
    public NodeInfo(String address, int port, String name) {
        this.address = address;
        this.port = port;
        this.name = name;
    }


    // constructor (server)
    public NodeInfo(String address, int port) {
        this(address, port, null);
    }


    // getters
    String getAddress() {
        return this.address;
    }


    int getPort() {
        return this.port;
    }


    String getName() {
        return this.name;
    }


    @Override
    public boolean equals(Object other) {
        // initialize variables
        String otherIP = ((NodeInfo) other).getAddress();
        int otherPort = ((NodeInfo) other).getPort();

        // compare current client info to "other" client info
        return otherIP.equals(this.address) && (otherPort == this.port);
    }
}