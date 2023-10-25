package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the primary model interface for playing a game of Reversi.
 */
public interface ReversiModel {
  void startGame(int noOfCells, int noOfHorizontalMinCells);

  public ArrayList<ArrayList<Cell>> getGrid();

  List<Cell> getPlayerDiscs(Player player);
}
