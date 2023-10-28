package cs3500.reversi.model;

public enum CellState {
  WHITE("O"),
  BLACK("X"),
  EMPTY("_");

  private final String descriptor;

  public String toString() {
    return descriptor;
  }

  /**
   * Gets the opposite of this cell state
   * @return White if black, black if white, otherwise empty
   */
  public CellState opposite() {
    switch (this) {
      case BLACK:
        return WHITE;
      case WHITE:
        return BLACK;
      default:
        return EMPTY;
    }
  }

  CellState(String d) {
    this.descriptor = d;
  }
}
