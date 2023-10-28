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
  public void testReversiGame() {
    ReversiModel model = new BasicReversi();
    model.startGame(4);
    model.startGame(6);
    Assert.assertEquals("Game is started",
            "     _ _ _ _ _ _      \n" +
                    "    _ _ _ _ _ _ _     \n" +
                    "   _ _ _ _ _ _ _ _    \n" +
                    "  _ _ _ _ _ _ _ _ _   \n" +
                    " _ _ _ _ X O _ _ _ _  \n" +
                    "_ _ _ _ O _ X _ _ _ _ \n" +
                    " _ _ _ _ X O _ _ _ _  \n" +
                    "  _ _ _ _ _ _ _ _ _   \n" +
                    "   _ _ _ _ _ _ _ _    \n" +
                    "    _ _ _ _ _ _ _     \n" +
                    "     _ _ _ _ _ _      \n", model.toString());
  }
}
