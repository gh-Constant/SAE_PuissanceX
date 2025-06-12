package boardifier.view;

import boardifier.model.GameElement;
import boardifier.model.TextElement;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextLook extends ElementLook {

    protected Text text;
    protected int fontSize;
    protected String color; // must be prodive as a string containing an hex value like "0x123456"

    public TextLook(int fontSize, String color, GameElement element) {
        super(element);
        this.fontSize = fontSize;
        this.color = color;
        TextElement te = (TextElement) element;
        text = new Text(te.getText());
        text.setFont(new Font(fontSize));
        text.setFill(Color.valueOf(color));
        addShape(text);
    }

    public void render() {
        TextElement te = (TextElement) getElement();
        text.setText(te.getText());
    }

    public void onFaceChange() {
        render();
    }
}
