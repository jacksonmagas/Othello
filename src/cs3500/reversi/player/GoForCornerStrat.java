package cs3500.reversi.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.Hex;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PlayerDisc;

/**
 * This class represents a strategy that tries to move to a corner. It is a fallible strategy,
 * which is why its main public method returns an {@code Optional<Hex>}.
 */
public class GoForCornerStrat implements FallibleReversiStrat {
  private List<Hex> allOptions = new ArrayList<>();

  @Override
  public Optional<Hex> chooseMove(IROModel model, PlayerDisc who) {
    List<Hex> corners = new CornerInfo(model).getAllCorners();
    Optional<Hex> bestMove = moveToCorners(model, corners);

    return bestMove;
  }

  @Override
  public List<Hex> allOptions() {
    return allOptions;
  }

  /**
   * This method moves through each corner (in an upper-left order) and checks if there is a
   * legal move there. If there is, it returns that move.
   * If there are no legal moves in any of the corners, it returns an empty optional.
   * @param model the model of the game
   * @param corners a list of all the corners of the board
   * @return the best move that moves to a corner
   */
  private Optional<Hex> moveToCorners(IROModel model, List<Hex> corners) {
    List<Optional<Hex>> options = new ArrayList<>();
    Optional<Hex> bestMove = Optional.empty();
    for (Hex hex : corners) {
      if (model.isLegal(hex.getQ(), hex.getR())) {
        options.add(Optional.of(hex));
      }
    }
    allOptions.addAll(corners);
    if (options.size() > 0) {
      bestMove = options.get(0);
    }
    return bestMove;
  }

}
