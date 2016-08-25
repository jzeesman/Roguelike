package rogue;

public class EelAi extends CreatureAi {
    
    // TODO: Add the necessary methods to the EelAi class
    // TODO: Remember to add Eels in the PlayScreen's createCreatures method
    // TODO: Remember to add water generation before adding eels
    // (since eels can only spawn and move in water, if there is no water the
    // game won't load)
    
    private Creature player;
    
    public EelAi(Creature creature, Creature player) {
        super(creature);
        this.player = player;
        creature.aquatic(true);
        creature.canSwim(true);
    }
    
}
