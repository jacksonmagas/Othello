package cs3500.reversi.provider.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.PlayerDisc;

/**
 * The class {@code CaptureMax} represents a strategy for choosing a move in Reversi that
 * maximizes the number of discs captured. It is a fallible strategy, which is why its only
 * public method returns an {@code Optional<Hex>}.
 * Among the various strategies that implement {@code FallibleReversiStrat}, this one is one of the
 * simpler ones that are less sophisticated than other strategies down the chain.
 * @see FallibleReversiStrat
 */
public class CaptureMaxStrat implements FallibleReversiStrat {
  private IROModel model;
  private List<Hex> allOptions = new ArrayList<>();

  @Override
  public Optional<Hex> chooseMove(IROModel model, PlayerDisc who) {
    Objects.requireNonNull(model);
    this.model = model;
    Optional<Hex> bestMove = Optional.empty();
    int maxCapturedDiscs = 0;

    for (Hex hex : model.getBoard().getHexagons().keySet()) {
      if (model.isLegal(hex.getQ(), hex.getR())) {
        int capturedDiscs = totalDiscsCapturedBy(hex);
        allOptions.add(hex);

        //case where there are multiple moves that capture the same number of discs
        if (capturedDiscs == maxCapturedDiscs && bestMove.isPresent()) {
          bestMove = assignUpperLeftMove(hex, bestMove);
        }

        if (capturedDiscs > maxCapturedDiscs) {  //case where there is only one best move
          maxCapturedDiscs = capturedDiscs;
          bestMove = Optional.of(hex);
        }
      }
    }
    return bestMove;
  }

  @Override
  public List<Hex> allOptions() {
    return allOptions;
  }

  /**
   * Assigns the upper left move to be the best move. This method starts by prioritizing the
   * upper coordinates, and then the left coordinates (i.e., coordinate is preferred the higher it
   * is on the board, and then, the more to the left it is on the board).
   * @param hex the hex representing what could be the new best move
   * @param bestMove an {@code Optional<Hex>} representing the current best move
   * @return the best move (the move that captures the most discs and is at the upper-left)
   */
  private static Optional<Hex> assignUpperLeftMove(Hex hex, Optional<Hex> bestMove) {
    if (hex.getQ() < bestMove.get().getQ() || hex.getR() < bestMove.get().getR()) {
      bestMove = Optional.of(hex);

    }
    return bestMove;
  }

  /**
   * Calculates the total amount of discs to be captured by placing a disc on this Hex.
   * @param hex the hex of the potential move
   * @return an integer representing the total number of discs to be captured
   */
  private int totalDiscsCapturedBy(Hex hex) {
    List<List<Hex>> paths = possiblePaths(hex);
    int sumOfLengths = 0;
    for (List<Hex> p : paths) {
      sumOfLengths += p.size();
    }
    return sumOfLengths;
  }

  /**
   * Finds all possible paths that can be taken from placing a disc on this hex.
   * @param hex the hex of the potential move
   * @return a list of lists of hexes representing all possible paths that can be taken
   */
  private List<List<Hex>> possiblePaths(Hex hex) {
    return model.getAllValidPaths(hex);
  }
}
