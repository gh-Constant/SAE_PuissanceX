package boardifier.view;

import boardifier.model.DiceElement;
import boardifier.model.GameElement;

public class Dice6Look extends SpriteImageLook {

    public Dice6Look(int width, int height, GameElement element) {
        super(width, height, element);
        DiceElement d = (DiceElement)element;
        for(int i=0;i<d.getNbSides();i++) {
            if (i%6 == 0) addImage("images/de6_1.png");
            else if (i%6 == 1) addImage("images/de6_2.png");
            else if (i%6 == 2) addImage("images/de6_3.png");
            else if (i%6 == 3) addImage("images/de6_4.png");
            else if (i%6 == 4) addImage("images/de6_5.png");
            else if (i%6 == 5) addImage("images/de6_6.png");
        }
    }
}
