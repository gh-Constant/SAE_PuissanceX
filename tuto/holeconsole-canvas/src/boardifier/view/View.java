package boardifier.view;

import boardifier.model.GameElement;

import boardifier.model.Model;

public class View {

    /**
     * The model
     */
    protected Model model;

    protected GameStageView gameStageView;

    protected RootPane rootPane;

    public View(Model model) {
        this.model = model;
        rootPane = new RootPane();
    }

    public GameStageView getGameStageView() {
        return gameStageView;
    }

    public void setView(GameStageView gameStageView)  {
        this.gameStageView = gameStageView;
        rootPane.init(gameStageView);
    }

    /* ***************************************
       TRAMPOLINE METHODS
    **************************************** */
    public ElementLook getElementLook(GameElement element) {
        if (gameStageView == null) return null;
        return gameStageView.getElementLook(element);
    }
    public ContainerLook getElementContainerLook(GameElement element) {
        return (ContainerLook)getElementLook(element.getContainer());
    }

    public void update() {
        // NB: rootPane update() calls the gameStageView update()
        rootPane.udpate();
        // by default, prints the viewport of the rootPane to display the board state in the console
        rootPane.print();
    }
}
