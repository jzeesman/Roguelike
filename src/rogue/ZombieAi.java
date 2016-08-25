package rogue;

/**
 * The ZombieAi class controls all of the behavior specific to Zombie creatures
 */
public class ZombieAi extends CreatureAi {
    
    /** The Creature object representing the player character **/
    private Creature player;
    
    /**
     * The constructor for the ZombieAi class
     * <br>
     * Inherits from: {@linkplain CreatureAi#CreatureAi(Creature)}
     * 
     * @param creature
     *        - the creature that will inherit the ZombieAi
     * @param player
     *        - the player
     */
    public ZombieAi(Creature creature, Creature player) {
        super(creature);
        this.player = player;
        creature.aquatic(false);
        creature.canSwim(false);
    }
    
    public void onUpdate() {
        if (Math.random() < 0.2) return;
        
        if (creature.canSee(player.x, player.y, player.z))
            hunt(player);
        else
            wander();
    }
    
}
