package cs3500.reversi;

import cs3500.reversi.controller.YourTurnListener;
import cs3500.reversi.model.Cell.Location;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;
import java.util.List;

public class MockModel implements ReversiModel {

  @Override
  public int getPlayerScore(CellState player) {
    System.out.println("GetPlayerScore: " + player);
    return 0;
  }

  @Override
  public CellState getCurrentPlayer() {
    System.out.println("GetCurrentPlayer: ");
    return null;
  }

  @Override
  public boolean isGameOver() {
    System.out.println("isGameOver: ");
    return false;
  }

  @Override
  public List<List<CellState>> getGameBoard() {
    System.out.println("getGameBoard: ");
    return null;
  }

  @Override
  public int[][] getBoard() {
    System.out.println("getBoard: ");
    return new int[0][];
  }

  @Override
  public CellState getTileAt(int hRow, int hIndex) {
    System.out.println("getTileAt: ");
    return null;
  }

  @Override
  public int sideLength() {
    System.out.println("sideLength: ");
    return 0;
  }

  @Override
  public boolean anyLegalMoves() {
    System.out.println("anyLegalMoves: ");
    return false;
  }

  @Override
  public Location getFirstAvailableMove() {
    System.out.println("getFirstAvailableMove: ");
    return null;
  }

  @Override
  public String getNextStepInstructions() {
    System.out.println("getNextStepInstructions: ");
    return null;
  }

  @Override
  public String getLastErrorMessage() {
    System.out.println("getLastErrorMessage: ");
    return null;
  }

  @Override
  public CellState getPieceAt(int r, int c) {
    System.out.printf("getPieceAt: r = %d, c = %d%n", r, c);
    return null;
  }

  @Override
  public List<Move> getLegalMoves() {
    System.out.println("getLegalMoves: ");
    return null;
  }

  @Override
  public Location getHighlightedCell() {
    System.out.println("getHighlightedCell: ");
    return null;
  }

  @Override
  public ReversiModel copy() {
    System.out.println("copy: ");
    return null;
  }

  @Override
  public int getColumns(int row) {
    System.out.printf("getColumns: row = %d%n", row);
    return 0;
  }

  @Override
  public int getRows() {
    System.out.println("getRows: ");
    return 0;
  }

  @Override
  public void refreshAllViews() {
    System.out.println("refreshAllViews: ");
  }

  @Override
  public void makeMove(int row, int column) {
    System.out.printf("makeMove: row = %d, column = %d%n", row, column);
  }

  @Override
  public void makeMove(Move move) {
    System.out.println("makeMove: " + move.toString());
  }

  @Override
  public void passTurn() {
    System.out.println("passTurn ");
  }

  @Override
  public void setHighlightedCell(int row, int col) {
    System.out.println("setHighlightedCell: ");
  }

  @Override
  public void newGame() {
    System.out.println("newGame: ");
  }

  @Override
  public void startGame() {
    System.out.println("startGame: ");
  }

  @Override
  public void addYourTurnListener(YourTurnListener listener) {
    System.out.println("addTurnListener for player: " + listener.getPlayer());
  }
}
