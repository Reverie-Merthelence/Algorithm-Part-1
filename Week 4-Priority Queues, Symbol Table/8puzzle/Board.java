import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private final int n;
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {  // defensive copy of mutable instance variables
            for (int j = 0; j < n; j++)
                this.tiles[i][j] = tiles[i][j];
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int shouldBe = 1; // check each element in array, a[0][0] should be 1, a[0][1] should be 2
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!(tiles[i][j] == shouldBe) && !(tiles[i][j] == 0))
                    count++;
                shouldBe++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!(tiles[i][j] == 0)) {
                    int shouldI = (tiles[i][j] - 1) / n;
                    int shouldJ = (tiles[i][j] - 1) % n;
                    int temp = Math.abs(shouldI - i) + Math.abs(shouldJ - j);
                    manhattan += temp;
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.n != this.n) {
            return false;
        }
        // Compare each element in both arrays.
        else return Arrays.deepEquals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // find the index of element 0;
        int zeroI = -1;
        int zeroJ = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }
        Queue<Board> neighbour = new Queue<>();
        // check if the zero element is at the first row
        if (zeroI - 1 >= 0) {
            Board neighbor1 = new Board(this.tiles);
            // exchange a[i][j] with a[i-1][j]
            neighbor1.exch(zeroI, zeroJ, zeroI - 1, zeroJ);
            neighbour.enqueue(neighbor1);
        }
        // check if the zero element is at the bottom row
        if (zeroI + 1 <= n - 1) {
            Board neighbor2 = new Board(this.tiles);
            // exchange a[i][j] with a[i+1][j]
            neighbor2.exch(zeroI, zeroJ, zeroI + 1, zeroJ);
            neighbour.enqueue(neighbor2);
        }
        // check if the zero element is at the left column
        if (zeroJ - 1 >= 0) {
            Board neighbor3 = new Board(this.tiles);
            // exchange a[i][j] with a[i][j-1]
            neighbor3.exch(zeroI, zeroJ, zeroI, zeroJ - 1);
            neighbour.enqueue(neighbor3);
        }
        // check if the zero element is at the right column
        if (zeroJ + 1 <= n - 1) {
            Board neighbor4 = new Board(this.tiles);
            // exchange a[i][j] with a[i][j+1]
            neighbor4.exch(zeroI, zeroJ, zeroI, zeroJ + 1);
            neighbour.enqueue(neighbor4);
        }
        return neighbour;
    }

    private void exch(int i1, int j1, int i2, int j2) {
        int temp = this.tiles[i1][j1];
        this.tiles[i1][j1] = this.tiles[i2][j2];
        this.tiles[i2][j2] = temp;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int exchI = -1;
        int exchJ = -1;
        //  exchange a[i][j] with a[i][j+1]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (this.tiles[i][j] != 0 && this.tiles[i][j + 1] != 0) {
                    exchI = i;
                    exchJ = j;
                }
            }
        }
        Board twin = new Board(this.tiles);
        twin.exch(exchI, exchJ, exchI, exchJ + 1);
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // print manhattan ()
        StdOut.println(initial.manhattan());

    }
}
