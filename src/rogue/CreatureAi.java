package rogue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CreatureAi class contains all the basic methods for any AI
 */
public class CreatureAi {
    
    /** Creature object **/
    protected Creature creature;
    
    /**
     * Map<String, String> object containing a list of all of the creature's
     * known item names
     **/
    private Map<String, String> itemNames;
    
    // TODO: Update links as you create new creatures
    
    /**
     * The general constructor for the CreatureAi class:
     * <br>
     * <br>
     * <b>See also:</b>
     * <ul>
     * <li>{@linkplain rogue.PlayerAi#PlayerAi(Creature, List, FieldOfView)}</li>
     * <li>{@linkplain rogue.BatAi#BatAi(Creature)}</li>
     * <li>{@linkplain rogue.FungusAi#FungusAi(Creature, StuffFactory)}</li>
     * <li>{@linkplain rogue.ZombieAi#ZombieAi(Creature, Creature)}</li>
     * <li>{@linkplain rogue.GoblinAi#GoblinAi(Creature, Creature)}</li>
     * <li>{@linkplain rogue.TrollAi#TrollAi(Creature, Creature)}</li>
     * <li>{@linkplain rogue.WeepingAngelAi#WeepingAngelAi(Creature, Creature)}</li>
     * <li>{@linkplain rogue.EelAi#EelAi(Creature, Creature)}</li>
     * </ul>
     * 
     * @param creature
     *        - the creature that will inherit the CreatureAi behaviors
     */
    public CreatureAi(Creature creature) {
        this.creature = creature;
        this.creature.setCreatureAi(this);
        itemNames = new HashMap<String, String>();
    }
    
    /**
     * Method for determining whether or not a creature can see a given tile
     * 
     * @param wx
     *        - x coordinate of tile to check
     * @param wy
     *        - y coordinate of tile to check
     * @param wz
     *        - z level of tile to check
     * @return returns true if can be seen, otherwise false
     */
    public boolean canSee(int wx, int wy, int wz) {
        if (creature.z != wz) return false;
        
        if ((creature.x - wx) * (creature.x - wx) + (creature.y - wy) * (creature.y - wy) > creature.visionRadius()
                * creature.visionRadius())
            return false;
        
        for (Point p : new Line(creature.x, creature.y, wx, wy)) {
            if (!creature.realTile(p.x, p.y, wz).blocksVision() || p.x == wx && p.y == wy) continue;
            
            return false;
        }
        
        return true;
    }
    
    /**
     * Method to check for basic collision (mainly into walls)
     * 
     * @param x
     *        - x coordinate of desired creature movement
     * @param y
     *        - y coordinate of desired creature movement
     * @param z
     *        - z level of desired creature movement
     * @param tile
     *        - tile to check
     */
    public void onEnter(int x, int y, int z, Tile tile) {
        if (!creature.aquatic()) {
            if (tile.isGround()) {
                creature.x = x;
                creature.y = y;
                creature.z = z;
            } else if ((creature.canSwim() || creature.canFly()) && tile.isWater()) {
                creature.x = x;
                creature.y = y;
                creature.z = z;
            } else {
                creature.doAction("bump into a wall");
            }
        } else {
            if (tile.isWater()) {
                creature.x = x;
                creature.y = y;
                creature.z = z;
            } else {
                creature.doAction("swim into a wall");
            }
        }
    }
    
    /**
     * Wander method for the Creature class
     * <br>
     * Makes sure that creatures can't attack creatures of the same type
     */
    public void wander() {
        int mx = (int) (Math.random() * 3) - 1;
        int my = (int) (Math.random() * 3) - 1;
        
        Creature other = creature.creature(creature.x + mx, creature.y + my, creature.z);
        
        if (other != null && other.glyph() == creature.glyph())
            return;
        else
            creature.moveBy(mx, my, 0);
    }
    
    /**
     * Method to check whether or not a creature can use a ranged attack
     * 
     * @param other
     *        - creature to attack
     * @return returns true if there is a creature to actually attack & if this
     *         creature's weapon is not null & has a ranged attack value > 0 &
     *         if this creature can see the other creature, otherwise false
     */
    protected boolean canRangedWeaponAttack(Creature other) {
        return creature.weapon() != null && creature.weapon().rangedAttackValue() > 0
                && creature.canSee(other.x, other.y, other.z);
    }
    
    /**
     * Method to check whether or not a creature can use a thrown attack
     * 
     * @param other
     *        - creature to attack
     * @return returns true if this creature can see the other creature & this
     *         creature's throwing weapon is not null
     */
    protected boolean canThrowAt(Creature other) {
        return creature.canSee(other.x, other.y, other.z) && getWeaponToThrow() != null;
    }
    
    /**
     * Method to check whether or not the creature has a throwable weapon
     * 
     * @return returns the item to be thrown, assuming it is not null
     */
    protected Item getWeaponToThrow() {
        Item toThrow = null;
        
        for (Item item : creature.inventory().getItems()) {
            if (item == null || creature.weapon() == item || creature.armor() == item) continue;
            
            if (toThrow == null || item.thrownAttackValue() > toThrow.attackValue()) toThrow = item;
        }
        
        return toThrow;
    }
    
    /**
     * Method to check whether or not a creature can pick up the item at its
     * feet
     * 
     * @return returns true if the creature's inventory is not full & there is
     *         an item at their feet, otherwise false
     */
    protected boolean canPickup() {
        return creature.item(creature.x, creature.y, creature.z) != null & !creature.inventory().isFull();
    }
    
    /**
     * Method for determining if a creature has better equipment in their
     * inventory compared to what they have equipped
     * 
     * @return returns true if they have a stronger weapon or better armor,
     *         otherwise false
     */
    protected boolean canUseBetterEquipment() {
        int currentWeaponRating = creature.weapon() == null ? 0
                : creature.weapon().attackValue() + creature.weapon().rangedAttackValue();
        int currentArmorRating = creature.armor() == null ? 0 : creature.armor().defenseValue();
        
        for (Item item : creature.inventory().getItems()) {
            if (item == null) continue;
            
            boolean isArmor = item.attackValue() + item.rangedAttackValue() < item.defenseValue();
            
            if ((item.attackValue() + item.rangedAttackValue() > currentWeaponRating)
                    || (isArmor && item.defenseValue() > currentArmorRating))
                return true;
        }
        
        return false;
    }
    
    /**
     * If {@linkplain rogue.CreatureAi#useBetterEquipment()}=true, equip the
     * items as necessary
     */
    protected void useBetterEquipment() {
        int currentWeaponRating = creature.weapon() == null ? 0
                : creature.weapon().attackValue() + creature.weapon().rangedAttackValue();
        int currentArmorRating = creature.armor() == null ? 0 : creature.armor().defenseValue();
        
        for (Item item : creature.inventory().getItems()) {
            if (item == null) continue;
            
            boolean isArmor = item.attackValue() + item.rangedAttackValue() < item.defenseValue();
            
            if ((item.attackValue() + item.rangedAttackValue() > currentWeaponRating)
                    || (isArmor && item.defenseValue() > currentArmorRating))
                creature.equip(item);
        }
    }
    
    /**
     * Method for path finding to a target
     * 
     * @param target
     *        - creature being hunted
     */
    public void hunt(Creature target) {
        List<Point> points = new Path(creature, target.x, target.y).points();
        
        if (points == null) return;
        if (points.isEmpty()) return;
        
        int mx = points.get(0).x - creature.x;
        int my = points.get(0).y - creature.y;
        
        creature.moveBy(mx, my, 0);
    }
    
    /**
     * Method to retrieve the name of an item
     * 
     * @param item
     *        - item name to be retrieved
     * @return returns the item name if known by this creature, otherwise
     *         returns the item's appearance
     */
    public String getName(Item item) {
        String name;
        if (item.effectVisible())
            name = itemNames.get(item.fullName());
        else
            name = itemNames.get(item.name());
        
        return name == null ? item.appearance() : name;
    }
    
    /**
     * Method for setting an item's name
     * 
     * @param item
     *        - item whose name will be set
     * @param name
     *        - name to be set
     */
    public void setName(Item item, String name) {
        itemNames.put(item.name(), name);
    }
    
    /**
     * Method for returning the tile this creature last remembers
     * 
     * @param wx
     *        - the x coordinate of the tile to be remembered
     * @param wy
     *        - the y coordinate of the tile to be remembered
     * @param wz
     *        - the z level of the tile to be remembered
     * @return returns the most recently remembered version of the tile at the
     *         given location (by default is set to
     *         {@linkplain rogue.Tile#UNKNOWN})
     */
    public Tile rememberedTile(int wx, int wy, int wz) {
        return Tile.UNKNOWN;
    }
    
    /**
     * onUpdate method, left blank in the CreatureAi class
     * <br>
     * Allows for individualization for each type of creature
     */
    public void onUpdate() {
    }
    
    /**
     * onNotify method, left blank in the CreatureAi class
     * <br>
     * Allows for individualization for each type of creature
     * 
     * @param message
     *        - the message to be passed along
     */
    public void onNotify(String message) {
    }
    
    /**
     * onGainLevel method, left blank in the CreatureAi class
     * <br>
     * Allows for individualization for each type of creature
     */
    public void onGainLevel() {
    }
    
}
