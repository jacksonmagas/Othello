package cs3500.reversi;

import org.junit.Assert;
import org.junit.Test;

import cs3500.reversi.controller.Player;
import cs3500.reversi.controller.PlayerImpl;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.GUIInputStrategy;

public class ExamplarPlayerTests {

  @Test
  public void testHints() {
    int size = 4;
    ReversiModel reversi = new BasicReversi(size);
    Player player1 = new PlayerImpl(CellState.BLACK, new GUIInputStrategy());
    Player player2 = new PlayerImpl(CellState.WHITE, new GUIInputStrategy());
    reversi.startGame();
    Assert.assertEquals("Player1 Turn", CellState.BLACK, reversi.getCurrentPlayer());
    Assert.assertEquals("Player1 score", 3, reversi.getPlayerScore(CellState.BLACK));
    Assert.assertEquals("Player2 score", 3, reversi.getPlayerScore(CellState.WHITE));
    Assert.assertEquals("Player1 Hints disabled", false, reversi.isPlayerHintsEnabled(player1.getPiece()));
    Assert.assertEquals("Player2 Hints disabled", false, reversi.isPlayerHintsEnabled(player2.getPiece()));
    reversi.togglePlayerHints(player1.getPiece());
    Assert.assertEquals("Player1 Hints enabled", true, reversi.isPlayerHintsEnabled(player1.getPiece()));
    Assert.assertEquals("No coins will be flipped", 0, player1.getPossiblePoints(reversi, player1.getPiece(), new Move(0, 0)));
    Assert.assertEquals("1 coins will be flipped", 1, player1.getPossiblePoints(reversi, player1.getPiece(), new Move(1, 2)));
    reversi.togglePlayerHints(player1.getPiece());
    Assert.assertEquals("Player1 Hints disabled", false, reversi.isPlayerHintsEnabled(player1.getPiece()));
    reversi.makeMove(1,2);
    Assert.assertEquals("Player1 score", 5, reversi.getPlayerScore(CellState.BLACK));
    Assert.assertEquals("Player2 score", 2, reversi.getPlayerScore(CellState.WHITE));
    Assert.assertEquals("Player2 Turn", CellState.WHITE, reversi.getCurrentPlayer());
    reversi.togglePlayerHints(player2.getPiece());
    Assert.assertEquals("Player2 Hints enabled", true, reversi.isPlayerHintsEnabled(player2.getPiece()));
    Assert.assertEquals("No coins will be flipped", 0, player2.getPossiblePoints(reversi, player2.getPiece(), new Move(0, 0)));
    Assert.assertEquals("2 coins will be flipped", 2, player2.getPossiblePoints(reversi, player2.getPiece(), new Move(0, 2)));
    reversi.togglePlayerHints(player2.getPiece());
    Assert.assertEquals("Player2 Hints disabled", false, reversi.isPlayerHintsEnabled(player2.getPiece()));
    reversi.makeMove(0,2);
    Assert.assertEquals("Player1 score", 3, reversi.getPlayerScore(CellState.BLACK));
    Assert.assertEquals("Player2 score", 5, reversi.getPlayerScore(CellState.WHITE));
    Assert.assertEquals("Player1 Turn", CellState.BLACK, reversi.getCurrentPlayer());
  }
}
