package cs3500.reversi.provider.player;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.PlayerDisc;

/**
 * The interface {@code FallibleReversiStrat} represents a strategy for choosing a move in
 * Reversi that may fail to find a move. It is the opposite of the interface {@code
 * InfallibleReversiStrat} as it includes the option that there might not be a best move to make
 * based on the strategies implemented in this interface. This interface's main method
 * takens in an {@code IROModel} to prevent unwanted mutation by the player. Allowing a mutable
 * model to the player can permit unwanted convenient cheating of the game.
 */
public interface FallibleReversiStrat {

  /**
   * Potentially chooses a move for the given player in the given model.
   * @param model the model to choose a move in
   * @param who the player to choose a move for
   * @return an Optional that contains a Hex that represents the move chosen, or is empty
   */
  public Optional<Hex> chooseMove(IROModel model, PlayerDisc who);

  /**
   * Gets a list of all the options that could be played with this strategy. This list is
   * unfiltered, so it doesn't choose by default the upperleft most option.
   * @return the list of all possible Hexes.
   */
  public List<Hex> allOptions();

}
