package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.model.ReversiModel;
import java.util.List;
import java.util.function.Function;

/**
 * A GameTree is a class which represents a game state and a set of possible future game states
 * giving each state an evaluation for it's expected win probability.
 */
public class GameTree implements IGameTree {
  private final ReversiModel state;
  private final Move move;
  protected final Function<ReadonlyReversiModel, Integer> evalFunc;
  private final int depth;
  private final IGameTree firstChild;
  private final IGameTree nextSibling;

  /**
   * Constructor for a default game tree which evaluates positions based on the relative difference
   * of their scores.
   * @param initialState The reversi model game to analyze
   * @param depth How many moves in the future to look. Depth of 0 is a tree with no nodes. A depth
   *              of one means examining all the legal moves for the current player, and depth of
   *              two means analyzing all the legal moves for the current player and all legal
   *              responses from the opposing player.
   */
  public GameTree(ReadonlyReversiModel initialState, int depth) {
    this(initialState, depth,
        (ReadonlyReversiModel m) -> m.getPlayerScore(CellState.BLACK)
                                    - m.getPlayerScore(CellState.WHITE));
  }

  // private constructor used to build more of the tree recursively.
  // first builds out all siblings then builds children
  private GameTree(ReadonlyReversiModel parentState, int depth, List<Move> remainingMoves,
      Function<ReadonlyReversiModel, Integer> evalFunc) {
    this.state = parentState.copy();
    this.depth = depth - 1;
    this.evalFunc = evalFunc;
    this.move = remainingMoves.remove(0);
    this.state.makeMove(this.move);
    this.nextSibling = buildOutThisLayer(parentState, depth, remainingMoves);
    this.firstChild = buildNextLayer();
  }

  /**
   * Specific GameTree constructor which allows specifying the function to use to evaluate game
   * states.
   * @param initialState The state of the reversi game which should be analyzed
   * @param depth The depth to make the tree to. A one node GameTree has depth 0.
   * @param evalFunc A function which evaluates a reversi model and returns an integer value
   *                 indicating the relative strength of position for both black and white.
   *                 A negative number means white is winning while a positive number means black
   *                 is winning. The magnitude of the number tells how large the difference between
   *                 the sides is.
   */
  public GameTree(ReadonlyReversiModel initialState, int depth,
      Function<ReadonlyReversiModel, Integer> evalFunc) {
    this.state = initialState.copy();
    this.evalFunc = evalFunc;
    this.depth = depth;
    this.nextSibling = new Leaf();
    this.firstChild = buildNextLayer();
    this.move = null;
  }

  // Build out this layer by adding the next possible move
  // or adding a leaf if all moves have been checked
  private IGameTree buildOutThisLayer(ReadonlyReversiModel parentState, int depth,
      List<Move> remainingMoves) {
    if (remainingMoves.isEmpty() || parentState.isGameOver()) {
      return new Leaf();
    } else {
      return new GameTree(parentState, depth, remainingMoves, this.evalFunc);
    }
  }

  // Build out the next layer by adding a new tree with this state
  private IGameTree buildNextLayer() {
    if (this.depth == 0 || this.state.isGameOver()) {
      return new Leaf();
    } else {
      return new GameTree(this.state, this.depth - 1,
          this.state.getLegalMoves(), this.evalFunc);
    }
  }

  private int evaluate() {
    return this.evalFunc.apply(this.state);
  }

  private static class Leaf implements IGameTree {
    @Override
    public int getExpectedResult() {
      return 0;
    }

    @Override
    public boolean isLeaf() {
      return true;
    }

    @Override
    public Move getBestMove() {
      throw new IllegalStateException("No best move of a leaf");
    }

    @Override
    public IGameTree getNext() {
      throw new IllegalStateException("No next node of a leaf");
    }

    @Override
    public Move getLastMove() {
      throw new IllegalStateException("Leaves aren't reached by moves.");
    }
  }

  @Override
  public int getExpectedResult() {
    return firstChild.isLeaf() ? evaluate()
        : bestChild().getExpectedResult();
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  // get the child that has the best expected result for the current player
  private IGameTree bestChild() {
    IGameTree currentSibling = firstChild;
    IGameTree best = firstChild;
    int bestEval = firstChild.getExpectedResult();
    while (!currentSibling.isLeaf()) {
      if (this.state.getCurrentPlayer() == CellState.BLACK) {
        if (currentSibling.getExpectedResult() > bestEval) {
          best = currentSibling;
          bestEval = currentSibling.getExpectedResult();
        }
      } else {
        if (currentSibling.getExpectedResult() < bestEval) {
          best = currentSibling;
          bestEval = currentSibling.getExpectedResult();
        }
      }
      currentSibling = currentSibling.getNext();
    }
    return best;
  }

  @Override
  public Move getBestMove() {
    if (this.firstChild.isLeaf()) {
      return new HighestScoringMove().chooseMove(this.state, this.state.getCurrentPlayer());
    }
    return bestChild().getLastMove();
  }

  @Override
  public IGameTree getNext() {
    return this.nextSibling;
  }

  @Override
  public Move getLastMove() {
    return this.move;
  }
}