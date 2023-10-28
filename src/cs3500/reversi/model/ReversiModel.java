package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the primary model interface for playing a game of Reversi.
 * Moves to this model are made using the coordinate system (row, index) where the row is the
 * horizontal row number and the index is the location in the row 0 indexed from left.
 */
public interface ReversiModel {
  void startGame(int noOfCells);

  void makeMove(int row, int column);

  void passTurn();
}
