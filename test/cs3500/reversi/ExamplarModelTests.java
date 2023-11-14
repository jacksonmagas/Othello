package cs3500.reversi;

import org.junit.Assert;
import org.junit.Test;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.ReversiModel;

/**
 * Public class ExamplarModelTests tests the methods in the
 * BasicReversi class and other classes.
 */
public class ExamplarModelTests {
  @Test
  public void testReversiGameInvalidArguments() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversi(2));
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversi(-1));
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversi(0));
  }

  @Test
  public void testReversiGame() {
    ReversiModel model = new BasicReversi(6);
    Assert.assertEquals("Game is started",
            "     _ _ _ _ _ _      \n"
                + "    _ _ _ _ _ _ _     \n"
                + "   _ _ _ _ _ _ _ _    \n"
                + "  _ _ _ _ _ _ _ _ _   \n"
                + " _ _ _ _ X O _ _ _ _  \n"
                + "_ _ _ _ O _ X _ _ _ _ \n"
                + " _ _ _ _ X O _ _ _ _  \n"
                + "  _ _ _ _ _ _ _ _ _   \n"
                + "   _ _ _ _ _ _ _ _    \n"
                + "    _ _ _ _ _ _ _     \n"
                + "     _ _ _ _ _ _      \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Player one turn (Black)!\n",
            model.toString());
  }


  @Test
  public void testReversiGameSmallestSize() {
    ReversiModel model = new BasicReversi(3);
    Assert.assertEquals("Game is started",
            "  _ _ _   \n"
                + " _ X O _  \n"
                + "_ O _ X _ \n"
                + " _ X O _  \n"
                + "  _ _ _   \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Player one turn (Black)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameSmallSize() {
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Player one turn (Black)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameMediumSize() {
    ReversiModel model = new BasicReversi(5);
    Assert.assertEquals("Game is started",
            "    _ _ _ _ _     \n"
                + "   _ _ _ _ _ _    \n"
                + "  _ _ _ _ _ _ _   \n"
                + " _ _ _ X O _ _ _  \n"
                + "_ _ _ O _ X _ _ _ \n"
                + " _ _ _ X O _ _ _  \n"
                + "  _ _ _ _ _ _ _   \n"
                + "   _ _ _ _ _ _    \n"
                + "    _ _ _ _ _     \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Player one turn (Black)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameLargeSize() {
    ReversiModel model = new BasicReversi(10);
    Assert.assertEquals("Game is started",
            "         _ _ _ _ _ _ _ _ _ _          \n"
                + "        _ _ _ _ _ _ _ _ _ _ _         \n"
                + "       _ _ _ _ _ _ _ _ _ _ _ _        \n"
                + "      _ _ _ _ _ _ _ _ _ _ _ _ _       \n"
                + "     _ _ _ _ _ _ _ _ _ _ _ _ _ _      \n"
                + "    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _     \n"
                + "   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _    \n"
                + "  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _   \n"
                + " _ _ _ _ _ _ _ _ X O _ _ _ _ _ _ _ _  \n"
                + "_ _ _ _ _ _ _ _ O _ X _ _ _ _ _ _ _ _ \n"
                + " _ _ _ _ _ _ _ _ X O _ _ _ _ _ _ _ _  \n"
                + "  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _   \n"
                + "   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _    \n"
                + "    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _     \n"
                + "     _ _ _ _ _ _ _ _ _ _ _ _ _ _      \n"
                + "      _ _ _ _ _ _ _ _ _ _ _ _ _       \n"
                + "       _ _ _ _ _ _ _ _ _ _ _ _        \n"
                + "        _ _ _ _ _ _ _ _ _ _ _         \n"
                + "         _ _ _ _ _ _ _ _ _ _          \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Player one turn (Black)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameMakeMove() {
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                  + "  _ _ _ _ _   \n"
                  + " _ _ X O _ _  \n"
                  + "_ _ O _ X _ _ \n"
                  + " _ _ X O _ _  \n"
                  + "  _ _ _ _ _   \n"
                  + "   _ _ _ _    \n"
                  + "Player one Score: 3\n"
                  + "Player two Score: 3\n"
                  + "Player one turn (Black)!\n",
            model.toString());
    model.makeMove(2, 4);
    Assert.assertEquals("Game is updated",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X X X _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Player one Score: 5\n"
                + "Player two Score: 2\n"
                + "Player two turn (White)!\n",
            model.toString());
    /* May be issue with this move in direction to down left
    model.makeMove(1, 4);
    Assert.assertEquals("Game is updated",
                    "   _ _ _ _    \n" +
                    "  _ _ _ _ O   \n" +
                    " _ _ X X O _  \n" +
                    "_ _ O _ O _ _ \n" +
                    " _ _ X O _ _  \n" +
                    "  _ _ _ _ _   \n" +
                    "   _ _ _ _    \n"+
                    "Player one Score: 3\n" +
                    "Player two Score: 5\n" +
                    "Computer turn (White Disc)!\n",
            model.toString());
    */
    model.makeMove(4, 1);
    Assert.assertEquals("Game is updated",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X X X _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ O O O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Player one Score: 4\n"
                + "Player two Score: 4\n"
                + "Player one turn (Black)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameMakeVertMove() {
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                    + "  _ _ _ _ _   \n"
                    + " _ _ X O _ _  \n"
                    + "_ _ O _ X _ _ \n"
                    + " _ _ X O _ _  \n"
                    + "  _ _ _ _ _   \n"
                    + "   _ _ _ _    \n"
                    + "Player one Score: 3\n"
                    + "Player two Score: 3\n"
                    + "Player one turn (Black)!\n",
            model.toString());
    model.makeMove(5, 2);
    Assert.assertEquals("Game is updated",
            "   _ _ _ _    \n"
                    + "  _ _ _ _ _   \n"
                    + " _ _ X O _ _  \n"
                    + "_ _ O _ X _ _ \n"
                    + " _ _ X X _ _  \n"
                    + "  _ _ X _ _   \n"
                    + "   _ _ _ _    \n"
                    + "Player one Score: 5\n"
                    + "Player two Score: 2\n"
                    + "Player two turn (White)!\n",
            model.toString());
    model.makeMove(2, 1);
    Assert.assertEquals("Game is updated",
            "   _ _ _ _    \n"
                    + "  _ _ _ _ _   \n"
                    + " _ O O O _ _  \n"
                    + "_ _ O _ X _ _ \n"
                    + " _ _ X X _ _  \n"
                    + "  _ _ X _ _   \n"
                    + "   _ _ _ _    \n"
                    + "Player one Score: 4\n"
                    + "Player two Score: 4\n"
                    + "Player one turn (Black)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameMakeVertInvalidMoveExpectError() {
    // confirm that a player cannot move to a location that won't flip any tiles
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                    + "  _ _ _ _ _   \n"
                    + " _ _ X O _ _  \n"
                    + "_ _ O _ X _ _ \n"
                    + " _ _ X O _ _  \n"
                    + "  _ _ _ _ _   \n"
                    + "   _ _ _ _    \n"
                    + "Player one Score: 3\n"
                    + "Player two Score: 3\n"
                    + "Player one turn (Black)!\n",
            model.toString());
    Assert.assertThrows("There is no legal move at 1, 1", IllegalStateException.class, () -> model.makeMove(1, 1));
  }

  @Test
  public void testReversiGameMakeVertOutOfBoundsMoveExpectError() {
    // confirm that a player cannot move to a location that's out of bounds negative coordinates
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                    + "  _ _ _ _ _   \n"
                    + " _ _ X O _ _  \n"
                    + "_ _ O _ X _ _ \n"
                    + " _ _ X O _ _  \n"
                    + "  _ _ _ _ _   \n"
                    + "   _ _ _ _    \n"
                    + "Player one Score: 3\n"
                    + "Player two Score: 3\n"
                    + "Player one turn (Black)!\n",
            model.toString());
    Assert.assertThrows("There is no legal move at -1, -1", IllegalStateException.class, () -> model.makeMove(-1, -1));
  }

  @Test
  public void testReversiGameMakeVertOutOfBoundsMoveExpectError2() {
    // confirm that a player cannot move to a location that's out of bounds beyond coordinates
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                    + "  _ _ _ _ _   \n"
                    + " _ _ X O _ _  \n"
                    + "_ _ O _ X _ _ \n"
                    + " _ _ X O _ _  \n"
                    + "  _ _ _ _ _   \n"
                    + "   _ _ _ _    \n"
                    + "Player one Score: 3\n"
                    + "Player two Score: 3\n"
                    + "Player one turn (Black)!\n",
            model.toString());
    Assert.assertThrows("There is no legal move at 15, 15", IllegalStateException.class, () -> model.makeMove(15, 15));
  }

  @Test
  public void testReversiGameWithWin() {
    ReversiModel model = new BasicReversi(3);
    
    Assert.assertEquals("Game is started",
            "  _ _ _   \n" +
                    " _ X O _  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 3\n" +
                    "Player two Score: 3\n" +
                    "Player one turn (Black)!\n",
            model.toString());
    model.makeMove(1, 3);
    Assert.assertEquals("Game is updated",
            "  _ _ _   \n" +
                    " _ X X X  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 5\n" +
                    "Player two Score: 2\n" +
                    "Player two turn (White)!\n",
            model.toString());
    model.makeMove(3, 0);
    Assert.assertEquals("Game is updated",
            "  _ _ _   \n" +
                    " _ X X X  \n" +
                    "_ O _ X _ \n" +
                    " O O O _  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 4\n" +
                    "Player two Score: 4\n" +
                    "Player one turn (Black)!\n",
            model.toString());

    /*
    model.makeMove(4, 1);
    Assert.assertEquals("Game is updated",
            "  _ _ _   \n" +
                    " _ X X X  \n" +
                    "_ O _ X _ \n" +
                    " O O X _  \n" +
                    "  _ X _   \n" +
                    "Player one Score: 6\n" +
                    "Player two Score: 3\n" +
                    "Player one turn (Black)!\n",
            model.toString());
    */
  }

  @Test
  public void testReversiGamePassTurn() {
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Player one turn (Black)!\n",
            model.toString());
    model.passTurn();
    model.makeMove(4, 1);
    Assert.assertEquals("Game is updated",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ O O O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Player one Score: 2\n"
                + "Player two Score: 5\n"
                + "Player one turn (Black)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameBothPassTurns() {
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Player one turn (Black)!\n",
            model.toString());
    model.passTurn();
    model.passTurn();
    Assert.assertEquals("Game is updated",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Game is over!\n"
                + " Tie game",
            model.toString());
    Assert.assertThrows("Game is over", IllegalStateException.class, model::passTurn);
  }

  @Test
  public void testReversiGameGetScores() {
    ReversiModel model = new BasicReversi(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Player one Score: 3\n"
                + "Player two Score: 3\n"
                + "Player one turn (Black)!\n",
            model.toString());
    Assert.assertEquals("Your score", 3, model.getPlayerScore(0));
    Assert.assertEquals("Computer score", 3, model.getPlayerScore(1));
  }
}
