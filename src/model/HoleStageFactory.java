package model;

import boardifier.model.ContainerElement;
import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

/**
 * HoleStageFactory must create the game elements that are defined in HoleStageModel
 * WARNING: it just creates the game element and NOT their look, which is done in HoleStageView.
 *
 * If there must be a precise position in the display for the look of a game element, then this element must be created
 * with that position in the virtual space and MUST NOT be placed in a container element. Indeed, for such
 * elements, the position in their virtual space will match the position on the display. For example, in the following,
 * the black pot is placed in 18,0. When displayed on screen, the top-left character of the black pot will be effectively
 * placed at column 18 and row 0.
 *
 * Otherwise, game elements must be put in a container and it will be the look of the container that will manage
 * the position of element looks on the display. For example, pawns are put in a ContainerElement. Thus, their virtual space is
 * in fact the virtual space of the container and their location in that space in managed by boardifier, depending of the
 * look of the container.
 *
 */
public class HoleStageFactory extends StageElementsFactory {
    private HoleStageModel stageModel;

    public HoleStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (HoleStageModel) gameStageModel;
    }

    @Override
    public void setup() {

        // create the text that displays the player name and put it in 0,0 in the virtual space
        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(0,0);
        stageModel.setPlayerName(text);

        // create the board, in 0,1 in the virtual space
        HoleBoard board = new HoleBoard(0, 1, stageModel);
        // assign the board to the game stage model
        stageModel.setBoard(board);

        //create the black pot in 18,0 in the virtual space
        HolePawnPot blackPot = new HolePawnPot(18,0, stageModel);
        // assign the black pot to the game stage model
        stageModel.setBlackPot(blackPot);
        //create the black pot in 25,0 in the virtual space
        HolePawnPot redPot = new HolePawnPot(25,0, stageModel);
        // assign the red pot to the game stage model
        stageModel.setRedPot(redPot);

        /* create the pawns
            NB: their coordinates are by default 0,0 but since they are put
            within the pots, their real coordinates will be computed by the view
         */
        Pawn[] blackPawns = new Pawn[4];
        for(int i=0;i<4;i++) {
            blackPawns[i] = new Pawn(i + 1, Pawn.PAWN_BLACK, stageModel);
        }
        // assign the black pawns to the game stage model
        stageModel.setBlackPawns(blackPawns);
        Pawn[] redPawns = new Pawn[4];
        for(int i=0;i<4;i++) {
            redPawns[i] = new Pawn(i + 1, Pawn.PAWN_RED, stageModel);
        }
        // assign the black pawns to the game stage model
        stageModel.setRedPawns(redPawns);

        // finally put the pawns to their pot
        for (int i=0;i<4;i++) {
            blackPot.addElement(blackPawns[i], i,0);
            redPot.addElement(redPawns[i], i,0);
        }

        /* Example with a main container that takes the ownership of the location
           of the element that are put within.
           If we put text, board, black/red pots within this container, their initial
           location in the virtual space is no more relevant.
           In such a case, we also need to create a look for the main container, see HoleStageView
           comment at the end of the class.

        // create the main container with 2 rows and 3 columns, in 0,0 in the virtual space
        ContainerElement mainContainer = new ContainerElement("rootcontainer",0,0,2,3, stageModel);
        // for cell 0,1, span over the row below => the column 1 goes from top to bottom of the container
        mainContainer.setCellSpan(0,1,2,1);
        // for cell 0,2, span over the row below => the column 2 goes from top to bottom of the container
        mainContainer.setCellSpan(0,2,2,1);
        // assign the
        stageModel.setMainContainer(mainContainer);
        // assign elements to main container cells
        mainContainer.addElement(text,0,0);
        mainContainer.addElement(board, 1,0);
        mainContainer.addElement(blackPot,0,1);
        mainContainer.addElement(redPot,0,2);
        */
    }
}
