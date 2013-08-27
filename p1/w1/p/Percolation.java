public class Percolation {
  private WeightedQuickUnionUF uf;
  private boolean[] sitesStatus;
  private int size;
  private int virtual_top;
  private int virtual_bottom;

  /** create N-by-N grid, with all sites blocked */
  public Percolation(int N) {
    size = N;
    // the last two site for virtual_top and virtual_bottom
    uf = new WeightedQuickUnionUF(size * size + 2);
    virtual_top = size * size;
    virtual_bottom = size * size + 1;

    // initialize all site blocked
    sitesStatus = new boolean[size*size + 2];
    for (int i = 0; i < size*size; i++) {
      sitesStatus[i] = false;
    }
    // initialize two virtual site open
    sitesStatus[virtual_top] = true;
    sitesStatus[virtual_bottom] = true;

    // connect each site in first row to virtual_top
    for (int i = 0; i < size; i++) {
      uf.union(virtual_top, i);
    }

    // connect each site in last row to virtual_bottom
    for (int i = (size-1)*size; i < size*size; i++) {
      uf.union(virtual_bottom, i);
    }

  }

  /** open site (row i, column j) if it is not already */
  // i,j between 1 and N
  public void open(int i, int j) {
    if (!validIndex(i, j)) {
      throw new IndexOutOfBoundsException("row or/and column index out of bounds");
    }

    if (!isOpen(i, j)) {
      int index_in_uf = xyTo1D(i, j);
      sitesStatus[index_in_uf] = true;
      connect_two_site(i, j, i-1, j);
      connect_two_site(i, j, i+1, j);
      connect_two_site(i, j, i  , j-1);
      connect_two_site(i, j, i  , j+1);
    }

  }

  private void connect_two_site(int x1, int y1, int x2, int y2) {
    if (validIndex(x2, y2)) {
      if (isOpen(x2, y2)) {
        int p = xyTo1D(x1, y1);
        int q = xyTo1D(x2, y2);
        uf.union(p, q);
      }
    }
  }

  /** is site (row i, column j) open? */
  public boolean isOpen(int i, int j) {
    int n = xyTo1D(i, j);
    return sitesStatus[n];
  }

  /** is site (row i, column j) full? */
  public boolean isFull(int i, int j) {
    int p = xyTo1D(i, j);
    return isOpen(i, j) && uf.connected(p, virtual_top);
  }

  /** does the system percolate? */
  public boolean percolates() {
    return uf.connected(virtual_bottom, virtual_top);
  }

  // convert 2 dimensional(row, column) pair to 1 dimensional index
  private int xyTo1D(int r, int c) {
    return ((r - 1) * size + c) - 1;
  }

  private boolean validIndex(int r, int c) {
    if (r < 1 || r > size || c < 1 || c > size) {
      return false;
    } else {
      return true;
    }
  }

  public static void main(String[] args) {
    Percolation p = new Percolation(20);
    p.open(1, 1);
    p.open(1, 2);

    StdOut.println(p.percolates());
    StdOut.println(p.isFull(1, 1));
    StdOut.println(p.isFull(1, 2));
    StdOut.println(p.isFull(1, 3));

  }

}