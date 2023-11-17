package cs3500.reversi;

import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.controller.ReversiTextualController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.ReversiModel;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests methods from controller classes.
 */
public class ExamplarControllerTests {
  @Test
  public void testStartGame() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("q");
    ReversiModel model = new BasicReversi(4);
    ReversiController controller = new ReversiTextualController(in, out);
    controller.playGame(model);
    assertFalse(out.toString().isEmpty());
    assertTrue(out.toString().contains("Player one Score: 3"));
    assertTrue(out.toString().contains("Player two Score: 3"));
    assertTrue(out.toString().contains("Player one turn (Black)!"));
  }

  @Test
  public void testStartGameMakeMoves() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("make-move 2 4 make-move 4 1 q");
    ReversiModel model = new BasicReversi(4);
    ReversiController controller = new ReversiTextualController(in, out);
    controller.playGame(model);
    assertFalse(out.toString().isEmpty());
    assertTrue(out.toString().contains("Player one Score: 4"));
    assertTrue(out.toString().contains("Player two Score: 4"));
    assertTrue(out.toString().contains("Player one turn (Black)!"));
  }

  @Test
  public void testStartGamePassTurn() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("pass-turn q");
    ReversiModel model = new BasicReversi(4);
    ReversiController controller = new ReversiTextualController(in, out);
    controller.playGame(model);
    assertFalse(out.toString().isEmpty());
    assertTrue(out.toString().contains("Player one Score: 3"));
    assertTrue(out.toString().contains("Player two Score: 3"));
    assertTrue(out.toString().contains("Player two turn (White)!"));
  }

  @Test
  public void testStartGameBothPassTurns() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("pass-turn pass-turn q");
    ReversiModel model = new BasicReversi(4);
    ReversiController controller = new ReversiTextualController(in, out);
    controller.playGame(model);
    assertFalse(out.toString().isEmpty());
    assertTrue(out.toString().contains("Player one Score: 3"));
    assertTrue(out.toString().contains("Player two Score: 3"));
    assertTrue(out.toString().contains("Game is over!"));

  }

  @Test
  public void testStartGameInvalidCommand() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("invalid q");
    ReversiModel model = new BasicReversi(4);
    ReversiController controller = new ReversiTextualController(in, out);
    controller.playGame(model);
    assertFalse(out.toString().isEmpty());
    assertTrue(out.toString().contains("Undefined command: invalid"));
  }
}
