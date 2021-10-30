import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;
    private int segmentsNumber;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validatePoints(points);
        this.points = new Point[points.length];
        System.arraycopy(points, 0, this.points, 0, points.length);
        segments = new LineSegment[points.length * points.length];
        processPoints();
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsNumber;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[segmentsNumber];
        System.arraycopy(segments, 0, lineSegments, 0, segmentsNumber);
        return lineSegments;
    }

    private void validatePoints(Point[] pointsToValidate) {
        if (pointsToValidate == null) {
            throw new IllegalArgumentException("Points array is null");
        }
        for (Point p : pointsToValidate) {
            if (p == null) {
                throw new IllegalArgumentException("At least one point is null");
            }
        }
        for (int i = 0; i < pointsToValidate.length - 1; i++) {
            for (int j = i + 1; j < pointsToValidate.length; j++) {
                if (pointsToValidate[i].compareTo(pointsToValidate[j]) == 0) {
                    String message = String.format("Points at indexes %d and %d are equal", i, j);
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    private void processPoints() {
        Point[] sortedPoints = new Point[points.length];
        System.arraycopy(points, 0, sortedPoints, 0, points.length);
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(sortedPoints, 0, sortedPoints.length, points[i].slopeOrder());
            int slopeCounter = 0;
            for (int j = 1; j < sortedPoints.length - 1; j++) {
                if (sortedPoints[0].slopeTo(sortedPoints[j])
                        == sortedPoints[0].slopeTo(sortedPoints[j + 1])) {
                    slopeCounter++;
                }
                else if (slopeCounter >= 2) {
                    addOrSkipSegment(sortedPoints, j, slopeCounter);
                    slopeCounter = 0;
                }
                else {
                    slopeCounter = 0;
                }
            }
            if (slopeCounter >= 2) {
                addOrSkipSegment(sortedPoints, sortedPoints.length - 1, slopeCounter);
            }
        }
    }

    private void addOrSkipSegment(Point[] sortedPoints, int j, int slopes) {
        int first = j - slopes;
        Arrays.sort(sortedPoints, first, j + 1);
        if (pointsInAscendingOrder(sortedPoints[0], sortedPoints[first])) {
            segments[segmentsNumber++] = new LineSegment(sortedPoints[0], sortedPoints[j]);
        }
    }

    private boolean pointsInAscendingOrder(Point current, Point compared) {
        return current.compareTo(compared) < 0;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("input20.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}