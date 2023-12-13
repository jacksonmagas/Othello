package cs3500.reversi;

import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.BoardType;
import cs3500.reversi.model.Cell.Location;
import cs3500.reversi.model.CellState;
import java.util.ArrayList;
import java.util.List;

/**
 * A BasicReversiBuilder is a class used to make a basic reversi in a certain state for tests.
 */
class BasicReversiBuilder {
  private final int sideLength;
  private boolean lastPlayerPassed;
  private CellState currentPlayer;
  private final List<Location> whiteTiles;
  private final List<Location> blackTiles;
  private BoardType type;

  BasicReversiBuilder(int sideLength) {
    this.sideLength = sideLength;
    this.whiteTiles = new ArrayList<>();
    this.blackTiles = new ArrayList<>();
    this.lastPlayerPassed = false;
    this.currentPlayer = CellState.BLACK;
    this.type = BoardType.HEXAGON;
  }

  /**
   * Terminal operation builds the basic reversi set up by this builder.
   * @return a configured basic reversi
   */
  BasicReversi build() {
    return new BasicReversi(type, sideLength, lastPlayerPassed, currentPlayer,
            blackTiles, whiteTiles);
  }

  /**
   * Terminal operation builds a Mock of the basic reversi set up by this builder.
   * @return a configured basic reversi
   */
  MockModel buildMock() {
    return new MockModel(this.build());
  }

  // add a new white tile at the location
  BasicReversiBuilder wAt(int row, int index) {
    this.whiteTiles.add(new Location(row, index));
    return this;
  }

  // add a new black tile at the location
  BasicReversiBuilder bAt(int row, int index) {
    this.blackTiles.add(new Location(row, index));
    return this;
  }

  // ensure that the tile at the location is empty
  BasicReversiBuilder empty(int row, int index) {
    this.whiteTiles.remove(new Location(row, index));
    this.blackTiles.remove(new Location(row, index));
    return this;
  }

  /**
   * Set the current player for this reversi builder.
   * This is an intermediate operation.
   * @param currentPlayer either 'w' for white or 'b' for black
   * @return the builder
   */
  BasicReversiBuilder player(char currentPlayer) {
    switch (currentPlayer) {
      case 'b':
        this.currentPlayer = CellState.BLACK;
        break;
      case 'w':
        this.currentPlayer = CellState.WHITE;
        break;
      default:
        throw new IllegalArgumentException("Player must be b or w");
    }
    return this;
  }

  BasicReversiBuilder boardType(char type) {
    switch (Character.toLowerCase(type)) {
      case 'h':
        this.type = BoardType.HEXAGON;
        break;
      case 's':
        this.type = BoardType.SQUARE;
        break;
      default:
        throw new IllegalArgumentException("Unsupported board type");
    }
    return this;
  }

  // set the last turn as passed
  BasicReversiBuilder pass() {
    this.lastPlayerPassed = true;
    return this;
  }
}
