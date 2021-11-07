import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Collections;

public class KdTree {

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Point is null");
        }

        root = insert(root, point, null, -1);
    }

    // does the set contain point p?
    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Point is null");
        }

        Node node = root;
        while (node != null) {
            if (node.point.equals(point)) {
                return true;
            }
            double cmp = levelCompareTo(node, point);
            if (cmp < 0) {
                node = node.leftBottom;
            }
            else {
                node = node.rightTop;
            }
        }

        return false;
    }

    // draw all points to standard draw
    public void draw() {
        if (root == null) {
            return;
        }

        StdDraw.setPenRadius(0.01);

        Node node = root;
        Queue<Node> queue = new Queue<>();
        queue.enqueue(node);
        while (!queue.isEmpty()) {
            node = queue.dequeue();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(node.point.x(), node.point.y());
            boolean vertical = node.level % 2 == 0;
            StdDraw.setPenColor(vertical ? StdDraw.RED : StdDraw.BLUE);
            StdDraw.setPenRadius();
            if (node.parent != null) {
                if (vertical) {
                    if (node.parent.point.y() - node.point.y() < 0) {
                        StdDraw.line(node.point.x(), node.parent.point.y(), node.point.x(), 1.0);
                    }
                    else {
                        StdDraw.line(node.point.x(), node.parent.point.y(), node.point.x(), 0.0);
                    }
                }
                else {
                    if (node.parent.point.x() - node.point.x() < 0) {
                        StdDraw.line(node.parent.point.x(), node.point.y(), 1.0, node.point.y());
                    }
                    else {
                        StdDraw.line(node.parent.point.x(), node.point.y(), 0.0, node.point.y());
                    }
                }
            }
            else {
                StdDraw.line(node.point.x(), 0.0, node.point.x(), 0.0);
            }
            if (node.leftBottom != null) {
                queue.enqueue(node.leftBottom);
            }
            if (node.rightTop != null) {
                queue.enqueue(node.rightTop);
            }
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rect is null");
        }

        if (root == null) {
            return Collections.emptyList();
        }

        Queue<Point2D> result = new Queue<>();
        Queue<Node> nodes = new Queue<>();
        nodes.enqueue(root);
        while (!nodes.isEmpty()) {
            Node node = nodes.dequeue();
            if (rect.contains(node.point)) {
                result.enqueue(node.point);
            }
            if (node.leftBottom != null && rect.intersects(node.leftBottom.rectangle)) {
                nodes.enqueue(node.leftBottom);
            }
            if (node.rightTop != null && rect.intersects(node.rightTop.rectangle)) {
                nodes.enqueue(node.rightTop);
            }
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Point is null");
        }

        if (root == null) {
            return null;
        }

        Pair result = new Pair(root.point, root.point.distanceSquaredTo(point));
        result = searchNearest(root, point, result);

        return result.point;
    }

    private Node insert(Node node, Point2D point, Node parent, int parentLevel) {
        if (node == null) {
            size++;
            return new Node(parent, point, parentLevel + 1);
        }

        if (node.point.equals(point)) {
            return node;
        }

        double cmp = levelCompareTo(node, point);
        if (cmp < 0) {
            node.leftBottom = insert(node.leftBottom, point, node, node.level);
        }
        else {
            node.rightTop = insert(node.rightTop, point, node, node.level);
        }
        return node;
    }

    private double levelCompareTo(Node node, Point2D point) {
        if (node.level % 2 == 0) {
            return point.x() - node.point.x();
        }
        else {
            return point.y() - node.point.y();
        }
    }

    private Pair searchNearest(Node node, Point2D point, Pair pair) {
        double distance = node.point.distanceSquaredTo(point);
        if (distance < pair.distance) {
            pair.distance = distance;
            pair.point = node.point;
        }
        boolean leftBottomPossible = node.leftBottom != null
                && node.leftBottom.rectangle.distanceSquaredTo(point) < pair.distance;
        boolean rightTopPossible = node.rightTop != null
                && node.rightTop.rectangle.distanceSquaredTo(point) < pair.distance;
        if (leftBottomPossible && rightTopPossible) {
            if (node.level % 2 == 0 && point.x() < node.point.x()
                    || node.level % 2 != 0 && point.y() < node.point.y()) {
                pair = searchNearest(node.leftBottom, point, pair);
                if (node.rightTop.rectangle.distanceSquaredTo(point) < pair.distance) {
                    pair = searchNearest(node.rightTop, point, pair);
                }
            }
            else if (node.level % 2 == 0 && point.x() >= node.point.x()
                    || node.level % 2 != 0 && point.y() >= node.point.y()) {
                pair = searchNearest(node.rightTop, point, pair);
                if (node.leftBottom.rectangle.distanceSquaredTo(point) < pair.distance) {
                    pair = searchNearest(node.leftBottom, point, pair);
                }
            }
        }
        else if (leftBottomPossible) {
            pair = searchNearest(node.leftBottom, point, pair);
        }
        else if (rightTopPossible) {
            pair = searchNearest(node.rightTop, point, pair);
        }

        return pair;
    }

    private static class Node {
        private final Node parent;
        private final Point2D point;
        private final int level;
        private RectHV rectangle;
        private Node leftBottom;
        private Node rightTop;

        public Node(Node parent, Point2D point, int level) {
            this.parent = parent;
            this.point = point;
            this.level = level;
            initRectangle();
        }

        private void initRectangle() {
            if (parent != null) {
                boolean vertical = parent.level % 2 == 0;
                if (vertical) {
                    if (parent.point.x() <= point.x()) {
                        rectangle = new RectHV(parent.point.x(), parent.rectangle.ymin(),
                                               parent.rectangle.xmax(),
                                               parent.rectangle.ymax());
                    }
                    else {
                        rectangle = new RectHV(parent.rectangle.xmin(), parent.rectangle.ymin(),
                                               parent.point.x(), parent.rectangle.ymax());
                    }
                }
                else {
                    if (parent.point.y() <= point.y()) {
                        rectangle = new RectHV(parent.rectangle.xmin(), parent.point.y(),
                                               parent.rectangle.xmax(),
                                               parent.rectangle.ymax());
                    }
                    else {
                        rectangle = new RectHV(parent.rectangle.xmin(), parent.rectangle.ymin(),
                                               parent.rectangle.xmax(), parent.point.y());
                    }
                }
            }
            else {
                rectangle = new RectHV(0.0, 0.0, 1.0, 1.0);
            }
        }
    }

    private static class Pair {
        private Point2D point;
        private double distance;

        public Pair(Point2D point, double distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // not needed
    }
}