import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final List<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] inputPoints) {
        if (inputPoints == null || Arrays.stream(inputPoints).anyMatch(p -> p == null)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        Point[] points = Arrays.copyOf(inputPoints, inputPoints.length);
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1].compareTo(points[i]) == 0) {
                throw new IllegalArgumentException("Duplicate points not allowed");
            }
        }

        Point[] slopesSorted = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
            Point p0 = points[i];
            Arrays.sort(slopesSorted, p0.slopeOrder());

            for (int s = 0; s < slopesSorted.length; s++) {

                int eqs = s;
                boolean wasIncluded = false;
                Point max = slopesSorted[s];
                while (eqs + 1 < slopesSorted.length && p0.slopeOrder().compare(slopesSorted[eqs + 1], slopesSorted[eqs]) == 0) {
                    eqs++;
                    if (max.compareTo(slopesSorted[eqs]) < 0) {
                        max = slopesSorted[eqs];
                    }
                    if (p0.compareTo(slopesSorted[eqs]) > 0 || p0.compareTo(slopesSorted[eqs - 1]) > 0) {
                        wasIncluded = true;
                    }
                }

                if (!wasIncluded && eqs - s > 1) {
                    lineSegments.add(new LineSegment(p0, max));
                }
                s = eqs;
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
        In in = new In("rs1423.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-1000, 33768);
        StdDraw.setYscale(-1000, 33768);


        // print and draw the line segments
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.GREEN);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        // PointsDraw.drawPoints(points);
    }

}
