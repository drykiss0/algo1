import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double MEAN_CONSTANT = 1.96;
    private final int trials;
    private final double[] fractions;
    private Double mean;
    private Double stddev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("n and trials should be > 0");
        }
        this.trials = trials;
        this.fractions = new double[trials];
        int sitesCount = n * n;
        int[] closedSites = new int[sitesCount];
        for (int t = 0; t < trials; t++) {
            for (int c = 0; c < closedSites.length; c++) {
                closedSites[c] = c;
            }
            Percolation perc = new Percolation(n);
            int closedCount = sitesCount;
            do {
                int randomOpenIdx = StdRandom.uniform(closedCount);
                int siteIdx = closedSites[randomOpenIdx];
                perc.open(siteIdx / n + 1, siteIdx % n + 1);
                closedSites[randomOpenIdx] = closedSites[closedCount - 1];
                closedCount--;
            } while (!perc.percolates());
            fractions[t] = (double) (sitesCount - closedCount) / sitesCount;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (this.mean == null) {
            this.mean = StdStats.mean(fractions);
        }
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (this.stddev == null) {
            this.stddev = StdStats.stddev(fractions);
        }
        return this.stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - MEAN_CONSTANT * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + MEAN_CONSTANT * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean\t\t\t\t\t= " + ps.mean());
        StdOut.println("sstddev\t\t\t\t\t= " + ps.stddev());
        StdOut.println("95% confidence interval\t= [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}