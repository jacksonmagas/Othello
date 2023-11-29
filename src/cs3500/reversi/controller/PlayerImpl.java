package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.InfallibleMoveStrategy;
import cs3500.reversi.strategy.Move;

/**
 * A simple Player implementation that delegates most of its
 * complexity to a {@link InfallibleMoveStrategy}
 * for choosing where to play next.
 */
public class PlayerImpl implements Player {

  private final CellState piece;
  private final InfallibleMoveStrategy moveStrategy;


  /**
   * Constructor for PlayerImp class.
   */
  public PlayerImpl(CellState piece, InfallibleMoveStrategy strategy) {
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

  @Override
  public void recieveGUIMove(Move m) {
    this.moveStrategy.newGUIMove(m);
  }
}
