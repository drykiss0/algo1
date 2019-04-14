import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class PointsDraw {

    public static void drawPoints(Point[] points) {
        // draw points
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (Point p : points) {
            p.draw();
            StdDraw.textLeft(p.getX() + 500, p.getY(), p.toString());
        }
        StdDraw.show();
    }
}
