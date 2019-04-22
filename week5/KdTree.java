import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int count;

    private class Node {

        public static final double MIN = 0;
        public static final double MAX = 1;
        private final Node parent;
        private final Point2D value;
        private final boolean verticalSplit;
        private RectHV rect;

        private Node left;
        private Node right;

        public Node(Point2D value, Node parent, boolean verticalSplit) {
            this.value = value;
            this.verticalSplit = verticalSplit;
            this.parent = parent;
        }

        public void calculateAndStoreRectangles() {
            if (parent == null) {
                this.rect = new RectHV(MIN, MIN, MAX, MAX);
                return;
            }
            if (verticalSplit) {
                if (parent.left == this) {
                    this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.value.y());
                } else {
                    this.rect = new RectHV(parent.rect.xmin(), parent.value.y(), parent.rect.xmax(), parent.rect.ymax());
                }
            } else {
                if (parent.left == this) {
                    this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.value.x(), parent.rect.ymax());
                } else {
                    this.rect = new RectHV(parent.value.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            }
        }

        public boolean insert(Point2D p) {
            if (p.equals(value)) {
                return false;
            }
            if (compareTo(p) >= 0) {
                if (left != null) {
                    return left.insert(p);
                } else {
                    left = new Node(p, this, !verticalSplit);
                    left.calculateAndStoreRectangles();
                }
            } else {
                if (right != null) {
                    return right.insert(p);
                } else {
                    right = new Node(p, this, !verticalSplit);
                    right.calculateAndStoreRectangles();
                }
            }
            return true;
        }

        public boolean contains(Point2D p) {
            if (p.equals(value)) {
                return true;
            }
            if (compareTo(p) >= 0) {
                return left != null && left.contains(p);
            }
            return right != null && right.contains(p);
        }

        public void draw() {
            value.draw();
            if (left != null) {
                left.draw();
            }
            if (right != null) {
                right.draw();
            }
        }

        public List<Point2D> range(RectHV queryRect) {
            List<Point2D> results = new ArrayList<>();
            if (queryRect.intersects(rect)) {
                if (left != null) {
                    results.addAll(left.range(queryRect));
                }
                if (right != null) {
                    results.addAll(right.range(queryRect));
                }
                if (queryRect.contains(value)) {
                    results.add(value);
                }
            }
            return results;
        }

        public Point2D nearest(Point2D queryPoint, Point2D neighbor) {
            if (queryPoint.distanceSquaredTo(neighbor) < rect.distanceSquaredTo(queryPoint)) {
                return neighbor;
            }
            Node firstExplore = left;
            Node secondExplore = right;
            if (compareTo(queryPoint) < 0) {
                firstExplore = right;
                secondExplore = left;
            }
            Point2D neighbor1 = firstExplore != null ?
                    firstExplore.nearest(queryPoint, neighbor) : neighbor;
            Point2D neighbor2 = secondExplore != null ?
                    secondExplore.nearest(queryPoint, neighbor1)
                    : neighbor1;

            return queryPoint.distanceSquaredTo(value) < queryPoint.distanceSquaredTo(neighbor2) ?
                    value : neighbor2;
        }

        public int compareTo(Point2D other) {
            if (other == null) {
                return 1;
            }
            if (verticalSplit) {
                return (int) Math.signum(value.x() - other.x());
            } else {
                return (int) Math.signum(value.y() - other.y());
            }
        }
    }

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        ensureNonNull(p);
        if (root == null) {
            root = new Node(p, null, true);
            root.calculateAndStoreRectangles();
            count++;
            return;
        }
        if (root.insert(p)) {
            count++;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        ensureNonNull(p);
        return root != null && root.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        if (root == null) {
            return;
        }
        root.draw();
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        ensureNonNull(rect);
        return !isEmpty() ? root.range(rect) : new ArrayList<>();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        ensureNonNull(p);
        return !isEmpty() ? root.nearest(p, root.value) : null;
    }

    private void ensureNonNull(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException();
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        StdOut.println("Nearest = " + tree.nearest(new Point2D(0.05, 0.77)));
    }
}