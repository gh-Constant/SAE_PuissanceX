package boardifier.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public final static int HUMAN = 1;
    public final static int COMPUTER = 2;

    protected int type;
    protected String name;
    /**
     * The list of keys currently pressed but not yet realeased
     */
    protected List<String> keyPressed;

    private Player(int type, String name) {
         this.type = type;
         this.name = name;
         this.keyPressed = new ArrayList<>();
    }

    public void reset() {
        keyPressed.clear();
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastKeyPressed() {
        if (keyPressed.isEmpty()) return "";
        return keyPressed.get(keyPressed.size()-1);
    }
    public void addKeyPressed(String lastKey) {
        if (!this.keyPressed.contains(lastKey)) {
            this.keyPressed.add(lastKey);
        }
    }
    public void removeKeyPressed(String lastKey) {
        this.keyPressed.remove(lastKey);
    }
    public boolean isKeyPressed(String key) {
        return keyPressed.contains(key);
    }

    public static Player createHumanPlayer(String name) {
        return new Player(HUMAN, name);
    }
    public static Player createComputerPlayer(String name) {
        return new Player(COMPUTER, name);
    }

}
