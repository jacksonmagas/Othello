package cs3500.reversi;

import java.util.Scanner;
import cs3500.reversi.controller.PlayerImpl;
import cs3500.reversi.controller.ReverseHexGridController;
import cs3500.reversi.controller.ReversiPlayerStrategyController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.FirstAvailableOpening;
import cs3500.reversi.strategy.HighestScoringMove;
import cs3500.reversi.strategy.PromptUser;

/**
 * Represent a Reversi game class.
 */
public class ReversiGUI {

  static final String HUMAN = "HUMAN";
  static final String STRATEGY1 = "Strategy1";
  static final String STRATEGY2 = "Strategy2";
  static final String STRATEGY3 = "Strategy3";

  /**
   * Constructor for Reversi public class.
   */
  public static void main(String[] args) {

    // defaults
    int noOfCells = 4;
    String player1Strategy = HUMAN;
    String player2Strategy = STRATEGY1;

    if (args.length > 0) {
      try {
        noOfCells = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Argument 1 - " + args[0] + " must be an integer.");
        throw new IllegalArgumentException("Argument 1 - " + args[0] + " must be an integer.");
      }
      try {
        player1Strategy = args[1];
      } catch (Exception e) {
        System.err.println("Argument 2 must be present.");
        throw new IllegalArgumentException("Argument 2 must be present.");
      }
      try {
        player2Strategy = args[2];
      } catch (Exception e) {
        System.err.println("Argument 3 must be present.");
        throw new IllegalArgumentException("Argument 3 must be present.");
      }
    }

    ReversiModel reversi = new BasicReversi(noOfCells);
    ReversiPlayerStrategyController controller = new ReverseHexGridController(reversi);
    setPlayerStrategy(player1Strategy, CellState.BLACK, controller);
    setPlayerStrategy(player2Strategy, CellState.WHITE, controller);
    controller.play();
  }

  private static void setPlayerStrategy(String strategy, CellState cellState, ReversiPlayerStrategyController controller) {
    if (STRATEGY1.equalsIgnoreCase(strategy)) {
      controller.addPlayer(new PlayerImpl(cellState, new FirstAvailableOpening()));
    } else if (STRATEGY2.equalsIgnoreCase(strategy)) {
      controller.addPlayer(new PlayerImpl(cellState, new HighestScoringMove()));
    } else if (STRATEGY3.equalsIgnoreCase(strategy)) {
      controller.addPlayer(new PlayerImpl(cellState, new HighestScoringMove()));
    } else {
      // defaults to HUMAN Strategy
      controller.addPlayer(new PlayerImpl(cellState, new PromptUser()));
    }
  }
}
