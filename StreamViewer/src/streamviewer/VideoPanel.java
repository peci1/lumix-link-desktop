package streamviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A Swing video panel, created by rapidly changing the underlying BufferedImage
 */
public class VideoPanel extends JPanel {

    /**
     * The last image read from the stream.
     */
    private BufferedImage bufferedImage = null;

    /**
     * Thread-Safe. Displays a new image in the panel.
     *
     * @param image the image to display
     */
    public void displayNewImage(BufferedImage image) {
        this.bufferedImage = image;
        SwingUtilities.invokeLater(this::repaint);
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
}
