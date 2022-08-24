/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    /* Don't know the number of line segments, so use array list (or link list) to
    store them temporarily, then use storeLineSeg.toArray(new LineSegment[count]) to copy link list to array
    see lines 70~74 for details */
    private ArrayList<LineSegment> storeLineSeg;
    private int count;  // count the number of segments

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        // corner cases
        if (points == null)
            throw new IllegalArgumentException("Argument to constructor is null");

        int n = points.length;  // length of points array
        for (int i = 0; i < n; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("One point in the array is null");
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Repeated point in the array");
                }
            }
        }

        // copy the points array into "copy"
        Point[] copy = Arrays.copyOf(points, n);

        // brute-force starts
        Arrays.sort(copy);  // sort the Point[] copy based on locations
        storeLineSeg = new ArrayList<>();
        for (int h = 0; h < n; h++) {
            for (int i = h + 1; i < n; i++) {
                double slope1 = copy[h].slopeTo(copy[i]);
                for (int j = i + 1; j < n; j++) {
                    double slope2 = copy[h].slopeTo(copy[j]);
                    if (slope1 == slope2) {
                        for (int k = j + 1; k < n; k++) {
                            double slope3 = copy[h].slopeTo(copy[k]);
                            if (slope1 == slope3) {
                                count++;
                                // create a new line segment, and add it to the link list
                                storeLineSeg.add(new LineSegment(copy[h], copy[k]));
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {       // the number of line segments
        return count;
    }

    public LineSegment[] segments() {    // the line segments
        // copy link list to lineSegment
        LineSegment[] lineSegment = storeLineSeg.toArray(new LineSegment[count]);
        return lineSegment;
    }


    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
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
        StdDraw.setPenRadius(0.01);  // make the points a bit larger
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // reset the pen radius
        StdDraw.setPenRadius();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
