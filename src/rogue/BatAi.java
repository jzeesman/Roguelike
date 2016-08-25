package rogue;

/**
 * The BatAi class represents the AI for Bat creatures
 * <br>
 * Extends {@linkplain CreatureAi#CreatureAi(Creature)}
 */
public class BatAi extends CreatureAi {
    
    /**
     * The main constructor for the BatAi class
     * 
     * @param creature
     *        - the creature that will inherit the BatAi behaviors
     */
    public BatAi(Creature creature) {
        super(creature);
        creature.aquatic(false);
        creature.canSwim(false);
        creature.canFly(true);
    }
    
    /**
     * Method that runs on update for the bat.
     * <br>
     * Moves twice.
     */
    public void onUpdate() {
        wander();
        wander();
    }
    
}
