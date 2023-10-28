package cs3500.reversi.model;

import cs3500.reversi.model.Cell.Location;
import java.util.ArrayList;
import java.util.List;

/**
 * It implements the primary model interface for playing a game of Reversi.
 * Moves to this model are made using the coordinate system (row, index) where the row is the
 * horizontal row number and the index is the location in the row 0 indexed from left.
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
  private int totalNumRows;

  public BasicReversi() {
    this.horizontalRows = new ArrayList<ArrayList<Cell>> ();
    this.downRightRows = new ArrayList<ArrayList<Cell>> ();
    this.downLeftRows = new ArrayList<ArrayList<Cell>> ();
  }

  // put the cell with the given horizontal row and index into the correct location in all three
  // lists
  private void setEmptyCellAt(int hRow, int hIndex) {
    Cell c = new Cell(new Location(hRow, hIndex));
    horizontalRows.get(hRow).add(hIndex, c);
    /*
    int lRow = getLRow(hRow, hIndex);
    int lIndex = getLIndex(hRow, hIndex);
    downLeftRows.get(lRow).add(lIndex, c);
    int rRow = getRRow(hRow, hIndex);
    int rIndex = getRIndex(hRow, hIndex);
    downRightRows.get(rRow).add(rIndex, c);
    */
  }

  //get the downRightRow row coordinate of the cell at the given horizontal row and index
  private int getRRow(int hRow, int hIndex) {
    if (hRow <= sideLength) {
      //at and above the centerline the RRow is mid - hIndex + hRow
      return sideLength - hIndex + hRow;
    } else {
      //n rows below the centerline the RRow is mid - hIndex + hRow - n
      return 2 * sideLength - hIndex;
    }
  }

  //get the downRightRow index coordinate of the cell at the given horizontal row and index
  private int getRIndex(int hRow, int hIndex) {
    int rRow = getRRow(hRow, hIndex);
    if (rRow <= sideLength) {
      // left of the downLeft centerline the lIndex is the horizontal row
      return hRow;
    } else {
      // n hIndicies to the right of downLeft centerline the lIndex is the hRow - n
      return hRow - rRow + sideLength;
    }
  }

  //get the downLeftRow row coordinate of the cell at the given horizontal row and index
  private int getLRow(int hRow, int hIndex) {
    if (hRow <= sideLength) {
      //at and above the centerline the LRow is simply the hIndex
      return hIndex;
    } else {
      //n rows below the centerline the LRow is hIndex + n
      return hIndex + hRow - sideLength;
    }
  }

  //get the downLeftRow index coordinate of the cell at the given horizontal row and index
  private int getLIndex(int hRow, int hIndex) {
    int lRow = getLRow(hRow, hIndex);
    if (lRow <= sideLength) {
      // left of the downLeft centerline the lIndex is the horizontal row
      return hRow;
    } else {
      // n hIndicies to the right of downLeft centerline the lIndex is the hRow - n
      return hRow - lRow + sideLength;
    }
  }

  @Override
  public void startGame(int sideLength) {
    if (this.isGameON) {
      throw new IllegalStateException("Game is already started!");
    }
    if (sideLength < 6 || (sideLength % 2) != 0) {
      throw new IllegalArgumentException("sideLength should be at-least 6 or above and even !");
    }

    //initialize game
    this.sideLength = sideLength;
    this.isGameON = true;

    int rowSize = sideLength;
    totalNumRows = 2 * this.sideLength - 1;

    //create empty arrayLists to hold cells
    for (int row = 0; row < totalNumRows; row++) {
      this.horizontalRows.add(new ArrayList<>());
      this.downRightRows.add(new ArrayList<>());
      this.downLeftRows.add(new ArrayList<>());
    }

    // build grid
    for (int rowNum = 0; rowNum < totalNumRows; rowNum++) {
      for (int col = 0; col < rowSize; col++) {
        setEmptyCellAt(rowNum, col);
      }
      if (rowNum < sideLength) {
        rowSize++;
      } else {
        rowSize--;
      }
    }

      //place each players 2 discs
    horizontalRows.get(this.sideLength - 2).get(this.sideLength - 2).setState(CellState.BLACK);
    horizontalRows.get(this.sideLength - 2).get(this.sideLength - 1).setState(CellState.WHITE);
    horizontalRows.get(this.sideLength - 1).get(this.sideLength - 2).setState(CellState.WHITE);
    horizontalRows.get(this.sideLength - 1).get(this.sideLength).setState(CellState.BLACK);
    horizontalRows.get(this.sideLength).get(this.sideLength - 2).setState(CellState.BLACK);
    horizontalRows.get(this.sideLength).get(this.sideLength - 1).setState(CellState.WHITE);
  }

  public ArrayList<ArrayList<Cell>> getGrid() {
    if (this.isGameON) {
      return horizontalRows;
    } else {
      throw new IllegalStateException("Game is not yet started!");
    }

  }

  public List<Cell> getPlayerDiscs(Player player) {
    List<Cell> discs = new ArrayList<Cell> ();
    /*
    for (int row = 0; row < this.noOfCellsInLongestRow; row++) {
      ArrayList<Cell> rows = new ArrayList<Cell>();
      for (int col = 0; col < noOfCellsInLongestRow; col++) {
        Cell cell = grid.get(row).get(col);
        if (cell.getState().equals(player)) {
          discs.add(cell);
        }
      }
    }
    */
    return discs;
  }

  @Override
  public String toString() {
    String output = "";
    int rowSize = sideLength;
    for (int rowNum = 0; rowNum < totalNumRows; rowNum++) {
      String rowStr = "";
      for (int col = 0; col < rowSize; col++) {
        Cell cell = horizontalRows.get(rowNum).get(col);
        rowStr += cell.toString() + " ";
      }
      if (rowNum < sideLength-1) {
        rowSize++;
      } else {
        rowSize--;
      }
      int leftSpaces = (totalNumRows + (totalNumRows - 1) - rowStr.length() + 1)/2;
      output += ((leftSpaces > 0) ? String.format("%-"+leftSpaces+"s", " ") : "") + rowStr + ((leftSpaces > 0) ? String.format("%-"+leftSpaces+"s", " ") : "");
      output += "\n";
    }
    return output;
  }
}
