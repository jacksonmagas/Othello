package cs3500.reversi;

import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.strategy.CombinedMoveStrategy;
import cs3500.reversi.strategy.ConsoleInputStrategy;
import cs3500.reversi.strategy.CornersStrategy;
import cs3500.reversi.strategy.HighestScoringMove;
import cs3500.reversi.strategy.InfallibleMoveStrategy;
import cs3500.reversi.model.Move;
import cs3500.reversi.strategy.PassIfWin;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for different strategies of the reversi game.
 */
public class StrategyTests {
  BasicReversiBuilder builder;

  @Before
  public void init() {
    builder = new BasicReversiBuilder(3);
  }

  @Test
  public void highestScoringMovePrintTranscript() {
    MockModel game = new MockModel(new BasicReversi(4));
    new HighestScoringMove().chooseMove(game, CellState.BLACK);
    try {
      try (FileWriter output = new FileWriter("strategy-transcript.txt")) {
        output.write(game.viewTranscript());
      }
    } catch (IOException e) {
      System.err.println("failed to print to file");
    }
    Assert.assertTrue(game.viewTranscript().contains("Move at 1, 2"));
  }

  @Test
  public void testPassIfWinStrategyValidMove() {
    MockModel game = builder.bAt(0, 0)
        .bAt(0, 1)
        .bAt(0, 2)
        .wAt(1, 0)
        .pass()
        .player('b')
        .buildMock();
    game.startGame();
    Assert.assertEquals(Optional.of(new Move(true, false, false)),
        new PassIfWin().chooseMove(game, CellState.BLACK));
  }


  @Test
  public void testPassIfWinStrategyPassLoses() {
    MockModel game = builder.bAt(0, 0)
        .bAt(0, 1)
        .bAt(0, 2)
        .wAt(1, 0)
        .pass()
        .player('w')
        .buildMock();
    game.startGame();
    Assert.assertEquals(new PassIfWin().chooseMove(game, CellState.WHITE),
        Optional.empty());
  }

  @Test
  public void testPassIfWinStrategyPassDraws() {
    MockModel game = builder.bAt(0, 0)
        .wAt(1, 0)
        .pass()
        .player('b')
        .buildMock();
    game.startGame();
    Assert.assertEquals(new PassIfWin().chooseMove(game, CellState.BLACK),
        Optional.empty());
  }

  @Test
  public void testPassIfWinStrategyNoGameOver() {
    MockModel game = builder.bAt(0, 0)
        .wAt(1, 0)
        .pass()
        .player('b')
        .buildMock();
    game.startGame();
    Assert.assertEquals(new PassIfWin().chooseMove(game, CellState.WHITE),
        Optional.empty());
  }

  @Test
  public void testCornersStrategyOneValidCorner() {
    MockModel game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .buildMock();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(0, 0)));
  }

  @Test
  public void testCornersStrategyEachCornerChecked() {
    BasicReversi game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .build();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(0, 0)));

    init();
    game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 0)
        .build();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(0, 2)));

    init();
    game = builder.player('b')
        .wAt(1, 0)
        .bAt(0, 0)
        .build();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(2, 0)));

    init();
    game = builder.player('b')
        .wAt(1, 3)
        .bAt(0, 2)
        .build();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(2, 4)));

    init();
    game = builder.player('b')
        .wAt(3, 0)
        .bAt(2, 0)
        .build();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(4, 0)));

    init();
    game = builder.player('b')
        .wAt(4, 1)
        .bAt(4, 0)
        .build();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(4, 2)));
  }

  @Test
  public void testCornersStrategyMultipleValidCorners() {
    MockModel game = builder.player('b')
        .bAt(0, 0)
        .wAt(0, 1)
        .wAt(1, 0)
        .buildMock();
    game.startGame();
    //valid moves in 2 corners: 0, 2 and 2, 0
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(0, 2)));
    game.makeMove(0, 2);
    game.passTurn();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.BLACK),
        Optional.of(new Move(2, 0)));
  }

  @Test
  public void testCornersStrategyNoValidCorners() {
    MockModel game = builder.player('w')
        .wAt(0, 0)
        .bAt(0, 1)
        .bAt(0, 2)
        .buildMock();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.WHITE),
        Optional.empty());
  }

  @Test
  public void testCombinedStrategyPass() {
    //set up valid corner at 0,0 , but passing wins
    MockModel game = builder
        .wAt(0, 1)
        .bAt(0, 2)
        .bAt(1, 0)
        .bAt(2, 0)
        .pass()
        .player('b')
        .buildMock();
    game.startGame();
    Assert.assertEquals(new CombinedMoveStrategy().chooseMove(game, CellState.BLACK),
        new Move(true, false, false));
  }

  @Test
  public void testCombinedStrategyCorner() {
    // same as last time but passing doesn't win
    MockModel game = builder
        .wAt(0, 1)
        .bAt(0, 2)
        .bAt(1, 0)
        .bAt(2, 0)
        .player('b')
        .buildMock();
    game.startGame();
    Assert.assertEquals(new CombinedMoveStrategy().chooseMove(game, CellState.BLACK),
        new Move(0, 0));
  }

  @Test
  public void testCombinedStrategyHighestScoring() {
    // no valid corner move
    MockModel game = builder.bAt(0, 0)
        .wAt(0, 1)
        .wAt(0, 2)
        .bAt(1, 0)
        .wAt(1, 1)
        .bAt(2, 0)
        .wAt(2, 1)
        .wAt(2, 2)
        .player('b')
        .buildMock();
    game.startGame();
    Assert.assertEquals(new CombinedMoveStrategy().chooseMove(game, CellState.BLACK),
        new Move(2, 3));
  }

  @Test
  public void testPromptUserValidMove() {
    MockModel game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .buildMock();
    game.startGame();
    String response = "make-move 0 0";
    InfallibleMoveStrategy prompt =
        new ConsoleInputStrategy(new Scanner(new StringReader(response)));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(0, 0));
  }

  @Test
  public void testPromptUserPassTurn() {
    MockModel game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .buildMock();
    game.startGame();
    String response = "pass-turn";
    StringReader reader = new StringReader(response);
    InfallibleMoveStrategy prompt = new ConsoleInputStrategy(new Scanner(reader));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(true, false, false));
  }

  @Test
  public void testPromptUserRestart() {
    MockModel game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .buildMock();
    game.startGame();
    String response = "restart";
    InfallibleMoveStrategy prompt =
        new ConsoleInputStrategy(new Scanner(new StringReader(response)));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(false, true, false));
  }

  @Test
  public void testPromptUserQuit() {
    MockModel game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .buildMock();
    game.startGame();
    String response = "quit";
    InfallibleMoveStrategy prompt =
        new ConsoleInputStrategy(new Scanner(new StringReader(response)));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(false, false, true));
  }

  @Test
  public void testPromptUserInvalidInput() {
    MockModel game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .buildMock();
    game.startGame();
    //useless input
    String response = "qoeifnodkf make-move hello 0 1";
    InfallibleMoveStrategy prompt =
        new ConsoleInputStrategy(new Scanner(new StringReader(response)));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(0, 1));
  }
}
