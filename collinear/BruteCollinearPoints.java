public class BruteCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;
    private int segmentsNumber;


    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        validatePoints(points);
        this.points = new Point[points.length];
        System.arraycopy(points, 0, this.points, 0, points.length);
        segments = new LineSegment[points.length];
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
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (j == i) {
                    continue;
                }
                for (int k = 0; k < points.length; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    if (twoSlopesAreEqual(points[i], points[j], points[k])) {
                        for (int m = 0; m < points.length; m++) {
                            if (m == i || m == j || m == k) {
                                continue;
                            }
                            if (twoSlopesAreEqual(points[j], points[k], points[m])) {
                                addOrSkipSegment(i, j, k, m);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addOrSkipSegment(int i, int j, int k, int m) {
        if (pointsInAscendingOrder(i, j, k, m)) {
            segments[segmentsNumber++] = new LineSegment(points[i], points[m]);
        }
    }

    private boolean pointsInAscendingOrder(int i, int j, int k, int m) {
        return points[i].compareTo(points[j]) < 0
                && points[j].compareTo(points[k]) < 0
                && points[k].compareTo(points[m]) < 0;
    }

    private boolean twoSlopesAreEqual(Point p1, Point p2, Point p3) {
        return p1.slopeTo(p2) == p2.slopeTo(p3);
    }
}