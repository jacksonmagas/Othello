package cs3500.reversi;

import cs3500.reversi.controller.Player;
import cs3500.reversi.controller.PlayerImpl;
import cs3500.reversi.controller.ReverseHexGridPlayerController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.FirstAvailableOpening;
import cs3500.reversi.strategy.HighestScoringMove;
import cs3500.reversi.strategy.PromptUser;
import cs3500.reversi.view.BasicReversiView;
import cs3500.reversi.view.ReversiFrame;

/**
 * Represent a Reversi game class as proposed in Part3 requirements.
 */
public class Reversi {

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
    int WIDTH = 1200;
    int HEIGHT = 800;

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
    ReversiFrame viewPlayer1 = new BasicReversiView(WIDTH, HEIGHT, reversi);
    ReversiFrame viewPlayer2 = new BasicReversiView(WIDTH, HEIGHT, reversi);

    Player player1 = getPlayerUsingStrategy(player1Strategy, CellState.BLACK);
    Player player2 = getPlayerUsingStrategy(player2Strategy, CellState.WHITE);
    ReverseHexGridPlayerController controller1 = new ReverseHexGridPlayerController(reversi, viewPlayer1, player1);
    ReverseHexGridPlayerController controller2 = new ReverseHexGridPlayerController(reversi, viewPlayer2, player2);
    reversi.addYourTurnListener(controller1);
    reversi.addYourTurnListener(controller2);
    viewPlayer1.setVisibleView(true);
    viewPlayer2.setVisibleView(false);
    reversi.startGame();
  }

  private static Player getPlayerUsingStrategy(String strategy, CellState cellState) {
    Player player;
    if (STRATEGY1.equalsIgnoreCase(strategy)) {
      player = new PlayerImpl(cellState, new FirstAvailableOpening());
    } else if (STRATEGY2.equalsIgnoreCase(strategy)) {
      player = new PlayerImpl(cellState, new HighestScoringMove());
    } else if (STRATEGY3.equalsIgnoreCase(strategy)) {
      player = new PlayerImpl(cellState, new HighestScoringMove());
    } else {
      // defaults to HUMAN Strategy
      player = new PlayerImpl(cellState, new PromptUser());
    }
    return player;
  }
}
