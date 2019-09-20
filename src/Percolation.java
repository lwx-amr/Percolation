import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] openedSites;
    private int vTopSite, vBottomSite, numOpenSites, gridSide;
    private WeightedQuickUnionUF wufGrid;
    private WeightedQuickUnionUF wufFull;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.gridSide = n;
        openedSites = new boolean[gridSide][gridSide];
        wufGrid = new WeightedQuickUnionUF(gridSide * gridSide + 2);
        wufFull = new WeightedQuickUnionUF(gridSide * gridSide + 1);
        vTopSite = gridSide * gridSide;
        vBottomSite = gridSide * gridSide + 1;
        numOpenSites = 0;
    }

    // union certain node
    private void unionSite(int index1, int index2) {
        wufGrid.union(index1, index2);
        wufFull.union(index1, index2);
    }

    // Check if indexes are valid
    private boolean areIndexesValid(int row, int col) {
        if (row < 1 || row > gridSide || col < 1 || col > gridSide) return false;
        return true;
    }

    public void open(int row, int col) {
        if (isOpen(row, col))
            return;
        if (!areIndexesValid(row, col))
            throw new java.lang.IllegalArgumentException();

        openedSites[row - 1][col - 1] = true;
        numOpenSites++;

        if (row == 1) {
            unionSite(col - 1, vTopSite);
        }
        if (row == gridSide) wufGrid.union((row - 1) * gridSide + col - 1, vBottomSite);
        if (row > 1 && isOpen(row - 1, col)) {
            unionSite((row - 1) * gridSide + col - 1, (row - 2) * gridSide + col - 1);
        }
        if (row < gridSide && isOpen(row + 1, col)) {
            unionSite((row - 1) * gridSide + col - 1, row * gridSide + col - 1);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            unionSite((row - 1) * gridSide + col - 1, (row - 1) * gridSide + col - 2);
        }
        if (col < gridSide && isOpen(row, col + 1)) {
            unionSite((row - 1) * gridSide + col - 1, (row - 1) * gridSide + col);
        }
    }

    public boolean isOpen(int row, int col) {
        if (areIndexesValid(row, col)) {
            return openedSites[row - 1][col - 1];
        }
        throw new java.lang.IllegalArgumentException();
    }

    public boolean isFull(int row, int col) {
        if (areIndexesValid(row, col)) {
            return wufFull.connected((row - 1) * gridSide + col - 1, vTopSite);
        }
        throw new java.lang.IllegalArgumentException();
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        return wufGrid.connected(vTopSite, vBottomSite);
    }
}
