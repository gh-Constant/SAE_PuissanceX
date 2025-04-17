package boardifier.view;

import boardifier.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class GameStageView {

    /**
     * The assocaited game stage model
     * Obviously, the model must be instantiated BEFORE the game stage.
     */
    protected GameStageModel gameStageModel;

    /**
     * The name of the stage.
     * It MUST correspond to the name registered in the StageFactory. It will be used by the factory
     * to create an instance of the stage, by calling Model.startStage().
     */
    protected String name;
    /**
     * All looks composing the stage.
     */
    protected List<ElementLook> looks;

    /**
     * The width of the game view in space.
     * If width and height are set to -1, the scene will be resized to the boundaries of all elements in the stage.
     * Otherwise, the scene will have the given dimension, clipping what is outside its boundaries.
     *
     * Setting width and height to a particular value should be done if some computations on the element position in space
     * involve to know the boundaries of this space. For example, if a sprite can only follow paths that are defined by a
     * regular mesh within the space (like in pacman), the characteristics of that mesh are determined from width and height.
     * These dimensions can also be used to detect if a sprite comes in contact with the boundaries of the space.
     */
    protected int width;
    /**
     * The height of the stage in space.
     * If width and height are set to -1, the scene will be resized to the boundaries of all elements in the stage.
     * Otherwise, the scene will have the given dimension, clipping what is outside its boundaries.
     */
    protected int height;
    private char[][] viewport; // a buffer of char that is used to store the visual aspect of the stage before begin printed o screen

    public GameStageView(String name, GameStageModel gameStageModel) {
        this.name = name;
        this.gameStageModel = gameStageModel;
        this.looks = new ArrayList<>();
                /* because of the StageFactory, there is a single constructor for stage that
        does not allow to set the width and height. Thus, they must be set after the main
        constructor has been called.
         */
        width = -1; // the scene will auto-resize to the boundaries of all elements.
        height = -1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<ElementLook> getLooks() {
        return looks;
    }

    public ElementLook getElementLook(GameElement element) {
        if (element == null) return null;
        for (ElementLook look : looks) {
            if (look.getElement() == element) return look;
        }
        return null;
    }

    public void addLook(ElementLook look) {

        looks.add(look);
        look.render();
    }

    public abstract void createLooks() throws GameException;
}
