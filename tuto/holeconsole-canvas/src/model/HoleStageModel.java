package model;

import boardifier.model.*;

/**
 * HoleStageModel defines the model for the single stage in "The Hole". Indeed,
 * there are no levels in this game: a party starts and when it's done, the game is also done.
 *
 * HoleStageModel must define all that is needed to manage a party : state variables and game elements.
 * In the present case, there are only 2 state variables that represent the number of pawns to play by each player.
 * It is used to detect the end of the party.
 * For game elements, it depends on what is chosen as a final UI design. For that demo, there are 12 elements used
 * to represent the state : the main board, 2 pots, 8 pawns, and a text for current player.
 *
 * WARNING ! HoleStageModel DOES NOT create itself the game elements because it would prevent the possibility to mock
 * game element classes for unit testing purposes. This is why HoleStageModel just defines the game elements and the methods
 * to set this elements.
 * The instanciation of the elements is done by the HoleStageFactory, which uses the provided setters.
 *
 * HoleStageModel must also contain methods to check/modify the game state when given events occur. This is the role of
 * setupCallbacks() method that defines a callback function that must be called when a pawn is put in a container.
 * This is done by calling onPutInContainer() method, with the callback function as a parameter. After that call, boardifier
 * will be able to call the callback function automatically when a pawn is put in a container.
 * NB1: callback functions MUST BE defined with a lambda expression (i.e. an arrow function).
 * NB2:  there are other methods to defines callbacks for other events (see onXXX methods in GameStageModel)
 * In "The Hole", everytime a pawn is put in the main board, we have to check if the party is ended and in this case, who is the winner.
 * This is the role of computePartyResult(), which is called by the callback function if there is no more pawn to play.
 *
 */
public class HoleStageModel extends GameStageModel {

    // define stage state variables
    private int blackPawnsToPlay;
    private int redPawnsToPlay;

    // define stage game elements
    private HoleBoard board;
    private HolePawnPot blackPot;
    private HolePawnPot redPot;
    private Pawn[] blackPawns;
    private Pawn[] redPawns;
    private TextElement playerName;
    // Uncomment next line if the example with a main container is used. see end of HoleStageFactory and HoleStageView
    //private ContainerElement mainContainer;

    public HoleStageModel(String name, Model model) {
        super(name, model);
        blackPawnsToPlay = 4;
        redPawnsToPlay = 4;
        setupCallbacks();
    }

    public HoleBoard getBoard() {
        return board;
    }

    public HolePawnPot getBlackPot() {
        return blackPot;
    }

    public HolePawnPot getRedPot() {
        return redPot;
    }

    public Pawn[] getBlackPawns() {
        return blackPawns;
    }

    public Pawn[] getRedPawns() {
        return redPawns;
    }

    public TextElement getPlayerName() {
        return playerName;
    }
    /*
    TO FULFILL:
        - create setters for all attributes. NB: in setters, do not forget to add elements to the stage (see addGrid() & addElement())
     */

    private void setupCallbacks() {

    /*
    TO FULFILL:
        - setup the onPutInGrid callback in order to decrease the number of pawns to play, and eventually to get the party result
     */
    }

    private void computePartyResult() {
        int idWinner = -1;
        // get the empty cell, which should be in 2D at [0,0], [0,2], [1,1], [2,0] or [2,2]
        // i.e. or in 1D at index 0, 2, 4, 6 or 8
        int i = 0;
        int nbBlack = 0;
        int nbRed = 0;
        int countBlack = 0;
        int countRed = 0;
        Pawn p = null;
        int row, col;
        for (i = 0; i < 9; i+=2) {
            if (board.isEmptyAt(i / 3, i % 3)) break;
        }
        // get the 4 adjacent cells (if they exist) starting by the upper one
        row = (i / 3) - 1;
        col = i % 3;
        for (int j = 0; j < 4; j++) {
            // skip invalid cells
            if ((row >= 0) && (row <= 2) && (col >= 0) && (col <= 2)) {
                p = (Pawn) (board.getElement(row, col));
                if (p.getColor() == Pawn.PAWN_BLACK) {
                    nbBlack++;
                    countBlack += p.getNumber();
                } else {
                    nbRed++;
                    countRed += p.getNumber();
                }
            }
            // change row & col to set them at the correct value for the next iteration
            if ((j==0) || (j==2)) {
                row++;
                col--;
            }
            else if (j==1) {
                col += 2;
            }
        }

        // decide whose winning
        if (nbBlack < nbRed) {
            idWinner = 0;
        }
        else if (nbBlack > nbRed) {
            idWinner = 1;
        }
        else {
            if (countBlack < countRed) {
                idWinner = 0;
            }
            else if (countBlack > countRed) {
                idWinner = 1;
            }
        }
        System.out.println("nb black: "+nbBlack+", nb red: "+nbRed+", count black: "+countBlack+", count red: "+countRed+", winner is player "+idWinner);
        // set the winner
        model.setIdWinner(idWinner);
        // stop de the game
        model.stopStage();
    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new HoleStageFactory(this);
    }
}
