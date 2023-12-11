package cs3500.reversi.provider.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.PlayerDisc;

/**
 * This class is responsible for chaining the strategies together. It is a fallible strategy, which
 * is why its {@code chooseMove} method returns an {@code Optional<Hex>}. This class enables
 * combining different strategies together easy. For instance, if a player wants to try a strategy
 * list where they do {@code CaptureMaxStrat} first and then {@code GoForCornerStr}, they can use
 * the {@code addStrat} method on those two strategies to combine them. As a field, this class
 * stores the list of strategies to try.
 */
public class TryN implements FallibleReversiStrat {
  private final List<FallibleReversiStrat> stratsToTry;
  private List<Hex> allOptions = new ArrayList<>();

  /**
   * Constructs a {@code TryN} object with the default list of strats (all four
   * strats based on sophistication).
   */
  public TryN() {
    this.stratsToTry = new ArrayList<>();
    addStrat(new MiniMaxStrat());
    addStrat(new GoForCornerStrat());
    addStrat(new AvoidNextToCornerStrat());
    addStrat(new CaptureMaxStrat());
  }

  /**
   * Constructs a {@code TryN} object with a list of strategies to try. The player can
   * combine strategies when they first construct their TryN.
   * @param stratsToTry the list of strategies to try.
   */
  public TryN(List<FallibleReversiStrat> stratsToTry) {
    this.stratsToTry = stratsToTry;
  }

  @Override
  public Optional<Hex> chooseMove(IROModel model, PlayerDisc who) {
    for (FallibleReversiStrat strat : stratsToTry) {
      Optional<Hex> maybeAns = strat.chooseMove(model, who);
      if (maybeAns.isPresent()) {
        allOptions.addAll(strat.allOptions());
        return maybeAns;
      }
    }
    return Optional.empty();
  }

  @Override
  public List<Hex> allOptions() {
    return null;
  }

  /**
   * Adds a strategy to the list of strategies to try. This method was set as protected so that
   * other subclasses of {@code TryN} can add their own strategies to the list.
   * @param strat the strategy to add to the list
   */
  protected void addStrat(FallibleReversiStrat strat) {
    this.stratsToTry.add(strat);
  }

  /**
   * Clears any strategies that could've been in {@code stratsToTry} and then
   * adds the strategies based on sophistication. The order of sophistication is as follows:
   * {@code MiniMaxStrat}, {@code GoForCornerStrat}, {@code AvoidNextToCornerStrat}, and then
   * {@code CaptureMaxStrat}.
   * @param model the current model.
   * @param who the current player for whom this TryN is making strategies for.
   * @return an {@code Optional<Hex>} representing the best move to make.
   */
  protected Optional<Hex> combineStratsBasedOnSophistication(IROModel model, PlayerDisc who) {
    this.stratsToTry.clear();
    this.addStrat(new MiniMaxStrat());
    this.addStrat(new GoForCornerStrat());
    this.addStrat(new AvoidNextToCornerStrat());
    this.addStrat(new CaptureMaxStrat());
    return this.chooseMove(model, who);
  }


}
