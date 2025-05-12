package control;

import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.control.Controller;
import model.PuissanceXModel;
import boardifier.model.action.ActionList;

public class PuissanceXDecider extends Decider {
    public PuissanceXDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        PuissanceXModel model = (PuissanceXModel) this.model;
        // TODO: Implement decision logic
        return null;
    }
}