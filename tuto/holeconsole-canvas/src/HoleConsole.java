import boardifier.model.GameException;
import boardifier.view.View;
import control.HoleController;

import boardifier.control.StageFactory;
import boardifier.model.Model;

public class HoleConsole {

    public static void main(String[] args) {

        int mode = 0;
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode <0) || (mode>2)) mode = 0;
            }
            catch(NumberFormatException e) {
                mode = 0;
            }
        }
        Model model = new Model();
        /*
        TO FULFILL:
            - add both players to model taking mode value into account
            - register the model and view class names (i.e model.HoleStageModel & view.HoleStageView
            - create the controller
            - set the name of the first stage to use when starting the game
            - start the game
            - start the stage loop.
         */
    }
}
