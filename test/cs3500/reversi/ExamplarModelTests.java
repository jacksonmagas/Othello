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
    ReversiModel model = new BasicReversi();
    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(0));
  }

  @Test
  public void testReversiGame() {
    ReversiModel model = new BasicReversi();
    model.startGame(6);
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
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameAlreadyStarted() {
    ReversiModel model = new BasicReversi();
    model.startGame(6);
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
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
    Assert.assertThrows(IllegalStateException.class, () -> model.startGame(6));
  }

  @Test
  public void testReversiGameSmallestSize() {
    ReversiModel model = new BasicReversi();
    model.startGame(3);
    Assert.assertEquals("Game is started",
            "  _ _ _   \n"
                + " _ X O _  \n"
                + "_ O _ X _ \n"
                + " _ X O _  \n"
                + "  _ _ _   \n"
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameSmallSize() {
    ReversiModel model = new BasicReversi();
    model.startGame(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameMediumSize() {
    ReversiModel model = new BasicReversi();
    model.startGame(5);
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
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameLargeSize() {
    ReversiModel model = new BasicReversi();
    model.startGame(10);
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
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameMakeMove() {
    ReversiModel model = new BasicReversi();
    model.startGame(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
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
                + "Your Score: 5\n"
                + "Computer Score: 2\n"
                + "Computer turn (White Disc)!\n",
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
                    "Your Score: 3\n" +
                    "Computer Score: 5\n" +
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
                + "Your Score: 4\n"
                + "Computer Score: 4\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameWithWin() {
    ReversiModel model = new BasicReversi();
    model.startGame(3);
    Assert.assertEquals("Game is started",
            "  _ _ _   \n" +
                    " _ X O _  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Your Score: 3\n" +
                    "Computer Score: 3\n" +
                    "Your turn (Black Disc)!\n",
            model.toString());
    model.makeMove(1, 3);
    Assert.assertEquals("Game is updated",
            "  _ _ _   \n" +
                    " _ X X X  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Your Score: 5\n" +
                    "Computer Score: 2\n" +
                    "Computer turn (White Disc)!\n",
            model.toString());
    model.makeMove(3, 0);
    Assert.assertEquals("Game is updated",
            "  _ _ _   \n" +
                    " _ X X X  \n" +
                    "_ O _ X _ \n" +
                    " O O O _  \n" +
                    "  _ _ _   \n" +
                    "Your Score: 4\n" +
                    "Computer Score: 4\n" +
                    "Your turn (Black Disc)!\n",
            model.toString());

    /*
    model.makeMove(4, 1);
    Assert.assertEquals("Game is updated",
            "  _ _ _   \n" +
                    " _ X X X  \n" +
                    "_ O _ X _ \n" +
                    " O O X _  \n" +
                    "  _ X _   \n" +
                    "Your Score: 6\n" +
                    "Computer Score: 3\n" +
                    "Your turn (Black Disc)!\n",
            model.toString());
    */
  }

  @Test
  public void testReversiGamePassTurn() {
    ReversiModel model = new BasicReversi();
    model.startGame(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
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
                + "Your Score: 2\n"
                + "Computer Score: 5\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
  }

  @Test
  public void testReversiGameBothPassTurns() {
    ReversiModel model = new BasicReversi();
    model.startGame(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
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
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Game is over!\n",
            model.toString());
    Assert.assertThrows("Game is over", IllegalStateException.class, () -> model.passTurn());
  }

  @Test
  public void testReversiGameGetScores() {
    ReversiModel model = new BasicReversi();
    model.startGame(4);
    Assert.assertEquals("Game is started",
            "   _ _ _ _    \n"
                + "  _ _ _ _ _   \n"
                + " _ _ X O _ _  \n"
                + "_ _ O _ X _ _ \n"
                + " _ _ X O _ _  \n"
                + "  _ _ _ _ _   \n"
                + "   _ _ _ _    \n"
                + "Your Score: 3\n"
                + "Computer Score: 3\n"
                + "Your turn (Black Disc)!\n",
            model.toString());
    Assert.assertEquals("Your score", 3, model.getYourScore());
    Assert.assertEquals("Computer score", 3, model.getComputerScore());
  }
}
