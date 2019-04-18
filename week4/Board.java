import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final int[][] blocks;
    private int zeroRow;
    private int zeroCol;
    private int hamming = 0;
    private int manhattan = 0;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException();
        }

        this.blocks = deepCopy(blocks);
        initializeDistances();
        initializeZeroPosition();
    }

    private void initializeZeroPosition() {
        for (int r = 0; r < blocks.length; r++) {
            for (int c = 0; c < blocks[r].length; c++) {
                if (blocks[r][c] == 0) {
                    zeroRow = r;
                    zeroCol = c;
                    return;
                }
            }
        }
    }

    private void initializeDistances() {
        for (int r = 0; r < blocks.length; r++) {
            for (int c = 0; c < blocks[r].length; c++) {
                final int distance = getManhattanDistance(r, c);
                manhattan += distance;
                if (distance > 0) {
                    hamming++;
                }
            }
        }
    }

    private int getManhattanDistance(int row, int col) {
        final int block = blocks[row][col];
        if (block == 0) {
            return 0;
        }
        return Math.abs((block - 1) / blocks.length - row) + Math.abs((block - 1) % blocks.length - col);
    }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (blocks.length < 2) {
            return new Board(blocks);
        }
        int firstBlockCol = blocks[0][0] != 0 ? 0 : 1;
        int secondBlockCol = blocks[1][0] != 0 ? 0 : 1;
        return createSwapped(0, firstBlockCol, 1, secondBlockCol);
    }

    private Board createSwapped(int rowFirst, int colFirst, int rowSecond, int colSecond) {

        int[][] blocksCopy = deepCopy(blocks);
        int firstBlock = blocksCopy[rowFirst][colFirst];
        blocksCopy[rowFirst][colFirst] = blocksCopy[rowSecond][colSecond];
        blocksCopy[rowSecond][colSecond] = firstBlock;

        return new Board(blocksCopy);
    }

    private int[][] deepCopy(int[][] source) {
        int[][] blocksCopy = source.clone();
        for (int i = 0; i < blocksCopy.length; i++) {
            blocksCopy[i] = blocksCopy[i].clone();
        }
        return blocksCopy;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Board that = (Board) other;
        return Arrays.deepEquals(blocks, that.blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        if (zeroRow > 0) {
            neighbors.add(createSwapped(zeroRow, zeroCol, zeroRow - 1, zeroCol));
        }
        if (zeroRow < dimension() - 1) {
            neighbors.add(createSwapped(zeroRow, zeroCol, zeroRow + 1, zeroCol));
        }
        if (zeroCol > 0) {
            neighbors.add(createSwapped(zeroRow, zeroCol, zeroRow, zeroCol - 1));
        }
        if (zeroCol < dimension() - 1) {
            neighbors.add(createSwapped(zeroRow, zeroCol, zeroRow, zeroCol + 1));
        }

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder(String.valueOf(blocks.length));
        for (int i = 0; i < blocks.length; i++) {
            s.append("\n");
            for (int j = 0; j < blocks[i].length; j++) {
                s.append(" ").append(blocks[i][j]);
            }
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {

        In in = new In("8puzzle/puzzle3x3-02.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println(initial);
        StdOut.println("Hamming = " + initial.hamming());
        StdOut.println("Manhattan = " + initial.manhattan());
        StdOut.println("\n---------- NEIGHBORS --------");
        for (Board neighbor: initial.neighbors()) {
            StdOut.println(neighbor);
        }
        StdOut.println("---------- TWIN ----------");
        Board twin = initial.twin();
        StdOut.println(twin);
        StdOut.println("Twin Hamming = " + twin.hamming());
        StdOut.println("Twin Manhattan = " + twin.manhattan());
        StdOut.println("\n---------- EQUALS --------");
        StdOut.println("Initial = Initial ? " + initial.equals(initial));
        StdOut.println("Initial = Twin ? " + initial.equals(twin));

    }
}