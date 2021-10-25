import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF data;
    private final int gridSize;
    private final int topSiteIndex;
    private final int bottomSiteIndex;
    private int openSites;
    private boolean[] open;
    private int[] bottomlessParent;
    private int[] bottomlessSize;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n has to be greater than 0");
        }

        gridSize = n;
        int elementsNumber = n * n;
        data = new WeightedQuickUnionUF(elementsNumber + 2);
        topSiteIndex = elementsNumber;
        bottomSiteIndex = elementsNumber + 1;
        openSites = 0;
        open = new boolean[elementsNumber];
        bottomlessParent = new int[elementsNumber + 1];
        bottomlessSize = new int[elementsNumber + 1];
        for (int i = 0; i <= elementsNumber; i++) {
            bottomlessParent[i] = i;
            bottomlessSize[i] = 1;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;
        }

        if (gridSize == 1) {
            open[0] = true;
            openSites++;
            connectSideRowsToVirtualSites(row, 0);
            return;
        }

        int currentSite = getIndex(row, col);
        open[currentSite] = true;
        openSites++;

        connectOpenAdjacentSites(currentSite, row, col);
        connectSideRowsToVirtualSites(row, currentSite);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        int index = getIndex(row, col);
        return open[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        int currentIndex = getIndex(row, col);
        return bottomlessConnected(currentIndex, topSiteIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        int topSiteRoot = data.find(topSiteIndex);
        int bottomSiteRoot = data.find(bottomSiteIndex);
        return topSiteRoot == bottomSiteRoot;
    }

    private int getIndex(int row, int col) {
        return (row - 1) * gridSize + (col - 1);
    }

    private void connectOpenAdjacentSites(int currentSite, int row, int col) {
        if (row + 1 <= gridSize) {
            int adjacentSite = currentSite + gridSize;
            connectSites(currentSite, adjacentSite);
        }
        if (row - 1 >= 1) {
            int adjacentSite = currentSite - gridSize;
            connectSites(currentSite, adjacentSite);
        }
        if (col + 1 <= gridSize) {
            int adjacentSite = currentSite + 1;
            connectSites(currentSite, adjacentSite);
        }
        if (col - 1 >= 1) {
            int adjacentSite = currentSite - 1;
            connectSites(currentSite, adjacentSite);
        }
    }

    private void connectSites(int currentSite, int adjacentSite) {
        if (open[adjacentSite]) {
            if (!areConnected(currentSite, adjacentSite)) {
                data.union(currentSite, adjacentSite);
            }
            if (!bottomlessConnected(currentSite, adjacentSite)) {
                bottomlessUnion(currentSite, adjacentSite);
            }
        }
    }

    private void connectSideRowsToVirtualSites(int row, int currentSite) {
        if (row == 1) {
            if (!areConnected(currentSite, topSiteIndex)) {
                data.union(currentSite, topSiteIndex);
            }
            if (!bottomlessConnected(currentSite, topSiteIndex)) {
                bottomlessUnion(currentSite, topSiteIndex);
            }
        }
        if (row == gridSize && !areConnected(currentSite, bottomSiteIndex)) {
            data.union(currentSite, bottomSiteIndex);
        }
    }

    private boolean areConnected(int firstSite, int secondSite) {
        return data.find(firstSite) == data.find(secondSite);
    }

    private void validate(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
            throw new IllegalArgumentException(
                    "row and col have to be between 1 and " + gridSize + " inclusively");
        }
    }

    private boolean bottomlessConnected(int firstSite, int secondSite) {
        return bottomlessFind(firstSite) == bottomlessFind(secondSite);
    }

    private void bottomlessUnion(int p, int q) {
        int rootP = bottomlessFind(p);
        int rootQ = bottomlessFind(q);
        if (rootP == rootQ) return;

        if (bottomlessSize[rootP] < bottomlessSize[rootQ]) {
            bottomlessParent[rootP] = rootQ;
            bottomlessSize[rootQ] += bottomlessSize[rootP];
        }
        else {
            bottomlessParent[rootQ] = rootP;
            bottomlessSize[rootP] += bottomlessSize[rootQ];
        }
    }

    private int bottomlessFind(int p) {
        while (p != bottomlessParent[p])
            p = bottomlessParent[p];
        return p;
    }

    // test client (optional)
    public static void main(String[] args) {
        // not needed
    }
}