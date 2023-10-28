package cs3500.reversi.model;

class Cell {
  private CellState state;
  private Location location;

  /**
   * A location with row 0 indexed from top, and column 0 indexed from right
   */
  static class Location
  {
    public Location(int row, int column)
    {
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
