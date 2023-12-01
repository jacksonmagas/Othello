package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;
import java.util.Optional;

/**
 * The CornersStrategy is to play the first legal move in a corner.
 * The corners are tried in a left to right top to bottom order, so the topmost leftmost corner
 * with a legal move will be chosen.
 * Returns an empty optional if there are no legal moves in corners.
 */
public class CornersStrategy implements FallibleMoveStrategy {

  @Override
  public Optional<Move> chooseMove(ReversiModel model, CellState player) {
    int lastRow = model.getRows() - 1;
    int midRow = model.sideLength() - 1;
    Move[] corners = new Move[6];
    corners[0] = new Move(0,0);
    corners[1] = new Move(0, model.getColumns(0) - 1);
    corners[2] = new Move(midRow, 0);
    corners[3] = new Move(midRow, model.getColumns(midRow) - 1);
    corners[4] = new Move(lastRow, 0);
    corners[5] = new Move(lastRow, model.getColumns(lastRow) - 1);

    for (Move m : corners) {
      try {
        model.copy().makeMove(m);
        // return the first corner that is a valid move
        return Optional.of(m);
      } catch (IllegalArgumentException ignored) {
        // when there is no legal move at the given location just move on to the next location
      }
    }

    return Optional.empty();
  }

  @Override
  public void newGUIMove(Move newMove) {
    //ignore
  }
}
