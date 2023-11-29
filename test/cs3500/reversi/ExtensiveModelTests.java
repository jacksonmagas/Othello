package cs3500.reversi;

import cs3500.reversi.model.BasicReversi;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests model strategies in game play.
 */
public class ExtensiveModelTests {
  private BasicReversiBuilder b;

  @Before
  public void init() {
    b = new BasicReversiBuilder(4);
  }

  // All of these moves should throw because they are going off the edge.
  //TODO: keep investigating this, found more strange moves in manual tests
  @Test
  public void testMovesOffEdgeOfBoardWhite() {
    //horizontal going left
    BasicReversi m1 = b
        .bAt(0, 0)
        .bAt(0, 1)
        .bAt(0, 2)
        .player('w')
        .build();
    Assert.assertThrows(IllegalArgumentException.class, () -> m1.makeMove(0, 3));

    //horizontal going right
    init();
    BasicReversi m2 = b
        .bAt(0, 1).bAt(0, 2).bAt(0, 3)
        .wAt(1, 2)
        .wAt(2, 2).bAt(2, 3).bAt(2, 4)
        .wAt(3, 2).bAt(3, 4)
        .bAt(4, 2).wAt(4, 3)
        .player('w')
        .build();
    Assert.assertThrows(IllegalArgumentException.class, () -> m1.makeMove(0, 0));


  }
}
