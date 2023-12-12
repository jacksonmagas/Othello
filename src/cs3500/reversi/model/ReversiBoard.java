package cs3500.reversi.model;

import cs3500.reversi.model.Cell.Location;
import java.util.List;

/**
 * The ReversiBoard interface describes any game board that can be used to play a game of reversi.
 * It is intended for internal use of reversi models.
 * The ReversiBoard interface currently only supports 2d boards.
 */
interface ReversiBoard {
  /**
   * Set the cell in the given location to the given state
   * @param location the location to place the cell according to this board's coordinate system
   * @param state the cell state to place in the cell
   * @throws IllegalArgumentException if the location is outside the bounds of this board
   */
  void placeDisc(Cell.Location location, CellState state) throws IllegalArgumentException;

  /**
   * Get the cell at the given location in the board.
   * @param location the location in the board's coordinate system to get a cell at.
   * @return the cell at that location
   * @throws IllegalArgumentException if the location is outside the bounds of this board
   */
  Cell getCellAt(Cell.Location location) throws IllegalArgumentException;

  /**
   * Return a deep copy of this reversi board.
   * The new cells in each position on the board will be new objects that are
   * value equal to the previous cell in the position.
   */
  ReversiBoard copy();

  /**
   * Get the number of tiles in this board with the given state.
   * @param state the state to count the tiles for
   * @return the number of tiles of that state
   */
  int numTiles(CellState state);

  /**
   * Return a list of the contents of this board in all of its major axis.
   * For example a square board would return a list of 2 board representations.
   * The first one would be a List of the rows of the board, while the second would be a list of the
   * columns of the board.
   * @return A list of the board representation that come from viewing the board
   *         along each of its major axis.
   */
  List<List<List<Cell>>> getBoardInAllDirections();

  /**
   * Return a list of locations corresponding to the given location in the board representations
   * returned by {@code getBoardInAllDirections();}.
   * Used to allow easy checks in each direction.
   * @param location The location to start from in the horizontal direction
   * @return A list of locations to start from in each direction.
   */
  List<Location> getLocationInAllDirections(Location location);

  /**
   * Return the side length of this board.
   * @return the side length
   */
  int sideLength();

  /**
   * Reset this board to its starting configuration.
   */
  void reset();

  /**
   * Get the board type of this ReversiBoard.
   * @return the board type of this board
   */
  BoardType getType();
}
