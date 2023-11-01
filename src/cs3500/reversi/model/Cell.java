package cs3500.reversi.model;

/**
 * A cell is a space for a tile to be placed in inside the hexagon grid.
 */
class Cell {
  private CellState state;
  private Location location;

  /**
   * A location with row 0 indexed from top, and column 0 indexed from right.
   */
  static class Location {
    public Location(int row, int column) {
      this.row = row;
      this.column = column;
    }

    public final int row;
    public final int column;
  }

  public Cell(Location point) {
    this.location = point;
    this.state = CellState.EMPTY;
  }

  public Cell(Location point, CellState state) {
    this.location = point;
    this.state = state;
  }

  /**
   * Finds if this cell is empty.
   * @return True if this cell is empty.
   */
  public boolean isEmpty() {
    return this.state == CellState.EMPTY;
  }

  public CellState getState() {
    return state;
  }

  public void setState(CellState state) {
    this.state = state;
  }

  @Override
  public String toString() {
    String output = "_";
    if (this.state != null) {
      output = this.state.toString();
    }
    return output;
  }
}
