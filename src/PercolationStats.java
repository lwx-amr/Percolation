/* *****************************************************************************
 *  Name: Amr Hussien
 *  Date: 2-4-2020
 *  Description: Runner for Percolation problem to calc some states
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // Variables
    private static final double CONFIDENCE_95 = 1.96;
    private int numOfTrials, gridSize;
    private double[] thresholds;

    // perform independent numOfTrials on an n-by-n grid
    public PercolationStats(int n, int numOfTrials) {
        if (n <= 0 || numOfTrials <= 0)
            throw new IllegalArgumentException(
                    "Expected n and trials > 0 and got n = " + n + " and trials = " + numOfTrials);
        this.numOfTrials = numOfTrials;
        this.gridSize = n;
        this.thresholds = new double[numOfTrials];
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

    // Get random numbers and open this site
    private void openRandomSite(Percolation percObj) {
        int row = StdRandom.uniform(1, gridSize + 1);
        int col = StdRandom.uniform(1, gridSize + 1);
        percObj.open(row, col);
    }

    // Main process
    public void runner() {
        Percolation percObj;
        for (int i = 0; i < numOfTrials; i++) {
            percObj = new Percolation(gridSize);
            while (!percObj.percolates())
                openRandomSite(percObj);
            thresholds[i] = (double) percObj.numberOfOpenSites() / (gridSize * gridSize);

        }
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats pStates = new PercolationStats(Integer.parseInt(args[0]),
                                                        Integer.parseInt(args[1]));
        pStates.runner();
        System.out.println("Mean:\t\t\t\t = " + pStates.mean());
        System.out.println("Stddev:\t\t\t\t = " + pStates.stddev());
        System.out.println("95% Confidence interval:\t = " + pStates.confidenceLo() + ", " + pStates
                .confidenceHi());
    }

}
