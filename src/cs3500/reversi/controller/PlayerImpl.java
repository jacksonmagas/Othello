package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.InfallibleMoveStrategy;
import cs3500.reversi.model.Move;
import cs3500.reversi.strategy.MoveTester;
import cs3500.reversi.strategy.ParameterizedMoveTester;

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
  public void receiveGUIAction(Move m) {
    this.moveStrategy.newGUIMove(m);
  }

  @Override
  public int getPossiblePoints(ReadonlyReversiModel model, CellState player, Move m) {
    int score = 0;
    if (player.equals(model.getCurrentPlayer())) {
      ScoreTester tester = new ScoreTester(model);
      try {
        score = tester.testMove(m);
        if (score < 0) {
          score = 0;
        }
      } catch (Exception ex) {
        System.err.println("getPossiblePoints error " + ex);
      }
      System.out.println("Move " + m.toString() + " has points " + score);
    }
    return score;
  }

  /**
   * An implementation of move tester which evaluates moves based on the score for the player
   * making the move. (Which is the opposite of the current player after the move is made.)
   */
  private static class ScoreTester implements MoveTester {

    private final MoveTester delegate;

    private ScoreTester(ReadonlyReversiModel model) {
      this.delegate = new ParameterizedMoveTester(model,
              (ReadonlyReversiModel m) -> m.getPlayerScore(m.getCurrentPlayer()));
    }

    @Override
    public int testMove(Move move) throws IllegalArgumentException {
      return delegate.testMove(move)-1;
    }
  }

}
