import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public final class Solver {

    private Stack<Board> solution;
    private int moves = 0;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Board is null");
        }
        if (initial.isGoal()) {
            solvable = true;
            solution = new Stack<>();
            solution.push(initial);
        }
        else {
            MinPQ<Node> originalQueue = new MinPQ<>(new ManhattanNodeComparator());
            originalQueue.insert(new Node(new Node(), initial, moves));
            Board twin = initial.twin();
            MinPQ<Node> twinQueue = new MinPQ<>(new ManhattanNodeComparator());
            twinQueue.insert(new Node(new Node(), twin, moves));

            while (!solvable && moves != -1) {
                processQueues(originalQueue, twinQueue);
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private void processQueues(MinPQ<Node> original, MinPQ<Node> twin) {
        Node minOriginal = original.delMin();
        Node minTwin = twin.delMin();
        if (minOriginal.board.isGoal()) {
            solvable = true;
            fillSolution(minOriginal);
            return;
        }
        else if (minTwin.board.isGoal()) {
            solvable = false;
            moves = -1;
            return;
        }
        moves = minOriginal.moves + 1;
        for (Board neighbor : minOriginal.board.neighbors()) {
            if (!neighbor.equals(minOriginal.parent.board)) {
                original.insert(new Node(minOriginal, neighbor, moves));
            }
        }
        for (Board neighbor : minTwin.board.neighbors()) {
            if (!neighbor.equals(minTwin.parent.board)) {
                twin.insert(new Node(minTwin, neighbor, moves));
            }
        }
    }

    private void fillSolution(Node node) {
        solution = new Stack<>();
        while (node.board != null) {
            solution.push(node.board);
            node = node.parent;
        }
    }

    private class Node {

        private Node parent;
        private Board board;
        private int moves;
        private int manhattan;

        private Node() {
        }

        private Node(Node parent, Board board, int moves) {
            this.parent = parent;
            this.board = board;
            this.moves = moves;
            manhattan = board.manhattan();
        }
    }

    private class ManhattanNodeComparator implements Comparator<Node> {

        public int compare(Node o1, Node o2) {
            int priority1 = o1.manhattan + o1.moves;
            int priority2 = o2.manhattan + o2.moves;
            if (priority1 != priority2) {
                return priority1 - priority2;
            }
            else {
                return o1.manhattan - o2.manhattan;
            }
        }
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
