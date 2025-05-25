package boardifier.model;

import boardifier.model.animation.AnimationStep;


public class TextElement extends GameElement {
    protected String text;


    public TextElement(String text, GameStageModel gameStageModel) {
        super(gameStageModel);
        this.text = text;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        addChangeFaceEvent();
    }

    public void update() {
        // if must be animated, move the text
        if (animation != null) {
            AnimationStep step = animation.next();
            if (step != null) {
                setLocation(step.getInt(0), step.getInt(1));
            }
            else {
                animation = null;
            }
        }
    }
}
