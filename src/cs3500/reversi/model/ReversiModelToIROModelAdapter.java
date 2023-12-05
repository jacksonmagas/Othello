package cs3500.reversi.model;

import cs3500.reversi.provider.controller.IModelFeatures;
import cs3500.reversi.provider.model.Board;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IMutableModel;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.PlayerDisc;
import cs3500.reversi.provider.model.Status;
import java.util.List;
import java.util.Optional;

/**
 * This class is an adaptor for a model that implements ReversiModel to allow it to be used
 * in IROModel based APIs.
 */
public class ReversiModelToIROModelAdapter implements IROModel {
  private ReversiModel base;

  public ReversiModelToIROModelAdapter(ReversiModel base) {
    this.base = base;
  }

  @Override
  public boolean isGameOver() {
    return base.isGameOver();
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
    return null;
  }

  @Override
  public PlayerDisc getNextPlayerTurn() {
    return null;
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
