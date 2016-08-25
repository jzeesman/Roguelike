package rogue;

/**
 * The LevelUpOption class contains the method to call upon level up
 */
public abstract class LevelUpOption {
    
    /** The name of the LevelUpOption **/
    private String name;
    
    /**
     * The LevelUpOption constructor
     * 
     * @param name
     *        - the name of the LevelUpOption
     */
    public LevelUpOption(String name) {
        this.name = name;
    }
    
    /**
     * The invoke method (abstract) allows for customized functionality upon
     * level up
     * 
     * @param creature
     *        - the creature calling the level up method
     */
    public abstract void invoke(Creature creature);
    
    /**
     * @return returns the name of the LevelUpOption
     */
    public String name() {
        return name;
    }
}
