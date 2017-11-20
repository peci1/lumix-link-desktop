package streamviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The options used in the LumixStreamViewer
 */
public class Options {

    private final String cameraIp;

    private final int cameraNetMaskBitSize;

    private Options(String cameraIp, int cameraNetMaskBitSize) {
        this.cameraIp = cameraIp;
        this.cameraNetMaskBitSize = cameraNetMaskBitSize;
    }

    /**
     * Either reads the options from the program arguments, or via standard input.
     *
     * @param args the program arguments
     * @return the LumixStreamViewer options
     */
    public static Options read(String[] args) {
        String cameraIp = "192.168.0.1";
        int cameraNetMaskBitSize = 24;

        if (args.length == 2) {
            cameraIp = args[0];
            try {
                cameraNetMaskBitSize = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }

        return new Options(cameraIp, cameraNetMaskBitSize);
    }

    public String getCameraIp() {
        return cameraIp;
    }

    public int getCameraNetMaskBitSize() {
        return cameraNetMaskBitSize;
    }
}
