import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int n;
    private double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials have to be greater than 0");
        }

        this.n = n;
        results = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            performTrial(i, percolation);
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double stddev = stddev();
        return mean - getConfidenceIntervalDifference(stddev, results.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double stddev = stddev();
        return mean + getConfidenceIntervalDifference(stddev, results.length);
    }

    private double getConfidenceIntervalDifference(double stddev, int trials) {
        return 1.96 * stddev / Math.sqrt(trials);
    }

    private void performTrial(int trial, Percolation percolation) {
        while (!percolation.percolates()) {
            openSite(percolation);
        }
        savePercolationThreshold(trial, percolation);
    }

    private void openSite(Percolation percolation) {
        int row = StdRandom.uniform(n) + 1;
        int col = StdRandom.uniform(n) + 1;
        while (percolation.isOpen(row, col)) {
            row = StdRandom.uniform(n) + 1;
            col = StdRandom.uniform(n) + 1;
        }
        percolation.open(row, col);
    }

    private void savePercolationThreshold(int trial, Percolation percolation) {
        double threshold = (double) percolation.numberOfOpenSites() / (n * n);
        results[trial] = threshold;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.printf("mean = %f%n", stats.mean());
        StdOut.printf("stddev = %f%n", stats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]%n", stats.confidenceLo(),
                      stats.confidenceHi());
    }
}