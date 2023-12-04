package cs3500.reversi.view;

/**
 * Represents a view for the Reversi game. This view can either be textual or graphical.
 * The view is responsible for rendering the current game state. It can also be responsible
 * for communicating with the controller to get the next move (in the case of a graphical view).
 */
public interface IView {

  /**
   * Renders the current game state. This method can either render the view in a printed format
   * that is presentable by system.out. (as is shown by the {@code ReversiTextualView} class or
   * it can render it through the graphical user interface (in which case it will set the
   * visibility of the frame to true.
   * @throws RuntimeException if the Appendable fails to append (particularly applicable to the
   *                          {@code ReversiTextualView} class.
   */
  public void render();
}
