package view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class PuissanceXFXRootPane extends Pane {

    public PuissanceXFXRootPane() {
        super();
        createContent();
    }

    private void createContent() {
        Rectangle background = new Rectangle(800, 600);
        background.setFill(Color.LIGHTGRAY);

        Text title = new Text("PuissanceX");
        title.setFont(new Font(30));
        title.setFill(Color.BLACK);
        title.setX(320);
        title.setY(80);

        getChildren().addAll(background, title);
    }
}