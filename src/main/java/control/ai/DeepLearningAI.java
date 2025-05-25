package control.ai;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import control.PuissanceXDecider;

public class DeepLearningAI extends PuissanceXDecider {
    public DeepLearningAI(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        // Implement random move logic here
        // Return an ActionList representing the chosen move
        return super.decide(); // Placeholder
    }
}