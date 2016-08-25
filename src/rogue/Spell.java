package rogue;

/**
 * The Spell class contains all of the necessary attributes for spells
 */
public class Spell {
    
    /** The name of the spell **/
    private String name;
    /** The mana cost of the spell **/
    private int manaCost;
    /** The effect of the spell **/
    private Effect effect;
    
    /**
     * The constructor for the Spell class
     * 
     * @param name
     *        - the name of the spell
     * @param manaCost
     *        - the mana cost of the spell
     * @param effect
     *        - the effect of the spell
     */
    public Spell(String name, int manaCost, Effect effect) {
        this.name = name;
        this.manaCost = manaCost;
        this.effect = effect;
    }
    
    /**
     * @return returns the name of the spell
     */
    public String name() {
        return name;
    }
    
    /**
     * @return returns the mana cost of the spell
     */
    public int manaCost() {
        return manaCost;
    }
    
    /**
     * @return returns the effect of the spell
     */
    public Effect effect() {
        return effect;
    }
}
