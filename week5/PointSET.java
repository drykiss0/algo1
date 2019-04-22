import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class PointSET {

    private final SET<Point2D> points;

    // construct an empty set of points 
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        ensureNonNull(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        ensureNonNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        points.iterator().forEachRemaining(p -> p.draw());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        ensureNonNull(rect);
        final List<Point2D> insideList = new ArrayList<>();
        points.iterator().forEachRemaining(p -> {
            if (rect.contains(p)) {
                insideList.add(p);
            }
        });
        return insideList;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D otherP) {
        ensureNonNull(otherP);

        Point2D resultPoint = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D p: points) {
            double distance = p.distanceSquaredTo(otherP);
            if (distance < minDistance) {
                resultPoint = p;
                minDistance = distance;
            }
        }
        return resultPoint;
    }

    private void ensureNonNull(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException();
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSet = new PointSET();
        pointSet.insert(new Point2D(0.7, 0.2));
        pointSet.insert(new Point2D(0.5, 0.4));
        pointSet.insert(new Point2D(0.2, 0.3));
        pointSet.insert(new Point2D(0.4, 0.7));
        pointSet.insert(new Point2D(0.9, 0.6));

        StdOut.println("Nearest = " + pointSet.nearest(new Point2D(0.05, 0.77)));
    }
}