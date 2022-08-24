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

public class FastCollinearPoints {
    private ArrayList<Double> storeSlope; // store slopes for checking duplication
    private ArrayList<LineSegment> storeLineSeg;  // store line segments temporarily
    private int count;  // count the number of segments


    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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


        // copy the points array into "copy", and sort it by coordinates
        Point[] copy = Arrays.copyOf(points, n);
        Arrays.sort(copy);
/*
        StdOut.println("start sorting based on coordination");
        for (int i = 0; i < n; i++) {
            StdOut.print(copy[i] + " - ");
        }
        StdOut.println("Cord Sorting Complete----------");
*/

        storeLineSeg = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Point origin = copy[i];
            // there can be two points in the array with the same slopes, so create each ArrayList for each point
            storeSlope = new ArrayList<>();
            // copy the points array again into "copyForSlope", and sort it by slope order of original
            Point[] copyForSlope = Arrays.copyOf(copy, n);
            Arrays.sort(copyForSlope, origin.slopeOrder());
/*
            StdOut.println("start sorting based on slope with" + origin);
            for (int o = 0; o < n; o++) {
                StdOut.print(copyForSlope[o] + " - ");
            }
            StdOut.println("Slope Sorting Complete----------");
 */

            int j = 0;
            int head = -1;
            int tail = -1;
            boolean found = false;
            /* given the origin, find if there are points meeting the requirements,
            if so, set "found" as yes and store the index into tail;
             */
            while (j + 3 < n) {
                /* if found three points having the same slopes with the origin points
                and if origin is less than copyForSlope[j + 1]*/
                if (origin.slopeTo(copyForSlope[j + 1]) == origin.slopeTo(copyForSlope[j + 2])
                        && origin.slopeTo(copyForSlope[j + 2]) == origin.slopeTo(
                        copyForSlope[j + 3])) {
                    head = j + 1;
                    found = true;
                    /*  check if there are more than three points, if so, move index pointing to the tail.
                    Also, put j+4 < n before the second condition, need to make sure j+4<n first. Otherwise,
                    index can be out of bounds */
                    while (j + 4 < n && origin.slopeTo(copyForSlope[j + 3]) == origin.slopeTo(
                            copyForSlope[j + 4])) {
                        j++;
                    }
                    tail = j + 3;
                }

                /* if the slope hasn't appeared before, and if the origin is smaller than the second point
                store the slope and the segments
                 */
                if (found && !storeSlope.contains(origin.slopeTo(copyForSlope[tail]))
                        && origin.compareTo(copyForSlope[head]) < 0) {
                    storeLineSeg.add(new LineSegment(origin, copyForSlope[tail]));
                    storeSlope.add(origin.slopeTo(copyForSlope[tail]));
                    // StdOut.println("founded" + origin + " -> " + copyForSlope[tail]);
                    count++;
                }
                found = false;
                j++;

            }
        }
    }

    public int numberOfSegments() { // the number of line segments
        return count;
    }

    public LineSegment[] segments() { // the line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
