package view;

import boardifier.model.GameElement;
import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Pawn;

public class PawnLook extends ElementLook {

    public PawnLook(GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element, 1, 1);
    }

    protected void render() {
        Pawn pawn = (Pawn)element;
        /*
        TO FULFILL:
            - put in shape[0][0] the pawn's value, char color = white on black, or black on red depending on the pawn color.
         */
    }

}
