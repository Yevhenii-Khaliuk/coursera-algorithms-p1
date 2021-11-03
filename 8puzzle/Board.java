import edu.princeton.cs.algs4.Queue;

import java.util.NoSuchElementException;

public class Board {

    private final int[][] tiles;
    private final int hamming;
    private final int manhattan;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, tiles[i].length);
        }
        hamming = calculateHamming();
        manhattan = calculateManhattan();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(tiles.length).append("\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.tiles.length != that.tiles.length) {
            return false;
        }
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles.length; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] blanks = findBlankIndexes();
        int blankI = blanks[0];
        int blankJ = blanks[1];
        Queue<Board> neighbors = new Queue<>();
        if (blankJ > 0) {
            int[][] neighbor = copyTiles();
            int tmp = neighbor[blankI][blankJ];
            neighbor[blankI][blankJ] = neighbor[blankI][blankJ - 1];
            neighbor[blankI][blankJ - 1] = tmp;
            neighbors.enqueue(new Board(neighbor));
        }
        if (blankJ < tiles.length - 1) {
            int[][] neighbor = copyTiles();
            int tmp = neighbor[blankI][blankJ];
            neighbor[blankI][blankJ] = neighbor[blankI][blankJ + 1];
            neighbor[blankI][blankJ + 1] = tmp;
            neighbors.enqueue(new Board(neighbor));
        }
        if (blankI > 0) {
            int[][] neighbor = copyTiles();
            int tmp = neighbor[blankI][blankJ];
            neighbor[blankI][blankJ] = neighbor[blankI - 1][blankJ];
            neighbor[blankI - 1][blankJ] = tmp;
            neighbors.enqueue(new Board(neighbor));
        }
        if (blankI < tiles.length - 1) {
            int[][] neighbor = copyTiles();
            int tmp = neighbor[blankI][blankJ];
            neighbor[blankI][blankJ] = neighbor[blankI + 1][blankJ];
            neighbor[blankI + 1][blankJ] = tmp;
            neighbors.enqueue(new Board(neighbor));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = copyTiles();
        int firstI = firstNonblankTile(twinTiles[0]);
        int secondI = firstNonblankTile(twinTiles[1]);
        int temp = twinTiles[0][firstI];
        twinTiles[0][firstI] = twinTiles[1][secondI];
        twinTiles[1][secondI] = temp;
        return new Board(twinTiles);
    }

    private int calculateHamming() {
        int result = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                int expectedValue = i * tiles.length + j + 1;
                if (tiles[i][j] != expectedValue) {
                    result++;
                }
            }
        }
        return result;
    }

    private int calculateManhattan() {
        int result = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                int expectedRow = (tiles[i][j] - 1) / tiles.length;
                int remainder = tiles[i][j] % tiles.length;
                int expectedColumn = remainder == 0 ? tiles.length - 1 : remainder - 1;
                result += Math.abs(expectedRow - i) + Math.abs(expectedColumn - j);
            }
        }
        return result;
    }

    private int[] findBlankIndexes() {
        int[] indexes = new int[2];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    indexes[0] = i;
                    indexes[1] = j;
                    return indexes;
                }
            }
        }
        throw new NoSuchElementException("Blank element not found");
    }

    private int[][] copyTiles() {
        int[][] arr = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            System.arraycopy(tiles[i], 0, arr[i], 0, tiles.length);
        }
        return arr;
    }

    private int firstNonblankTile(int[] subArray) {
        for (int i = 0; i < subArray.length; i++) {
            if (subArray[i] != 0) {
                return i;
            }
        }
        throw new NoSuchElementException("Nonblank tile not found");
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 2, 0 }, { 1, 3 } };
        Board board = new Board(tiles);
        System.out.println("Test board");
        System.out.println(board);
        System.out.println("Board dimension (should be 2): " + board.dimension());
        System.out.println("Board hamming (should be 3): " + board.hamming());
        System.out.println("Board manhattan (should be 3): " + board.manhattan());
        System.out.println("Board isGoal(): " + board.isGoal());
        Board twin = board.twin();
        System.out.println("Twin board");
        System.out.println(twin);
        System.out.println("Test board and twin are equal: " + board.equals(twin));
        System.out.println("Board neighbors");
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor);
        }
        int[][] goalTiles = { { 1, 2 }, { 3, 0 } };
        Board goalBoard = new Board(goalTiles);
        System.out.println("Goal board");
        System.out.println(goalBoard);
        System.out.println("Goal board isGoal(): " + goalBoard.isGoal());
        Board test = new Board(new int[][] { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } });
        System.out.println("Test: " + test.manhattan());
    }
}