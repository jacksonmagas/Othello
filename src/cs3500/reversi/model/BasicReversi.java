package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * It implements the primary model interface for playing a game of Reversi.
 */
public class BasicReversi implements ReversiModel {

  private ArrayList<ArrayList<Cell>> grid;
  boolean isGameON = false;

  private int noOfCells = 0;
  private int noOfHorizontalMinCells = 0;

  @Override
  public void startGame(int noOfCells, int noOfHorizontalMinCells) {
    if (this.isGameON) {
      throw new IllegalStateException("Game is already started!");
    }
    if (noOfCells <= 3 || (noOfCells % 2) != 0) {
      throw new IllegalArgumentException("noOfCells should be at-least 4 or above and even !");
    }

    //initialize game
    this.noOfCells = noOfCells;
    this.noOfHorizontalMinCells = noOfHorizontalMinCells;
    this.isGameON = true;

    // build grid
    if (noOfHorizontalMinCells == 0) {
      grid = new ArrayList<ArrayList<Cell>> ();
      for (int row = 0; row < noOfCells; row++) {
        ArrayList<Cell> rows = new ArrayList<Cell>();
        for (int col = 0; col < noOfCells; col++) {
          Cell pt = new Cell(new Point(row, col));
          rows.add(pt);
        }
        grid.add(rows);
      }

      //place each players 2 discs
      int startPosition = (noOfCells/2) - 1;
      grid.get(startPosition).get(startPosition).setPlayer(new BlackPlayer());
      grid.get(startPosition).get(startPosition+1).setPlayer(new WhitePlayer());
      grid.get(startPosition+1).get(startPosition).setPlayer(new WhitePlayer());
      grid.get(startPosition+1).get(startPosition+1).setPlayer(new BlackPlayer());
    }

  }

  public ArrayList<ArrayList<Cell>> getGrid() {
    if (this.isGameON) {
      return grid;
    } else {
      throw new IllegalStateException("Game is not yet started!");
    }

  }

  public List<Cell> getPlayerDiscs(Player player) {
    List<Cell> discs = new ArrayList<Cell> ();
    for (int row = 0; row < this.noOfCells; row++) {
      ArrayList<Cell> rows = new ArrayList<Cell>();
      for (int col = 0; col < noOfCells; col++) {
        Cell cell = grid.get(row).get(col);
        if (cell.getPlayer().equals(player)) {
          discs.add(cell);
        }
      }
    }
    return discs;
  }

  @Override
  public String toString() {
    String output = "";
    for (int row = 0; row < this.noOfCells; row++) {
      ArrayList<Cell> rows = new ArrayList<Cell>();
      for (int col = 0; col < noOfCells; col++) {
        Cell cell = grid.get(row).get(col);
        output += cell.toString() + " ";
      }
      output += "\n";
    }
    return output;
  }
}
