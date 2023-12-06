package cs3500.reversi;

import cs3500.reversi.controller.Player;
import cs3500.reversi.controller.PlayerImpl;
import cs3500.reversi.controller.ReverseHexGridPlayerController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiModelToIMutableModelAdapter;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.player.IPlayer;
import cs3500.reversi.provider.player.PlayerType;
import cs3500.reversi.provider.view.BoardPanel;
import cs3500.reversi.provider.view.JFrameView;
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

  static final String PROVIDER_STRATEGY1 = "capture";
  static final String PROVIDER_STRATEGY2 = "avoid";
  static final String PROVIDER_STRATEGY3 = "corner";
  static final String PROVIDER_STRATEGY4 = "minimax";
  static final String PROVIDER_STRATEGY5 = "combo";

  static final String HOME_TEAM = "hometeam";
  static final String PROVIDER_TEAM = "providerteam";

  /**
   * Constructor for Reversi public class.
   */
  public static void main(String[] args) {

    // defaults
    int noOfCells = 4;
    String player1Strategy = HUMAN;
    String player2StrategyProvider = PROVIDER_TEAM;
    String player2Strategy = STRATEGY1;
    int width = 1200;
    int height = 800;

    if (args.length > 0) {
      try {
        noOfCells = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Argument 1 - " + args[0] + " must be an integer and greater or equal to 3.");
        throw new IllegalArgumentException("Argument 1 - " + args[0] + " must be an integer and greater or equal to 3.");
      }
      try {
        player1Strategy = args[1];
      } catch (Exception e) {
        System.err.println("Argument 2 must be present as player1 strategy. Valid values are Human or FirstMove or HighestScoring or Combined or MiniMax or Console.");
        throw new IllegalArgumentException("Argument 2 must be present as player1 strategy. Valid values are Human or FirstMove or HighestScoring or Combined or MiniMax or Console.");
      }
      try {
        player2Strategy = args[2];
      } catch (Exception e) {
        System.err.println("Argument 3 must be present as player2 strategy. Valid values are Human or Capture or Avoid or Corner or MiniMax or Combo.");
        throw new IllegalArgumentException("Argument 3 must be present as player2 strategy. Valid values are Human or Capture or Avoid or Corner or MiniMax or Combo.");
      }
      try {
        player2StrategyProvider = args[3];
      } catch (Exception e) {
        System.err.println("Argument 4 must be present as player2 strategy provider. Valid values are HomeTeam or ProviderTeam.");
        throw new IllegalArgumentException("Argument 4 must be present. Valid values are HomeTeam or ProviderTeam.");
      }
    } else {
      System.err.println("Requires 4 argument.");
    }

    ReversiModel reversi = new BasicReversi(noOfCells);
    ReversiFrame viewPlayer1 = new BasicReversiView(reversi, "Black");
    Player player1 = getPlayerUsingHomeTeamStrategy(player1Strategy, CellState.BLACK);
    ReverseHexGridPlayerController controller1 = new ReverseHexGridPlayerController(reversi,
            viewPlayer1, player1);
    reversi.addYourTurnListener(controller1);
    viewPlayer1.setVisibleView(true);

    if (PROVIDER_TEAM.equalsIgnoreCase(player2StrategyProvider)) {
      IROModel providerModel = new ReversiModelToIMutableModelAdapter(reversi);
      JFrameView viewPlayer2 = new JFrameView(providerModel);
      BoardPanel panel = new BoardPanel(providerModel, viewPlayer2);
      IPlayer player2 = getPlayerUsingProviderStrategy(player2Strategy, CellState.WHITE);
      viewPlayer2.setSize(width, height);
      viewPlayer2.render();
      panel.setVisible(true);
    } else {
      ReversiFrame viewPlayer2 = new BasicReversiView(reversi, "White");
      Player player2 = getPlayerUsingHomeTeamStrategy(player2Strategy, CellState.WHITE);
      ReverseHexGridPlayerController controller2 = new ReverseHexGridPlayerController(reversi,
              viewPlayer2, player2);
      reversi.addYourTurnListener(controller2);
      viewPlayer2.setVisibleView(true);
    }

    reversi.startGame();
  }

  // gets player using home team strategy
  private static Player getPlayerUsingHomeTeamStrategy(String strategy, CellState cellState) {
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

  // gets player using provider strategy
  private static IPlayer getPlayerUsingProviderStrategy(String strategy, CellState cellState) {
    IPlayer player;
    switch (strategy.toLowerCase()) {
      case PROVIDER_STRATEGY1:
        player = new cs3500.reversi.provider.player.Player(PlayerType.CAPTURE);
        break;
      case PROVIDER_STRATEGY2:
        player = new cs3500.reversi.provider.player.Player(PlayerType.AVOID);
        break;
      case PROVIDER_STRATEGY3:
        player = new cs3500.reversi.provider.player.Player(PlayerType.CORNER);
        break;
      case PROVIDER_STRATEGY4:
        player = new cs3500.reversi.provider.player.Player(PlayerType.MINIMAX);
        break;
      case PROVIDER_STRATEGY5:
        player = new cs3500.reversi.provider.player.Player(PlayerType.COMBO);
        break;
      default:
        player = new cs3500.reversi.provider.player.Player(PlayerType.HUMAN);
    }
    return player;
  }
}
