package streamviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * An improved version of the Panasonic Lumix camera desktop stream viewer. Based upon the work published at:  
 * http://www.personal-view.com/talks/discussion/6703/control-your-gh3-from-a-web-browser-now-with-video-/p1
 * 
 * @version 2.0.0
 * 
 * @author personal-view.com
 * @author Martin Pecka
 */
public class LumixStreamViewer extends JPanel {

    /** The local UDP socket for receiving the video stream. */
    protected DatagramSocket localUdpSocket = null;
    /** The UDP port to listen on. */
    protected int localUdpPort;
    /** IP address of the local network interface communicating with the camera. */
    protected InetAddress localIpAddress = null;

    /** The last image read from the stream. */
    protected BufferedImage bufferedImage = null;

    /**
     * Create the Lumix videostream reader connected to the default UDP port 49199.
     * 
     * @param cameraIp IPv4 address of the camera.
     * @param cameraNetmaskBitSize Size of the camera network's subnet.
     * 
     * @throws UnknownHostException If the camera IP address cannot be parsed.
     * @throws SocketException On network communication errors.
     */
    public LumixStreamViewer(String cameraIp, int cameraNetmaskBitSize)
            throws UnknownHostException, SocketException {
        this(cameraIp, cameraNetmaskBitSize, 49199);
    }
    
    /**
     * Create the Lumix videostream reader.
     * 
     * @param cameraIp IPv4 address of the camera.
     * @param cameraNetmaskBitSize Size of the camera network's subnet.
     * @param udpPort The UDP port to listen on.
     * 
     * @throws UnknownHostException If the camera IP address cannot be parsed.
     * @throws SocketException On network communication errors.
     */
    public LumixStreamViewer(String cameraIp, int cameraNetmaskBitSize, int udpPort)
            throws UnknownHostException, SocketException {
        this.localIpAddress = findLocalIpInSubnet(cameraIp, cameraNetmaskBitSize);

        this.localUdpPort = udpPort;
        this.localUdpSocket = new DatagramSocket(this.localUdpPort);

        System.out.println("UDP Socket on " + this.localIpAddress.getHostAddress() + ":" + this.localUdpPort
                + " created");
    }

    /**
     * Run the stream reading loop and cyclically redraw the panel.
     */
    public void streamReadingLoop() {
        // magic numbers
        int videoDataStart = 132;
        byte[] udpPacketBuffer = new byte['田'];
        
        int neverZero = 1;
        while (neverZero != 0) {
            try {
                final DatagramPacket receivedPacket = new DatagramPacket(udpPacketBuffer, udpPacketBuffer.length, 
                        this.localIpAddress, this.localUdpPort);
                
                this.localUdpSocket.receive(receivedPacket);

                final byte[] udpData = receivedPacket.getData();
                // magic numbers
                for (int k = 130; k < 320 && k < (receivedPacket.getLength() - 1) ; k++) {
                    // magic numbers
                    if ((udpData[k] == -1) && (udpData[(k + 1)] == -40)) {
                        videoDataStart = k;
                    }
                }
                final byte[] videoData = Arrays.copyOfRange(udpData, videoDataStart, receivedPacket.getLength());

                this.bufferedImage = ImageIO.read(new ByteArrayInputStream(videoData));

                this.repaint();
            } catch (IOException e) {
                System.out.println("Error with client request : " + e.getMessage());
            }
        }
        
        this.localUdpSocket.close();
    }
    
    /**
     * Find a local interface IPv4 address that lies in the same subnet as the given IP address.
     * 
     * @param anyIpInSubnet Any other IP address in the searched subnet.
     * @param subnetMaskBitSize Size of the subnet's bitmask (0-32).
     * @return The local address lying in the given subnet.
     * 
     * @throws UnknownHostException If the reference IP address cannot be parsed.
     * @throws SocketException On network communication errors.
     */
    protected static Inet4Address findLocalIpInSubnet(String anyIpInSubnet, int subnetMaskBitSize)
            throws UnknownHostException, SocketException {
        final int anyIpInt = inetAddrToInt(InetAddress.getByName(anyIpInSubnet));

        // list all interfaces
        final Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        for (; netInterfaces.hasMoreElements();) {
            final NetworkInterface netInterface = netInterfaces.nextElement();           
            
            // list all addresses of the interface
            final Enumeration<InetAddress> netInterfaceAddresses = netInterface.getInetAddresses();
            for (; netInterfaceAddresses.hasMoreElements();) {
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
    protected static boolean isInSameSubnet(int ip1, int ip2, int netMaskBitSize) {
        final int netMask = -1 << (32 - netMaskBitSize);
        return (ip1 & netMask) == (ip2 & netMask);
    }

    /**
     * Convert the given InetAddr to the integer corresponding to the IPv4 address (if it is an IPv4 address).
     * 
     * @param address The address to convert.
     * @return The integer corresponding to the given address, or zero if the address is not IPv4.
     */
    protected static int inetAddrToInt(InetAddress address) {
        if (address instanceof Inet4Address) {
            byte[] ipv4Address = ((Inet4Address) address).getAddress();
            return ((ipv4Address[0] & 0xFF) << 24)
                    | ((ipv4Address[1] & 0xFF) << 16)
                    | ((ipv4Address[2] & 0xFF) << 8)
                    | ((ipv4Address[3] & 0xFF) << 0);
        } else {
            return 0;
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }

    @Override
    public void paint(Graphics graphics) {
        if (this.bufferedImage != null) {
            graphics.drawImage(this.bufferedImage, 0, 0, null);
        }
    }

    public static void main(String[] args) {
        String cameraIp;
        int cameraNetMaskBitSize = 24;
        
        if (args.length == 3) {
            cameraIp = args[1];           
            try {
                cameraNetMaskBitSize = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println(e);
            }
        } else {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {                
                // camera IP
                System.out.print("Camera IP address [192.168.0.1]: ");
                cameraIp = in.readLine();
                if (cameraIp.length() == 0) {
                    cameraIp = "192.168.0.1";
                }

                // camera netmask
                System.out.print("Camera IP netmask size [24]: ");
                String mask = in.readLine();
                if (mask.length() > 0) {
                    cameraNetMaskBitSize = Integer.parseInt(mask);
                } else {
                    cameraNetMaskBitSize = 24;
                }
            } catch (IOException e) {
                cameraIp = "192.168.0.1";
                cameraNetMaskBitSize = 24;
                System.err.println(e);
            }
        }
        
        System.out.println("Trying to connect to camera " + cameraIp + " on subnet with mask size " + 
                cameraNetMaskBitSize);

        LumixStreamViewer streamViewer = null;
        try {
            streamViewer = new LumixStreamViewer(cameraIp, cameraNetMaskBitSize);
        } catch (SocketException e) {
            System.out.println("Socket creation error : " + e.getMessage());
            System.exit(1);
        } catch (UnknownHostException e) {
            System.out.println("Cannot parse camera IP address: " + cameraIp + ".");
            System.exit(2);
        }
    
        final JFrame window = new JFrame("Lumix Live Stream viewer on " + 
                streamViewer.localIpAddress + ":" + streamViewer.localUdpPort);
        final JPanel imagePanel = new JPanel();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        imagePanel.add(streamViewer);
        window.add(imagePanel);
        window.pack();
        window.setVisible(true);

        streamViewer.streamReadingLoop();
    }
}
