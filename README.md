# CS3500-Reversi
Overview:
Invariants: 
- All three lists contain the same cells
- The horizontal rows 0 indexed from top
Reversi is a two-player game played on a regular grid of cells. Each player has a color—black or white—and
the game pieces are discs colored black on one side and white on the other. Game play begins with equal
numbers of both colors of discs arranged in the middle of the grid. In our game, our grid will be made up of
hexagons.

Player Black moves first. On each turn, a player may do one of two things:
They may pass, and let the other player move.
They may select a legal empty cell and play a disc in that cell.

A play is legal for player A if the disc being played is adjacent
1 (in at least one direction) to a straight line of the opponent player B’s discs,
at the far end of which is another player A disc. The result of a legal move is
that all player B’s discs in all directions that are sandwiched between two discs
of player A get flipped to player A. We say that player A has captured player B’s discs.

If a player has no legal moves, they are required to pass.
If both players pass in a row, the game ends.

Modeling game:
The board is represented by 3 2d arrays of cells representing the cells of the game ordered along all three axis of a hexagon.

Visualizing game:
You are not required in this assignment to create a GUI view of your game. Instead, you will start with a
simpler textual view.
<br/>


Textual view:<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*\_ _ _*   <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*\_ _ _ _*   <br/>
&nbsp;&nbsp;*\_ _ X O _ _* <br/>
*\_ _ O _ X _ _*<br/>
&nbsp;&nbsp;*\_ _ X O _ _*<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*\_ _ _ _*<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*\_ _ _*   <br/>
Player one Score: 3<br/>
Player two Score: 3<br/>
Player one turn (Black)!<br/>
<br/>
where <br/>
X - Player one with Black colored Disc<br/>
O - Player two with White colored Disc

<br/><br/>
Source details:
1. BasicReversi model implements the Reversi game.
   This model uses 2 players Player One and Player Two. It uses Hex coordinates based grid.
   It has horizontal, down left and down right rows.
   Each cell extends Location (x,y) coordinates and may have Player's state if it is used by player.
2. ReversiTextualController to take Player One moves from user input, play move and display the output.
3. ReversiTextualView to display the textual output.
4. Classes are implemented for future use:<br/>
   Player.java, BlackPlayer.java, WhitePlayer.java, HexGrid.java, Point.java
5. Functionalities need to be updated for future work such as down left or right rows to support those directional moves.

<br/><br/>
Rule details:
1. Reversi game is implemented with below rules to ensure the move is valid:<br/>
a. The cell to move in is empty.<br/>
b. The cell to move in is adjacent to the one of the opponent player's cells.<br/>
c. When moving in that direction there is a friendly player cell before empty cell/end of list.<br/>
2. Horizontal moves are implemented. It also flips opposite player's discs if move is valid as per the rules of the game.
3. Game pass turns are implemented. If both players pass their turns in a row then game will be over.


# Changes for part 2
Added:
1. isGameOver();<br/>
a. The interface did not expose a way to check if the game was over, so we added a method to do that.<br/>
2. getTileAt(hRow, hIndex);<br/>
a. The interface did not expose a way to see the state of a specific tile, so we added a
method to do that.<br/>
3. getGameBoard();<br/>
a. The only way to get the state of the game before was to parse the string from the toString method,
so we added a method to get the state of each cell.
4. ReadonlyReversiModel interface is created to only support views.
5. ReversiHexGridController is implemented to support GUI based mouse click game actions and views.
6. ReversiFrame interface is created to support GUI based view. BasicReversiView is implemented to support this interface.
7. Hexagon is created to build the Hexagon based grid.
8. Reversi and ReversiGUI Main classes allows one to play this Reversi game in textual and GUI views.
9. Make move mouse clicks, Pass Turn button clicks and Restart button clicks are implemented as part of Hexagonal Grid GUI based game.
10. Strategy pattern is implemented to simulate the computer Move for selected player.<br/>
    PromptUser, FirstAvailableOpening moves are implemented.
11. Player is implemented to run as per his strategical move.
12. If computer player do not have any valid moves left then computer will automatically pass turn.
13. Vertical down left and right moves are implemented.
14. Display of Winner and Tie messages are implemented.
15. Mouse move over motion is implemented to display the associated cell as highlighted.
16. Keyboard support is implemented to support make-move and pass-turn commends as part of Reversi GUI.


<br/><br/>
New constructor:
1. We did not have a way to set up scenarios to test the model in specific situations,
   so we added a new constructor that takes in a list of locations for white tiles and for black tiles
   as well as other information about the state of the game.<br/>
2. We also added a copy constructor to be able to make copies of a game in progress.<br/>
   a. This required also adding a copy constructor to the cell class.<br/>


# Changes for part 3
Added:
1. Keyboard support is implemented to support make-move, pass-turn, restart and quit commends as part of ReversiGUI.
2. Reversi main class is created to accept command line strategies from user to simulate the computer player strategy.<br/>
   If Strategy is unable to return valid move then it defaults to pass turn.
   For example:<br/>
   Reversi "Human" "Human"<br/>
   Reversi "Human" "FirstMove"<br/>
   Reversi "Human" "HighestScoring"<br/>
   Reversi "Human" "Combined"<br/>
   Reversi "Human" "MiniMax"<br/>
   Reversi "Human" "Console"<br/>
   where<br/>
   FirstMove - First Available Opening Strategy<br/>
   HighestScoring - Highest Scoring Strategy<br/>
   Combined - Combined Moved Strategy using Pass If Win, Corners and Highest Scoring Move<br/>
   MiniMax - Game tree search of depth 2 using minimax to choose the best move<br/>
   Console - Console based Moves Strategy<br/>
   Human - Human Mouse or Keys based Moves Strategy<br/>
3. Mouse event handling decoupled from controller, view now sends the controller the logical result of the click instead of exact location
4. Implemented 2 Controllers and 2 Views as Part3 requirements as part of Reversi main class method and kept Part 2 implementation as part of previous ReversiGUI main class method.
5. Your Turn and Refresh View events are implemented to enable Player 2 to simulate the moves as per implemented strategies.
6. Manifest file is now using cs3500.reversi.Reversi as main class.
7. Keyboard support is implemented to support below keys using Key Listener, Key Bindings and Key Release event<br/>
   Up, Down, Left and Right Arrow keys will allow user to move selected cell on the game board.<br/>
   P for Pass-Turn, R for Restart and Q for Quit will allow user to make actions on the game board.<br/>
8. Above Strategies are tested through test class.
9. Created mock for the model that records all method calls and their arguments in a transcript including
   method calls in copies of the model. This allows strategies to be tested by seeing which of the legal moves they check.

# Changes for part 4
Added:
1. Integrated Provider view for player2 based upon command line params.
2. Reversi main class is updated to accept command line strategies from user to simulate the computer player strategy.<br/>
   The 4th param as Player2StrategyProvider is added to determine the Home Team strategy vs the Provider Team strategy.
      For example:<br/>
      Reversi 4 "Human" "Human" "HomeTeam"<br/>
      Reversi 4 "Human" "FirstMove" "HomeTeam"<br/>
      Reversi 4 "Human" "HighestScoring" "HomeTeam"<br/>
      Reversi 4 "Human" "Combined" "HomeTeam"<br/>
      Reversi 4 "Human" "MiniMax" "HomeTeam"<br/>
      Reversi 4 "Human" "Console" "HomeTeam"<br/>
      Reversi 4 "Human" "Human" "ProviderTeam"<br/>
      Reversi 4 "Human" "Capture" "ProviderTeam"<br/>
      Reversi 4 "Human" "Avoid" "ProviderTeam"<br/>
      Reversi 4 "Human" "Corner" "ProviderTeam"<br/>
      Reversi 4 "Human" "MiniMax" "ProviderTeam"<br/>
      Reversi 4 "Human" "Combo" "ProviderTeam"<br/>
      where<br/>
      1st param = Number of cells = 4<br/>
      2nd param = Player1Strategy of HomeTeam<br/>
      Valid values from HomeTeam:<br/>
      FirstMove - First Available Opening Strategy<br/>
      HighestScoring - Highest Scoring Strategy<br/>
      Combined - Combined Moved Strategy using Pass If Win, Corners and Highest Scoring Move<br/>
      MiniMax - Game tree search of depth 2 using minimax to choose the best move<br/>
      Console - Console based Moves Strategy<br/>
      Human - Human Mouse or Keys based Moves Strategy<br/>
      3rd param = Player2Strategy of either HomeTeam or ProviderTeam<br/>
      Valid values from ProviderTeam:<br/>
      Capture - Capture Max Strategy<br/>
      Avoid = Avoid Next To Corner Strategy<br/>
      Corner - Go For Corner Strategy<br/>
      MiniMax - Mini Max to choose the best move<br/>
      Combo - Combined Move Strategy using MiniMax, Corner, Avoid and Capture<br/>
      Human - Human Mouse or Keys based Moves Strategy<br/>
      4th param = Player2StrategyProvider. Valid value is either HomeTeam or ProviderTeam.<br/>
