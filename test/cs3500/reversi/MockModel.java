package cs3500.reversi;

import cs3500.reversi.controller.YourTurnListener;
import cs3500.reversi.model.Cell.Location;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;
import java.util.List;

public class MockModel implements ReversiModel {
  private final ReversiModel delegate;
  private final StringBuilder transcript;
  private final boolean isCopy;

  public MockModel(ReversiModel delegate) {
    this.delegate = delegate;
    isCopy = false;
    this.transcript = new StringBuilder();
  }

  // copy this mock, maintaining references to the same transcript but copying the delegate
  private MockModel(MockModel template) {
    this.delegate = template.delegate.copy();
    this.transcript = template.transcript;
    this.isCopy = true;
  }

  /** 
   * print the message to the transcript.
   * @param msg message to print
   */
  private void printT(String msg) {
    transcript.append(msg).append(System.lineSeparator());
  }
  
  @Override
  public int getPlayerScore(CellState player) {
    printT("GetPlayerScore: " + player);
    return delegate.getPlayerScore(player);
  }

  @Override
  public CellState getCurrentPlayer() {
    printT("GetCurrentPlayer: ");
    return delegate.getCurrentPlayer();
  }

  @Override
  public boolean isGameOver() {
    printT("isGameOver: ");
    return delegate.isGameOver();
  }

  @Override
  public List<List<CellState>> getGameBoard() {
    printT("getGameBoard: ");
    return delegate.getGameBoard();
  }

  @Override
  public int[][] getBoard() {
    printT("getBoard: ");
    return delegate.getBoard();
  }

  @Override
  public CellState getTileAt(int hRow, int hIndex) {
    printT("getTileAt: ");
    return delegate.getTileAt(hRow, hIndex);
  }

  @Override
  public int sideLength() {
    printT("sideLength: ");
    return delegate.sideLength();
  }

  @Override
  public boolean anyLegalMoves() {
    printT("anyLegalMoves: ");
    return delegate.anyLegalMoves();
  }

  @Override
  public String getNextStepInstructions() {
    printT("getNextStepInstructions: ");
    return delegate.getNextStepInstructions();
  }

  @Override
  public String getLastErrorMessage() {
    printT("getLastErrorMessage: ");
    return delegate.getLastErrorMessage();
  }

  @Override
  public CellState getPieceAt(int r, int c) {
    printT(String.format("getPieceAt: r = %d, c = %d%n", r, c));
    return delegate.getPieceAt(r, c);
  }

  @Override
  public List<Move> getLegalMoves() {
    printT("getLegalMoves: ");
    return delegate.getLegalMoves();
  }

  @Override
  public Location getHighlightedCell() {
    printT("getHighlightedCell: ");
    return delegate.getHighlightedCell();
  }

  @Override
  public ReversiModel copy() {
    printT("copy: ");
    return new MockModel(this);
  }

  @Override
  public int getColumns(int row) {
    printT(String.format("getColumns: row = %d%n", row));
    return delegate.getColumns(row);
  }

  @Override
  public int getRows() {
    printT("getRows: ");
    return delegate.getRows();
  }

  @Override
  public void refreshAllViews() {
    printT("refreshAllViews: ");
    delegate.refreshAllViews();
  }

  @Override
  public void makeMove(int row, int column) {
    Move move = new Move(row, column);
    transcript.append(isCopy ? "Testing move: " : "Making move: ").append(move);
    transcript.append(" for ").append(getCurrentPlayer());
    delegate.makeMove(row, column);
  }

  @Override
  public void makeMove(Move move) {
    transcript.append(isCopy ? "Testing move: " : "Making move: ").append(move);
    transcript.append(" for ").append(getCurrentPlayer());
    delegate.makeMove(move);
  }

  @Override
  public void passTurn() {
    printT("passTurn ");
    transcript.append("passing turn/n");
    delegate.passTurn();
  }

  @Override
  public void setHighlightedCell(int row, int col) {
    printT("setHighlightedCell: ");
    delegate.setHighlightedCell(row, col);
  }

  @Override
  public void newGame() {
    printT("newGame: ");
    delegate.newGame();
  }

  @Override
  public void startGame() {
    printT("startGame: ");
    delegate.startGame();
  }

  @Override
  public void addYourTurnListener(YourTurnListener listener) {
    printT("addTurnListener for player: " + listener.getPlayer());
    delegate.addYourTurnListener(listener);
  }

  /**
   * Show the transcript from this mock.
   * @return the transcript from this mock
   */
  String viewTranscript() {
    return this.transcript.toString();
  }
}
