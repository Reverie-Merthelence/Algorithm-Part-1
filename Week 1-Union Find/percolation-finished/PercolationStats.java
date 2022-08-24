/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double sampleMean, sampleStd;
    private final int times;

    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("N or T is less than or equal to 0.");
        }
        times = t;
        double[] x = new double[t];  // the fraction of open sites
        // perform T times experiments
        for (int i = 0; i < t; i++) {
            Percolation perc = new Percolation(n);
            int openCount = 0;
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                    openCount++;
                }
            }
            x[i] = (double) openCount / (n * n);
        }
        sampleMean = StdStats.mean(x);
        sampleStd = StdStats.stddev(x);
    }

    // sample mean of percolation threshold
    public double mean() {
        return sampleMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return sampleStd;
    }

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        double mu = mean();
        double sigma = stddev();
        return mu - CONFIDENCE_95 * sigma / Math.sqrt(times);
    }

    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        double mu = mean();
        double sigma = stddev();
        return mu + CONFIDENCE_95 * sigma / Math.sqrt(times);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("the length of args is not 2.");
        }
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, t);  // if T == 1, stddev() return NaN
        StdOut.println("mean() = " + percStats.mean());
        StdOut.println("stddev() = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats
                .confidenceHi() + "]");
    }
}
