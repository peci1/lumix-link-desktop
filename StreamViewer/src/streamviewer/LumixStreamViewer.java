package streamviewer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/**
 * An improved version of the Panasonic Lumix camera desktop stream viewer. Based upon the work published at:
 * http://www.personal-view.com/talks/discussion/6703/control-your-gh3-from-a-web-browser-now-with-video-/p1
 *
 * @author personal-view.com
 * @author Martin Pecka, Azzurite
 * @version 3.0.0
 */
public class LumixStreamViewer {

    private static Thread streamViewerThread;

    public static void main(String[] args) {
        Options options = Options.read(args);
        String cameraIp = options.getCameraIp();
        int cameraNetMaskBitSize = options.getCameraNetMaskBitSize();

        VideoPanel videoPanel = new VideoPanel();

        System.out.println("Trying to connect to camera " + cameraIp + " on subnet with mask size " +
                cameraNetMaskBitSize);
        try {
            StreamViewer streamViewer = new StreamViewer(videoPanel::displayNewImage, cameraIp, cameraNetMaskBitSize);
            streamViewerThread = new Thread(streamViewer);
            streamViewerThread.start();
        } catch (SocketException e) {
            System.out.println("Socket creation error : " + e.getMessage());
            System.exit(1);
        } catch (UnknownHostException e) {
            System.out.println("Cannot parse camera IP address: " + cameraIp + ".");
            System.exit(2);
        }

        final JFrame window = new JFrame("Lumix Live Stream viewer on " + cameraIp + ":49199");
        window.add(videoPanel);
        window.pack();
        window.setVisible(true);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                try {
                    streamViewerThread.interrupt();
                    streamViewerThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.exit(0);
            }
        });
    }


}
