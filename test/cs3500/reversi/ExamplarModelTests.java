package cs3500.reversi;

import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

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
                    "Player one turn (Black)!\n",
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
    Assert.assertThrows("There is no legal move at 1, 1", IllegalArgumentException.class, ()
        -> model.makeMove(1, 1));
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
    Assert.assertThrows("There is no legal move at -1, -1", IllegalArgumentException.class, ()
        -> model.makeMove(-1, -1));
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
    Assert.assertThrows("There is no legal move at 15, 15", IllegalArgumentException.class, ()
        -> model.makeMove(15, 15));
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
    model.makeMove(0, 1);
    Assert.assertEquals("Game is updated",
            "  _ O _   \n" +
                    " _ O X X  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 4\n" +
                    "Player two Score: 4\n" +
                    "Player one turn (Black)!\n",
            model.toString());

    model.passTurn();
    Assert.assertEquals("Game is updated",
            "  _ O _   \n" +
                    " _ O X X  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 4\n" +
                    "Player two Score: 4\n" +
                    "Player two turn (White)!\n",
            model.toString());
    model.makeMove(3, 3);
    Assert.assertEquals("Game is updated",
            "  _ O _   \n" +
                    " _ O O X  \n" +
                    "_ O _ O _ \n" +
                    " _ X O O  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 2\n" +
                    "Player two Score: 7\n" +
                    "Player one turn (Black)!\n",
            model.toString());
    model.passTurn();
    model.passTurn();
    Assert.assertEquals("Game is updated",
            "  _ O _   \n" +
                    " _ O O X  \n" +
                    "_ O _ O _ \n" +
                    " _ X O O  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 2\n" +
                    "Player two Score: 7\n" +
                    "Game is over!\n" +
                    " Player two (White) won",
            model.toString());
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
    Assert.assertEquals("Your score", 3, model.getPlayerScore(CellState.BLACK));
    Assert.assertEquals("Computer score", 3, model.getPlayerScore(CellState.WHITE));
  }

  @Test
  public void testReversiGameGetPlayers() {
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
    Assert.assertEquals("Player one turn (Black)!", CellState.BLACK, model.getCurrentPlayer());
    model.passTurn();
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
                    + "Player two turn (White)!\n",
            model.toString());
    Assert.assertEquals("Player two turn (White)!", CellState.WHITE, model.getCurrentPlayer());
  }

  @Test
  public void testReversiGameIsGameOver() {
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
    Assert.assertEquals("Player one turn (Black)!", CellState.BLACK, model.getCurrentPlayer());
    model.passTurn();
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
                    + "Player two turn (White)!\n",
            model.toString());
    Assert.assertEquals("Player two turn (White)!", CellState.WHITE, model.getCurrentPlayer());
    model.passTurn();
    Assert.assertEquals("Game is over", true, model.isGameOver());
  }

  @Test
  public void testReversiGameGetBoard() {
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
    int[][] board = model.getBoard();
    Assert.assertEquals("X match", (int)'X', board[2][2]);
    Assert.assertEquals("O match", (int)'O', board[2][3]);
  }

  @Test
  public void testReversiGameBoardValidMovesFirstAvailableMoveValidateCells() {
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
    List<List<CellState>> gameBoard = model.getGameBoard();
    Assert.assertEquals("Player one turn (Black)!", CellState.BLACK, gameBoard.get(2).get(2));
    Assert.assertEquals("Player two turn (WHITE)!", CellState.WHITE, gameBoard.get(2).get(3));
    Assert.assertEquals("Player one turn (BLACK)!", CellState.BLACK, model.getTileAt(2, 2));
    Assert.assertEquals("Player two turn (WHITE)!", CellState.WHITE, model.getTileAt(2, 3));
    Assert.assertEquals("Game board side length is 4!", 4, model.sideLength());

    Assert.assertEquals("Game board has any legal moves!", true, model.anyLegalMoves());

    List<Move> moves =  model.getLegalMoves();
    Assert.assertEquals("Game board has 7 legal moves!", 7, moves.size());
    Assert.assertEquals("Game board has 1st legal move match row!", 1, moves.get(0).getPosn().row);
    Assert.assertEquals("Game board has 1st legal move match col!", 2, moves.get(0).getPosn().col);
    Assert.assertEquals("Game board has last legal move match is pass-turn!", true, model.getLegalMoves().get(6).isPassTurn());

    Assert.assertEquals("Game board has 1st legal move match row!",
        1, model.getLegalMoves().get(0).getPosn().row);
    Assert.assertEquals("Game board has 1st legal move match col!",
        2, model.getLegalMoves().get(0).getPosn().col);

    Assert.assertEquals("Player next instructions match!", "Player one turn (Black)!\n", model.getNextStepInstructions());
    Assert.assertEquals("Game do not have any error now!", "", model.getLastErrorMessage());

    Assert.assertEquals("Game current row matched!", 0, model.getHighlightedCell().getRow());
    Assert.assertEquals("Game current column matched!", 0, model.getHighlightedCell().getColumn());
    model.setHighlightedCell(1,1);
    Assert.assertEquals("Game current row matched!", 1, model.getHighlightedCell().getRow());
    Assert.assertEquals("Game current column matched!", 1, model.getHighlightedCell().getColumn());
  }

  @Test
  public void testReversiGameWithWinThenRestart() {
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
    model.makeMove(0, 1);
    Assert.assertEquals("Game is updated",
            "  _ O _   \n" +
                    " _ O X X  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 4\n" +
                    "Player two Score: 4\n" +
                    "Player one turn (Black)!\n",
            model.toString());

    model.passTurn();
    Assert.assertEquals("Game is updated",
            "  _ O _   \n" +
                    " _ O X X  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 4\n" +
                    "Player two Score: 4\n" +
                    "Player two turn (White)!\n",
            model.toString());
    model.makeMove(3, 3);
    Assert.assertEquals("Game is updated",
            "  _ O _   \n" +
                    " _ O O X  \n" +
                    "_ O _ O _ \n" +
                    " _ X O O  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 2\n" +
                    "Player two Score: 7\n" +
                    "Player one turn (Black)!\n",
            model.toString());
    model.passTurn();
    model.passTurn();
    Assert.assertEquals("Game is updated",
            "  _ O _   \n" +
                    " _ O O X  \n" +
                    "_ O _ O _ \n" +
                    " _ X O O  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 2\n" +
                    "Player two Score: 7\n" +
                    "Game is over!\n" +
                    " Player two (White) won",
            model.toString());
    model.newGame();
    Assert.assertEquals("Game is updated",
            "  _ _ _   \n" +
                    " _ X O _  \n" +
                    "_ O _ X _ \n" +
                    " _ X O _  \n" +
                    "  _ _ _   \n" +
                    "Player one Score: 3\n" +
                    "Player two Score: 3\n" +
                    "Player one turn (Black)!\n",
            model.toString());
  }
}
