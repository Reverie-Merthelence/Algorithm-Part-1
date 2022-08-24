/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // define Search Node class
    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int move;
        private final int manCost;
        // private final int hamCost;
        private final SearchNode previous;
        private final boolean isTwin;

        // constructor
        public SearchNode(Board board, int move, SearchNode previous, boolean isTwin) {
            this.board = board;
            this.move = move;
            this.previous = previous;
            this.manCost = manhattanFunction();
            // this.hamCost = hammingFunction();
            this.isTwin = isTwin;
        }

        /* public int hammingFunction() {
            return (this.board.hamming() + this.move);
        } */

        public int manhattanFunction() {
            return (this.board.manhattan() + this.move);
        }

        // compare this node and that node by Manhattan function (replaced by Integer compare)
        public int compareTo(SearchNode that) {
            return Integer.compare(this.manCost, that.manCost);
        }
    }

    // --------------------------------
    private boolean solved;
    private Stack<Board> tracking = new Stack<>();
    private int minMove = -1;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("the initial board is null");

        // initialize initial search node, and put it in both priority queue
        SearchNode initialSearchNode = new SearchNode(initial, 0, null, false);
        SearchNode initialSearchNodeTwin = new SearchNode(initial.twin(), 0, null, true);
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(initialSearchNode);
        pq.insert(initialSearchNodeTwin);

        while (true) {
            SearchNode deletedNode = pq.delMin();

            // StdOut.println("dequeued node is:" + deletedNode.board);
            if (deletedNode.board.isGoal()) { // check if the deleted node has the goal board.
                if (!deletedNode.isTwin) {
                    solved = true;
                    minMove = deletedNode.move;
                    // start with the deleted node (i.e., goal node), put them in stack
                    while (deletedNode.previous != null) {
                        tracking.push(deletedNode.board);
                        deletedNode = deletedNode.previous;
                    }
                    tracking.push(initial); // put the initial node at the top of the stack
                }
                break;
            }

            for (Board a : deletedNode.board.neighbors()) { // for each neighbor "a" of the deleted node
                SearchNode addedSearchNode = new SearchNode(a, deletedNode.move + 1, deletedNode,
                                                            deletedNode.isTwin);
                /* critical optimization: check if the neighbor "a" equals to the parent of deletedNode*/
                if (deletedNode.previous == null)
                    pq.insert(addedSearchNode);
                else if (!a.equals(deletedNode.previous.board))
                    pq.insert(addedSearchNode);
                // StdOut.println("inserted node is:" + addedSearchNode.board);
            }
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        // if unsolvable, then the minMove value is still -1
        return minMove;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        else
            return tracking;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
