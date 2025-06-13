package boardifier.view;

import boardifier.model.GameElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BackgroundLook extends ElementLook {

    public final static int BACKGROUND_COLOR = 0;
    public final static int BACKGROUND_IMAGE = 1;

    protected Rectangle frame;
    protected ImageView view;

    public BackgroundLook(int width, int height, int type, String value, GameElement element) {
        super(element);
        if (type == BACKGROUND_COLOR) {
            frame = new Rectangle(width, height, Color.valueOf(value));
            addShape(frame);
        }
        else if (type == BACKGROUND_IMAGE) {
            Image img = new Image(value);
            view = new ImageView();
            view.setFitWidth(width);
            view.setFitHeight(height);
            view.setImage(img);
            addNode(view);
        }
    }

    public void render() {

    }
}
