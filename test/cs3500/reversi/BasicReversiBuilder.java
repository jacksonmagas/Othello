package cs3500.reversi;

import cs3500.reversi.model.BasicReversi;
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

  BasicReversiBuilder(int sideLength) {
    this.sideLength = sideLength;
    this.whiteTiles = new ArrayList<>();
    this.blackTiles = new ArrayList<>();
    this.lastPlayerPassed = false;
    this.currentPlayer = CellState.BLACK;
  }

  BasicReversi build() {
    return new BasicReversi(sideLength, lastPlayerPassed, currentPlayer, blackTiles, whiteTiles);
  }

  BasicReversiBuilder wAt(int row, int index) {
    this.whiteTiles.add(new Location(row, index));
    return this;
  }

  BasicReversiBuilder bAt(int row, int index) {
    this.blackTiles.add(new Location(row, index));
    return this;
  }

  BasicReversiBuilder empty(int row, int index) {
    this.whiteTiles.remove(new Location(row, index));
    this.blackTiles.remove(new Location(row, index));
    return this;
  }

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

  BasicReversiBuilder pass() {
    this.lastPlayerPassed = true;
    return this;
  }
}
