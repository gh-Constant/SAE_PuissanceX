package boardifier.control;

import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControllerAction implements EventHandler<ActionEvent> {

    protected Model model;
    protected View view;
    protected Controller control;

    public ControllerAction(Model model, View view, Controller control) {
        this.model = model;
        this.view = view;
        this.control = control;
    }

    // by default, do nothing. Must be overridden in subclasses
    public void handle(ActionEvent event) {}
}
