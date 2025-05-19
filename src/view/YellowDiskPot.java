package view;

import boardifier.model.ContainerElement;
import boardifier.view.TableLook;

/**
 * Black pot inherits from TableLook, using the constructor for
 * flexible cell sizes and a visible border. It implies that if there is no element in a cell
 * it has a zero size and thus, is not displayed. This is why during the game,
 * the black pot will reduce in size because of pawn are removed from the pot to be placed
 * on the main board. At then end, it will totally disappear.
 *
 * Note that this class is not necessary and the HoleStageView could create directly an instance of TableLook.
 * So, this subclass is just in case of we would like to change the look of the black pot in the future.
 */
public class YellowDiskPot extends TableLook {

    public YellowDiskPot(ContainerElement containerElement) {
        super(containerElement, -1, 1);
    }
}