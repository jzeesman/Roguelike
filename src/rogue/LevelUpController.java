package rogue;

import java.util.ArrayList;
import java.util.List;

/**
 * LevelUpController class handles what happens upon level up
 */
public class LevelUpController {
    
    // TODO: Create new attributes
    // TODO: Create new level up options
    
    /**
     * LevelUpOption array containing all of the choices one has upon leveling
     * up
     */
    //@formatter:off
    private static LevelUpOption[] options = new LevelUpOption[]{
            new LevelUpOption("Increased hit points"){
                public void invoke(Creature creature) {
                    creature.modifyMaxHp(10);
                    creature.modifyHp(10, null);
                    creature.doAction("look healthier");
                }
            },
            new LevelUpOption("Increased mana"){
                public void invoke(Creature creature){
                    creature.modifyMaxMana(5);
                    creature.modifyMana(5);
                    creature.doAction("look more magical");
                }
            },
            new LevelUpOption("Increased strength"){
                public void invoke(Creature creature){
                    creature.modifyStrength(1);
                    creature.doAction("look stronger");
                }
            },
            new LevelUpOption("Increased dexterity"){
                public void invoke(Creature creature){
                    creature.modifyDexterity(1);
                    creature.doAction("look dexterous");
                }
            },
            new LevelUpOption("Increased attack value"){
                public void invoke(Creature creature){
                    creature.modifyAttackValue(2);
                    creature.doAction("look tougher");
                }
            },
            new LevelUpOption("Increased defense value"){
                public void invoke(Creature creature){
                    creature.modifyDefenseValue(2);
                    creature.doAction("look bulkier");
                }
            },
            new LevelUpOption("Increased vision"){
                public void invoke(Creature creature){
                    creature.modifyVisionRadius(2);
                    creature.doAction("look more aware");
                }
            },
            new LevelUpOption("Increased health regen"){
                public void invoke(Creature creature){
                    creature.modifyRegenHpPer1000(5);
                    creature.doAction("look a little less bruised");
                }
            },
            new LevelUpOption("Increased mana regen"){
                public void invoke(Creature creature){
                    creature.modifyRegenManaPer1000(10);
                    creature.doAction("look a little less tired");
                }
            }
    };
    //@formatter:on
    
    /**
     * Method that applies a random LevelUpOption upon level up
     * 
     * @param creature
     *        - creature to apply the LevelUpOption to
     */
    public void autoLevelUp(Creature creature) {
        options[(int) (Math.random() * options.length)].invoke(creature);
    }
    
    /**
     * Method to get all of the different LevelUpOptions
     * 
     * @return returns a list of all of the LevelUpOptions
     */
    public List<String> getLevelUpOptions() {
        List<String> names = new ArrayList<String>();
        for (LevelUpOption option : options) {
            names.add(option.name());
        }
        return names;
    }
    
    /**
     * Method that gets the LevelUpOption requested
     * 
     * @param name
     *        - the name of the option to get
     * @return returns the option requested
     */
    public LevelUpOption getLevelUpOption(String name) {
        for (LevelUpOption option : options) {
            if (option.name().equals(name)) return option;
        }
        return null;
    }
}
