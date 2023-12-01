package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;

/**
 * The combined move strategy tries multiple fallible strategies in order and defaults to an
 * infallible move strategy if the fallible strategies fail to produce valid moves.
 */
public class CombinedMoveStrategy implements InfallibleMoveStrategy {
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    return new PassIfWin().chooseMove(model, player)
        .orElse(new CornersStrategy().chooseMove(model, player)
            .orElse(new HighestScoringMove().chooseMove(model, player)));
  }

  @Override
  public void newGUIMove(Move newMove) {
    //ignore
  }
}
