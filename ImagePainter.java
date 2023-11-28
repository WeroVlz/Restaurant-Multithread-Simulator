import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class ImagePainter extends Thread {
    private int x;
    private int y;
    private Graphics g;
    private Image image;

    public ImagePainter(int x, int y, Graphics g, Image img) {
        this.x = x;
        this.y = y;
        this.g = g;
        image = img;
    }

    @Override
    public void run() {
        g.drawImage(image, x, y, null);
    }
}
