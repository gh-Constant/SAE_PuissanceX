package boardifier.model;

@FunctionalInterface
public interface ContainerOpCallback {
    public void execute(GameElement element, ContainerElement containerDest, int rowDest, int colDest);
}
