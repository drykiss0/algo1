/******************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

public class PointSETVisualizer {

    public static void main(String[] args) {
        RectHV rect = new RectHV(0.2, 0.3, 0.7, 0.6);
        Point2D testPoint = new Point2D(0.5, 0.5);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        rect.draw();
        testPoint.draw();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.show();
        PointSET kdtree = new PointSET();
        while (true) {
            if (StdDraw.isMousePressed() && !kdtree.contains(new Point2D(StdDraw.mouseX(), StdDraw.mouseY()))) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                kdtree.insert(p);
                Point2D neighbor = kdtree.nearest(testPoint);
                Iterable<Point2D> insidePoints = kdtree.range(rect);

                StdDraw.clear();

                StdDraw.setPenColor(Color.GREEN);
                StdDraw.line(testPoint.x(), testPoint.y(), neighbor.x(), neighbor.y());

                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                rect.draw();
                testPoint.draw();

                StdDraw.setPenColor(StdDraw.BLACK);
                kdtree.draw();

                StdDraw.setPenColor(Color.GREEN);
                for (Point2D insidePoint: insidePoints) {
                    insidePoint.draw();
                }

                StdDraw.show();
            }
            StdDraw.pause(50);
        }

    }
}
