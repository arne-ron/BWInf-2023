package aufgabe2;


import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.Collection;
import java.util.Iterator;


@SuppressWarnings("serial")
class MyCanvas extends Canvas {
    
    int scale;
    BufferedImage bufferedImage = new BufferedImage(Aufgabe2.width * Aufgabe2.scale, Aufgabe2.height * Aufgabe2.scale, BufferedImage.TYPE_INT_ARGB);

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2D = bufferedImage.createGraphics();

        Collection<Point> tempClone = Aufgabe2.open.values();
        Iterator<Point> it = tempClone.iterator();

        while (it.hasNext()) {

            Point p = it.next();
            g2D.setColor(p.color);
            g2D.fillRect(p.x * scale, p.y * scale, scale, scale);

        }

        tempClone = Aufgabe2.closed.values();
        it = tempClone.iterator();

        while (it.hasNext()) {

            Point p = it.next();
            it.remove();
            g2D.setColor(p.color);
            g2D.fillRect(p.x * scale, p.y * scale, scale, scale);

        }

        g.drawImage(bufferedImage, 0, 0, null);
        
    }
}
