package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

/**
 * Disc represents a game piece in Puissance X (Connect X).
 * Each disc belongs to a player and has a specific color.
 */
public class Disc extends GameElement {
    
    private int playerId;
    private String color;
    
    // Constants for player colors
    public static final String COLOR_PLAYER1 = "yellow";
    public static final String COLOR_PLAYER2 = "red";
    
    /**
     * Creates a new disc for a specific player.
     * 
     * @param x The x-coordinate of the disc
     * @param y The y-coordinate of the disc
     * @param playerId The ID of the player who owns this disc
     * @param gameStageModel The game stage model that contains this disc
     */
    public Disc(int x, int y, int playerId, GameStageModel gameStageModel) {
        super(x, y, gameStageModel);
        
        // Register element type for discs if not already registered
        ElementTypes.register("disc", 51);
        type = ElementTypes.getType("disc");
        
        // Store the player ID
        this.playerId = playerId;
        
        // Set the color based on the player ID
        if (playerId == 0) {
            this.color = COLOR_PLAYER1;
        } else {
            this.color = COLOR_PLAYER2;
        }
    }
    
    /**
     * Gets the ID of the player who owns this disc.
     * 
     * @return The player ID
     */
    public int getPlayerId() {
        return playerId;
    }
    
    /**
     * Gets the color of this disc.
     * 
     * @return The color as a string
     */
    public String getColor() {
        return color;
    }
    
    @Override
    public void update() {
        // Handle animations if needed
        if (animation != null) {
            animation.next();
        }
    }
}