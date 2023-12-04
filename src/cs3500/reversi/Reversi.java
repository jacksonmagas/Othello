package cs3500.reversi;

import cs3500.reversi.controller.Player;
import cs3500.reversi.controller.PlayerImpl;
import cs3500.reversi.controller.ReverseHexGridPlayerController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.BasicMinimaxStrategy;
import cs3500.reversi.strategy.CombinedMoveStrategy;
import cs3500.reversi.strategy.FirstAvailableOpening;
import cs3500.reversi.strategy.GUIInputStrategy;
import cs3500.reversi.strategy.HighestScoringMove;
import cs3500.reversi.strategy.ConsoleInputStrategy;
import cs3500.reversi.view.BasicReversiView;
import cs3500.reversi.view.ReversiFrame;

/**
 * Represent a Reversi game class which creates two views of reversi, one for each player.
 * The main expects 3 command line arguments: an integer representing the size of the board,
 * the strategy for the first player, and the strategy for the second player.
 * The valid strategies are: human, firstmove, highestscoring, combined, console, and minimax
 */
public class Reversi {

  static final String HUMAN = "human";
  static final String STRATEGY1 = "firstmove";
  static final String STRATEGY2 = "highestscoring";
  static final String STRATEGY3 = "combined";
  static final String STRATEGY4 = "minimax";
  static final String STRATEGY5 = "console";
  //static final String STRATEGY6 = "Strategy6";

  /**
   * Constructor for Reversi public class.
   */
  public static void main(String[] args) {

    // defaults
    int noOfCells = 4;
    String player1Strategy = HUMAN;
    String player2Strategy = STRATEGY1;
    int width = 1200;
    int height = 800;

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
        System.err.println("Argument 1 must be present.");
        throw new IllegalArgumentException("Argument 1 must be present.");
      }
      try {
        player2Strategy = args[2];
      } catch (Exception e) {
        System.err.println("Argument 2 must be present.");
        throw new IllegalArgumentException("Argument 2 must be present.");
      }
    } else {
      System.err.println("Requires 3 argument.");
    }

    ReversiModel reversi = new BasicReversi(noOfCells);
    ReversiFrame viewPlayer1 = new BasicReversiView(reversi, "Black");
    ReversiFrame viewPlayer2 = new BasicReversiView(reversi, "White");

    Player player1 = getPlayerUsingStrategy(player1Strategy, CellState.BLACK);
    Player player2 = getPlayerUsingStrategy(player2Strategy, CellState.WHITE);
    ReverseHexGridPlayerController controller1 = new ReverseHexGridPlayerController(reversi,
            viewPlayer1, player1);
    ReverseHexGridPlayerController controller2 = new ReverseHexGridPlayerController(reversi,
            viewPlayer2, player2);
    reversi.addYourTurnListener(controller1);
    reversi.addYourTurnListener(controller2);
    viewPlayer1.setVisibleView(true);
    viewPlayer2.setVisibleView(true);
    reversi.startGame();
  }

  // gets player using strategy
  private static Player getPlayerUsingStrategy(String strategy, CellState cellState) {
    Player player;
    switch (strategy.toLowerCase()) {
      case STRATEGY1:
        player = new PlayerImpl(cellState, new FirstAvailableOpening());
        break;
      case STRATEGY2:
        player = new PlayerImpl(cellState, new HighestScoringMove());
        break;
      case STRATEGY3:
        player = new PlayerImpl(cellState, new CombinedMoveStrategy());
        break;
      case STRATEGY4:
        player = new PlayerImpl(cellState, new BasicMinimaxStrategy());
        break;
      case STRATEGY5:
        player = new PlayerImpl(cellState, new ConsoleInputStrategy());
        break;
      default:
        player = new PlayerImpl(cellState, new GUIInputStrategy());
    }
    return player;
  }
}
