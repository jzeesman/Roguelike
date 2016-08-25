package rogue;

public class TrollAi extends CreatureAi {
    
    /** The Creature object representing the player **/
    private Creature player;
    
    /**
     * The TrollAi constructor
     * 
     * @param creature
     *        - the creature that will inherit the TrollAi
     * @param player
     *        - the player
     */
    public TrollAi(Creature creature, Creature player) {
        super(creature);
        this.player = player;
    }
    
    public void onUpdate() {
        for (Creature c : player.getCreaturesInStealthRadius()) {
            if (creature.equals(c)) {
                creature.hasSeenPlayer(true);
            }
        }
        
        if (creature.hasSeenPlayer()) {
            if (!creature.canSee(player.x, player.y, player.z)) {
                creature.setLastSeenPlayer(creature.lastSeenPlayer() + 1);
            } else {
                creature.setLastSeenPlayer(0);
            }
            if (creature.lastSeenPlayer() == 2) {
                creature.hasSeenPlayer(false);
                return;
            }
            if (creature.canSee(player.x, player.y, player.z))
                hunt(player);
            else if (canPickup()) if (creature.item(creature.x, creature.y, creature.z).isMace()) creature.pickup();
        } else {
            wander();
        }
    }
    
}
