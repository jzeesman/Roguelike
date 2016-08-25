package rogue;

public class WeepingAngelAi extends CreatureAi {
    
    /** The Creature object representing the player **/
    private Creature player;
    
    /**
     * The WeepingAngelAi constructor
     * 
     * @param creature
     *        - the creature that will inherit the WeepingAngelAi
     * @param player
     *        - the player
     */
    public WeepingAngelAi(Creature creature, Creature player) {
        super(creature);
        this.player = player;
        creature.aquatic(false);
        creature.canSwim(false);
    }
    
    public void onUpdate() {
        int dx = creature.x - player.x;
        int dy = creature.y - player.y;
        int dist = (int) Math.sqrt((dx * dx) + (dy * dy));
        if (!player.canSee(creature.x, creature.y, creature.z) && player.detectCreatures() == 0 && (dist < 30) && player.z == creature.z){
            hunt(player);
        } else if(dist < 3 && creature.z == player.z){
            warp(player);
        }
    }
    
    public void warp(Creature player){
        player.doAction("fade out");
        
        int mx = 0;
        int my = 0;
        
        do {
            mx = (int) (Math.random() * 31) - 15;
            my = (int) (Math.random() * 31) - 15;
        } while ((mx >= 6 || mx <= -6) && (my >= 6 || my <= -6) && !player.canEnter(player.x + mx, player.y + my, player.z));
        
        player.moveBy(mx, my, 0);
        
        player.doAction("fade in");
        
        player.modifyHp(-25, "Killed by a Weeping Angel.");
    }
    
}
