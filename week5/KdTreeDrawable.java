import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class KdTreeDrawable {

    private class Node implements Comparable<Point2D> {

        public static final double MIN = 0;
        public static final double MAX = 1;
        private final Node parent;
        private final Point2D value;
        private final boolean verticalSplit;
        private RectHV rectLeft;
        private RectHV rectRight;
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
                if (isLeftChild()) {
                    this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.value.y());
                } else {
                    this.rect = new RectHV(parent.rect.xmin(), parent.value.y(), parent.rect.xmax(), parent.rect.ymax());
                }
            } else {
                if (isLeftChild()) {
                    this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.value.x(), parent.rect.ymax());
                } else {
                    this.rect = new RectHV(parent.value.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            }
        }

        public void calculateAndStoreRectangles_old() {
            if (verticalSplit) {
                this.rectLeft = new RectHV(getMinX(), getMinY(), value.x(), getMaxY());
                this.rectRight = new RectHV(value.x(), getMinY(), getMaxX(), getMaxY());
            } else {
                this.rectLeft = new RectHV(getMinX(), getMinY(), getMaxX(), value.y());
                this.rectRight = new RectHV(getMinX(), value.y(), getMaxX(), getMaxY());
            }
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Node getParent() {
            return parent;
        }

        public RectHV getRect() {
            return rect;
        }

        public boolean isLeftChild() {
            return parent != null && parent.getLeft() == this;
        }

        public boolean isRightChild() {
            return parent != null && parent.getRight() == this;
        }

        public double getKey() {
            return verticalSplit ? value.x() : value.y();
        }

        public Point2D getValue() {
            return value;
        }

        public boolean isVerticalSplit() {
            return verticalSplit;
        }

        private double getBoundCoord(boolean isMax, boolean yCoord) {
            Node node = verticalSplit ^ yCoord ? parent : this;
            Predicate<Node> childCheck = isMax ? n -> n.isRightChild() : n -> n.isLeftChild();
            while (node != null && childCheck.test(node)) {
                node = node.getParent().getParent();
            }
            if (node == null || node.parent == null) {
                return isMax ? MAX : MIN;
            }
            if (yCoord) {
                return node.parent.value.y();
            } else {
                return node.parent.value.x();
            }
        }

        public double getMinX() {
            return getBoundCoord(false, false);
        }

        public double getMaxX() {
            return getBoundCoord(true, false);
        }

        public double getMinY() {
            return getBoundCoord(false, true);
        }

        public double getMaxY() {
            return getBoundCoord(true, true);
        }

        @Override
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

    private Node root;
    private int count;

    // construct an empty set of points
    public KdTreeDrawable() {
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
            return;
        }
        if (insertInternal(p, root)) {
            count++;
        }
    }

    private boolean insertInternal(Point2D p, Node node) {
        if (p.equals(node.getValue())) {
            return false;
        }
        if (node.compareTo(p) >= 0) {
            if (node.getLeft() != null) {
                return insertInternal(p, node.getLeft());
            } else {
                node.setLeft(new Node(p, node, !node.isVerticalSplit()));
                node.getLeft().calculateAndStoreRectangles();
            }
        } else {
            if (node.getRight() != null) {
                return insertInternal(p, node.getRight());
            } else {
                node.setRight(new Node(p, node, !node.isVerticalSplit()));
                node.getRight().calculateAndStoreRectangles();
            }
        }
        return true;
    }

    private boolean containsInternal(Point2D p, Node node) {
        if (node == null) {
            return false;
        }
        if (p.equals(node.getValue())) {
            return true;
        }
        if (node.compareTo(p) >= 0) {
            return containsInternal(p, node.getLeft());
        }
        return containsInternal(p, node.getRight());
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        ensureNonNull(p);
        return containsInternal(p, root);
    }

    private void drawInternal(Node node) {
        if (node == null) {
            return;
        }

        StdDraw.setPenRadius(0.003);
        if (node.isVerticalSplit()) {
            StdDraw.setPenColor(StdDraw.RED);
            //StdDraw.line(node.getValue().x(), node.getMinY(), node.getValue().x(), node.getMaxY());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            //StdDraw.line(node.getMinX(), node.getValue().y(), node.getMaxX(), node.getValue().y());
        }

        StdDraw.setPenRadius(0.004);
        StdDraw.setPenColor(StdDraw.YELLOW);
        //node.getRect().draw();

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.getValue().draw();

        drawInternal(node.getLeft());
        drawInternal(node.getRight());
    }

    // draw all points to standard draw
    public void draw() {
        if (root == null) {
            return;
        }
        drawInternal(root);
        StdDraw.show();
    }

    private List<Point2D> rangeInternal(RectHV rect, Node node) {
        List<Point2D> results = new ArrayList<>();
        if (node != null && rect.intersects(node.getRect())) {
            results.addAll(rangeInternal(rect, node.getLeft()));
            results.addAll(rangeInternal(rect, node.getRight()));
            if (rect.contains(node.getValue())) {
                results.add(node.getValue());
            }
        }
        return results;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        ensureNonNull(rect);
        return rangeInternal(rect, root);
    }

    private Point2D nearestInternal(Point2D queryPoint, Point2D neighbor, Node node) {
        if (node == null || queryPoint.distanceTo(neighbor) < node.rect.distanceTo(queryPoint)) {
            return neighbor;
        }
        Node firstExplore = node.getLeft();
        Node secondExplore = node.getRight();
        if (node.getRight() != null && node.getRight().getRect().contains(queryPoint)) {
            firstExplore = node.getRight();
            secondExplore = node.getLeft();
        }
        Point2D neighbor1 = nearestInternal(queryPoint, neighbor, firstExplore);
        Point2D neighbor2 = nearestInternal(queryPoint, neighbor1, secondExplore);

        return queryPoint.distanceTo(node.getValue()) < queryPoint.distanceTo(neighbor2) ?
                node.getValue() : neighbor2;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        ensureNonNull(p);
        return !isEmpty() ? nearestInternal(p, root.value, root) : null;
    }

    private void ensureNonNull(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException();
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        
    }
}