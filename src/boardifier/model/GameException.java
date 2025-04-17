
package boardifier.model;

public class GameException extends Exception {

    GameElement element;
    public GameException(String message) {
        super(message);
        element = null;
    }
    public GameException(String message, GameElement element) {
        super(message);
        this.element = element;
    }

    @Override
    public String getMessage() {
        if (element != null) {
            return element+" has caused the following exception: "+super.getMessage();
        }
        return super.getMessage();
    }
}
