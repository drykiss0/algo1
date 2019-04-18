import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

public class BoardDrawer {

    private static final int LENGTH = 1000;

    public BoardDrawer() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-LENGTH / 4, 1.25 * LENGTH);
        StdDraw.setYscale(-LENGTH / 4, 1.25 * LENGTH);
        StdDraw.show();
    }

    public void draw(Board board, int sleepAfter) {

        double diff = (double)LENGTH / board.dimension();

        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.filledSquare(LENGTH / 2, LENGTH / 2, LENGTH / 2 + 20);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.filledSquare(LENGTH / 2, LENGTH / 2, LENGTH / 2);

        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 50));
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        StdDraw.setPenRadius(0.005);
        for (int row = 0; row < board.dimension(); row++) {
            for (int col = 0; col < board.dimension(); col++) {
                int block = 0;// TODO uncomment board.getBlock(row, col);
                if (block != 0) {
                    StdDraw.text(diff * col + diff / 2, diff * (board.dimension()-row-1) + diff / 2.5, String.valueOf(block));
                } else {
                    StdDraw.setPenColor(Color.LIGHT_GRAY);
                    StdDraw.filledSquare(diff * col + diff / 2, diff * (board.dimension()-row-1) + diff / 2, diff / 2);
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                }
            }
        }

        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.005);
        for (int i = 0; i <= board.dimension(); i++) {
            StdDraw.line(diff*i, 0, diff*i, LENGTH);
            StdDraw.line(0, diff*i, LENGTH, diff*i);
        }

        StdDraw.show();
        try {
            Thread.sleep(sleepAfter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        In in = new In("8puzzle/puzzle3x3-unsolvable2.txt");
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

        BoardDrawer drawer = new BoardDrawer();
        drawer.draw(initial, 2000);
        Solver solver = new Solver(initial);
        if (solver.isSolvable()) {
            for (Board board: solver.solution()) {
                drawer.draw(board, 2000);
            }
            StdDraw.setPenColor(Color.GREEN);
            StdDraw.text(LENGTH / 2, -LENGTH / 8, "Solved !!!");
            StdDraw.show();

            StdOut.println("\n--------------- SOLUTION -------------");
            StdOut.println("Moves = " + solver.moves());
            for (Board board: solver.solution()) {
                StdOut.println(board);
            }
        } else {
            StdDraw.setPenColor(Color.RED);
            StdDraw.text(LENGTH / 2, -LENGTH / 8, "Not Solvable");
            StdDraw.show();
        }
    }

}
