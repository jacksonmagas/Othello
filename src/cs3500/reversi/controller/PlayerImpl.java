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

  /**
   * Retrieves the Scoring hints for given player based upon selected cell.
   * @param model the model to check score of the move on
   * @param player the player to check the move for
   * @param m the move that corresponds to the last mouse or key move
   * @return the number of points the move would score, or -1 if the move is not legal
   */
  @Override
  public int getPossiblePoints(ReadonlyReversiModel model, CellState player, Move m) {
    int score = 0;
    if (model.getLegalMoves().contains(m)) {
      if (player.equals(model.getCurrentPlayer())) {
        ScoreTester tester = new ScoreTester(model);
        try {
          score = tester.testMove(m);
          if (score < 0) {
            score = 0;
          }
        } catch (IllegalStateException e) {
          // do not print error message for game over message
        } catch (Exception ex) {
          // print error message for other errors
          System.err.println("getPossiblePoints error " + ex);
        }
        // System.out.println("Move " + m.toString() + " has points " + score);
      }
      return score;
    }
    return -1;
  }

  /**
   * An implementation of move tester as part of scoring hints determination which
   * evaluates possible move based on the score for the player making the move.
   * (Which is the opposite of the current player after the move is made.)
   */
  private static class ScoreTester implements MoveTester {

    private final MoveTester delegate;

    private ScoreTester(ReadonlyReversiModel model) {
      this.delegate = new ParameterizedMoveTester(model, (ReadonlyReversiModel m) ->
          m.getPlayerScore(m.getCurrentPlayer().opposite()));
    }

    @Override
    public int testMove(Move move) throws IllegalArgumentException {
      return delegate.testMove(move) - 1;
    }
  }

}
