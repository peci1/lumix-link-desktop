package streamviewer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetUtil {

    /**
     * Find a local interface IPv4 address that lies in the same subnet as the given IP address.
     *
     * @param anyIpInSubnet Any other IP address in the searched subnet.
     * @param subnetMaskBitSize Size of the subnet's bitmask (0-32).
     * @return The local address lying in the given subnet.
     * @throws UnknownHostException If the reference IP address cannot be parsed.
     * @throws SocketException On network communication errors.
     */
    public static Inet4Address findLocalIpInSubnet(String anyIpInSubnet, int subnetMaskBitSize)
            throws UnknownHostException, SocketException {
        final int anyIpInt = inetAddrToInt(InetAddress.getByName(anyIpInSubnet));

        // list all interfaces
        final Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        for (; netInterfaces.hasMoreElements(); ) {
            final NetworkInterface netInterface = netInterfaces.nextElement();

            // list all addresses of the interface
            final Enumeration<InetAddress> netInterfaceAddresses = netInterface.getInetAddresses();
            for (; netInterfaceAddresses.hasMoreElements(); ) {
                final InetAddress netInterfaceAddress = netInterfaceAddresses.nextElement();

                // if the address is IPv4 and in the given subnet, return it
                if (netInterfaceAddress instanceof Inet4Address) {
                    final int interfaceIpInt = inetAddrToInt(netInterfaceAddress);
                    if (isInSameSubnet(interfaceIpInt, anyIpInt, subnetMaskBitSize)) {
                        return (Inet4Address) netInterfaceAddress;
                    }
                }
            }
        }

        throw new SocketException("Could not find any local IP address in subnet " + anyIpInSubnet + "/" + subnetMaskBitSize + " .");
    }

    /**
     * Check if the two given IPv4 addresses are in the same subnet.
     *
     * @param ip1 The first IPv4 address (converted by #inetAddrToInt()).
     * @param ip2 The second IPv4 address (converted by #inetAddrToInt()).
     * @param netMaskBitSize Size of the subnet's bitmask (0-32).
     * @return Whether the two addresses lie in the same subnet.
     */
    private static boolean isInSameSubnet(int ip1, int ip2, int netMaskBitSize) {
        final int netMask = -1 << (32 - netMaskBitSize);
        return (ip1 & netMask) == (ip2 & netMask);
    }

    /**
     * Convert the given InetAddr to the integer corresponding to the IPv4 address (if it is an IPv4 address).
     *
     * @param address The address to convert.
     * @return The integer corresponding to the given address, or zero if the address is not IPv4.
     */
    private static int inetAddrToInt(InetAddress address) {
        if (address instanceof Inet4Address) {
            byte[] ipv4Address = ((Inet4Address) address).getAddress();
            return ((ipv4Address[0] & 0xFF) << 24)
                    | ((ipv4Address[1] & 0xFF) << 16)
                    | ((ipv4Address[2] & 0xFF) << 8)
                    | ((ipv4Address[3] & 0xFF));
        } else {
            return 0;
        }
    }
}
