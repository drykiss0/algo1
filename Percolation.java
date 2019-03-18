import edu.princeton.cs.algs4.StdArrayIO;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final WeightedQuickUnionUF uf;
    private final boolean[] sites;
    private int openSitesCount;
    private final int vsIdx;
    private final int veIdx;

    // create n-by-n grid, with all setSite blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n should be > 0");
        }
        this.n = n;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.sites = new boolean[n * n + 2];
        this.vsIdx = 0;
        this.veIdx = this.sites.length - 1;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        if (row == 1) {
            uf.union(vsIdx, idx(row, col));
        }
        if (row == n) {
            uf.union(veIdx, idx(row, col));
        }
        unionNeighbors(row, col);
        sites[idx(row, col)] = true;
        openSitesCount++;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateArguments(n, row, col);
        return sites[idx(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validateArguments(n, row, col);
        return (row == 1 && isOpen(row, col)) || uf.connected(vsIdx, idx(row, col));
    }

    // number of open setSite
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(vsIdx, veIdx);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        StdArrayIO.print(perc.sites);

        perc.open(1, 1);
        perc.open(2, 2);
        perc.open(3, 3);
        perc.open(1, 2);
        StdArrayIO.print(perc.sites);
        StdOut.println("Percolates? " + perc.percolates());

        perc.open(2, 3);
        StdArrayIO.print(perc.sites);
        StdOut.println("Percolates? " + perc.percolates());
    }

    private void unionNeighbors(int row, int col) {
        int[] neighborsIndices = {idx(row - 1, col), idx(row + 1, col), idx(row, col - 1), idx(row, col + 1)};
        for (int neighborIdx : neighborsIndices) {
            if (neighborIdx > -1 && sites[neighborIdx]) {
                uf.union(idx(row, col), neighborIdx);
            }
        }
    }

    private int idx(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            return -1;
        }
        return n * (row - 1) + col;
    }

    private void validateArguments(int count, int... args) {
        for (int p : args) {
            if (p < 1 || p > count) {
                throw new IllegalArgumentException("index " + p + " is not between 1 and " + count);
            }
        }
    }
}