package cs3500.reversi;

import cs3500.reversi.model.BoardType;
import cs3500.reversi.model.adaptors.ReversiModelToIMutableModelAdapter;
import cs3500.reversi.controller.Player;
import cs3500.reversi.controller.PlayerImpl;
import cs3500.reversi.controller.ReverseHexGridPlayerController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.controller.adaptors.PlayerToIPlayerAdapter;
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
import cs3500.reversi.view.SquareReversiView;
import cs3500.reversi.view.adaptors.IGraphicalReversiViewToReversiFrame;
import cs3500.reversi.view.ReversiFrame;

/**
 * Represent a Reversi game class which creates two views of reversi, one for each player, and
 * plays the game of reversi.
 *
 * <p>The main function requires the following arguments:
 * <ul><li> {@code -p1} strategy
 * <li> {@code -p2} strategy
 * </ul>
 *
 * <p>Where strategy is one of our strategies:
 * <ul><li> human
 * <li> first-move
 * <li> highest-scoring
 * <li> combined
 * <li> tree-minimax
 * <li> console
 * </ul> Or one of the provider strategies: <ul>
 * <li> capture
 * <li> avoid
 * <li> corner
 * <li> minimax
 * <li> combo
 * </ul>
 *
 * <p>And accepts the following additional arguments:
 * <li> -s n or -size n, where n is an integer greater than 2. If square board is chosen the size
 * must be even and at least 4.
 * <li> -d n or -depth n where n is a positive integer.
 * WARNING: Using tree-minimax with a depth that is above 3 or on a very large board can cause
 * out of memory errors.
 * <li> -b or -board followed by either hexagon, or square. Our view is the only view that
 * supports square game.
 * <li> -v1 ours, or -v1 provider, which chooses which view to use for player 1
 * <li> -v2 ours, or -v2 provider, which chooses which view to use for player 2
 */
public class Reversi {

  static final String HUMAN = "human";
  static final String STRATEGY1 = "first-move";
  static final String STRATEGY2 = "highest-scoring";
  static final String STRATEGY3 = "combined";
  static final String STRATEGY4 = "tree-minimax";
  static final String STRATEGY5 = "console";

  static final String PROVIDER_STRATEGY1 = "capture";
  static final String PROVIDER_STRATEGY2 = "avoid";
  static final String PROVIDER_STRATEGY3 = "corner";
  static final String PROVIDER_STRATEGY4 = "minimax";
  static final String PROVIDER_STRATEGY5 = "combo";

  static final String OURS = "ours";
  static final String PROVIDER = "provider";
  static int depth = 3;
  static int size = 4;
  static String player1;
  static String player2;
  static String VIEW1;
  static String VIEW2;

  static BoardType boardType;

  /**
   * Constructor for Reversi public class.
   */
  public static void main(String[] args) {
    // defaults
    int width = 1200;
    int height = 800;

    parseArgs(args);

    // defaulting board type
    if (boardType == null) {
      boardType = BoardType.HEXAGON;
    } else if (boardType == BoardType.SQUARE &&
        (VIEW1.equalsIgnoreCase("provider") || VIEW2.equalsIgnoreCase("provider"))) {
      throw new IllegalArgumentException("Provider view not supported for square boards.");
    }

    System.out.println("BoardType "+ boardType.toString());

    ReversiModel baseModel = new BasicReversi(boardType, size);
    try {
      setUpPlayer(VIEW1, baseModel, width, height, CellState.BLACK,
          getPlayer(player1, CellState.BLACK));
      setUpPlayer(VIEW2, baseModel, width, height, CellState.WHITE,
          getPlayer(player2, CellState.WHITE));
    } catch (NullPointerException e) {
      System.err.println("-p1 and -p2 flags are required to specify strategies");
      throw new IllegalArgumentException("-p1 and -p2 flags are required to specify player"
          + "strategies");
    }

    baseModel.startGame();
  }

  private static void setUpPlayer(String s, ReversiModel reversi,
      int width, int height, CellState player, Player strategy) {
    if (s.equalsIgnoreCase(PROVIDER)) {
      IROModel providerModel = new ReversiModelToIMutableModelAdapter(reversi);
      JFrameView player2Frame = new JFrameView(providerModel);
      BoardPanel panel = new BoardPanel(providerModel, player2Frame);
      player2Frame.setSize(width, height);
      player2Frame.render();
      new ReverseHexGridPlayerController(reversi,
          new IGraphicalReversiViewToReversiFrame(player2Frame,
              providerModel,
              player), strategy);
      panel.setVisible(true);
    } else {
      ReversiFrame viewPlayer2;
      if (boardType == BoardType.HEXAGON) {
        viewPlayer2 = new BasicReversiView(reversi, player);
      } else {
        viewPlayer2 = new SquareReversiView(reversi, player);
      }
      new ReverseHexGridPlayerController(reversi,
          viewPlayer2, strategy);
      viewPlayer2.setVisibleView(true);
    }
  }

  private static void parseArgs(String[] args) {
    try {
      for (int i = 0; i < args.length; i++) {
        switch (args[i]) {
          case "-p1":
            player1 = args[i + 1];
            break;
          case "-p2":
            player2 = args[i + 1];
            break;
          case "-size":
          case "-s":
            try {
              size = Integer.parseInt(args[i + 1]);
              if ((size <= 2)) {
                throw new NumberFormatException();
              }
            } catch (NumberFormatException e) {
              System.err.println("-s flag must be followed by an integer"
                  + "greater than 2");
              throw new IllegalArgumentException("-s flag must be followed by an integer"
                  + "greater than 2");
            }
            break;
          case "-board":
          case "-b":
            try {
              boardType = BoardType.valueOf(args[i + 1].toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
              System.err.println("board flag must be followed by a valid board specifier, either HEXAGON, or SQUARE.");
              throw new IllegalArgumentException("board flag must be followed by a valid board specifier, either HEXAGON, or SQUARE.");
            }
            break;
          case "-depth":
          case "-d":
            try {
              depth = Integer.parseInt(args[i + 1]);
              if ((size < 1)) {
                throw new NumberFormatException();
              }
            } catch (NumberFormatException e) {
              System.err.println("-d flag must be followed by a positive integer");
              throw new IllegalArgumentException("-d flag must be followed by a positive integer");
            }
            break;
          case "-v1":
            if (args[i + 1].equalsIgnoreCase(OURS)) {
              VIEW1 = OURS;
            } else if (args[i + 1].equalsIgnoreCase(PROVIDER)) {
              VIEW1 = PROVIDER;
            } else {
              System.err.println("view flags must be followed by a valid view specifier, either"
                  + "ours, or provider.");
              throw new IllegalArgumentException("view flags must be followed by a valid view"
                  + "specifier, either ours, or provider.");
            }
            break;
          case "-v2":
            if (args[i + 1].equalsIgnoreCase(OURS)) {
              VIEW2 = OURS;
            } else if (args[i + 1].equalsIgnoreCase(PROVIDER)) {
              VIEW2 = PROVIDER;
            } else {
              System.err.println("view flags must be followed by a valid view specifier, either"
                  + "ours, or provider.");
              throw new IllegalArgumentException("view flags must be followed by a valid view"
                  + "specifier, either ours, or provider.");
            }
            break;
          default:
        }
      }
    } catch (IndexOutOfBoundsException e) {
      System.err.println("Last argument cannot be a flag without it's value.");
    }
  }

  private static Player getPlayer(String arg, CellState player) {
    Player temp;
    try {
      temp = getPlayerUsingHomeTeamStrategy(arg, player);
    } catch (IllegalArgumentException e) {
      try {
        temp = getPlayerUsingProviderStrategy(arg, player);
      } catch (IllegalArgumentException exception) {
        System.err.println("player 1 strategy must be specified with the -p1 flag"
            + "Valid values are human, first-move, highest-scoring, combined, tree-minimax,"
            + " Console, capture, avoid, corner, minimax, and combo");
        throw  new IllegalArgumentException("player 1 strategy must be specified with the"
            + "-p1 flag"
            + "Valid values are human, first-move, highest-scoring, combined, tree-minimax,"
            + " Console, capture, avoid, corner, minimax, and combo");
      }
    }
    return temp;
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
        player = new PlayerImpl(cellState, new BasicMinimaxStrategy(depth));
        break;
      case STRATEGY5:
        player = new PlayerImpl(cellState, new ConsoleInputStrategy());
        break;
      case HUMAN:
        player = new PlayerImpl(cellState, new GUIInputStrategy());
        break;
      default:
        throw new IllegalArgumentException("Not our strategy");
    }
    return player;
  }

  // gets player using provider strategy
  private static Player getPlayerUsingProviderStrategy(String strategy, CellState cellState) {
    Player player;
    switch (strategy.toLowerCase()) {
      case PROVIDER_STRATEGY1:
        player = new PlayerToIPlayerAdapter(PlayerType.CAPTURE, cellState);
        break;
      case PROVIDER_STRATEGY2:
        player = new PlayerToIPlayerAdapter(PlayerType.AVOID, cellState);
        break;
      case PROVIDER_STRATEGY3:
        player = new PlayerToIPlayerAdapter(PlayerType.CORNER, cellState);
        break;
      case PROVIDER_STRATEGY4:
        player = new PlayerToIPlayerAdapter(PlayerType.MINIMAX, cellState);
        break;
      case PROVIDER_STRATEGY5:
        player = new PlayerToIPlayerAdapter(PlayerType.COMBO, cellState);
        break;
      case HUMAN:
        player = new PlayerToIPlayerAdapter(PlayerType.HUMAN, cellState);
        break;
      default:
        throw new IllegalArgumentException("Not a provider strategy");
    }
    return player;
  }
}
