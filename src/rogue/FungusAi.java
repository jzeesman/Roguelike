package rogue;

/**
 * The FungusAi class controls all of the behavior specific to Fungus creatures
 */
public class FungusAi extends CreatureAi {
    
    /**
     * StuffFactory object
     * <br>
     * Involved in the {@linkplain rogue.FungusAi#spread()} method
     */
    private StuffFactory factory;
    
    /** Keeps track of how many "children" any given Fungus has spawned **/
    private int spreadCount;
    
    /**
     * The constructor for the FungusAi class
     * <br>
     * Inherits from: {@linkplain CreatureAi#CreatureAi(Creature)}
     * 
     * @param creature
     *        - the creature that will inherit the FungusAi
     * @param factory
     *        - the StuffFactory that will be used in the
     *        {@linkplain #spread()}
     *        method
     */
    public FungusAi(Creature creature, StuffFactory factory) {
        super(creature);
        this.factory = factory;
        creature.aquatic(false);
        creature.canSwim(false);
    }
    
    /**
     * Method that runs on update for the fungus.
     * <br>
     * Chance to spread
     */
    public void onUpdate() {
        if (spreadCount < 1 && Math.random() < 0.02) spread();
    }
    
    /**
     * Method that has a chance to spawn a new fungus
     */
    private void spread() {
        int x = creature.x + (int) (Math.random() * 11) - 8;
        int y = creature.y + (int) (Math.random() * 11) - 8;
        
        if (!creature.canEnter(x, y, creature.z)) return;
        
        Creature child = factory.newFungus(creature.z);
        creature.doAction("spawn a child");
        
        child.x = x;
        child.y = y;
        child.z = creature.z;
        spreadCount++;
    }
    
}
