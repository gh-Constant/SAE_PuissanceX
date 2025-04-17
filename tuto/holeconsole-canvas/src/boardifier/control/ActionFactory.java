package boardifier.control;

import boardifier.model.ContainerElement;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.*;

/**
 * this class provides static methods to ease the creation of action lists.
 * Since boardifierconsole operates in text mode, these methods have parameters that do not allow to create
 * an animation. Thus, the content of this file is different from that of boardifier.
 */
public class ActionFactory {

    public static ActionList generatePutInContainer(Model model, GameElement element, String nameContainerDest, int rowDest, int colDest) {
        ContainerElement containerSrc = element.getContainer();
        ContainerElement containerDest = model.getContainer(nameContainerDest);

        ActionList list = new ActionList();

        // if the element is yet in a container, first remove from it
        if (containerSrc != null) {
            GameAction remove = new RemoveFromContainerAction(model, element);
            list.addSingleAction(remove);
        }
        // create the put in container action,if possible
        if (containerDest != null) {
            // generate the action + animation
            GameAction put = new PutInContainerAction(model, element, nameContainerDest, rowDest, colDest);
            list.addSingleAction(put);
        }

        return list;
    }

    public static ActionList generateMoveWithinContainer(Model model, GameElement element, int rowDest, int colDest) {
        ContainerElement containerSrc = element.getContainer();
        ActionList list = new ActionList();

        // create the move within container action,if possible
        if (containerSrc != null) {
            GameAction put = new MoveWithinContainerAction(model, element, rowDest, colDest);
            list.addSingleAction(put);
        }
        return list;
    }

    public static ActionList generateRemoveFromContainer(Model model, GameElement element) {
        ContainerElement containerSrc = element.getContainer();
        ActionList list = new ActionList();
        // if the element is yet in a container, first remove from it
        if (containerSrc != null) {
            GameAction remove = new RemoveFromContainerAction(model, element);
            list.addSingleAction(remove);
        }
        return list;
    }

    public static ActionList generateRemoveFromStage(Model model, GameElement element) {

        ActionList list = new ActionList();
        // if the element is yet in a container, first remove from it
        GameAction remove = new RemoveFromStageAction(model, element);
        list.addSingleAction(remove);
        return list;
    }

    public static ActionList generateDrawDice(Model model, GameElement element) {

        ActionList list = new ActionList();
        // if the element is yet in a container, first remove from it
        GameAction draw = new DrawDiceAction(model, element);
        list.addSingleAction(draw);
        return list;
    }
}