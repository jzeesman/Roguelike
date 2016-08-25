package rogue;

/**
 * The Effect class handles potion effects and how they affect mobs
 */
public class Effect {
    
    /** Represents the Effect's duration **/
    protected int duration;
    
    /**
     * The main constructor for the Effect class
     * 
     * @param duration
     *        - how long the effect will last
     */
    public Effect(int duration) {
        this.duration = duration;
    }
    
    /**
     * This is a copy constructor for the Effect class
     * 
     * @param other
     *        - the effect to copy
     */
    public Effect(Effect other) {
        this.duration = other.duration;
    }
    
    /**
     * The main update method
     * <br>
     * Simply decreases the duration by 1 upon update
     * 
     * @param creature
     *        - creature whose effect will be updated
     */
    public void update(Creature creature) {
        duration--;
    }
    
    /**
     * The start method, empty for the Effect class
     * 
     * @param creature
     *        - creature to be affected
     */
    public void start(Creature creature) {
        
    }
    
    /**
     * The end method, empty for the Effect class
     * 
     * @param creature
     *        - creature to be affected
     */
    public void end(Creature creature) {
        
    }
    
    /**
     * @return returns whether or not this effect on this creature is finished
     */
    public boolean isDone() {
        return duration < 1;
    }
    
}
