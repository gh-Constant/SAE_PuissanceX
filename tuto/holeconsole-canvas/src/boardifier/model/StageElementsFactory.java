package boardifier.model;

public abstract class StageElementsFactory {

    protected GameStageModel model;

    public StageElementsFactory(GameStageModel model) {
        this.model = model;
    }

    public abstract void setup();
}
