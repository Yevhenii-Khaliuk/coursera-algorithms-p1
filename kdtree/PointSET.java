import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

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
    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Point is null");
        }
        points.add(point);
    }

    // does the set contain point p?
    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Point is null");
        }

        return points.contains(point);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rect is null");
        }

        List<Point2D> result = new ArrayList<>();

        for (Point2D point : points) {
            if (rect.contains(point)) {
                result.add(point);
            }
        }

        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D thatPoint) {
        if (thatPoint == null) {
            throw new IllegalArgumentException("Point is null");
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D minDistancePoint = null;

        for (Point2D point : points) {
            double distance = point.distanceSquaredTo(thatPoint);
            if (distance < minDistance) {
                minDistance = distance;
                minDistancePoint = point;
            }
        }

        return minDistancePoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // not needed
    }
}