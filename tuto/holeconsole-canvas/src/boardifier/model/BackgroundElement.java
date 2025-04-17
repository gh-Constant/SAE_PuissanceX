package boardifier.model;

public class BackgroundElement extends StaticElement {

    public BackgroundElement(int x, int y, GameStageModel gameStageModel) {
        super(x, y, gameStageModel, ElementTypes.getType("background"));
    }
}
