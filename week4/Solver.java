import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final Stack<Board> solution = new Stack<>();

    private class SearchNode implements Comparable<SearchNode> {
        private final int movesCount;
        private final Board board;
        private final SearchNode predecessor;
        private final int priority;

        public SearchNode(Board board, SearchNode predecessor) {
            this.predecessor = predecessor;
            this.movesCount = predecessor != null ? predecessor.movesCount + 1 : 0;
            this.board = board;
            this.priority = board.manhattan() + movesCount;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPredecessor() {
            return predecessor;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this == that) {
                return 0;
            }
            if (that == null) {
                return 1;
            }
            return (int) Math.signum(this.priority - that.priority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        solve(initial);
    }

    private Comparator<SearchNode> getPriorityComparator() {
        return (b1, b2) -> {
            if (b1 == b2) {
                return 0;
            }
            if (b1 == null) {
                return -1;
            }
            return b1.compareTo(b2);
        };
    }

    private void solve(Board initial) {

        final MinPQ<SearchNode> boardMinQueue = new MinPQ<>(getPriorityComparator());
        final MinPQ<SearchNode> twinMinQueue = new MinPQ<>(getPriorityComparator());

        boardMinQueue.insert(new SearchNode(initial, null));
        twinMinQueue.insert(new SearchNode(initial.twin(), null));
        while (!boardMinQueue.min().getBoard().isGoal() && !twinMinQueue.min().getBoard().isGoal()) {
            enqueueNeighbors(boardMinQueue.delMin(), boardMinQueue);
            enqueueNeighbors(twinMinQueue.delMin(), twinMinQueue);
        }

        if (boardMinQueue.min().getBoard().isGoal()) {
            SearchNode node = boardMinQueue.delMin();
            while (node != null) {
                solution.push(node.getBoard());
                node = node.getPredecessor();
            }
        }
    }

    private void enqueueNeighbors(SearchNode node, MinPQ<SearchNode> minQueue) {
        for (Board neighborBoard : node.getBoard().neighbors()) {
            Board prevBoard = node.getPredecessor() != null ? node.getPredecessor().getBoard() : null;
            if (!neighborBoard.equals(prevBoard)) {
                minQueue.insert(new SearchNode(neighborBoard, node));
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solution.size() > 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solution.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? solution : null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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