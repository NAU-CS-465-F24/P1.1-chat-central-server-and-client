package utils;


import java.net.*;
import java.util.Enumeration;


/**
 * Utility class for networking.
 */
public class NetworkUtilities {
    /**
     * Helper class to retrieve ones own IPv4.
     *
     * @return my own IPv4 address
     * @see <a href="https://stackoverflow.com/questions/8083479/java-getting-my-ip-address">original source</a>
     */
    public static String getMyIP() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp() || networkInterface.isVirtual()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    final String myIP = address.getHostAddress();

                    if (Inet4Address.class == address.getClass()) {
                        return myIP;
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}