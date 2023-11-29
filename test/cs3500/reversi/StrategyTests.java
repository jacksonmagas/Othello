package cs3500.reversi;

import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.strategy.CombinedMoveStrategy;
import cs3500.reversi.strategy.CornersStrategy;
import cs3500.reversi.strategy.InfallibleMoveStrategy;
import cs3500.reversi.strategy.Move;
import cs3500.reversi.strategy.PassIfWin;
import cs3500.reversi.strategy.PromptUser;
import java.io.StringReader;
import java.util.Optional;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StrategyTests {
  BasicReversiBuilder builder;

  @Before
  public void init() {
    builder = new BasicReversiBuilder(3);
  }

  @Test
  public void testPassIfWinStrategyValidMove() {
    BasicReversi game = builder.bAt(0, 0)
        .bAt(0, 1)
        .bAt(0, 2)
        .wAt(1, 0)
        .pass()
        .player('b')
        .build();
    game.startGame();
    Assert.assertEquals(Optional.of(new Move(true, false, false)),
        new PassIfWin().chooseMove(game, CellState.BLACK));
  }

  @Test
  public void testPassIfWinStrategyPassLoses() {
    BasicReversi game = builder.bAt(0, 0)
        .bAt(0, 1)
        .bAt(0, 2)
        .wAt(1, 0)
        .pass()
        .player('w')
        .build();
    game.startGame();
    Assert.assertEquals(new PassIfWin().chooseMove(game, CellState.WHITE),
        Optional.empty());
  }

  @Test
  public void testPassIfWinStrategyPassDraws() {
    BasicReversi game = builder.bAt(0, 0)
        .wAt(1, 0)
        .pass()
        .player('b')
        .build();
    game.startGame();
    Assert.assertEquals(new PassIfWin().chooseMove(game, CellState.BLACK),
        Optional.empty());
  }

  @Test
  public void testPassIfWinStrategyNoGameOver() {
    BasicReversi game = builder.bAt(0, 0)
        .wAt(1, 0)
        .pass()
        .player('b')
        .build();
    game.startGame();
    Assert.assertEquals(new PassIfWin().chooseMove(game, CellState.WHITE),
        Optional.empty());
  }

  @Test
  public void testCornersStrategyOneValidCorner() {
    BasicReversi game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .build();
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
    BasicReversi game = builder.player('b')
        .bAt(0, 0)
        .wAt(0, 1)
        .wAt(1, 0)
        .build();
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
    BasicReversi game = builder.player('w')
        .wAt(0, 0)
        .bAt(0, 1)
        .bAt(0, 2)
        .build();
    game.startGame();
    Assert.assertEquals(new CornersStrategy().chooseMove(game, CellState.WHITE),
        Optional.empty());
  }

  @Test
  public void testCombinedStrategyPass() {
    //set up valid corner at 0,0 , but passing wins
    BasicReversi game = builder
        .wAt(0, 1)
        .bAt(0, 2)
        .bAt(1, 0)
        .bAt(2, 0)
        .pass()
        .player('b')
        .build();
    game.startGame();
    Assert.assertEquals(new CombinedMoveStrategy().chooseMove(game, CellState.BLACK),
        new Move(true, false, false));
  }

  @Test
  public void testCombinedStrategyCorner() {
    // same as last time but passing doesn't win
    BasicReversi game = builder
        .wAt(0, 1)
        .bAt(0, 2)
        .bAt(1, 0)
        .bAt(2, 0)
        .player('b')
        .build();
    game.startGame();
    Assert.assertEquals(new CombinedMoveStrategy().chooseMove(game, CellState.BLACK),
        new Move(0, 0));
  }

  @Test
  public void testCombinedStrategyHighestScoring() {
    // no valid corner move
    BasicReversi game = builder.bAt(0, 0)
        .wAt(0, 1)
        .wAt(0, 2)
        .bAt(1, 0)
        .wAt(1, 1)
        .bAt(2, 0)
        .wAt(2, 1)
        .wAt(2, 2)
        .player('b')
        .build();
    game.startGame();
    Assert.assertEquals(new CombinedMoveStrategy().chooseMove(game, CellState.BLACK),
        new Move(2, 3));
  }

  @Test
  public void testPromptUserValidMove() {
    BasicReversi game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .build();
    game.startGame();
    String response = "make-move 0 0";
    InfallibleMoveStrategy prompt = new PromptUser(new Scanner(new StringReader(response)));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(0, 0));
  }

  @Test
  public void testPromptUserPassTurn() {
    BasicReversi game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .build();
    game.startGame();
    String response = "pass-turn";
    StringReader reader = new StringReader(response);
    InfallibleMoveStrategy prompt = new PromptUser(new Scanner(reader));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(true, false, false));
  }

  @Test
  public void testPromptUserRestart() {
    BasicReversi game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .build();
    game.startGame();
    String response = "restart";
    InfallibleMoveStrategy prompt = new PromptUser(new Scanner(new StringReader(response)));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(false, true, false));
  }

  @Test
  public void testPromptUserQuit() {
    BasicReversi game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .build();
    game.startGame();
    String response = "quit";
    InfallibleMoveStrategy prompt = new PromptUser(new Scanner(new StringReader(response)));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(false, false, true));
  }

  @Test
  public void testPromptUserInvalidInput() {
    BasicReversi game = builder.player('b')
        .wAt(0, 1)
        .bAt(0, 2)
        .build();
    game.startGame();
    //useless input
    String response = "qoeifnodkf make-move hello 0 1";
    InfallibleMoveStrategy prompt = new PromptUser(new Scanner(new StringReader(response)));
    Assert.assertEquals(prompt.chooseMove(game, CellState.BLACK), new Move(0, 1));
  }
}
