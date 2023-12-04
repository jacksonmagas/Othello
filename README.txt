README:

Welcome to our cool game of ⬡cs3500.reversi.Reversi⬡!
 __
/R \
\__/ 
/E \
\__/
/V \
\__/
/E \
\__/
/R \
\__/
/S \
\__/
/I \
\__/


Overview

This codebase is intended to implement a Hexagonal cs3500.reversi.Reversi game, allowing players to play the game,
make moves, and determine the winner. It solves the problem of implementing the game logic for a
Hexagonal cs3500.reversi.Reversi, which is different from traditional Square cs3500.reversi.Reversi due to the hexagonal board
structure. This codebase also solves the issue of creating a user interface for players to
interact with the game, including creating a textual rendering of the game board, score,
and player turn management and handling player input such as placing a disc or passing their turn.
Here is an example of the textual rendering of the 3-ringed board:
  _ _ _
 _ X O _
_ O _ X _
 _ X O _
  _ _ _

Some high-level assumptions are that users are assumed to be familiar with the rules of Hexagonal
cs3500.reversi.Reversi, as the codebase may not explicitly teach or explain the rules; users are expected to
assign proper player/color discs themselves and maintain that player color, meaning that
there is nothing stopping the user from being both player 1 and 2 or have the players switch
colors after the game had started; users have a good understanding of how a pointy-side up
hexagonal grid/axis coordinates work; assume that users have Java Development Kit (JDK)
installed on their system and have a suitable IDE (Integrated Development Environment) for
Java development.

**CHANGES TO PART 2**
Some more high-level assumptions are that users are assumed to be aware of the characters Kirby
(pink) and Meta Knight (blue) from the Kirby video game series. Users are also assumed to be aware
that Kirby starts first (as they represent player black) and that Meta Knight starts second (as
they represent player white).


Quick Start

In order to get started using this codebase, a user will have to know:
1. How to create new RulesKeeper objects
   1. The user has 3 options to create a new RulesKeeper. They may type:
      1. new RulesKeeper();  → creates a RulesKeeper game model with a radius of 3 (starting at 0,
       meaning that the board has 3 outer rings)
      2. new RulesKeeper(int radius); → creates a RulesKeeper game model with a custom radius.
      If the user were to use this constructor “int radius” should be replaced with any integer
      that represents the radius/size of the game they want to play
      3. new RulesKeeper(Board board); → creates a RulesKeeper game model with a custom board.
      If the user were to use this constructor “Board board” should be replaced with a new Board
      object (which we will explain how to create next)
2. How to create new Board objects
   1. new Board(int radius); → creates a Board, representing the playable space of a cs3500.reversi.Reversi game.
   When using this constructor the user should replace “int radius” with an integer appropriate
   for the size of the game model (refer to the relevant RulesKeeper object).
3. How to create a new ReversiTextualView
   1. new ReversiTextualView(Board board, IROModel model, Appendable out); → creates a View that
   is easy to understand by the player while also containing all necessary and relevant information
   for the player. When using this constructor the user should replace “Board board” with the
   desired board object, “IROModel model” with the desired model, and “Appendable out” with
   System.out or something equivalent.
4. How to create a Hex
   1. new Hex(int q, int r); → creates a Hex coordinate, which is what the game board and legal
   move system is based off of. “int q” and “int r” should be replaced with the relevant
   coordinates desired by the user.
5. How to start the game
   1. rulesKeeper.startGame(); → This method officially starts the game of cs3500.reversi.Reversi.
   If the user does not call this method onto the desired RulesKeeper object, the game
   will be unplayable and players will not be able to make moves.
6. How to complete your turn
   1. rulesKeeper.makeMove(Hex coord, PlayerDisc who); → This method allows the player to
   make a move or forces the player to skip if there are no valid moves for them. If the user
   does not call this method then the game will not progress, unless they choose to call
   skipTurn() instead. This method takes in parameters “Hex coord”, which should be replaced
   with the coordinate position that the user wants to put their disc on, and “PlayerDisc who”,
   which should be replaced with the respective PlayerDisc enum.
7. How to skip your turn
   1. rulesKeeper.skipTurn(); → This method allows the player to not place any discs and instead
   skip their turn and force the other player to make their move.
8. How to render the game
   1. reversiTextualView.render(); → This method renders and prints out an easy-to-understand
   version of the given reversi game.

** CHANGES TO PART 2 **
9. How to render the GUI
    1. the GUI (Graphical User Interface) can be rendered by doing the following:
        1. create a new ReversiGUIView object
        2. call the .render() method on the ReversiGUIView object inside the main method
        3. the GUI will then be rendered and the user can play the game through the GUI
10. How to use the GUI
    1. the user can use the GUI by doing the following:
        1. click on the cell they want to place their disc on. this cell can be deselected by clicking
           on it again, by clicking another cell, or by clicking off of the playable board.
        3. if the user wants to skip their turn, they can press the 'S' key on their keyboard.
        4. if the user wants to quit the game, they can press on the 'Q' key on their keyboard.
        5. if the user wants to make their move, they can press the 'Enter' key on their keyboard
           (assuming that they have selected a cell to place their disc on).
11. How to call a strategy
    1. the user can call a strategy by doing the following:
        1. create a new Strategy object (this can be new CaptureMax(), new AvoidNextToCorner(),
           new GoForCornerStrat(), or new MiniMax())
        2. call the .chooseMove() method on the Strategy object
        3. the strategy will then return a Hex object that represents the best move for that
           strategy
12. How to chain strategies
    1. the user can chain strategies by doing the following:
        1. create a new TryN object
        2. call the .addStrategy() method on the TryN object to add one "chain" to a list of
           strategies (you may add as many as you like here. the order you add them in will be
           the order they are executed in)
        3. call the .chooseMove() method on the TryN object
        4. the TryN object will then return an Optional<Hex> object that represents the best move
           for that chain of strategies

Key Components

Driving Components
* The interface IMutableModel extends IROModel (which I will elaborate on next) and represents
the cs3500.reversi.Reversi game model that is able to mutate and change due to the player’s/user’s interactions
with the game. Any specific moves that control the state of the game (such as capturing a disc,
checking if a move is legal, getting the neighbors of a cell that a player hopes to play a disc on)
are implemented in the classes that implement this interface to allow more flexibility of
different versions of the game and to avoid unnecessary coupling or mutations.

Driven Components
* The interface IROModel represents a read-only version of the cs3500.reversi.Reversi game model.
It provides information for querying the game state without allowing modifications to
the game board (i.e., players can observe the state of the cs3500.reversi.Reversi game through this
interface without altering the ongoing gameplay).
* The interface IView represents an easy-to-understand textual-view of the cs3500.reversi.Reversi game
that is to be printed so that players may have access to what the game board looks like,
the score, and who’s turn it is. This interface does not have the ability to mutate the model
or game state itself.

** CHANGES TO PART 2 **

Driving Components:
* The interface FallibleReversiStrat represents a strategy for choosing a move in Reversi that
  may fail to find a move. This interface is also the opposite of InfallibleReversiStrat.
* The interface InfallibleReversiStrat represents a strategy for choosing a move in Reversi that
    is guaranteed to find a move. This interface is also the opposite of FallibleReversiStrat. This
    means that if the strategy is not able to find a Hex coord to place a disc on, the player will
    skip its turn.

Driven Components:
* The interface IFeatures represents the features that the GUI will have. This interface is
  implemented by the ReversiGUIView class. Different features include the different key buttons
  that the user can press to interact with the game (such as the 'S' key to skip a turn or the
  'Q' key to quit the game)
* IGraphicalReversiView represents a graphical view of our Reversi Game. This interface extends the
  IView interface. This interface is implemented by the JFrameView class. This interface is used
  to render the GUI and to allow the user to interact with the game through the GUI.
* IReversiPanel represents the panel that will be used to render the GUI. This interface is
  implemented by the ReversiPanel class. This interface is used to render the GUI and to allow
  the user to interact with the game through the GUI.


Key Subcomponents

Hex is an object that represents an axial coordinate on a hexagonal grid using two axes (q and r)
to represent positions within a hexagonal grid. Axial coordinates were used for our implementation
of the cs3500.reversi.Reversi model as they are more intuitive to use than cube coordinates (i.e., they provide
the same amount of information with less data and axial coords more closely resemble the
row x column representation of a Cartesian coordinate system, which is generally more widely
used and familiarized).

RulesKeeper represents the rules of a game of cs3500.reversi.Reversi. It keeps track of the status of the game,
the score, the state of the board, and the next player to go. This class also handles checking
the legality of moves (whether they are valid or invalid, along with if the player has any valid
move to make at all).

Board consists of a HashMap that stores the Hex as the Key and the PlayerDisc as the Value.
The Board also has a radius that determines the size of the Board, which is measured using
the distance between the center points of each individual Hex. For instance, for a Board to
have a radius of 1 would mean that there is only one more outer layer of Hexagons surrounding
the center. This means that our representation of the radius uses a 1-based index rather than
a 0-based index.

Hexagons are the cells of the board which are represented as a Hashmap. Hashmaps were used to make
the process of storing and accessing discs more convenient. Hashmaps also ensure that a certain
key Hex does not have more than one value (i.e., the PlayerDisc). However, Hashmaps  do not make
the process of retrieving neighboring cells of a given hex as convenient. Retrieving neighboring
cells is a key action in our implementation of the cs3500.reversi.Reversi model. To compensate for this
potential drawback, we decided to create helper methods that utilize a List of Hexes to represent
and retrieve neighbors of a certain cell. Hexes in this list are then used as keys to retrieve
values in our Hashmap representation of the grid.

ReversiTextualView represents a textual view of the cs3500.reversi.Reversi game and is formatted in a manner
that provides all relevant and necessary information in an intuitive/easy-to-understand way.
This view is used to display the game state in a printed format, and it is what the player will
see while they are playing the game. This IView takes in an IROModel to ensure that the View does
 not have access to mutating the model.

PlayerDisc is an enumeration that represents the different states a disc in our game of cs3500.reversi.Reversi
can be in. A disc can be either Black, White, or Empty. A potential drawback of our design choice
with representing players is utilizing the PlayerDisc enum. While we use this enum to represent
the state of a disc, we also use this enum inside the model class(es) to represent the different
player's turn. Instead of creating a separate enum that solely represents the player's turn,
we decided to use our original enum as the former approach could create redundant code.
Sticking to the latter approach can create unwanted situations where the player's turn can be
{@code PlayerDisc.EMPTY}. To prevent such unwanted scenarios, we ensure that
{@code this.nextPlayerDisc} is never {@code PlayerDisc.EMPTY} in the constructor and that
this state is preserved throughout all the methods that attempt to mutate
{@code this.nextPlayerDisc}. Any methods that require knowing the next player's turn but don't
need to change it use the {@code getNextPlayerDisc} to prevent unwanted mutation.

Status is an enumeration that represents the different states a game of cs3500.reversi.Reversi can be in.
The game’s Status can be ongoing, or it can be over with a winner/loser or without a
winner/loser (Stalemate).

** CHANGES TO PART 2 **

CaptureMax is a class that implements the first of the computer AI strategies. It allows the computer
to determine which (valid) move would allow them to claim the most amount of discs. This strategy
is the simplest of the four strategies, and it is the least sophisticated.

AvoidNextToCornerStrat is a class that implements the second of the computer AI strategies. It allows
the computer to determine if a valid move places a disc at a cell adjacent to the corner of the,
allowing the opponent to potentially capture the corner. This strategy is the second most simple of
the four strategies.

GoForCorner is a class that implements the third of the computer AI strategies. It allows
the computer to determine if a valid move places a disc at a cell that allows the opponent to
capture the corner. This strategy is the second most sophisticated of the four strategies, as
capturing a corner is a very important move in Reversi since it is the only way to guarantee that
a disc will not be captured.

CornerInfo is a class that represents the information of a corner. This class is a function-object
class that is used to store the information of the 6 corners of the reversi board. This class was
created due to the fact that two of our strategies heavily rely on the information of the corners
of the board.

MiniMax is a class that implements the fourth of the computer AI strategies. It allows the computer
to determine which (valid) move would prevent their opponent's best move according to the strategy
they are using. This strategy is the most sophisticated of the four strategies, as it is able to
predict the opponent's next move and prevent it from happening.

CompleteStratFromFallible is a class that implements the FallibleReversiStrat interface. This class
is used to create a strategy that is guaranteed to find a move. This is the only class that is
guaranteed to allow the player to make a move.

TryN is a class that implements the FallibleReversiStrat interface. This class is used to chain
multiple strategies together. This class is able to chain any number of strategies together, and
it will return the best move according to the first strategy that is able to find a move. This class
is used to chain strategies together in order to create a more sophisticated strategy.

PlayerMoves is an enumeration that represents the different states a player can be in. A player can
either have a MAKE_MOVE (which represents the event in which they have a valid disc to place) or a
SKIP_TURN (which represents the event in which they do not have a valid disc to place and are
forced to skip their turn).


Source Organization

We have two main directories for our codebase: src and test. Src contains all of the implementation
for the model and view (and in the future, controller and player) while the test directory stores
all the test methods we created and used to ensure the validity of our code and to check for
bugs.

Under our src directory, we will store our README.txt file, which you are currently reading!
Additionally, within src we also have the cs3500.reversi package, which stores the packages
controller, model, view, and player. Our controller package is currently empty as we have yet
to implement that mechanic into our game. Our player package only has a PlayerDesign.txt file,
which holds the information on how we are planning on designing and creating our Player interface
and relevant subclasses.

Within the cs3500.reversi.model package, we have our interfaces IROModel and IMutableModel;
classes Board, Hex, and Ruleskeeper; and enumerations PlayerDisc and Status.

Within the cs3500.reversi.view package, we have the IView interface and the ReversiTextualView
class.

** CHANGES TO PART 2 **

Within the cs3500.reversi.player package, we have the interfaces FallibleReversiStrat, InfallibleReversiStrat,
along with the classes CaptureMax, AvoidNextToCornerStrat, GoForCornerStrat, MiniMax,
CompleteStratFromFallible, TryN, PlayerMoves, and CornerInfo.

Within the cs3500.reversi.view package, we have added the interfaces IGraphicalReversiView and
IReversiPanel, along with the classes JFrameView and BoardPanel.

Within the cs3500.reversi.controller package, we have added the interface IFeatures, along with the
class Controller.

*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
Changes for part 2
1. Our original getBoard() method (which can be found in the RulesKeeper class and IROModel
   interface) returned the board as is, rather than as a copy. In order to prevent unwanted
   mutations, we changed the method to return a copy of the board instead.
        a. this lead us to create a new constructor for our Board class that takes in an existing
           board and creates a copy of it.
2. We had a private method called numOfRemainingMoves() within our RulesKeeper class that
   returned the number of valid moves a player (who's turn it currently is) have. We decided to
   expose this method to our IROModel interface (as it acts as a getter method and is important
   information for the player/user) and made it public within the RulesKeeper class.
        a. We also changed the name of this method to numCurrentValidMoves() as we thought it more
           accurately described the function of the method.
3. We added a method to the IROModel interface called getDiscAt(int coordQ, coordR) that returns
   the PlayerDisc at the given coordinate. This method is important for the player to know what
   the state of a certain cell is, so we decided to expose this method to the interface.
        a. we chose to give it primitive integer parameters to prevent coupling with the Hex class
           and to make it easier for the player to use.
4. We changed our getHexagons() to return a copy of the HashMap<Hexagons, PlayerDisc> rather than
   the HashMap itself. This was done to prevent unwanted mutations to the board.
        a. Because of this change, we also made our field hexagons final, as we recognized that we
           are never directly mutating it.
5. We added a field to RulesKeeper that is a Hex called latestMove. This field is used to store
   the latest move that was made by the player. This field is used to help the MiniMax AI strategy
   determine the opponents current strategy.
        a. In addition to this method, we have also created a public getter method called getLatestMove()
           that returns the latest move that was made by the player.
6. We changed our getInBoundNeighbors() and getAllValidPaths() methods so that they are public and
   exposed to the IROModel interface.
        a. Information regarding the neighbors of a certain hex was made public as it contains
           important information about the model: one core verb of our model is that the model ought
           to know who its neighbors are. By publicizing this information, this method can
           find relevant info about a hex’s neighbors that can help in planning strategic game plays.


EXTRA CREDIT:
    - For our assignment, we decided to attempt all the extra credit options. We implemented the remaining
      three computer AI strategies AvoidNextToCorner (strategy 2 on the assignment spec.), GoForCornerStrat
      (strategy 3 on the assignment spec), and MiniMax (strategy 4 on the assignment spec). These classes
      and their implementations can be found under the Player directory and within their respective classes.
    - We also created a classes called TryN, which allows us to chain any number of strategies
      together. This class can be found under the Player directory as well.


NAVIGATING OUR JAR FILE & COMMAND LINES:
Once the user navigated their way to the directory that contains our jar file and has typed
"java -jar Reversi.jar", the user has the option to type three kinds of argument or nothing at all.

When the user provides no command line arguments, the game will start with a default radius of 3 and
two human players.

When the user provides 3 command line arguments, the first argument will be an integer that represents
the size of the board. By size of the board, we mean the radius of the board (how many rings/layers far
from the origin). For instance, a radius of three will create board that has 3 outer layers from the origin.
The second argument will be a string that represents the first type of player. This can be one of: human,
capture, avoid, corner, minimax, or combo. The third argument must be another string that represents
the second type of player. We generally recommend a radius from size 3-6 and two humans players, or
1 human player and 1 computer player.

*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
Changes for part 3
1. Previously we had commented out the code associated with the controller in our
BoardPanel class. We have now uncommented this code and have added a new Controller class
 that implements the {@code IFeatures} interface. We also added two types of
Features interfaces: one for the Views and another for the Model. The model features interface
allows the model to notify the controller of whose turn it is. The view features interface allows
the view to notify the high-level events that have occurred as a result of the user's low-level
events. The View and Model only interacts with the Features interfaces and never with the
concrete Controller class.
2. We have also fixed our BoardPanel class so that it allows for resizing of the board and adjusts
the composition of all the texts and components accordingly. This meant that we had to edit
the constructor of the BoardPanel and include a JFrame in it. We added this JFrame because
we needed information from the view. If the user were to manually expand the window size, we would
need the new JFrame size, so we retrieved this information from the newly added JFrame field.
3. We added several mock classes (one for the view, another for the model, and two more for the
player classes) because we wanted to make sure that the controller was calling the right methods
from the right classes.
4. Aside from our main Reversi class, we also added a ReversiCreator class that instantiated the
appropriate game version based on the command lines arguments. This class can create 6 different
types of players: human, capture-max ai, avoid-next-to-corner ai, go-for-corner ai, minimax ai,
and a combo ai that chains all the previous four ai's.
5. We also changed our view so that instead of stating the score in the title (which is already
displayed in the panel), we display whose view the window is playing for. If someone were to win,
this would also be displayed in the title.