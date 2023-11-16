package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;
import cs3500.reversi.strategy.InfallibleMoveStrategy;

/**
 * A simple Player implementation that delegates most of its
 * complexity to a {@link InfallibleMoveStrategy}
 * for choosing where to play next
 */
public class PlayerImpl implements Player {
  private final CellState piece;
  private InfallibleMoveStrategy moveStrategy;

  /**
   * Constructor for PlayerImpl class.
   */
  public PlayerImpl(CellState piece, InfallibleMoveStrategy strategy) {
    this.piece = piece;
    this.moveStrategy = strategy;
  }

  /**
   * Moves a tile for computer player.
   */
  @Override
  public Move play(ReversiModel model) {
    return moveStrategy.chooseMove(model, this.piece);
  }

  /**
   * Gets the piece and returns it.
   */
  @Override
  public CellState getPiece() {
    return this.piece;
  }
}
