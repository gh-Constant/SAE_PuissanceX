package boardifier.view;

import boardifier.model.GameElement;
import boardifier.model.SpriteElement;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class SpriteDrawnLook extends SpriteLook {

    List<Shape>[] drawnFaces;

    public SpriteDrawnLook(GameElement element) {
        super(element);
        SpriteElement se = (SpriteElement)element;
        drawnFaces = new ArrayList[se.getNbFaces()];
        for(int i = 0; i<se.getNbFaces(); i++) {
            drawnFaces[i] = new ArrayList<>();
        }
    }

    /**
     * Adding a shape to one of the faces of this look.
     * By default, all the shapes put at index 0 are added to the group of the look, so that
     * at least on face is drawn at the game stage start.
     * @param index the index of the look
     * @param shape the shape to add
     */
    public void addShapeToDrawnLook(int index, Shape shape) {
        drawnFaces[index].add(shape);
        if (index == 0) {
            addShape(shape);
        }
    }
    @Override
    public void render() {
        clearGroup();
        clearShapes();
        // this method is called after checking oldIndex was != new index, and
        // if it is the case, oldIndex takes the new value. We can use it here.
        SpriteElement se = (SpriteElement) getElement();
        for(Shape shape : drawnFaces[se.getCurrentFaceIndex()]) {
            addShape(shape);
        }
    }
}
