package cs3500.reversi.provider.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.PlayerDisc;

/**
 * The class {code AvoidNextToCornerStrat }represents a strategy for choosing a move in Reversi
 * that avoids the cells adjacent to corners. For instance, if there was a game of Reversi with
 * the radius of 2, the hex (0, -2) would be the corner, and this strategy would
 * avoid the cells (1, -2), (1, -1), and (0, -1).
 * If there is a tie between the various options (i.e., there is more
 * than one cell that is not a neighbor of a corner), this strategy will choose the upper left
 * most option (upper is taken priority of left). Its main method {@code chooseMove} returns
 * an {@code Optional<Hex>} because it is a fallible strategy.
 */
public class AvoidNextToCornerStrat implements FallibleReversiStrat {
  private List<Hex> allOptions = new ArrayList<>();

  @Override
  public Optional<Hex> chooseMove(IROModel model, PlayerDisc who) {
    List<Hex> corners = new CornerInfo(model).getAllCorners();
    Optional<Hex> bestMove = avoidCorners(model);
    return bestMove;
  }

  @Override
  public List<Hex> allOptions() {
    return allOptions;
  }

  /**
   * Returns the best move that avoids the cells adjacent to corners. If there is a tie
   * between the various options, this strategy will choose the upper left most option (upper is
   * taken priority of left). It does this by organizing the options by an upper-left order and
   * then choosing the first hex from that list. If there are no options, this method returns an
   * empty optional.
   * @param model the model of the game
   * @return the best move that avoids the cells adjacent to corners
   */
  private Optional<Hex> avoidCorners(IROModel model) {
    Optional<Hex> bestMove = Optional.empty();
    List<Hex> options = new ArrayList<>();
    for (Hex hex : model.getBoard().getHexagons().keySet()) {
      if (!neighborsOfCorner(model).contains(hex) && model.isLegal(hex.getQ(), hex.getR())) {
        options.add(hex);
      }
    }
    allOptions.addAll(options);
    if (options.size() > 0) {
      organizeMoves(options);
      bestMove = Optional.of(options.get(0));
    }
    return bestMove;
  }

  /**
   * Returns a list of all the neighbors of the corners of the board.
   * This list will be used as a reference to determine which moves to avoid.
   * @param model the model of the game
   * @return a list of all the neighbors of the corners of the board
   */
  private List<Hex> neighborsOfCorner(IROModel model) {
    List<Hex> neighbors = new ArrayList<>();
    for (Hex corner : new CornerInfo(model).getAllCorners()) {
      neighbors.addAll(model.getInboundNeighbors(corner.getQ(), corner.getR()));
    }
    return neighbors;
  }

  /**
   * Organizes the moves by an upper-left order. It does this by first organizing
   * the moves by their r values, and then organizing the moves by their q values. This method
   * is used to determine which move is the best move to make.
   * @param potentialHexes the list of potential moves
   */
  private void organizeMoves(List<Hex> potentialHexes) {
    Collections.sort(potentialHexes, Comparator
            .<Hex, Integer>comparing(Hex::getR)
            .thenComparing(Comparator.comparing(Hex::getQ).reversed()));
  }

}
