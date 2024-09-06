package utils;

import java.net.*;
import java.util.Enumeration;

public class NetworkUtilities {
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