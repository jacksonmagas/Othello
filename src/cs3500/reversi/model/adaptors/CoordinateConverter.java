package cs3500.reversi.model.adaptors;

import cs3500.reversi.model.Move;
import cs3500.reversi.provider.model.Hex;

/**
 * Utility class to allow for conversions between the 0 indexed row, 0 indexed position in the row
 * coordinate system and the axial coordinate system for hexagons.
 * Each CoordinateConverter object works for only hexagons of a specific size.
 */
public class CoordinateConverter {
  int sideLength;

  public CoordinateConverter(int sideLength) {
    this.sideLength = sideLength;
  }

  /**
   * Get the row of all hexes with the given axial r coordinate.
   * The q coordinate is not needed to determine row, only board size and r.
   * @param r the r coordinate
   * @return the row of the cell
   */
  public int rowFromAxial(int r) {
    int mid = sideLength - 1;
    return r + mid;
  }

  /**
   * Get the column of the hex at the axial coordinate (q, r).
   * @param q the q coordinate
   * @param r the r coordinate
   * @return the column of the cell
   */
  public int colFromAxial(int q, int r) {
    int mid = sideLength - 1;
    if (r < 0) {
      return q + mid + r;
    } else {
      return q + mid;
    }
  }

  /**
   * Get the Move object that would make a move in the hex at the axial coordinate (q, r).
   * @param q the q coordinate
   * @param r the r coordinate
   * @return the move that corresponds to make-move q r
   */
  public Move moveFromAxial(int q, int r) {
    return new Move(rowFromAxial(r), colFromAxial(q, r));
  }

  /**
   * Get the hex pointed to by the given row and column by doing a coordinate conversion.
   * @param row the row for the hex
   * @param col the column for the hex
   * @return the hex at the row and column
   */
  public Hex hexFromRowCol(int row, int col) {
    int mid = sideLength - 1;
    int r = row - mid;
    int q = row >= mid ? col - mid : col - mid - r;
    return new Hex(q, r);
  }
}
