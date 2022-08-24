/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF objects, dupObjects;  // one-dimensional union find objects
    private final int gridSize;   // grid size N
    private int count;
    private boolean[][] opened;  // true -> open, false -> blocked. Initialized as false

    // create N-by-N grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("N is less than or equal to 0.");
        gridSize = n;
        count = 0;
        opened = new boolean[n][n];
        objects = new WeightedQuickUnionUF(n * n + 2);  // the first and the last are virtual nodes
        dupObjects = new WeightedQuickUnionUF(n * n + 1);  // the first is virtual-top node
        // link to the virtual-top
        for (int i = 1; i <= n; i++) {
            objects.union(i, 0);
            dupObjects.union(i, 0);
        }
        // link to the virtual-bottom
        for (int i = n * n - n + 1; i <= n * n; i++) {
            objects.union(i, n * n + 1);
        }
    }

    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        validate(i, j);
        if (isOpen(i, j))
            return;
        count++;
        opened[i - 1][j - 1] = true; // mark it as opened
        int p = map(i, j);
        // link to open neighbors of four directions
        int[] dir1 = { 1, -1, 0, 0 };
        int[] dir2 = { 0, 0, 1, -1 };
        for (int idx = 0; idx < dir1.length; idx++) {
            int row = i + dir1[idx];
            int col = j + dir2[idx];
            if (inRange(row, col) && isOpen(row, col)) {
                int q = map(row, col);
                objects.union(p, q);
                dupObjects.union(p, q);
            }
        }
    }

    // check whether (i, j) is open
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return opened[i - 1][j - 1];
    }

    // check whether (i, j) is full
    public boolean isFull(int i, int j) {
        validate(i, j);
        int p = map(i, j);
        if (!isOpen(i, j))
            return false;
        else
            return dupObjects.find(p) == dupObjects.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // check whether the system percolates
    public boolean percolates() {
        if (gridSize == 1)
            return isOpen(1, 1);
        return objects.find(0) == objects.find(gridSize * gridSize + 1);
    }

    /* map two-dimensional (row, column) pair to a one-dimensional union find objects,
       row first*/
    private int map(int i, int j) {
        validate(i, j);   // check bounds
        return (i - 1) * gridSize + j;
        /* because the first node (say, objects(n*n+2)) is the virtual top node, when mapping,
        there would be 1 offset: {(i - 1) * gridSize + j - 1} + 1
         */
    }

    // validate that (i, j) is a valid index
    private void validate(int i, int j) {
        if (i < 1 || i > gridSize)
            throw new IllegalArgumentException("row index " + i + " is not valid.");
        if (j < 1 || j > gridSize)
            throw new IllegalArgumentException("column index " + j + " is not valid.");
    }

    // check whether index (i, j)  is in bound
    private boolean inRange(int i, int j) {
        return (i >= 1 && i <= gridSize) && (j >= 1 && j <= gridSize);
    }

    public static void main(String[] args) {
        Percolation test = new Percolation(3);
        test.open(1, 1);
        test.open(2, 1);
        test.open(3, 1);
        for (int i = 0; i < test.opened.length; i++) {
            for (int j = 0; j < test.opened[i].length; j++) {
                StdOut.print(test.opened[i][j] + " ");
            }
            StdOut.println(" ");

        }
        if (test.percolates()) {
            StdOut.println("counts: " + test.count);
            StdOut.println("percolates!");
        }
        else {
            StdOut.println("not percolate...");
        }
    }
}
