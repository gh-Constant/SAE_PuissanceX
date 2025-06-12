package boardifier.control;

import boardifier.model.ContainerElement;
import boardifier.model.Coord2D;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.*;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.ContainerLook;
import boardifier.view.ElementLook;

public class ActionFactory {

    public static ActionList generatePutInContainer(Controller control, Model model, GameElement element, String nameContainerDest, int rowDest, int colDest) {
        return generatePutInContainer(control, model, element, nameContainerDest,rowDest, colDest, AnimationTypes.NONE, 0);
    }

    public static ActionList generatePutInContainer(Controller control, Model model, GameElement element, String nameContainerDest, int rowDest, int colDest, String animationName, double factor) {
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
            if (AnimationTypes.NONE.equals(animationName)) {
                GameAction put = new PutInContainerAction(model, element, nameContainerDest, rowDest, colDest);
                list.addSingleAction(put);
            }
            else {
                // get the look of the element
                ElementLook elementLook = control.getElementLook(element);
                // get the container look of the destination container
                ContainerLook containerLook = (ContainerLook) control.getElementLook(containerDest);
                // get the location of the element look within this destination look, if placed in rowDest, colDest
                Coord2D center = containerLook.getRootPaneLocationForLookFromCell(elementLook, rowDest, colDest);
                // generate the action + animation
                GameAction put = new PutInContainerAction(model, element, nameContainerDest, rowDest, colDest, animationName, center.getX(), center.getY(), factor);
                list.addSingleAction(put);
            }
        }
        return list;
    }

    public static ActionList generateMoveWithinContainer(Controller control, Model model, GameElement element, int rowDest, int colDest) {
        return generateMoveWithinContainer(control, model, element, rowDest, colDest, AnimationTypes.NONE, 0);
    }
    public static ActionList generateMoveWithinContainer(Controller control, Model model, GameElement element, int rowDest, int colDest, String animationName, double factor) {
        ContainerElement containerSrc = element.getContainer();
        ActionList list = new ActionList();

        // create the move within container action,if possible
        if (containerSrc != null) {
            if (AnimationTypes.NONE.equals(animationName)) {
                GameAction put = new MoveWithinContainerAction(model, element, rowDest, colDest);
                list.addSingleAction(put);
            }
            else {
                // get the look of the element
                ElementLook elementLook = control.getElementLook(element);
                // get the container look of the destination container
                ContainerLook containerLook = (ContainerLook) control.getElementLook(containerSrc);
                // get the location of the element look within this destination look, if placed in rowDest, colDest
                Coord2D center = containerLook.getContainerLocationForLookFromCell(elementLook, rowDest, colDest);
                // generate the action + animation
                GameAction put = new MoveWithinContainerAction(model, element, rowDest, colDest, animationName, center.getX(), center.getY(), factor);
                list.addSingleAction(put);
            }
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

    // draw dice without aniamtion
    public static ActionList generateDrawDice(Model model, GameElement element) {

        ActionList list = new ActionList();
        GameAction draw = new DrawDiceAction(model, element);
        list.addSingleAction(draw);
        return list;
    }

    public static ActionList generateDrawDice(Model model, GameElement element, int nbCycles, int waitBetweenSides) {

        ActionList list = new ActionList();
        GameAction draw = new DrawDiceAction(model, element, nbCycles, waitBetweenSides);
        list.addSingleAction(draw);
        return list;
    }
}