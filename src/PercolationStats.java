/* *****************************************************************************
 *  Name: PercolationStats
 *  Date: 5-9-2019
 *  Description: Perform percolation many times to get P threshold
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] thresholds;
    private final int numOfTrials, gridSide;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        } else {
            this.numOfTrials = trials;
            this.gridSide = n;
            thresholds = new double[numOfTrials];
            performTests();
        }
    }

    // Perform actual test
    private void performTests() {
        Percolation perObj;
        for (int i = 0; i < numOfTrials; i++) {
            perObj = new Percolation(gridSide);
            while (!perObj.percolates()) {
                openRandomSite(perObj);
            }
            thresholds[i] = (double) perObj.numberOfOpenSites() / (gridSide * gridSide);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(numOfTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(numOfTrials));
    }

    // Open Random site in the network
    private void openRandomSite(Percolation p) {
        int row, col;
        while (true) {
            row = StdRandom.uniform(1, gridSide + 1);
            col = StdRandom.uniform(1, gridSide + 1);
            if (!p.isOpen(row, col))
                break;
        }
        p.open(row, col);
    }

    // test client (see below)
    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(num, trails);
        System.out.println("Mean:\t\t\t\t = " + p.mean());
        System.out.println("Stddev:\t\t\t\t = " + p.stddev());
        System.out.println("95% Confidence interval:\t = " + p.confidenceLo() + ", " + p.confidenceHi());
    }
}
