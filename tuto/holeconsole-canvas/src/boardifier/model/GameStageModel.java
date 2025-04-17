package boardifier.model;

import java.util.ArrayList;
import java.util.List;

public abstract class GameStageModel {
    
    /**
     * The local state of the stage.
     * This state is different from the global state that is defined in model.
     */
    protected int state; // the local state of the stage.
    /**
     * The parent model.
     * Obviously, the model must be instantiated BEFORE the game stage.
     */
    protected Model model;

    /**
     * The name of the stage.
     * It MUST correspond to the name registered in the StageFactory. It will be used by the factory
     * to create an instance of the stage, by calling Model.startStage().
     */
    protected String name;
    /**
     * The containers of the stage.
     * Theses are mostly needed in board games. For games with sprites, it is most often useless
     * to define containers.
     */
    protected List<ContainerElement> containers;
    /**
     * All elements composing the stage.
     * It includes the containers.
     */
    protected List<GameElement> elements;
    /**
     * List of elements that are currently selected.
     */
    protected List<GameElement> selected;

    SelectionCallback onSelectionChangeCallback;
    ContainerOpCallback onPutInContainerCallback;
    ContainerOpCallback onMoveInContainerCallback;
    ContainerOpCallback onRemoveFromContainerCallback;

    public GameStageModel(String name, Model model) {
        this.name = name;
        this.model = model;
        containers = new ArrayList<>();
        elements = new ArrayList<>();
        selected = new ArrayList<>();
        /* WARNING :
           In order to fulfil some initializations, createElements() is not called
           automatically at the end of the constructor. Thus the dev. has to do itself
           explicitly.
         */
        // define NOP callbacks
        onSelectionChangeCallback = () -> {};
        onPutInContainerCallback = (element, containerDest, rowDest, colDest) -> {};
        onMoveInContainerCallback = (element, containerDest, rowDest, colDest) -> {};
        onRemoveFromContainerCallback = (element, containerDest, rowDest, colDest) -> {};
    }

    public String getName() {
        return name;
    }

    public Model getModel() {
        return model;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void reset() {
        containers.clear();
        elements.clear();
        selected.clear();
    }

    public List<GameElement> getElements() { return elements; }

    public void addElement(GameElement element) {
        elements.add(element);
    }

    public boolean isElementInStage(GameElement element) {
        if (elements.contains(element)) return true;
        return false;
    }

    public List<ContainerElement> getContainers() {
        return containers;
    }

    public ContainerElement getContainer(String name) {
        for(ContainerElement container : containers) {
            if (container.name.equals(name)) return container;
        }
        return null;
    }
    public void addContainer(ContainerElement container) {
        containers.add(container);
        elements.add(container);
    }

    public List<GameElement> getSelected() {
        return selected;
    }

    // method called by GameElement when they are selected to keep track of all selected elements (if needed)
    public void setSelected(GameElement element, boolean selected) {
        if (!selected) {
            this.selected.remove(element);
        }
        else {
            this.selected.add(element);
        }
        onSelectionChangeCallback.execute();
    }

    public void unselectAll() {
        /* unselect the element by calling their own unselect() method
           so, it must be done backwards because it will call the above
           method setSelected(), that will remove the element from the set selected.

         */
        for(int i=selected.size()-1;i>=0;i--) {
            GameElement element = selected.get(i);
            element.unselect();
        }
        selected.clear();
        onSelectionChangeCallback.execute();
    }


    /**
     * Create the elements of this stage thanks to a StageElementFactory
     * This method is just a wrapper to call the setup() method of a StageElementsFactory.
     * This method MUST NOT BE called directly. It is done by the controller when a stage must
     * be set.
     * @param elementsFactory
     */
    public void createElements(StageElementsFactory elementsFactory) {
        elementsFactory.setup();
    }

    public abstract StageElementsFactory getDefaultElementFactory();

    /* **********************************************************
       common callbacks methods, used for common types of action
       WARNING : these callbakcs are called AFTER the associated action
       has been played
    ************************************************************ */
    public void onSelectionChange(SelectionCallback callback) {
        onSelectionChangeCallback = callback;
    }
    public void onPutInContainer(ContainerOpCallback callback) {
        onPutInContainerCallback = callback;
    }
    public void onMoveInContainer(ContainerOpCallback callback) {
        onMoveInContainerCallback = callback;
    }
    public void onRemoveFromContainer(ContainerOpCallback callback) {
        onRemoveFromContainerCallback = callback;
    }

    public void putInContainer(GameElement element, ContainerElement containerDest, int rowDest, int colDest) {
        onPutInContainerCallback.execute(element, containerDest, rowDest, colDest);
    }
    public void movedInContainer(GameElement element, ContainerElement containerDest, int rowDest, int colDest) {
        onMoveInContainerCallback.execute(element, containerDest, rowDest, colDest);
    }
    public void removedFromContainer(GameElement element, ContainerElement container, int row, int col) {
        onRemoveFromContainerCallback.execute(element, container, row, col);
    }

    // by default removing = hide the element and move it outside the current scope of the view
    public void removeElement(GameElement element) {
        element.setLocation(-10000,-10000);
        element.setVisible(false);
        if (element.getContainer() != null) {
            element.getContainer().removeElement(element);
        }
    }

    /* ********************************
       Helpers methods
    ******************************** */

    public String getCurrentPlayerName() {
        return model.getCurrentPlayerName();
    }

    // get the container element (if it exists) where is assigned another element
    public ContainerElement elementContainer(GameElement element) {
        for(ContainerElement container : containers) {
            if (container.contains(element)) return container;
        }
        return null;
    }

    public List<GameElement> elementsByType(int type) {
        List<GameElement> list = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            GameElement element = elements.get(i);
            if (element.type == type) {
                //System.out.println("found");
                list.add(element);
            }
        }
        return list;
    }

}
