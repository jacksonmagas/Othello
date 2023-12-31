package cs3500.reversi.model;

import java.util.Objects;

/**
 * A cell is a space for a tile to be placed in inside the hexagon grid.
 */
public class Cell {

  private CellState state;
  private final Location location;

  /**
   * A location with row 0 indexed from top, and column 0 indexed from right.
   */
  public static class Location {

    /**
     * Constructor for public class Location.
     */
    public Location(int row, int column) {
      this.row = row;
      this.column = column;
    }

    final int row;
    final int column;

    // gets the row of a cell
    public int getRow() {
      return row;
    }

    // gets the column of a cell
    public int getColumn() {
      return column;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Location location = (Location) o;
      return row == location.row && column == location.column;
    }

    @Override
    public int hashCode() {
      return Objects.hash(row, column);
    }
  }

  /**
   * Create a new cell at the given location and with the given state.
   * @param point the location of the cell.
   * @param state the state of the cell
   */
  Cell(Location point, CellState state) {
    this.location = point;
    this.state = state;
  }

  /**
   * Create an empty cell at the given location.
   * @param point the location to put the cell.
   */
  Cell(Location point) {
    this(point, CellState.EMPTY);
  }

  /**
   * Create copy of the base cell.
   * @param base the cell to copy.
   */
  Cell(Cell base) {
    this.state = base.state;
    this.location = base.location;
  }

  /**
   * Finds if this cell is empty.
   * @return True if this cell is empty.
   */
  boolean isEmpty() {
    return this.state == CellState.EMPTY;
  }

  CellState getState() {
    return state;
  }

  void setState(CellState state) {
    this.state = state;
  }


  Location getLocation() {
    return this.location;
  }

  /**
   * Turns output to a String.
   */
  @Override
  public String toString() {
    String output = "_";
    if (this.state != null) {
      output = this.state.toString();
    }
    return output;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cell cell = (Cell) o;
    return state == cell.state && Objects.equals(location, cell.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, location);
  }
}
