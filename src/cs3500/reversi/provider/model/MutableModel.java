package cs3500.reversi.provider.model;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.provider.controller.IModelFeatures;

public class MutableModel implements IMutableModel {

  private final int center;
  private final Board board;
  private PlayerDisc currentDisc;

  public MutableModel(int sideLength, Board board, PlayerDisc currentDisc) {
    this.center = sideLength - 1;
    this.board = board;
    this.currentDisc = currentDisc;
  }

  @Override
  public void startGame(boolean initializeGrid) {

  }

  @Override
  public void makeMove(int coordQ, int coordR, PlayerDisc who) {

  }

  @Override
  public void skipTurn(PlayerDisc who) {

  }

  @Override
  public void notifyPlayerTurn(PlayerDisc player) {

  }

  @Override
  public void notifyGameOver(String winner) {

  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getScore(PlayerDisc p) {
    return 0;
  }

  @Override
  public PlayerDisc getWinner() {
    return null;
  }

  @Override
  public Status getStatus() {
    return null;
  }

  @Override
  public int getRadius() {
    return 0;
  }

  @Override
  public boolean isBoardFull() {
    return false;
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public PlayerDisc getNextPlayerTurn() {
    return currentDisc;
  }

  @Override
  public int countDiscs(PlayerDisc playerDisc) {
    return 0;
  }

  @Override
  public PlayerDisc getDiscAt(int coordQ, int coordR) {
    return null;
  }

  @Override
  public int getNumOfLegalMovesForCurrentPlayer() {
    return 0;
  }

  @Override
  public boolean isLegal(int q, int r) {
    return false;
  }

  @Override
  public List<Hex> getInboundNeighbors(int q, int r) {
    return null;
  }

  @Override
  public List<List<Hex>> getAllValidPaths(Hex startHex) {
    return null;
  }

  @Override
  public Optional<Hex> getLatestMove() {
    return Optional.empty();
  }

  @Override
  public void addFeatures(IModelFeatures features) {

  }

  @Override
  public IMutableModel getPreviousModel(IROModel currentModel) {
    return null;
  }

  @Override
  public boolean isGameStarted() {
    return false;
  }
}
