package cs3500.reversi.controller;

import java.util.Optional;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.FallibleMoveStrategy;
import cs3500.reversi.strategy.Move;
import cs3500.reversi.strategy.InfallibleMoveStrategy;
import cs3500.reversi.strategy.MoveStrategy;

/**
 * A simple Player implementation that delegates most of its
 * complexity to a {@link InfallibleMoveStrategy}
 * for choosing where to play next.
 */
public class PlayerImpl implements Player {

  private final CellState piece;
  private final MoveStrategy moveStrategy;


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
    if (moveStrategy instanceof FallibleMoveStrategy) {
      Optional<Move> move = ((FallibleMoveStrategy)moveStrategy).chooseMove(model, this.piece);
      if (move != null && move.isPresent()) {
        Move movePresent = move.get();
        return movePresent;
      } else {
        return new Move(true, false, false);
      }
    } else if (moveStrategy instanceof InfallibleMoveStrategy) {
      return ((InfallibleMoveStrategy)moveStrategy).chooseMove(model, this.piece);
    }
    return new Move(true, false, false);
  }

  /**
   * Gets the piece cellState.
   */
  @Override
  public CellState getPiece() {
    return this.piece;
  }
}
