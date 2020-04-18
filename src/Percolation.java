/* *****************************************************************************
 *  Name: Amr Hussien
 *  Date: 2-4-2020
 *  Description: Running the percolation problem
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Variables
    private int gridSize, numOfOpen, vTopSite, vBottomSite;
    private int[][] grid;
    private WeightedQuickUnionUF wquGrid;
    private WeightedQuickUnionUF wquFull;

    // Constructor creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Expected n > 0 and got n = " + n);
        this.gridSize = n;
        wquGrid = new WeightedQuickUnionUF((n * n) + 2);
        wquFull = new WeightedQuickUnionUF((n * n) + 1);
        vBottomSite = n * n + 1;
        vTopSite = 0;
        createGrid();
    }

    // Create initial grid
    public void createGrid() {
        grid = new int[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = 0;
            }
        }
    }

    // Check range
    private void checkRange(int row, int col) {
        if ((row < 0 || row > gridSize - 1) || (col < 0 || col > gridSize - 1))
            throw new IllegalArgumentException(
                    "Expected col and row between 1 and n, but got col = " + col + " row = " + row);
    }

    // Maping from 2D to 1D to access WQU Array
    private int mappingTo1D(int row, int col) {
        return (1 + (row * gridSize + col));
    }

    // To make union in both grid and full
    private void doubleUnion(int point1, int point2) {
        wquGrid.union(point1, point2);
        wquFull.union(point1, point2);
    }

    // Check neighbours of new opened site to see if there is any open site
    private void checkNeighbours(int row, int col) {
        int indexInWQU = mappingTo1D(row, col);
        if (row == 0)
            doubleUnion(indexInWQU, vTopSite);
        else if (isOpen(row - 1, col)) {
            doubleUnion(indexInWQU, mappingTo1D(row - 1, col));
        }
        if (row == gridSize - 1)
            wquGrid.union(indexInWQU, vBottomSite);
        else if (isOpen(row + 1, col)) {
            doubleUnion(indexInWQU, mappingTo1D(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            doubleUnion(indexInWQU, mappingTo1D(row, col - 1));
        }
        if (col < gridSize - 1 && isOpen(row, col + 1)) {
            doubleUnion(indexInWQU, mappingTo1D(row, col + 1));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        row--;
        col--;
        if (isOpen(row, col))
            return;
        checkRange(row, col);
        grid[row][col] = 1;
        checkNeighbours(row, col);
        numOfOpen++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return grid[row][col] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return wquFull.connected(mappingTo1D(row, col), vTopSite);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOfOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return wquGrid.connected(vTopSite, vBottomSite);
    }

    // test client (optional)
    /* public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        System.out.println(perc.mappingTo1D(1, 2));
    }*/

}
