package boardifier.view;

import boardifier.model.GameElement;
import boardifier.model.SpriteElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class SpriteImageLook extends SpriteLook {
    protected List<Image> images;
    protected ImageView view;
    int width;
    int height;

    public SpriteImageLook(int width, int height, GameElement element) {
        super(element);
        this.width = width;
        this.height = height;
        images = new ArrayList<>();
        view = new ImageView();
        view.setFitWidth(width);
        view.setFitHeight(height);
        addNode(view);
    }

    public void addImage(String fileName) {
        Image image = new Image(fileName);
        images.add(image);
        if (images.size() == 1) {
            view.setImage(image);
        }
    }

    public void render() {
        SpriteElement se = (SpriteElement) getElement();
        int newIndex = se.getCurrentFaceIndex();
        // change the image only if it exists in images[newIndex]
        if (newIndex < images.size()) {
            view.setImage(images.get(newIndex));
        }
    }
}
