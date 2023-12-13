package cs3500.reversi;

import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.BoardType;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for square reversi model.
 */
public class SquareModelTests {
  private BasicReversiBuilder b;

  @Before
  public void init() {
    b = new BasicReversiBuilder(4).boardType('s');
  }

  @Test
  public void testInvalidArguments() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversi(BoardType.SQUARE,
            3));
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversi(BoardType.SQUARE,
            -1));
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversi(BoardType.SQUARE,
            0));
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversi(BoardType.SQUARE,
            5));
  }

  @Test
  public void testGameStartView() {
    BasicReversi testModel4 = new BasicReversi(BoardType.SQUARE, 4);
    Assert.assertEquals("_ _ _ _ \n"
        + "_ X O _ \n"
        + "_ O X _ \n"
        + "_ _ _ _ \n"
        + "Player one Score: 2\n"
        + "Player two Score: 2\n"
        + "Player one turn (Black)!\n", testModel4.toString());
    BasicReversi testModel6 = new BasicReversi(BoardType.SQUARE, 6);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ X O _ _ \n"
        + "_ _ O X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 2\n"
        + "Player two Score: 2\n"
        + "Player one turn (Black)!\n", testModel6.toString());
  }

  @Test
  public void testMakeMove() {
    BasicReversi testModel6 = new BasicReversi(BoardType.SQUARE, 6);
    testModel6.makeMove(2, 4);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ X X X _ \n"
        + "_ _ O X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 4\n"
        + "Player two Score: 1\n"
        + "Player two turn (White)!\n", testModel6.toString());
    testModel6.makeMove(1, 2);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ O _ _ _ \n"
        + "_ _ O X X _ \n"
        + "_ _ O X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 3\n"
        + "Player two Score: 3\n"
        + "Player one turn (Black)!\n", testModel6.toString());
    testModel6.makeMove(2, 1);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ O _ _ _ \n"
        + "_ X X X X _ \n"
        + "_ _ O X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 5\n"
        + "Player two Score: 2\n"
        + "Player two turn (White)!\n", testModel6.toString());
    testModel6.makeMove(3, 4);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ O _ _ _ \n"
        + "_ X X O X _ \n"
        + "_ _ O O O _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 3\n"
        + "Player two Score: 5\n"
        + "Player one turn (Black)!\n", testModel6.toString());
  }

  @Test
  public void testCopyPreservesEquality() {
    ReversiModel testModel1 = new BasicReversi(BoardType.SQUARE, 6);
    testModel1.makeMove(1, 3);
    ReversiModel testModel2 = new BasicReversi(BoardType.SQUARE, 6);
    testModel2.makeMove(1, 3);
    Assert.assertEquals(testModel1.copy(), testModel2);
  }

  @Test
  public void testDiagonalMove() {
    BasicReversi testModel = new BasicReversi(BoardType.SQUARE, 6);
    testModel.makeMove(1, 3);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ _ X _ _ \n"
        + "_ _ X X _ _ \n"
        + "_ _ O X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 4\n"
        + "Player two Score: 1\n"
        + "Player two turn (White)!\n", testModel.toString());
    testModel.makeMove(1, 4);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ _ X O _ \n"
        + "_ _ X O _ _ \n"
        + "_ _ O X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 3\n"
        + "Player two Score: 3\n"
        + "Player one turn (Black)!\n", testModel.toString());
    testModel.makeMove(3, 1);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ _ X O _ \n"
        + "_ _ X O _ _ \n"
        + "_ X X X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 5\n"
        + "Player two Score: 2\n"
        + "Player two turn (White)!\n", testModel.toString());
    testModel.makeMove(2, 1);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ _ _ X O _ \n"
        + "_ O O O _ _ \n"
        + "_ X X X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 4\n"
        + "Player two Score: 4\n"
        + "Player one turn (Black)!\n", testModel.toString());
    testModel.makeMove(1, 1);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ X _ X O _ \n"
        + "_ X X O _ _ \n"
        + "_ X X X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 7\n"
        + "Player two Score: 2\n"
        + "Player two turn (White)!\n", testModel.toString());
    testModel.makeMove(4, 1);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ X _ X O _ \n"
        + "_ X X O _ _ \n"
        + "_ X O X _ _ \n"
        + "_ O _ _ _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 6\n"
        + "Player two Score: 4\n"
        + "Player one turn (Black)!\n", testModel.toString());
    testModel.makeMove(4, 3);
    Assert.assertEquals("_ _ _ _ _ _ \n"
        + "_ X _ X O _ \n"
        + "_ X X O _ _ \n"
        + "_ X X X _ _ \n"
        + "_ O _ X _ _ \n"
        + "_ _ _ _ _ _ \n"
        + "Player one Score: 8\n"
        + "Player two Score: 3\n"
        + "Player two turn (White)!\n", testModel.toString());
  }

  @Test
  public void testGameOver() {
    b.wAt(0, 0).wAt(0, 1).bAt(1, 0).bAt(1, 1).pass();
    BasicReversi testModel = b.build();
    testModel.passTurn();
    Assert.assertTrue(testModel.isGameOver());
    Assert.assertEquals(Optional.empty(), testModel.getWinner());
    testModel = b.wAt(2, 1).build();
    testModel.passTurn();
    Assert.assertEquals(Optional.of(CellState.WHITE), testModel.getWinner());
    testModel = b.empty(2, 1).bAt(2, 1).bAt(2, 2).bAt(2, 3).build();
    testModel.passTurn();
    Assert.assertEquals(Optional.of(CellState.BLACK), testModel.getWinner());
  }

  @Test
  public void testGetLegalMoves() {
    BasicReversi testModel = new BasicReversi(BoardType.SQUARE, 4);
    Assert.assertEquals(List.of(new Move(0, 2), new Move(1, 3), new Move(2, 0), new Move(3, 1),
        new Move(true, false, false, false)), testModel.getLegalMoves());
  }
}
