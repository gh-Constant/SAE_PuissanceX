package boardifier.control;

import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ControllerMouse  implements EventHandler<MouseEvent> {
    protected Model model;
    protected View view;
    protected Controller control;

    public ControllerMouse(Model model, View view, Controller control) {
        this.model = model;
        this.view = view;
        this.control = control;
        view.getRootPane().addEventFilter(MouseEvent.MOUSE_PRESSED, this::handle);
    }

    // by default, do nothing. Must be overridden in subclasses
    public void handle(MouseEvent event) {}

}
