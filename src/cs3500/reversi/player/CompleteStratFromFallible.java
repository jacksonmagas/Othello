package cs3500.reversi.player;

import java.util.Optional;

import cs3500.reversi.model.Hex;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PlayerDisc;

/**
 * The class {@code CompleteStratFromFallible} is a class that implements the interface
 * {@code InfallibleReversiStrat} by using a {@code FallibleReversiStrat} to choose a move.
 * If the {@code FallibleReversiStrat} returns an empty {@code Optional}, then this class
 * returns {@code PlayerMoves.SKIP_TURN} that communicates to the player that the best move they
 * can make is to skip their turn.
 * @see InfallibleReversiStrat for more information.
 */
public class CompleteStratFromFallible implements InfallibleReversiStrat {
  FallibleReversiStrat stratToTry;

  /**
   * Constructs a {@code CompleteStratFromFallible} object.
   * @param stratToTry the {@code FallibleReversiStrat} to use to choose a move
   *                   (if it returns an empty {@code Optional}, then this class
   *                   returns {@code PlayerMoves.SKIP_TURN}).
   */
  public CompleteStratFromFallible(FallibleReversiStrat stratToTry) {
    this.stratToTry = stratToTry;
  }

  @Override
  public PlayerMoves chooseMove(IROModel model, PlayerDisc who) throws IllegalStateException {
    Optional<Hex> maybeAns = this.stratToTry.chooseMove(model, who);
    if (maybeAns.isPresent()) {
      PlayerMoves.MAKE_MOVE.setSelectedHex(maybeAns.get());
      return PlayerMoves.MAKE_MOVE;
    }
    return PlayerMoves.SKIP_TURN;
  }

}
