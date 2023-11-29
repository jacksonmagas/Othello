package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import java.util.Optional;

/**
 * PassIfWin is a strategy for reversi that involves passing the turn if it will result in a win.
 * It is a falliable strategy because passing does not always lead to a win.
 */
public class PassIfWin implements FallibleMoveStrategy {
  @Override
  public Optional<Move> chooseMove(ReversiModel model, CellState player) {
    ReversiModel copy = model.copy();
    copy.passTurn();
    if (copy.isGameOver()
        && copy.getPlayerScore(player) > copy.getPlayerScore(player.opposite())) {
      return Optional.of(new Move(true, false, false));
    } else {
      return Optional.empty();
    }
  }
}
