package cs3500.reversi.model;

import cs3500.reversi.model.Cell.Location;
import java.util.ArrayList;
import java.util.List;

/**
 * It implements the primary model interface for playing a game of Reversi.
 */
public class BasicReversi implements ReversiModel {
  //Keeps track of the cells in the game in 3 lists, one for each principal direction of a hexagon
  //Invariant: All three lists contain the same cells
  //the horizontal rows 0 indexed from top
  private final ArrayList<ArrayList<Cell>> horizontalRows;
  //the rows which follow the down-right direction 0 indexed from top right of hexagon
  private final ArrayList<ArrayList<Cell>> downRightRows;
  //the rows which follow the down-left direction 0 indexed from top left of hexagon
  private final ArrayList<ArrayList<Cell>> downLeftRows;

  boolean isGameON = false;

  private int sideLength;
  private int numRows;

  public BasicReversi() {
    this.horizontalRows = new ArrayList<ArrayList<Cell>> ();
    this.downRightRows = new ArrayList<ArrayList<Cell>> ();
    this.downLeftRows = new ArrayList<ArrayList<Cell>> ();
  }

  //put the cell with the given coordinates at the right spot in all three lists
  private void setEmptyCellAt(int row, int col) {
    Cell c = new Cell(new Location(row, col));
    int mid = sideLength + 1;
    int hIndex = Math.abs(row - mid) - col;
    horizontalRows.get(row).add(hIndex, c);
    
  }

  @Override
  public void startGame(int sideLength) {
    if (this.isGameON) {
      throw new IllegalStateException("Game is already started!");
    }
    if (sideLength <= 3 || (sideLength % 2) != 0) {
      throw new IllegalArgumentException("sideLength should be at-least 4 or above and even !");
    }

    //initialize game
    this.sideLength = sideLength;
    this.isGameON = true;
    numRows = 2 * this.sideLength + 1;

    int rowSize = sideLength;
    int middleRowNum = sideLength;

    // build grid

    for (int rowNum = 0; rowNum < sideLength; rowNum++) {
      ArrayList<Cell> rows = new ArrayList<Cell>();
      for (int col = 0; col < rowSize; col++) {
        Cell pt = new Cell(new Location(rowNum, col));
        rows.add(pt);
      }
      grid.add(rows);
    }

      //place each players 2 discs
      int startPosition = (sideLength/2) - 1;
      grid.get(startPosition).get(startPosition).setState(CellState.BLACK);
      grid.get(startPosition).get(startPosition+1).setState(CellState.WHITE);
      grid.get(startPosition+1).get(startPosition).setState(CellState.WHITE);
      grid.get(startPosition+1).get(startPosition+1).setState(CellState.BLACK);
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
    for (int row = 0; row < this.noOfCellsInLongestRow; row++) {
      ArrayList<Cell> rows = new ArrayList<Cell>();
      for (int col = 0; col < noOfCellsInLongestRow; col++) {
        Cell cell = grid.get(row).get(col);
        if (cell.getState().equals(player)) {
          discs.add(cell);
        }
      }
    }
    return discs;
  }

  @Override
  public String toString() {
    String output = "";
    for (int row = 0; row < this.noOfCellsInLongestRow; row++) {
      ArrayList<Cell> rows = new ArrayList<Cell>();
      for (int col = 0; col < noOfCellsInLongestRow; col++) {
        Cell cell = grid.get(row).get(col);
        output += cell.toString() + " ";
      }
      output += "\n";
    }
    return output;
  }
}
