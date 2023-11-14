package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;
import cs3500.reversi.strategy.MoveStrategy;

/**
 * A simple Player implementation that delegates most of its complexity to a {@link MoveStrategy}
 * for choosing where to play next.
 */
public class PlayerImpl implements Player {

  private final CellState piece;

  private MoveStrategy moveStrategy;

  /**
   * Constructor for PlayerImp class.
   */
  public PlayerImpl(CellState piece, MoveStrategy strategy) {
    this.piece = piece;
    this.moveStrategy = strategy;
  }

  /**
   * Plays the game.
   */
  @Override
  public Move play(ReversiModel model) {
    return moveStrategy.chooseMove(model, this.piece);
  }

  /**
   * Gets the piece cellState.
   */
  @Override
  public CellState getPiece() {
    return this.piece;
  }
}
