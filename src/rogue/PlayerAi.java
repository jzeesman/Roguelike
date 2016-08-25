package rogue;

import java.util.List;

/**
 * The PlayerAi class controls the specific behaviors unique to the player
 * character
 */
public class PlayerAi extends CreatureAi {
    
    /**
     * A List of String objects that represent the messages to pass to the
     * player on-screen
     **/
    private List<String> messages;
    
    /**
     * A FieldOfView object responsible for handling the player's fov, and how
     * it displays on-screen
     **/
    private FieldOfView fov;
    
    /**
     * The constructor for the PlayerAi class
     * <br>
     * Inherits from: {@linkplain rogue.CreatureAi#CreatureAi(Creature)}
     * 
     * @param creature
     *        - the creature that will inherit the FungusAi
     * @param messages
     *        - the messages that will be sent to the player
     * @param fov
     *        - the FieldOfView object specific to the player
     */
    public PlayerAi(Creature creature, List<String> messages, FieldOfView fov) {
        super(creature);
        this.messages = messages;
        this.fov = fov;
        creature.aquatic(false);
        creature.canSwim(true);
    }
    
    @Override
    public boolean canSee(int wx, int wy, int wz) {
        return fov.isVisible(wx, wy, wz);
    }
    
    public void onEnter(int x, int y, int z, Tile tile) {
        if (tile.isGround() || tile.isWater()) {
            creature.x = x;
            creature.y = y;
            creature.z = z;
        } else if (tile.isDiggable()) {
            creature.dig(x, y, z);
        }
    }
    
    public Tile rememberedTile(int wx, int wy, int wz) {
        return fov.tile(wx, wy, wz);
    }
    
    public void onGainLevel() {
    }
    
    public void onNotify(String message) {
        messages.add(message);
    }
}
