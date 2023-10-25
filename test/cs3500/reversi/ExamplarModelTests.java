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
    model.startGame(4, 0);
    Assert.assertEquals("Game is started",
            "- - - - \n" +
                    "- X O - \n" +
                    "- O X - \n" +
                    "- - - - \n", model.toString());
  }
}
