package cs3500.reversi.model;

public enum CellState {
  WHITE("O"),
  BLACK("X"),
  EMPTY("_");

  private final String descriptor;

  public String toString() {
    return descriptor;
  }

  CellState(String d) {
    this.descriptor = d;
  }
}
