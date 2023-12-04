package cs3500.reversi.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.Hex;
import cs3500.reversi.model.IMutableModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PlayerDisc;

/**
 * The class {@code MiniMax} represents a strategy for choosing a move in Reversi
 * that minimizes the opponent's ability to make their best move. It is a fallible strategy, which
 * is why its main public method returns an {@code Optional<Hex>}. It stores a list of strategies
 * to check if the opponent's moves correlate with any of the strategies in this list.
 */
public class MiniMaxStrat implements FallibleReversiStrat {
  private final List<FallibleReversiStrat> stratList = new ArrayList<>();
  private List<Hex> allOptions = new ArrayList<>();

  @Override
  public Optional<Hex> chooseMove(IROModel model, PlayerDisc who) {
    PlayerDisc opp = who.getOpp();
    Optional<Hex> bestMove = Optional.empty();
    Optional<FallibleReversiStrat> stratBeingUsed = Optional.empty();

    if (model.getLatestMove().isEmpty() || !model.getNextPlayerTurn().equals(who)) {
      bestMove = Optional.empty();
    } else {
      for (FallibleReversiStrat strat : combineAllStrats()) {
        Optional<Hex> oldMove = model.getLatestMove();
        IMutableModel prevModel = model.getPreviousModel(model);
        Optional<Hex> currentMove = strat.chooseMove(prevModel, opp);
        if (oldMove.isPresent() && currentMove.isPresent()
                && strat.allOptions().contains(oldMove.get())) {
          stratBeingUsed = Optional.of(strat);
        }
      }
      Optional<Hex> newMove = stratBeingUsed.flatMap(beingUsed -> beingUsed.chooseMove(model, opp));
      if (newMove.isPresent() && canStealOpponentBestMove(model, newMove.get())) {
        bestMove = newMove;
      }
    }
    return bestMove;
  }

  @Override
  public List<Hex> allOptions() {
    return allOptions;
  }

  /**
   * Combines all the strategies into one list. method can be changed in the future if more
   * strategies are added/created.
   * @return a list of all the strategies
   */
  private List<FallibleReversiStrat> combineAllStrats() {
    stratList.add(new CaptureMaxStrat());
    stratList.add(new AvoidNextToCornerStrat());
    stratList.add(new GoForCornerStrat());
    stratList.add(new MiniMaxStrat());

    return stratList;
  }

  /**
   * Determines if the opponent's best move can be 'stolen'. meaning, if the opponent's best move
   * is legal for the current player's move, then the current player will place their disc there.
   * @param model the current model
   * @param oppMove the opponent's best move for their next turn
   * @return {@code true} if the opponent's best move can be stolen, {@code false} otherwise
   */
  private boolean canStealOpponentBestMove(IROModel model, Hex oppMove) {
    return model.isLegal(oppMove.getQ(), oppMove.getR());
  }


}
