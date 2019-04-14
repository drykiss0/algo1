import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] pointsIn) {
        if (pointsIn == null || Arrays.stream(pointsIn).anyMatch(p -> p == null)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        Point[] points = Arrays.copyOf(pointsIn, pointsIn.length);
        Arrays.sort(points);

        for (int i = 1; i < points.length; i++) {
            if (points[i-1].compareTo(points[i]) == 0) {
                throw new IllegalArgumentException("Duplicate points not allowed");
            }
        }

        for (int i = 0; i < points.length; i++) {
            Point p0 = points[i];
            for (int j = i + 1; j < points.length; j++) {
                double slope = p0.slopeTo(points[j]);
                int numFound = 0;
                for (int k = j + 1; k < points.length; k++) {
                    if (slope == p0.slopeTo(points[k])) {
                        numFound++;
                    }
                    if (numFound == 2) {
                        lineSegments.add(new LineSegment(p0, points[k]));
                        break;
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("vertical5.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-1000, 32768);
        StdDraw.setYscale(-1000, 32768);


        // print and draw the line segments
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.GREEN);
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        // PointsDraw.drawPoints(points);
    }
}
