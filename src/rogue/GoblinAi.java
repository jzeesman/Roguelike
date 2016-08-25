package rogue;

/**
 * The GoblinAi class controls all of the behavior specific to Goblin creatures
 */
public class GoblinAi extends CreatureAi {
    
    /** The Creature object representing the player **/
    private Creature player;
    
    /**
     * The GoblinAi constructor
     * 
     * @param creature
     *        - the creature that will inherit the GoblinAi
     * @param player
     *        - the player
     */
    public GoblinAi(Creature creature, Creature player) {
        super(creature);
        this.player = player;
        creature.aquatic(false);
        creature.canSwim(false);
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
            if (canRangedWeaponAttack(player))
                creature.rangedWeaponAttack(player);
            else if (canThrowAt(player))
                creature.throwItem(getWeaponToThrow(), player.x, player.y, player.z);
            else if (creature.canSee(player.x, player.y, player.z))
                hunt(player);
            else if (canPickup()) creature.pickup();
        } else {
            wander();
        }
    }
    
}
