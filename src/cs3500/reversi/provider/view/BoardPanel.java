package cs3500.reversi.provider.view;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.FontMetrics;
import java.awt.FontFormatException;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.Action;


import cs3500.reversi.provider.controller.IViewFeatures;
import cs3500.reversi.provider.model.Board;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.MutableModel;
import cs3500.reversi.provider.model.PlayerDisc;


/**
 * The class {@code BoardPanel} helps implement the graphical view of the game. It takes in a
 * {@code IROModel} to render the graphical view of the game based on that model's state.
 * This class is the main panel for the view and includes information about the relevant
 * listeners that will be used for the user to interact with the game.
 */
public class BoardPanel extends JPanel implements IReversiPanel {
  private final int boardWidth;
  private final int boardHeight;
  private HashMap<Hex, PlayerDisc> hexagons;
  private int sideLength;
  private final int halfSideLength;
  private final int scaleBy;
  private Optional<Hex> selectedHex;
  private final List<IViewFeatures> featureListeners = new ArrayList<>();
  private final Color defaultBackgroundColor = Color.decode("#e3c7ff");
  private final Color clickedBackgroundColor = Color.decode("#b79ced");
  private final JLabel coordInfo;
  private final IROModel model;
  private Board board;
  private int curFrameWidth;
  private int curFrameHeight;

  /**
   * <p>Constructs a {@code BoardPanel} object. It takes in a {@code IROModel} to render the
   * graphical view of the game based on model's state. In our game, the side length
   * represents any of the size edges of a hexagon. To ensure that all elements of our view are
   * visible we also include the integer {@code scaleBy} to help enlarge any elements that uses
   * logical dimensions rather than physical dimensions.</p>
   *
   * <p>In addition to setting up the dimensions of the panel, this constructor also sets up
   * information about each hex coord. This information is rendered onto the panel to aid the
   * player understand which hex they are placing a disc on. The coord info is a {@code JLabel}
   * that can be conveniently removed, should the player decide that they do not want to include
   * hex coordinate information in the view. This method also calculates the width and height
   * of each hexagon based on the value of {@code sideLength}. It sets the value of the
   * {@code seletedHex} as empty, as no hex has yet been selected when the view is first
   * constructed.</p>
   *
   * <p>Lastly, the constructor of this panel also includes the mouse listener and the input/action
   * maps for the key listeners. This panel supports two kinds of keyboard inputs: pressing
   * the "s" key will skip the player's turn if that movement is legal and pressing the "enter"
   * key will place the disc on the cell that the player has selected if that movement is legal.</p>
   *
   * <p>This constructor ensures the following invariants:</p>
   * <ul>
   *   <li>The model and board is never null.</li>
   *   <li>The hashmap that represents the hexagons is never null.</li>
   *   <li>The side length is always positive.</li>
   *   <li>The value of scale is always positive.</li>
   *   <li>The height and width of the hexagons are always positive.</li>
   *   <li>The board height and width are always positive.</li>
   *   <li>Only one cell can be selected at a time</li>
   * </ul>
   *
   * @param model the model whose board and status information will be displayed in this panel.
   * @param frame the frame in which this panel will be referring to when recomposition happens.
   */
  public BoardPanel(IROModel model, JFrame frame) {
    super(new BorderLayout());
    Objects.requireNonNull(model);
    this.model = model;
    this.board = model.getBoard();
    this.scaleBy = 12;
    this.sideLength = scaleBy * 3;
    this.boardHeight = calculateDimension();
    this.boardWidth = calculateDimension();
    ensurePositiveDimension(boardHeight, boardWidth);
    ensurePositiveUnit(scaleBy);
    ensurePositiveUnit(sideLength);
    Dimension dimension = new Dimension(boardWidth, boardHeight);
    this.setPreferredSize(dimension);

    coordInfo = new JLabel("");

    hexagons = board.getHexagons();
    Objects.requireNonNull(board);
    Objects.requireNonNull(hexagons);
    halfSideLength = sideLength / 2;
    int hexHeight = sideLength * 2;
    int hexWidth = sideLength * 3 / 2;
    ensurePositiveDimension(hexHeight, hexWidth);
    selectedHex = Optional.empty();
    mouseClickedCallBack();

    adjustPanelBasedOnSize(frame);

    //gets the input map and action map
    InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = this.getActionMap();

    //maps the keys to actions
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "skipTurn");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "placeDisc");
    actionMap.put("skipTurn", skipTurn);
    actionMap.put("placeDisc", placeDisc);
  }

  //ENSURERS:

  /**
   * Ensures that the given unit is non-zero positive integer.
   *
   * @param intToCheck the unit to be checked.
   */
  private void ensurePositiveUnit(int intToCheck) {
    if (intToCheck <= 0) {
      throw new IllegalArgumentException("Scale by must be positive");
    }
  }

  /**
   * Ensures that the given dimension involves non-zero positive integers.
   *
   * @param height the height of the dimension.
   * @param width  the width of the dimension.
   */
  private void ensurePositiveDimension(int height, int width) {
    if (height <= 0 || width <= 0) {
      throw new IllegalArgumentException("Height and width must be positive");
    }
  }

  //RENDERING METHODS:

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    AffineTransform oldTransform = g2d.getTransform();
    g2d.translate(0, 0);
    g2d.scale(1, 1);

    URL background = ClassLoader.getSystemResource("background.png");
    // check if the image was loaded successfully
    if (background != null) {
      ImageIcon backgroundImage = new ImageIcon(background);
      g2d.drawImage(backgroundImage.getImage(), 0, 0,
              curFrameWidth, curFrameHeight, this);
    } else {
      // if image not found, set a solid color background
      this.setBackground(Color.decode("#301934"));
    }

    for (Hex hex : hexagons.keySet()) {
      Point cartesianPoint = convertAxialToPhysical(hex.getQ(), hex.getR());
      drawHex(g2d, isThisHexSelected(hex), cartesianPoint.x, cartesianPoint.y);
      fillCellAccordingly(g2d, isThisHexSelected(hex));
      placeDiscOnTakenCells(hex, g2d, cartesianPoint);
      coordInfo.setBounds(0, curFrameHeight / 2 - 50, getWidth(), getHeight());
    }
    addBoardInfo(g2d);
    g2d.setTransform(oldTransform);
  }

  /**
   * Calculates the dimension of the board based on the radius. The formula used to calculate the
   * board ensures that dimensions stay in proportion to the radius, should the player choose
   * to configure the game with a different radius.
   *
   * @return the dimension of the board rounded as an integer.
   */
  private int calculateDimension() {
    int factor = scaleBy / 3;
    int adjustBy = scaleBy - (factor);
    return sideLength * board.getRadius() * (1 + factor) -
            (adjustBy * board.getRadius());
  }


  /**
   * Renders the selected hex coordinate information. This method is called when the user clicks
   * on a hexagon. It removes the previous JLabel and replaces it with a new JLabel that displays
   * the selected hex coordinate information. If no hex is selected, the JLabel is removed.
   * This method is called in the {@code mouseClickedCallBack} method. It uses a custom font
   * (Londrina Solid Regular) that was downloaded from the following link:
   * {<a href="https://fonts.google.com/specimen/Londrina+Solid">...</a>}.
   */
  private void renderSelectedHexCoord() {
    if (selectedHex.isPresent()) {
      String selectedCell = "Selected Cell: (" + selectedHex.get().getQ() + ", "
              + selectedHex.get().getR() + ")";
      coordInfo.setText(selectedCell);
    }
    if (selectedHex.isEmpty()) {
      coordInfo.setText("");
    }

    Font customFont = loadCustomFont(15.0f);
    coordInfo.setFont(customFont);
    coordInfo.setForeground(Color.WHITE);
    coordInfo.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(coordInfo);
  }

  /**
   * Retrieves the currently selected cell and returns it as a StringBuilder object.
   * This method allows the user to easily understand which cell is selected. it is also made
   * protected as it allows us to easily test our GUI and key inputs.
   *
   * @return a StringBuilder object representing the currently selected cell. the StringBuilder
   *         object is empty if no cell is selected.
   */
  protected StringBuilder getSelectedCoord() {
    StringBuilder cell = new StringBuilder();
    selectedHex.ifPresent(hex -> cell.append("Selected Cell: (").append(hex.getQ()).append(", ")
            .append(hex.getR()).append(")"));
    return cell;
  }

  /**
   * Adds information about the board to the panel. This information includes the score of each
   * player and who's turn it is. This method is a void method because it directly adds text
   * to the given graphics. This method is called by the {@code paintMethod} method.
   * This method uses a custom font (Londrina Solid Regular) that was downloaded from the following
   * {<a href="https://fonts.google.com/specimen/Londrina+Solid">...</a>}.
   *
   * @param g2d the graphics to which the rendered information is added.
   */
  private void addBoardInfo(Graphics2D g2d) {
    Font bigFont = loadCustomFont(20.0f);
    g2d.setFont(bigFont);
    g2d.setColor(Color.WHITE);
    updateTurnInfo(g2d, bigFont);

    Font smallFont = loadCustomFont(15.0f);
    String kirbyScore = "Kirby: " + model.getScore(PlayerDisc.BLACK);
    String metaScore = "Meta Knight: " + model.getScore(PlayerDisc.WHITE);
    g2d.setFont(smallFont);
    FontMetrics scoreMetrics = g2d.getFontMetrics(smallFont);
    int scoreHeight = scoreMetrics.getHeight();
    int msX = (curFrameWidth - scoreMetrics.stringWidth(metaScore)) - scaleBy;
    g2d.drawString(kirbyScore, scaleBy, curFrameHeight - scoreHeight * 2);
    g2d.drawString(metaScore, msX, curFrameHeight - scoreHeight * 2);
  }

  /**
   * Updates the turn information. This method is called in the {@code addBoardInfo} method.
   * @param g2d the graphics to which the rendered information is added.
   * @param bigFont the font of the text.
   */
  private void updateTurnInfo(Graphics2D g2d, Font bigFont) {
    String turn = nextPlayerTurnInfo();
    FontMetrics turnMetrics = g2d.getFontMetrics(bigFont);
    int turnX = turnMetrics.stringWidth(turn);
    g2d.drawString(turn, (curFrameWidth / 2) - turnX / 2, scaleBy * 2 - 5);
  }

  /**
   * Finds the appropriate string that represents the current player's turn. This method is
   * called in the {@code addBoardInfo} method. The {@code PlayerDisc.BLACK}'s turn represents
   * Kirby in the context of our game and the {@code PlayerDisc.WHITE}'s turn represents Meta
   * Knight in the context of our game.
   *
   * @return a String that indicates whose turn it is.
   */
  private String nextPlayerTurnInfo() {
    if (model.getNextPlayerTurn() == PlayerDisc.BLACK) {
      return "Kirby's turn.";
    } else {
      return "Meta Knight's turn.";
    }
  }

  /**
   * Loads a custom font from the given file name and font size. This method is called in the
   * {@code addBoardInfo} method and the {@code renderSelectedHexCoord} method. The font file
   * must be in the same directory as the src folder. The link to the font can be found in:
   * {<a href="https://fonts.google.com/specimen/Londrina+Solid">...</a>}.
   *
   * @param fontSize the size of the font.
   * @return a Font object representing the custom font.
   */
  private Font loadCustomFont(float fontSize) {
    try {
      URL fontPath = ClassLoader.getSystemResource("LondrinaSolid-Regular.ttf");
      return Font.createFont(Font.TRUETYPE_FONT, fontPath.openStream()).deriveFont(fontSize);
    } catch (IOException | FontFormatException | NullPointerException e) {
      return new Font("Times New Roman", Font.PLAIN, 20);
    }
  }

  /**
   * Updates the hexagons on the board. Since {@code model.getBoard} and {@code board.getHexagons}
   * retrieve copies of the board and hexagons, they have to be constantly updated to reflect
   * the changes in the game.
   */
  private void updateHexagons() {
    board = model.getBoard();
    hexagons = board.getHexagons();
  }

  /**
   * <p>Places a disc on the given hex. This method is called in the {@code paintComponent}
   * method. It places a disc on the given hex if the hex is not empty. It places a black disc
   * if the hex is occupied by a black disc and a white disc if the hex is occupied by a white
   * disc. This method is void because it directly paints the disc on the given graphics.</p>
   *
   * <p>It uses the {@code ImageIcon} class to load the image of the disc. The images of the
   * discs are downloaded from the following link:
   * {<a href="https://tinyurl.com/52uazkzh">...</a>} (for Kirby)
   * and {<a href="https://tinyurl.com/2bvt7eyp">...</a>} (Meta Knight).
   * NOTE: Although the image is not all rights reserved, we are only using it for educational
   * purposes in the context of this class.
   * The image of the discs are by default set proportionate to the side length.</p>
   *
   * @param hex the hex to be checked.
   * @param g2d the graphics to which the disc is painted.
   * @param cartesianPoint the cartesian coordinates of the hex.
   */
  private void placeDiscOnTakenCells(Hex hex, Graphics2D g2d, Point cartesianPoint) {
    updateHexagons();
    AffineTransform oldTransform = g2d.getTransform();
    cartesianPoint.x -= sideLength - halfSideLength + sideLength / 7;
    cartesianPoint.y -= sideLength;

    int imgX = cartesianPoint.x + curFrameWidth / 2;
    int imgY = cartesianPoint.y + curFrameHeight / 2;
    URL kirby = ClassLoader.getSystemResource("kirby.png");
    URL metaKnight = ClassLoader.getSystemResource("metaKnight.png");

    // check if the image was loaded successfully
    if (hexagons.get(hex).equals(PlayerDisc.BLACK)) {
      renderKirby(g2d, kirby, imgX, imgY, sideLength);
    }
    if (hexagons.get(hex).equals(PlayerDisc.WHITE)) {
      renderMetaKnight(g2d, metaKnight, imgX, imgY, sideLength);
    }
    g2d.setTransform(oldTransform);
  }

  /**
   * Renders an image of MetaKnight by finding the URL in the resources' directory. If the image
   * is not found, it creates a simpler image of MetaKnight by drawing a circle.
   * @param g2d the graphics to which the image is painted.
   * @param metaKnight the URL of the image.
   * @param imgX the x coordinate of the image.
   * @param imgY the y coordinate of the image.
   * @param imageSize the size of the image.
   */
  private void renderMetaKnight(Graphics2D g2d, URL metaKnight, int imgX, int imgY, int imageSize) {
    if (metaKnight != null) {
      ImageIcon metaKnightHead = new ImageIcon(metaKnight);
      g2d.drawImage(metaKnightHead.getImage(), imgX, imgY, imageSize, imageSize, this);
    } else {
      // if meta knight image not found, create simpler image
      Ellipse2D metaCircle = new Ellipse2D.Double(imgX, imgY, sideLength, sideLength);
      g2d.setColor(Color.decode("#3d007c"));
      g2d.fill(metaCircle);
      g2d.draw(metaCircle);
    }
  }

  /**
   * Renders an image of Kirby by finding the URL in the resources' directory. If the image
   * is not found, it creates a simpler image of Kirby by drawing a circle.
   * @param g2d the graphics to which the image is painted.
   * @param kirby the URL of the image.
   * @param imgX the x coordinate of the image.
   * @param imgY the y coordinate of the image.
   * @param imageSize the size of the image.
   */
  private void renderKirby(Graphics2D g2d, URL kirby, int imgX, int imgY, int imageSize) {
    if (kirby != null) {
      ImageIcon kirbyHead = new ImageIcon(kirby);
      g2d.drawImage(kirbyHead.getImage(), imgX, imgY, imageSize, imageSize, this);
    } else {
      // if kirby image not found, create simpler image
      Ellipse2D kirbyCircle = new Ellipse2D.Double(imgX, imgY, sideLength, sideLength);
      g2d.setColor(Color.decode("#d74894"));
      g2d.fill(kirbyCircle);
      g2d.draw(kirbyCircle);
    }
  }

  /**
   * <p>Draws a hexagon with the given parameters. The parameters {@param originX}
   * and {@code originY} represent the origin of the hexagon. The origin of the hexagon
   * represents the center point of each hex. These coordinates use a Cartesian coordinate
   * and not an axial coordinate. It uses the class {@code Path2D.Double} to draw lines
   * from one point to the other. It uses the class {@code AffineTransform} to rotate the
   * hexagon by 90 degrees. It uses the class {@code Graphics2D} to fill the hexagon with
   * the appropriate color and to draw the border of the hexagon.</p>
   *
   * @param g2d      the graphics object.
   * @param selected whether the hexagon is selected or not.
   * @param originX  the x coordinate of the hexagon.
   * @param originY  the y coordinate of the hexagon.
   */
  private void drawHex(Graphics2D g2d, boolean selected, int originX, int originY) {
    int center = (curFrameHeight / 2) - halfSideLength;

    Path2D.Double hexagon = new Path2D.Double();
    for (int i = 0; i < 6; i++) {
      double angle = 2.0 * Math.PI / 6 * i;
      double x = originX + center + sideLength * Math.cos(angle);
      double y = originY - center + sideLength * Math.sin(angle);

      if (i == 0) {
        hexagon.moveTo(x, y);
      } else {
        hexagon.lineTo(x, y);
      }
    }

    hexagon.closePath();
    AffineTransform rotation = AffineTransform
            .getRotateInstance(Math.PI / 2, originX, originY);
    hexagon.transform(rotation);
    fillCellAccordingly(g2d, selected);
    g2d.fill(hexagon);
    g2d.setColor(Color.BLACK); //set the border color
    g2d.draw(hexagon);
  }

  /**
   * <p>Chooses the color to fill the hexagon with based on whether the hex is selected or not.</p>
   *
   * @param g2d      the graphics object.
   * @param selected whether the hexagon is selected or not.
   */
  private void fillCellAccordingly(Graphics2D g2d, boolean selected) {
    if (selected) {
      g2d.setColor(clickedBackgroundColor);
    } else {
      g2d.setColor(defaultBackgroundColor);
    }
  }

  /**
   * Adjusts the composition of the panel based on the size of the frame.
   * @param frame the frame in which this panel will be referring to when recomposition happens.
   */
  private void adjustPanelBasedOnSize(JFrame frame) {
    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        // initialize the current frame dimensions
        curFrameWidth = frame.getWidth();
        curFrameHeight = frame.getHeight();
        int resizingBuffer = curFrameWidth - calculateDimension();
        sideLength += resizingBuffer / 10;
        repaint();
      }
    });
  }

  @Override
  public void updateBoard() {
    repaint();
  }

  @Override
  public Dimension getDimensions() {
    return new Dimension(boardWidth - 5, boardHeight + 26);
  }

  //ACTION METHODS:

  /**
   * This method represents the mouse listener for the panel. It listens for mouse clicks and
   * converts the physical coordinates of the mouse click to axial coordinates. It then checks
   * if the hexagon that was clicked is empty. If it is empty, it checks if the hexagon that was
   * clicked is the same as the previously selected hexagon. If it is the same, it deselects the
   * hexagon. If it is not the same, it deselects the previously selected hexagon and selects the
   * new hexagon. If the hexagon that was clicked is not empty, it does nothing.
   */
  private void mouseClickedCallBack() {
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        boolean hexClicked = false;
        Point physicalP = e.getPoint();
        Hex mouseClicked = BoardPanel.this.convertPhysicalToAxial(physicalP.x,
                physicalP.y, sideLength);
        if (board.getHexagons().containsKey(mouseClicked)
                && board.getHexagons().get(mouseClicked).equals(PlayerDisc.EMPTY)) {
          adjustSelection(mouseClicked);
          hexClicked = true;
        }
        resetIfNoHexClicked(hexClicked);
        renderSelectedHexCoord();
        repaint();
      }
    });
  }

  /**
   * <p>Resets the selected hex if no hex was clicked. This method is called in the
   * {@code mouseClickedCallBack} method.</p>
   *
   * @param hexClicked whether a hex was clicked or not.
   */
  private void resetIfNoHexClicked(boolean hexClicked) {
    if (!hexClicked) {
      selectedHex = Optional.empty();
    }
  }

  /**
   * <p>This method adjusts the selected cell based on the current settings of the panel.
   * In particular, it checks if the hexagon that was clicked is empty. It also checks if the
   * hexagon that was clicked is the same as the previously selected hexagon.</p>
   *
   * @param hex the hexagon that was clicked.
   */
  private void adjustSelection(Hex hex) {
    if (selectedHex.isEmpty()) {
      selectCell(hex);
    } else if (selectedHex.get().equals(hex)) {
      deSelectCell();
    } else {
      deSelectCell();
      selectCell(hex);
    }
  }

  /**
   * Selects the given hex by setting the optional selected hex to the given hex. This method is
   * protected so that it can be used in our tests for our GUI and key inputs.
   *
   * @param hex the hex to be selected.
   */
  protected void selectCell(Hex hex) {
    selectedHex = Optional.of(hex);
  }

  /**
   * Deselects the selected hex by setting the optional selected hex to empty.
   */
  private void deSelectCell() {
    selectedHex = Optional.empty();
  }

  /**
   * <p>Checks if the given hex is the same as the selected hex. This method is used to find
   * whether the given hex has been clicked/ selected by the user.</p>
   *
   * @param hex the hex to be checked.
   * @return whether the given hex has been clicked.
   */
  private boolean isThisHexSelected(Hex hex) {
    return selectedHex.isPresent() && this.selectedHex.get().equals(hex);
  }

  /**
   * <p>Converts the given axial coordinates to cartesian coordinates.</p>
   *
   * @param q the q coordinate in axial coordinates.
   * @param r the r coordinate in axial coordinates.
   * @return a Point representing the cartesian coordinates.
   */
  private Point convertAxialToPhysical(int q, int r) {
    int x = (int) (sideLength * Math.sqrt(3) * (q + r / 2.0));
    int y = (int) (sideLength * 3 / 2.0 * r);
    return new Point(x, y);
  }

  @Override
  public void addFeatures(IViewFeatures feature) {
    featureListeners.add(feature);
  }

  /**
   * <p>This method instantiates the class {@code AbstractAction} to represent the action
   * of skipping turns. While this panel does not directly mutate the model, this method is used
   * to communicate a callback to any of the listeners of this panel. This method is still
   * incomplete because there is no controller to add itself as a listener of this view.
   * We commented out the lines of code that we plan to add once our features interface, as well
   * as our controller, are completely implemented.</p>
   *
   * @returns the Action that the key 's' is mapped to.
   */
  private final Action skipTurn = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      for (IViewFeatures feature : featureListeners) {
        feature.skipTurn();
      }
    }
  };

  /**
   * <p>This method instantiates the class {@code AbstractAction} to represent the action
   * of physically placing a disc on the board. While this panel does not directly mutate
   * the model, this method is used to communicate a callback to any of the listeners of this panel.
   * This method is still incomplete because there is no controller to add itself as a
   * listener of this view. We commented out the lines of code that we plan to add once our
   * features interface, as well as our controller, are completely implemented.</p>
   *
   * @returns the Action that the key 'enter' is mapped to.
   */
  private final Action placeDisc = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      for (IViewFeatures feature : featureListeners) {
        selectedHex.ifPresent(hex -> feature.placeDisc(hex.getQ(), hex.getR()));
      }
    }
  };

  /**
   * Converts physical screen coordinates to axial coordinates. It uses the values of
   * the radius and scaleBy to calculate the axial coordinates of our Reversi game.
   * It uses the method {@code axialRound} to round the values of q and r to the appropriate
   * axial coordinate.
   *
   * @param x       represents the x coordinate of the physical screen.
   * @param y       represents the y coordinate of the physical screen.
   * @param hexSize represents the size of the hexagon.
   * @return a Hex object representing the axial coordinates of the given physical screen
   *         coordinates.
   */
  private Hex convertPhysicalToAxial(double x, double y, int hexSize) {
    // Adjust for the hex grid layout
    x -= (curFrameWidth / 2) - scaleBy * 3 / 4 - 5;
    y += scaleBy * 3 / 4 - (curFrameHeight / 2);

    // Calculate axial coordinates
    double q = (Math.sqrt(3.0) / 3.0 * x - 1.0 / 3.0 * y) / hexSize;
    double r = (2.0 / 3.0 * y) / hexSize;

    return axialRound(q, r);
  }

  /**
   * Rounds the given fractional cube to the nearest axial with integer coordinates.
   * It rounds the fractional cube, then rounds it to a cube, then converts this cube to an axial.
   *
   * @param q the q coordinate of the fractional cube.
   * @param r the r coordinate of the fractional cube.
   * @return the hex that is involved with the given  axial coordinates.
   */
  private static Hex axialRound(double q, double r) {
    return cubeToAxial(cubeRound(new FractionalCube(q, r, -q - r)));
  }

  /**
   * <p>Rounds the given fractional cube to the nearest cube with integer coordinates.
   * It uses the method {@code cubeToAxial} to convert the cube coordinates to axial coordinates.
   * It uses the method {@code cubeRound} to round the cube coordinates to the nearest cube
   * with integer coordinates. This panel temporarily uses cube coordinates to help determine
   * the specific axial coordinate of a physical coordinate. Without the value of s, that is
   * generally included in cube coordinates and not axial coordinates, it is difficult to
   * have the mouse coordinate reference to the exact correct axial hex if the user were to press
   * on the lower half of the hexagon.</p>
   *
   * @param frac the fractional cube to be rounded.
   * @return a Cube object representing the cube with integer coordinates.
   */
  private static Cube cubeRound(FractionalCube frac) {
    int q = (int) Math.round(frac.q);
    int r = (int) Math.round(frac.r);
    int s = (int) Math.round(frac.s);

    double qDiff = Math.abs(q - frac.q);
    double rDiff = Math.abs(r - frac.r);
    double sDiff = Math.abs(s - frac.s);

    if (qDiff > rDiff && qDiff > sDiff) {
      q = -r - s;
    } else if (rDiff > sDiff) {
      r = -q - s;
    } else {
      s = -q - r;
    }

    return new Cube(q, r, s);
  }

  /**
   * Converts a cube to an axial.
   *
   * @param cube the cube to be converted.
   * @return a Hex object representing the axial coordinates of the given cube.
   */
  private static Hex cubeToAxial(Cube cube) {
    int q = cube.q;
    int r = cube.r;
    return new Hex(q, r);
  }

  /**
   * This private static class was included solely for the purpose of determining the right
   * axial coordinate of a physical mouse coordinate. Without the value of s, that is generally
   * included in cube coordinates and not axial coordinates, it is difficult to have the mouse
   * coordinate reference to the exact correct axial hex if the user were to press on the lower
   * half of the hexagon. This idea was inspired by the method used in the following link:
   * {<a href="https://www.redblobgames.com/grids/hexagons/#pixel-to-hex">...</a>}.
   */
  private static class Cube {
    private final int q;
    private final int r;
    private final int s;

    /**
     * <p>Constructs a cube with the given coordinates.</p>
     * <p>The following invariants apply to the parameters of this class: q + r + s must always
     * equal to 0, and all coordinates must be positive.</p>
     *
     * @param q the q coordinate of the cube.
     * @param r the r coordinate of the cube.
     * @param s the s coordinate of the cube.
     */
    protected Cube(int q, int r, int s) {
      this.q = q;
      this.r = r;
      this.s = s;
      if (q + r + s != 0) {
        throw new IllegalArgumentException("q + r + s must be 0");
      }
      if (q < 0 && r < 0 && s < 0) {
        throw new IllegalArgumentException("q, r, and s must be positive");
      }
    }

    @Override
    public String toString() {
      return "Cube(q=" + q + ", r=" + r + ", s=" + s + ")";
    }

  }

  /**
   * This private static class was included solely for the purpose of determining the right
   * axial coordinate of a physical mouse coordinate. Without the value of s, that is generally
   * included in cube coordinates and not axial coordinates, it is difficult to have the mouse
   * coordinate reference to the exact correct axial hex if the user were to press on the lower
   * half of the hexagon. It represents a fractional cube, which is an incomplete cube coordinate
   * that does include precise integers to determine its location. This idea was inspired by the
   * method used in the following link:
   * {<a href="https://www.redblobgames.com/grids/hexagons/#pixel-to-hex">...</a>}.
   */
  private static class FractionalCube {
    private final double q;
    private final double r;
    private final double s;

    /**
     * <p>Constructs a fractional cube with the given coordinates. An invariant of this class
     * is that no coordinates should be less than 0.</p>
     *
     * @param q the q coordinate of the fractional cube.
     * @param r the r coordinate of the fractional cube.
     * @param s the s coordinate of the fractional cube.
     */
    protected FractionalCube(double q, double r, double s) {
      this.q = q;
      this.r = r;
      this.s = s;

      if (q < 0 && r < 0 && s < 0) {
        throw new IllegalArgumentException("q, r, and s must be positive");
      }
    }
  }
  public static void main(String[] args) {

    int width = 1200;
    int height = 800;
    int noOfCells = 4;
    HashMap<Hex, PlayerDisc> hexagons = new HashMap<Hex, PlayerDisc>();
    int rowSize = noOfCells;
    int totalNumRows = 2 * noOfCells - 1;
    int center = noOfCells - 1;
    // build grid

    for (int rowNum = 0, rowNum2 = 0; rowNum < totalNumRows; rowNum++) {
      for (int col2 = 0, col = -rowNum2; col2 < rowSize; col++,col2++) {
        System.out.println("row "+(rowNum-center)+" col "+col);
        hexagons.put(new Hex(col, rowNum-center), PlayerDisc.EMPTY);
      }
      if ((rowNum-center) < 0) {
        rowNum2++;
      }
      if (rowNum < center) {
        rowSize++;
      } else {
        rowSize--;
      }
    }


    //Board board = new Board(noOfCells);
    Board board = new Board(hexagons);
    IROModel providerModel = new MutableModel(noOfCells, board, PlayerDisc.WHITE);
    JFrameView f = new JFrameView(providerModel);
    //BoardPanel p = new BoardPanel(providerModel, f);

    //f.setContentPane(p);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.pack();
    f.setLocationRelativeTo(null);
    f.setSize(width, height);
    f.setVisible(true);
  }
}


