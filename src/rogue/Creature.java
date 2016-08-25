package rogue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The Creature class handles all the specific attributes of a given creature
 */
public class Creature {
    
    /** The world in which the creature is in **/
    private World world;
    
    /** The x coordinate of the creature **/
    public int x;
    /** The y coordinate of the creature **/
    public int y;
    /** The z level of the creature **/
    public int z;
    
    /** The glyph that represents the creature in the world **/
    private char glyph;
    /** The color of the glyph that will be in the world **/
    private Color color;
    /** The proper name of the creature **/
    private String name;
    
    /** A String representing the creature's (the player's) cause of death **/
    private String causeOfDeath;
    
    /** The max hp of the creature **/
    private int maxHp;
    /** The current hp of the creature **/
    private int hp;
    /** The max mana of the creature **/
    private int maxMana;
    /** The current mana of the creature **/
    private int mana;
    
    /** The attack value of the creature **/
    private int attackValue;
    /** The defense value of the creature **/
    private int defenseValue;
    
    private int strength;
    private int dexterity;
    
    /** The max food capacity of the creature **/
    private int maxFood;
    /** The current food level of the creature **/
    private int food;
    
    /** The amount of "time" between each hp regen **/
    private int regenHpCooldown;
    /** How fast a creature regens hp **/
    private int regenHpPer1000;
    
    /** The amount of "time" between each mana regen **/
    private int regenManaCooldown;
    /** How fast a creature regens mana **/
    private int regenManaPer1000;
    
    /** The creature's weapon **/
    private Item weapon;
    /** The creature's armor **/
    private Item armor;
    private Item ring;
    private Item charm;
    
    private boolean rooted;
    private boolean invisible;
    
    private int stealthRadius;
    private boolean hasSeenPlayer;
    private int lastSeenPlayer;
    
    private boolean aquatic;
    private boolean canSwim;
    private boolean canFly;
    
    /** The creature's xp for the current level **/
    private int xp;
    /** The creature's current level **/
    private int level;
    
    /** The creature's vision radius **/
    private int visionRadius;
    
    /** Value determining whether or not a creature has a "sixth sense" **/
    private int detectCreatures;
    
    /** The list of all effects currently active on the creature **/
    private List<Effect> effects;
    
    /** The creature's inventory **/
    private Inventory inventory;
    
    /** The creature's inherited AI **/
    private CreatureAi ai;
    
    /**
     * General constructor for the Creature class
     * 
     * @param world
     *        - the world in which the creature is located
     * @param name
     *        - the name of the creature
     * @param glyph
     *        - the char that represents the creature
     * @param color
     *        - the color of the glyph
     * @param maxHp
     *        - the maximum hp of the creature
     * @param maxMana
     *        - the maximum mana of the creature
     * @param attack
     *        - the attackValue of the creature
     * @param defense
     *        - the defenseValue of the creature
     */
    public Creature(World world, String name, char glyph, Color color, int maxHp, int maxMana, int attack, int defense,
            int strength, int dexterity) {
        this.world = world;
        this.name = name;
        this.glyph = glyph;
        this.color = color;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMana = maxMana;
        this.mana = maxMana;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.strength = strength;
        this.dexterity = dexterity;
        this.visionRadius = 5;
        this.inventory = new Inventory(20);
        this.maxFood = 1000;
        this.food = maxFood / 3 * 2;
        this.level = 1;
        this.regenHpPer1000 = 50;
        this.regenManaPer1000 = 100;
        effects = new ArrayList<Effect>();
        rooted = false;
        invisible = false;
        hasSeenPlayer = false;
        lastSeenPlayer = 0;
    }
    
    public Creature(World world, String name, char glyph, Color color, String className) {
        this.world = world;
        this.name = name;
        this.glyph = glyph;
        this.color = color;
        this.inventory = new Inventory(20);
        
        this.visionRadius = 5;
        this.maxFood = 1000;
        this.food = maxFood / 3 * 2;
        this.level = 1;
        effects = new ArrayList<Effect>();
        rooted = false;
        invisible = false;
        hasSeenPlayer = false;
        lastSeenPlayer = 0;
        
        Class startingClass = getClassStats(className);
        
        this.maxHp = startingClass.maxHp();
        this.regenHpPer1000 = startingClass.hpRegen();
        this.maxMana = startingClass.maxMana();
        this.regenManaPer1000 = startingClass.manaRegen();
        this.attackValue = startingClass.attackValue();
        this.defenseValue = startingClass.defenseValue();
        this.strength = startingClass.strength();
        this.dexterity = startingClass.dexterity();
        
        for (Item i : startingClass.startingItems().getItems()) {
            this.inventory.add(i);
        }
        
        this.hp = maxHp;
        this.mana = maxMana;
        
        if (this.isPlayer()) {
            stealthRadius = 7;
            visionRadius = 10;
        }
    }
    
    // TODO: Implement creatures sleeping
    // Creatures will wander/sleep (implement sleep) oblivious to the player,
    // until they get within stealth range
    // Creatures will go from wander -> hunt if within player's stealth range
    // However, if the player gets far enough away, the creature will "lose
    // interest"
    
    // (if the creature hasn't seen the player in X number of turns, they lose
    // interest)
    
    // (player wants stealth range as small as possible)
    // heavier armor increases stealth range
    // "larger" weapons increase stealth range
    // certain rings/charms increase/decrease stealth range
    // certain tiles will increase/decrease stealth range whilst standing on
    // them
    
    private Class getClassStats(String startingClass) {
        if (this.isPlayer()) {
            return getPlayerClass(startingClass);
        }
        // TODO: Add Class method for Goblins (knight, mage, rogue, ranger)
        // TODO: Add Class method for Skeletons (soldier, mage, archer)
        // (implement Skeletons)
        Item[] startingItems = new Item[5];
        return new Class(100, 50, 10, 100, 5, 5, 3, 3, startingItems);
    }
    
    private Class getPlayerClass(String startingClass) {
        StuffFactory factory = new StuffFactory(world);
        if (startingClass.equalsIgnoreCase("knight")) {
            
            // Knight Player class
            Item[] startingItems = new Item[20];
            
            startingItems[0] = factory.newPotionOfHealth();
            startingItems[1] = factory.newPotionOfHealth();
            
            this.weapon = factory.newSword();
            this.armor = factory.newChainmail();
            
            return new Class(250, 50, 0, 100, 7, 9, 3, 2, startingItems);
        } else if (startingClass.equalsIgnoreCase("paladin")) {
            
            // Paladin Player class
            Item[] startingItems = new Item[20];
            
            startingItems[0] = factory.newMace();
            startingItems[1] = factory.newPlatemail();
            startingItems[2] = factory.newWhiteMagesSpellbook();
            startingItems[3] = factory.newPotionOfMana();
            startingItems[4] = factory.newBread();
            
            this.weapon = startingItems[0];
            this.armor = startingItems[1];
            
            return new Class(200, 50, 25, 100, 5, 6, 5, 2, startingItems);
        } else if (startingClass.equalsIgnoreCase("mage")) {
            
            // Mage Player class
            Item[] startingItems = new Item[20];
            
            startingItems[0] = factory.newStaff();
            startingItems[1] = factory.newRobes();
            startingItems[2] = factory.newRedMagesSpellbook();
            startingItems[3] = factory.newPotionOfMana();
            startingItems[4] = factory.newPotionOfMana();
            
            this.weapon = startingItems[0];
            this.armor = startingItems[1];
            
            return new Class(100, 50, 50, 200, 4, 4, 1, 1, startingItems);
        } else if (startingClass.equalsIgnoreCase("rogue")) {
            
            // Rogue Player class
            Item[] startingItems = new Item[20];
            
            startingItems[0] = factory.newDagger();
            startingItems[1] = factory.newTunic();
            startingItems[2] = factory.newRandomRing();
            startingItems[3] = factory.newPotionOfInvisibility();
            startingItems[4] = factory.newApple();
            
            this.weapon = startingItems[0];
            this.armor = startingItems[1];
            this.ring = startingItems[2];
            this.ring.setEffectVisible(true);
            
            return new Class(150, 50, 15, 100, 9, 7, 2, 4, startingItems);
        } else if (startingClass.equalsIgnoreCase("ranger")) {
            
            // Ranger Player class
            Item[] startingItems = new Item[20];
            
            startingItems[0] = factory.newBow();
            startingItems[1] = factory.newTunic();
            startingItems[2] = factory.newDagger();
            startingItems[3] = factory.newMeat();
            startingItems[4] = factory.newMeat();
            
            this.weapon = startingItems[0];
            this.armor = startingItems[1];
            
            return new Class(125, 50, 10, 100, 8, 8, 2, 5, startingItems);
        } else {
            
            // Deprived Player class
            Item[] startingItems = new Item[20];
            
            startingItems[0] = factory.newRandomRing();
            startingItems[1] = factory.newRandomCharm();
            startingItems[0] = factory.newBread();
            startingItems[1] = factory.newBread();
            startingItems[2] = factory.newBread();
            startingItems[3] = factory.newApple();
            startingItems[4] = factory.newApple();
            startingItems[5] = factory.newApple();
            
            this.ring = startingItems[0];
            this.ring.setEffectVisible(true);
            this.charm = startingItems[1];
            this.charm.setEffectVisible(true);
            
            return new Class(200, 50, 10, 100, 6, 6, 5, 5, startingItems);
        }
    }
    
    /**
     * Method responsible for creature's updates
     */
    public void update() {
        modifyFood(-1);
        regenerateHealth();
        regenerateMana();
        updateEffects();
        ai.onUpdate();
    }
    
    /**
     * Method responsible for updating effects on creatures
     */
    private void updateEffects() {
        List<Effect> done = new ArrayList<Effect>();
        
        for (Effect effect : effects) {
            effect.update(this);
            if (effect.isDone()) {
                effect.end(this);
                done.add(effect);
            }
        }
        
        effects.removeAll(done);
    }
    
    /**
     * Method used by AI to notify the player
     * 
     * @param message
     *        - the message to pass
     * @param params
     *        - the parameters to input into the message
     */
    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }
    
    /**
     * Method that returns whether or not a creature can spawn at a specific
     * location
     * 
     * @param wx
     *        - the x coordinate of the tile to search
     * @param wy
     *        - the y coordinate of the tile to search
     * @param wz
     *        - the z coordinate of the tile to search
     * @return returns true if the tile has no creature and is not a wall,
     *         otherwise false
     */
    public boolean canEnter(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
    }
    
    /**
     * Method used to set the creature's AI
     * 
     * @param ai
     *        - the AI to be used
     */
    public void setCreatureAi(CreatureAi ai) {
        this.ai = ai;
    }
    
    /**
     * Move method similar to the player's, except is dictated by AI instead of
     * user input
     * 
     * @param mx
     *        - the direction to move along the x axis
     * @param my
     *        - the direction to move along the y axis
     * @param mz
     *        - the direction to move along the z level (up or down a floor)
     */
    public void moveBy(int mx, int my, int mz) {
        if (rooted) return;
        if (mx == 0 && my == 0 && mz == 0) return;
        
        Tile tile = world.tile(x + mx, y + my, z + mz);
        
        if (mz == -1) {
            if (tile == Tile.STAIRS_DOWN) {
                doAction("walk up the stairs to level %d", z + mz + 1);
            } else {
                doAction("try to go up but are stopped by the cave ceiling");
                return;
            }
        } else if (mz == 1) {
            if (tile == Tile.STAIRS_UP) {
                doAction("walk down the stairs to level %d", z + mz + 1);
            } else {
                doAction("try to go down but are stopped by the cave floor");
                return;
            }
        }
        
        Creature other = world.creature(x + mx, y + my, z + mz);
        
        modifyFood(-1);
        
        if (other == null)
            ai.onEnter(x + mx, y + my, z + mz, tile);
        else
            meleeAttack(other);
    }
    
    /**
     * Method to allow creatures to see what "type" another creature is
     * 
     * @param wx
     *        - the x coordinate of the tile to check
     * @param wy
     *        - the y coordinate of the tile to check
     * @param wz
     *        - the z level of the tile to check
     * @return returns the creature at the given location if present
     */
    public Creature creature(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz))
            return world.creature(wx, wy, wz);
        else
            return null;
    }
    
    /**
     * Method to allow creatures to see what an item is
     * 
     * @param wx
     *        - the x coordinate of the tile to check
     * @param wy
     *        - the y coordinate of the tile to check
     * @param wz
     *        - the z level of the tile to check
     * @return returns the item at the given location if present
     */
    public Item item(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz))
            return world.item(wx, wy, wz);
        else
            return null;
    }
    
    /**
     * Method used for summoning creatures into the world (not including initial
     * world generation)
     * 
     * @param other
     *        - the creature being summoned
     */
    public void summon(Creature other) {
        world.add(other);
    }
    
    /**
     * Method for casting spells
     * 
     * @param spell
     *        - the spell being cast
     * @param x2
     *        - the x direction the spell is being cast
     * @param y2
     *        - the y direction the spell is being cast
     */
    public void castSpell(Spell spell, int x2, int y2) {
        Creature other = creature(x2, y2, z);
        
        if (spell.manaCost() > mana) {
            doAction("point and mumble but nothing happens");
            return;
        } else if (other == null) {
            doAction("point and mumble at nothing");
            return;
        }
        
        other.addEffect(spell.effect());
        modifyMana(-spell.manaCost());
    }
    
    /**
     * Handles the formula for gaining xp upon slaying a creature
     * 
     * @param other
     *        - the creature being slain for xp
     */
    public void gainXp(Creature other) {
        int amount = other.maxHp + other.attackValue() + other.defenseValue() - level * 2;
        
        if (amount > 0) modifyXp(amount);
    }
    
    /**
     * Method that notifies nearby creatures when something happens.
     * <br>
     * <br>
     * Similar to: {@linkplain #doAction(Item, String, Object...)}
     * 
     * @param message
     *        - the message you want to display to others
     * @param params
     *        - the parameters to input into the message
     */
    public void doAction(String message, Object... params) {
        for (Creature other : getCreaturesWhoSeeMe()) {
            if (other == this)
                other.notify("You " + message + ".", params);
            else if (other.canSee(x, y, z))
                other.notify(String.format("The '%s' %s.", name, makeSecondPerson(message)), params);
        }
    }
    
    /**
     * Similar to the
     * {@linkplain #doAction(String message, Object ... params)} method, this is
     * used specifically to learn a potion's name if thrown at a creature.
     * 
     * @param item
     *        - the item being used upon action
     * @param message
     *        - the message you want to display to others
     * @param params
     *        - the parameters to input into the message
     */
    public void doAction(Item item, String message, Object... params) {
        if (hp < 1) return;
        
        for (Creature other : getCreaturesWhoSeeMe()) {
            if (other == this) {
                other.notify("You " + message + ".", params);
                other.learnName(item);
            } else {
                other.notify(String.format("The %s %s.", name, makeSecondPerson(message)), params);
            }
        }
    }
    
    /**
     * Method returning a list of all creatures who see the creature running the
     * method
     * 
     * @return the list of creatures that can see this creature
     */
    private List<Creature> getCreaturesWhoSeeMe() {
        List<Creature> others = new ArrayList<Creature>();
        int r = 9;
        for (int ox = -r; ox < r + 1; ox++) {
            for (int oy = -r; oy < r + 1; oy++) {
                if (ox * ox + oy * oy > r * r) continue;
                
                Creature other = world.creature(x + ox, y + oy, z);
                
                if (other == null) continue;
                
                others.add(other);
            }
        }
        return others;
    }
    
    public List<Creature> getCreaturesInStealthRadius() {
        List<Creature> others = new ArrayList<Creature>();
        int r = stealthRadius;
        if (stealthRadius <= 0) {
            return others;
        }
        for (int ox = -r; ox < r + 1; ox++) {
            for (int oy = -r; oy < r + 1; oy++) {
                if (ox * ox + oy * oy > r * r) continue;
                
                Creature other = world.creature(x + ox, y + oy, z);
                
                if (other == null) continue;
                
                others.add(other);
            }
        }
        return others;
    }
    
    /**
     * Method used to make notification messages grammatically correct
     * 
     * @param text
     *        - the text you want to correct
     * @return returns the grammatically correct message
     */
    private String makeSecondPerson(String text) {
        String[] words = text.split(" ");
        words[0] = words[0] + "s";
        
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(" ");
            builder.append(word);
        }
        
        return builder.toString().trim();
    }
    
    /**
     * Method used to modify a creature's hp<br>
     * If the creature's hp is < 1, they are considered dead, and are removed
     * 
     * @param amount
     *        - the amount to modify hp by
     * @param causeOfDeath
     *        - String representing a cause of death related to what caused
     *        fatal damage
     */
    public void modifyHp(int amount, String causeOfDeath) {
        hp += amount;
        this.causeOfDeath = causeOfDeath;
        
        if (hp > maxHp) hp = maxHp;
        
        if (hp < 1) {
            doAction("die");
            leaveCorpse();
            world.remove(this);
        }
    }
    
    /**
     * Method to modify a creature's food level
     * 
     * @param amount
     *        - amount to modify food level by
     */
    public void modifyFood(int amount) {
        food += amount;
        if (food > maxFood) {
            maxFood = (maxFood + food) / 2;
            food = maxFood;
            notify("You can't believe your stomach can hold that much!");
            notify("Your stomach slightly expanded, but hurts you in the process.");
            modifyHp(-1, "Killed by overeating.");
        } else if (food < maxFood * 0.2) {
            modifyHp(-10, "You are starving.");
        } else if (food < 1 && isPlayer()) {
            modifyHp(-1000, "Starved to death.");
        }
    }
    
    /**
     * @param item
     *        - item to be quaffed
     */
    public void quaff(Item item) {
        doAction("quaff a " + nameOf(item));
        consume(item);
    }
    
    /**
     * @param item
     *        - item to be eaten
     */
    public void eat(Item item) {
        doAction("eat a " + nameOf(item));
        consume(item);
    }
    
    /**
     * @param item
     *        - item to be eaten/quaffed
     */
    private void consume(Item item) {
        if (item.foodValue() < 0) notify("Gross!");
        
        if (item.quaffEffect() != null && nameOf(item) == item.appearance()) learnName(item);
        addEffect(item.quaffEffect());
        
        modifyFood(item.foodValue());
        getRidOf(item);
    }
    
    /**
     * Adds the given effect to the creature calling this method
     * 
     * @param effect
     *        - effect to add
     */
    private void addEffect(Effect effect) {
        if (effect == null) return;
        
        effect.start(this);
        effects.add(effect);
    }
    
    /**
     * @return returns whether or not a creature is the player
     */
    public boolean isPlayer() {
        return glyph == '@';
    }
    
    /**
     * Method that allows creatures to leave their corpse in the world when
     * killed
     */
    private void leaveCorpse() {
        Item corpse = new Item('%', color, name + " corpse", null);
        corpse.modifyFoodValue((int) (maxHp * 2.5));
        corpse.isFood(true);
        world.addAtEmptySpace(corpse, x, y, z);
        for (Item item : inventory.getItems()) {
            if (item != null) drop(item);
        }
    }
    
    /**
     * Method that allows creatures to pickup items
     */
    public void pickup() {
        Item item = world.item(x, y, z);
        
        if (inventory.isFull() || item == null) {
            doAction("grab at the ground");
        } else {
            doAction("pickup a '%s'", nameOf(item));
            world.remove(x, y, z);
            inventory.add(item);
        }
    }
    
    /**
     * Method that allows the player to drop items
     * 
     * @param item
     *        - item to be dropped
     */
    public void drop(Item item) {
        if (world.addAtEmptySpace(item, x, y, z)) {
            doAction("drop a " + nameOf(item));
            inventory.remove(item);
            unequip(item);
        } else {
            notify("There's nowhere to drop the %s.", nameOf(item));
        }
    }
    
    /**
     * Method that allows players to unequip weapons and armor
     * 
     * @param item
     *        - item to be unequipped
     */
    public void unequip(Item item) {
        if (item == null) return;
        
        if (item.stealthModifier() != 0) stealthRadius += -(item.stealthModifier());
        
        if (item == armor && !item.isCursed()) {
            armor = null;
        } else if (item == weapon && !item.isCursed()) {
            weapon = null;
        } else if (item == ring && !item.isCursed()) {
            ring = null;
        } else if (item == charm && !item.isCursed()) {
            charm = null;
        }
    }
    
    /**
     * Method that allows players to equip weapons and armor
     * 
     * @param item
     *        - item to be equipped
     */
    public void equip(Item item) {
        if (!inventory.contains(item)) {
            if (inventory.isFull()) {
                notify("Can't equip %s since you're holding too much stuff.", nameOf(item));
                return;
            } else {
                world.remove(item);
                inventory.add(item);
            }
        }
        
        if (item.attackValue() == 0 && item.defenseValue() == 0 && !item.isRing() && !item.isCharm()) return;
        
        // If trying to equip items that are already equipped
        if (item == weapon) {
            // Handles if the weapon you're trying to unequip is cursed
            if (!item.isCursed()) {
                unequip(item);
                doAction("put away your " + nameOf(item));
            } else {
                doAction("cannot unequip %s", item.name());
            }
            return;
        } else if (item == armor) {
            // Handles if the armor you're trying to unequip is cursed
            if (!item.isCursed()) {
                unequip(item);
                doAction("take off your " + nameOf(item));
            } else {
                doAction("cannot remove %s", item.name());
            }
            return;
        } else if (item == ring) {
            // Handles if the ring you're trying to unequip is cursed
            if (!item.isCursed()) {
                unequip(item);
                doAction("take off your " + nameOf(item));
            } else {
                doAction("cannot remove %s", item.name());
            }
            return;
        } else if (item == charm) {
            // Handles if the charm you're trying to unequip is cursed
            if (!item.isCursed()) {
                unequip(item);
                doAction("remove your " + nameOf(item));
            } else {
                doAction("cannot remove %s", item.name());
            }
            return;
        }
        
        if (item.attackValue() >= item.defenseValue() && !item.isRing() && !item.isCharm()) {
            if (weapon != null) {
                if (!weapon.isCursed()) {
                    if (strength >= item.strengthRequirement()) {
                        unequip(weapon);
                        weapon = item;
                        if (this.isPlayer()) item.setEffectVisible(true);
                        if (item.stealthModifier() != 0) stealthRadius += (item.stealthModifier());
                        doAction("wield a " + nameOf(item));
                        if (item.enchantment() != null) {
                            // NOTE: I don't include the good prefixes or
                            // suffixes because they don't affect the wearer
                            for (String s : Enchantment.getBadWeaponPrefixes()) {
                                if (s.equals(item.prefix())) addEffect(item.enchantment());
                            }
                            for (String s : Enchantment.getBadWeaponSuffixes()) {
                                if (s.equals(item.suffix())) addEffect(item.enchantment());
                            }
                        }
                    } else {
                        doAction("feel too weak to wield %s", item.name());
                        return;
                    }
                } else {
                    doAction("cannot unequip %s", item.name());
                    return;
                }
            } else {
                if (strength >= item.strengthRequirement()) {
                    weapon = item;
                    if (this.isPlayer()) item.setEffectVisible(true);
                    if (item.stealthModifier() != 0) stealthRadius += (item.stealthModifier());
                    doAction("wield a " + nameOf(item));
                    if (item.enchantment() != null) {
                        // NOTE: I don't include the good prefixes or suffixes
                        // because they don't affect the wearer
                        for (String s : Enchantment.getBadWeaponPrefixes()) {
                            if (s.equals(item.prefix())) addEffect(item.enchantment());
                        }
                        for (String s : Enchantment.getBadWeaponSuffixes()) {
                            if (s.equals(item.suffix())) addEffect(item.enchantment());
                        }
                    }
                } else {
                    doAction("feel too weak to wield %s", item.name());
                    return;
                }
            }
        } else if (!item.isRing() && !item.isCharm()) {
            if (armor != null) {
                if (!armor.isCursed()) {
                    if (strength >= item.strengthRequirement()) {
                        unequip(armor);
                        armor = item;
                        if (this.isPlayer()) item.setEffectVisible(true);
                        if (item.stealthModifier() != 0) stealthRadius += (item.stealthModifier());
                        doAction("put on a " + nameOf(item));
                        if (item.enchantment() != null) {
                            for (String s : Enchantment.getGoodArmorPrefixes()) {
                                if (s.equals(item.prefix())) addEffect(item.enchantment());
                            }
                            for (String s : Enchantment.getBadArmorPrefixes()) {
                                if (s.equals(item.prefix())) addEffect(item.enchantment());
                            }
                            for (String s : Enchantment.getGoodArmorSuffixes()) {
                                if (s.equals(item.suffix())) addEffect(item.enchantment());
                            }
                            for (String s : Enchantment.getBadArmorSuffixes()) {
                                if (s.equals(item.suffix())) addEffect(item.enchantment());
                            }
                        }
                    } else {
                        doAction("feel too weak to wear %s", item.name());
                        return;
                    }
                } else {
                    doAction("cannot remove %s", item.name());
                    return;
                }
            } else {
                if (strength >= item.strengthRequirement()) {
                    armor = item;
                    if (this.isPlayer()) item.setEffectVisible(true);
                    if (item.stealthModifier() != 0) stealthRadius += (item.stealthModifier());
                    doAction("put on a " + nameOf(item));
                    if (item.enchantment() != null) {
                        for (String s : Enchantment.getGoodArmorPrefixes()) {
                            if (s.equals(item.prefix())) addEffect(item.enchantment());
                        }
                        for (String s : Enchantment.getBadArmorPrefixes()) {
                            if (s.equals(item.prefix())) addEffect(item.enchantment());
                        }
                        for (String s : Enchantment.getGoodArmorSuffixes()) {
                            if (s.equals(item.suffix())) addEffect(item.enchantment());
                        }
                        for (String s : Enchantment.getBadArmorSuffixes()) {
                            if (s.equals(item.suffix())) addEffect(item.enchantment());
                        }
                    }
                } else {
                    doAction("feel too weak to wear %s", item.name());
                    return;
                }
            }
        }
        
        if (item.isRing()) {
            if (ring != null) {
                if (!ring.isCursed()) {
                    unequip(ring);
                    ring = item;
                    if (this.isPlayer()) item.setEffectVisible(true);
                    if (item.stealthModifier() != 0) stealthRadius += (item.stealthModifier());
                    doAction("put on a " + nameOf(item));
                    if (item.enchantment() != null) {
                        for (String s : Enchantment.getGoodRingSuffixes()) {
                            if (s.equals(item.suffix())) addEffect(item.enchantment());
                        }
                        for (String s : Enchantment.getBadRingSuffixes()) {
                            if (s.equals(item.suffix())) addEffect(item.enchantment());
                        }
                    }
                } else {
                    doAction("cannot remove %s", item.name());
                    return;
                }
            } else {
                ring = item;
                if (this.isPlayer()) item.setEffectVisible(true);
                if (item.stealthModifier() != 0) stealthRadius += (item.stealthModifier());
                doAction("put on a " + nameOf(item));
                if (item.enchantment() != null) {
                    for (String s : Enchantment.getGoodRingSuffixes()) {
                        if (s.equals(item.suffix())) addEffect(item.enchantment());
                    }
                    for (String s : Enchantment.getBadRingSuffixes()) {
                        if (s.equals(item.suffix())) addEffect(item.enchantment());
                    }
                }
            }
        } else if (item.isCharm()) {
            if (charm != null) {
                if (!charm.isCursed()) {
                    unequip(charm);
                    charm = item;
                    if (this.isPlayer()) item.setEffectVisible(true);
                    if (item.stealthModifier() != 0) stealthRadius += (item.stealthModifier());
                    doAction("wear a " + nameOf(item));
                    if (item.enchantment() != null) {
                        for (String s : Enchantment.getGoodCharmSuffixes()) {
                            if (s.equals(item.suffix())) addEffect(item.enchantment());
                        }
                        for (String s : Enchantment.getBadCharmSuffixes()) {
                            if (s.equals(item.suffix())) addEffect(item.enchantment());
                        }
                    }
                } else {
                    doAction("cannot remove %s", item.name());
                    return;
                }
            } else {
                charm = item;
                if (this.isPlayer()) item.setEffectVisible(true);
                if (item.stealthModifier() != 0) stealthRadius += (item.stealthModifier());
                doAction("wear a " + nameOf(item));
                if (item.enchantment() != null) {
                    for (String s : Enchantment.getGoodCharmSuffixes()) {
                        if (s.equals(item.suffix())) addEffect(item.enchantment());
                    }
                    for (String s : Enchantment.getBadCharmSuffixes()) {
                        if (s.equals(item.suffix())) addEffect(item.enchantment());
                    }
                }
            }
        }
        
        if (item.isCursed()) {
            if (this.isPlayer()) {
                item.setCursedVisible(true);
                notify("%s is cursed!", item.name());
            }
        }
    }
    
    /**
     * Method that allows a creature to throw an item
     * 
     * @param item
     *        - item to be thrown
     * @param wx
     *        - the x direction for the item to be thrown in
     * @param wy
     *        - the y direction for the item to be thrown in
     * @param wz
     *        - the z level on which the item is being thrown
     */
    public void throwItem(Item item, int wx, int wy, int wz) {
        Point end = new Point(x, y, 0);
        
        for (Point p : new Line(x, y, wx, wy)) {
            if (!realTile(p.x, p.y, z).isGround()) break;
            end = p;
        }
        
        wx = end.x;
        wy = end.y;
        
        Creature c = creature(wx, wy, wz);
        
        if (c != null)
            throwAttack(item, c);
        else
            doAction("throw a %s", nameOf(item));
        
        if (item.quaffEffect() != null && c != null) {
            getRidOf(item);
            c.addEffect(item.quaffEffect());
            learnName(item);
        } else
            putAt(item, wx, wy, wz);
    }
    
    /**
     * Method that modifies how quickly a creature regens hp
     * 
     * @param amount
     *        - the amount to modify how fast a creature regens hp
     */
    public void modifyRegenHpPer1000(int amount) {
        regenHpPer1000 += amount;
    }
    
    /**
     * Method that modifies how quickly a creature regens mana
     * 
     * @param amount
     *        - the amount to modify how fast a creature regens mana
     */
    public void modifyRegenManaPer1000(int amount) {
        regenManaPer1000 += amount;
    }
    
    /**
     * Method responsible for creature health regen
     */
    private void regenerateHealth() {
        if (food > maxFood * 0.2) {
            regenHpCooldown -= regenHpPer1000;
            if (armor != null) regenHpCooldown -= armor.healthRegenBoost();
            if (regenHpCooldown <= 0) {
                modifyHp(1, null);
                modifyFood(-1);
                regenHpCooldown += 1000;
            }
        }
    }
    
    /**
     * Method responsible for creature mana regen
     */
    private void regenerateMana() {
        regenManaCooldown -= regenManaPer1000;
        if (armor != null) regenManaCooldown -= armor.manaRegenBoost();
        if (regenManaCooldown <= 0) {
            if (mana < maxMana) {
                modifyMana(1);
                modifyFood(-1);
            }
            regenManaCooldown += 1000;
        }
    }
    
    /**
     * Method allowing creatures (and the player) to "dig" through walls
     * 
     * @param wx
     *        - the desired x direction to dig
     * @param wy
     *        - the desired y direction to dig
     * @param wz
     *        - the z level on which you want to dig
     */
    public void dig(int wx, int wy, int wz) {
        modifyFood(-10);
        world.dig(wx, wy, wz);
        doAction("dig");
    }
    
    /**
     * Method called for melee attacks
     * <br>
     * <br>
     * Calls: {@linkplain #commonAttack(Creature, int, String, Object...)}
     * 
     * @param other
     *        - creature being attacked
     */
    public void meleeAttack(Creature other) {
        
        if (other.invisible) {
            this.doAction("bump into %s", other.name());
            other.invisible(false);
            return;
        }
        
        // If attacking from invisibility, double damage (melee only)
        if (this.invisible) modifyAttackValue(attackValue());
        
        // Prevents monsters from attacking each other
        // Makes the game more difficult
        if (!this.isPlayer() && !other.isPlayer()) return;
        
        if (weapon == null) {
            commonAttack(other, attackValue(), "hit the %s for %d damage", other.name);
        } else {
            
            if (weapon.isSpear()) {
                commonAttack(other, attackValue(), "pierce the %s for %d damage", other.name);
                if (weapon.prefix() != null) {
                    for (String s : Enchantment.getGoodWeaponPrefixes()) {
                        if (s.equals(weapon.prefix())) other.addEffect(weapon.enchantment());
                    }
                }
                if (weapon.suffix() != null) {
                    for (String s : Enchantment.getGoodWeaponSuffixes()) {
                        if (s.equals(weapon.suffix())) other.addEffect(weapon.enchantment());
                    }
                }
                int dx = other.x - this.x;
                int dy = other.y - this.y;
                Creature behind = creature(this.x + (2 * dx), this.y + (2 * dy), this.z);
                if (behind != null) {
                    commonAttack(behind, (attackValue() - (attackValue() / 4)), "pierce the %s for %d damage",
                            behind.name);
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) behind.addEffect(weapon.enchantment());
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) behind.addEffect(weapon.enchantment());
                        }
                    }
                }
            }
            
            if (weapon.isAxe()) {
                if (weapon.isSpear()) return;
                commonAttack(other, attackValue(), "cleave the %s for %d damage", other.name);
                
                int dx = other.x - this.x;
                int dy = other.y - this.y;
                
                if (dx == -1 && dy == -1) {
                    Creature left = creature(this.x - 1, this.y, this.z);
                    if (left != null) commonAttack(left, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", left.name);
                    
                    Creature right = creature(this.x, this.y + 1, this.z);
                    if (right != null) commonAttack(right, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", right.name);
                    
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                } else if (dx == 0 && dy == -1) {
                    Creature left = creature(this.x - 1, this.y - 1, this.z);
                    if (left != null) commonAttack(left, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", left.name);
                    
                    Creature right = creature(this.x + 1, this.y - 1, this.z);
                    if (right != null) commonAttack(right, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", right.name);
                    
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                } else if (dx == 1 && dy == -1) {
                    Creature left = creature(this.x, this.y - 1, this.z);
                    if (left != null) commonAttack(left, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", left.name);
                    
                    Creature right = creature(this.x + 1, this.y, this.z);
                    if (right != null) commonAttack(right, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", right.name);
                    
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                } else if (dx == -1 && dy == 0) {
                    Creature left = creature(this.x - 1, this.y - 1, this.z);
                    if (left != null) commonAttack(left, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", left.name);
                    
                    Creature right = creature(this.x - 1, this.y + 1, this.z);
                    if (right != null) commonAttack(right, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", right.name);
                    
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                } else if (dx == 1 && dy == 0) {
                    Creature left = creature(this.x + 1, this.y - 1, this.z);
                    if (left != null) commonAttack(left, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", left.name);
                    
                    Creature right = creature(this.x + 1, this.y + 1, this.z);
                    if (right != null) commonAttack(right, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", right.name);
                    
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                } else if (dx == -1 && dy == 1) {
                    Creature left = creature(this.x - 1, this.y, this.z);
                    if (left != null) commonAttack(left, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", left.name);
                    
                    Creature right = creature(this.x, this.y + 1, this.z);
                    if (right != null) commonAttack(right, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", right.name);
                    
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                } else if (dx == 0 && dy == 1) {
                    Creature left = creature(this.x - 1, this.y + 1, this.z);
                    if (left != null) commonAttack(left, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", left.name);
                    
                    Creature right = creature(this.x + 1, this.y + 1, this.z);
                    if (right != null) commonAttack(right, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", right.name);
                    
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                } else if (dx == 1 && dy == 1) {
                    Creature left = creature(this.x, this.y + 1, this.z);
                    if (left != null) commonAttack(left, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", left.name);
                    
                    Creature right = creature(this.x + 1, this.y, this.z);
                    if (right != null) commonAttack(right, attackValue() - (attackValue() / 4),
                            "cleave the %s for %d damage", right.name);
                    
                    if (weapon.prefix() != null) {
                        for (String s : Enchantment.getGoodWeaponPrefixes()) {
                            if (s.equals(weapon.prefix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                    if (weapon.suffix() != null) {
                        for (String s : Enchantment.getGoodWeaponSuffixes()) {
                            if (s.equals(weapon.suffix())) {
                                if (left != null) left.addEffect(weapon.enchantment());
                                if (right != null) right.addEffect(weapon.enchantment());
                            }
                        }
                    }
                }
            }
            
            if (weapon.isMace()) {
                other.defenseValue -= (weapon.armorPen() / 2);
                commonAttack(other, attackValue() + strength, "hammer the %s for %d damage", other.name);
                other.defenseValue += (weapon.armorPen() / 2);
                
                if (weapon.prefix() != null) {
                    for (String s : Enchantment.getGoodWeaponPrefixes()) {
                        if (s.equals(weapon.prefix())) other.addEffect(weapon.enchantment());
                    }
                }
                if (weapon.suffix() != null) {
                    for (String s : Enchantment.getGoodWeaponSuffixes()) {
                        if (s.equals(weapon.suffix())) other.addEffect(weapon.enchantment());
                    }
                }
            }
            
            if (weapon.isClub()) {
                commonAttack(other, attackValue() + (strength * 2), "club the %s for %d damage", other.name);
                
                if (weapon.prefix() != null) {
                    for (String s : Enchantment.getGoodWeaponPrefixes()) {
                        if (s.equals(weapon.prefix())) other.addEffect(weapon.enchantment());
                    }
                }
                if (weapon.suffix() != null) {
                    for (String s : Enchantment.getGoodWeaponSuffixes()) {
                        if (s.equals(weapon.suffix())) other.addEffect(weapon.enchantment());
                    }
                }
            }
            
            if (weapon.isDagger()) {
                int amount = (int) (Math.random() * attackValue()) + dexterity + 1;
                commonAttack(other, amount, "cut the %s for %d damage", other.name);
                
                if (weapon.prefix() != null) {
                    for (String s : Enchantment.getGoodWeaponPrefixes()) {
                        if (s.equals(weapon.prefix())) other.addEffect(weapon.enchantment());
                    }
                }
                if (weapon.suffix() != null) {
                    for (String s : Enchantment.getGoodWeaponSuffixes()) {
                        if (s.equals(weapon.suffix())) other.addEffect(weapon.enchantment());
                    }
                }
            }
            
            if (weapon.isSword()) {
                commonAttack(other, attackValue(), "attack the %s for %d damage", other.name);
                
                if (weapon.prefix() != null) {
                    for (String s : Enchantment.getGoodWeaponPrefixes()) {
                        if (s.equals(weapon.prefix())) other.addEffect(weapon.enchantment());
                    }
                }
                if (weapon.suffix() != null) {
                    for (String s : Enchantment.getGoodWeaponSuffixes()) {
                        if (s.equals(weapon.suffix())) other.addEffect(weapon.enchantment());
                    }
                }
            }
        }
        if (this.invisible) modifyAttackValue(-attackValue());
        invisible = false;
    }
    
    /**
     * Method called for throwing attacks
     * <br>
     * <br>
     * Calls: {@linkplain #commonAttack(Creature, int, String, Object...)}
     * 
     * @param item
     *        - item being thrown
     * @param other
     *        - creature being attacked
     */
    private void throwAttack(Item item, Creature other) {
        int amount = item.thrownAttackValue();
        amount = (int) (Math.random() * amount) + dexterity + 1;
        commonAttack(other, (attackValue / 2) + amount, "throw a %s at the %s for %d damage", nameOf(item), other.name);
        other.addEffect(item.quaffEffect());
        if (item.appearance() != null && nameOf(item) != item.appearance()) learnName(item);
    }
    
    /**
     * Method called for ranged attacks
     * <br>
     * <br>
     * Calls: {@linkplain #commonAttack(Creature, int, String, Object...)}
     * 
     * @param other
     *        - creature being attacked
     */
    public void rangedWeaponAttack(Creature other) {
        int amount = weapon.rangedAttackValue();
        amount = (int) (Math.random() * amount) + dexterity + 1;
        
        if (this.invisible) amount *= 2;

        commonAttack(other, (attackValue / 2) + amount, "fire a %s at the %s for %d damage", nameOf(weapon),
                other.name);
        
        invisible = false;
    }
    
    /**
     * Method for handling melee, thrown, and ranged attacks
     * 
     * @param other
     *        - the creature being attacked
     * @param attack
     *        - the damage being dealt
     * @param action
     *        - the notification action to display
     * @param params
     *        - the parameters to pass into the action message
     */
    private void commonAttack(Creature other, int attack, String action, Object... params) {
        modifyFood(-2);
        
        int amount = Math.max(0, attack - other.defenseValue());
        
        amount = (int) (Math.random() * amount) + 1;
        
        Object[] params2 = new Object[params.length + 1];
        for (int i = 0; i < params.length; i++) {
            params2[i] = params[i];
        }
        params2[params2.length - 1] = amount;
        
        doAction(action, params2);
        
        other.modifyHp(-amount, "Killed by a " + name);
        
        if (other.hp < 1) gainXp(other);
    }
    
    /**
     * Method for getting rid of items
     * <br>
     * (i.e. getting rid of a thrown potion when it hits a creature)
     * 
     * @param item
     *        - item to get rid of
     */
    private void getRidOf(Item item) {
        inventory.remove(item);
        unequip(item);
    }
    
    /**
     * Method for placing items
     * <br>
     * (i.e. when a rock is thrown, and it lands)
     * 
     * @param item
     *        - item being thrown
     * @param wx
     *        - x direction of where item is being placed
     * @param wy
     *        - y direction of where item is being placed
     * @param wz
     *        - z level of where the item is being placed
     */
    private void putAt(Item item, int wx, int wy, int wz) {
        inventory.remove(item);
        unequip(item);
        world.addAtEmptySpace(item, wx, wy, wz);
    }
    
    /**
     * Method that determines whether or not a creature can detect other
     * creatures via a "sixth sense"
     * 
     * @param amount
     *        - used to alter the state of a creature's "sixth sense"
     */
    public void modifyDetectCreatures(int amount) {
        detectCreatures += amount;
    }
    
    public int detectCreatures() {
        return detectCreatures;
    }
    
    /**
     * Method to check whether or not a creature can see a specific
     * tile/creature
     * 
     * @param wx
     *        - x coordinate of tile to check
     * @param wy
     *        - y coordinate of tile to check
     * @param wz
     *        - z level to check
     * @return returns true if the creature can see the tile, or if the creature
     *         has a "sixth sense" and there is a creature present at said
     *         location
     */
    public boolean canSee(int wx, int wy, int wz) {
        return (!invisible && detectCreatures > 0 && world.creature(wx, wy, wz) != null || ai.canSee(wx, wy, wz));
    }
    
    /**
     * @return returns the creature's inventory
     */
    public Inventory inventory() {
        return inventory;
    }
    
    /**
     * Method that returns the actual real tile at a given location
     * 
     * @param wx
     *        - x coordinate of tile to check
     * @param wy
     *        - y coordinate of tile to check
     * @param wz
     *        - z level of tile to check
     * @return returns the actual tile
     */
    public Tile realTile(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz);
    }
    
    /**
     * Method that returns the tile that the creature can see, or has remembered
     * seeing
     * 
     * @param wx
     *        - x coordinate of tile to check
     * @param wy
     *        - y coordinate of tile to check
     * @param wz
     *        - z level of tile to check
     * @return if the creature can see the tile, return the actual tile, else
     *         return the tile the creature last remembered seeing
     */
    public Tile tile(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz))
            return world.tile(wx, wy, wz);
        else
            return ai.rememberedTile(wx, wy, wz);
    }
    
    /**
     * @return returns the creature's name
     */
    public String name() {
        return name;
    }
    
    /**
     * @return returns the creature's glyph
     */
    public char glyph() {
        return glyph;
    }
    
    /**
     * @return returns the creature's color
     */
    public Color color() {
        return color;
    }
    
    /**
     * @return returns the creature's max hp
     */
    public int maxHp() {
        if (armor != null) return maxHp + armor.healthBoost();
        return maxHp;
    }
    
    /**
     * @return returns the creature's hp
     */
    public int hp() {
        return hp;
    }
    
    /**
     * @return returns the creature's max mana
     */
    public int maxMana() {
        if (armor != null) return maxMana + armor.manaBoost();
        return maxMana;
    }
    
    /**
     * @return returns the creature's mana
     */
    public int mana() {
        return mana;
    }
    
    /**
     * Method to modify a creature's mana
     * 
     * @param amount
     *        - amount by which to modify a creature's mana
     */
    public void modifyMana(int amount) {
        mana = Math.max(0, Math.min(mana + amount, maxMana));
    }
    
    /**
     * Method to modify a creature's max hp
     * 
     * @param amount
     *        - amount by which to modify a creature's max hp
     */
    public void modifyMaxHp(int amount) {
        maxHp += amount;
    }
    
    /**
     * Method to modify a creature's max mana
     * 
     * @param amount
     *        - amount by which to modify a creature's max mana
     */
    public void modifyMaxMana(int amount) {
        maxMana += amount;
    }
    
    /**
     * @return returns the creature's attack value
     */
    public int attackValue() {
        return attackValue + (weapon == null ? 0 : weapon.attackValue()) + (armor == null ? 0 : armor.attackValue());
    }
    
    /**
     * Method to modify a creature's attack value
     * 
     * @param amount
     *        - amount by which to modify a creature's attack value
     */
    public void modifyAttackValue(int amount) {
        attackValue += amount;
    }
    
    /**
     * @return returns the creature's defense value
     */
    public int defenseValue() {
        return defenseValue + (weapon == null ? 0 : weapon.defenseValue()) + (armor == null ? 0 : armor.defenseValue());
    }
    
    /**
     * Method to modify a creature's defense value
     * 
     * @param amount
     *        - amount by which to modify a creature's defense value
     */
    public void modifyDefenseValue(int amount) {
        defenseValue += amount;
    }
    
    /**
     * @return returns the creature's vision radius
     */
    public int visionRadius() {
        return visionRadius;
    }
    
    /**
     * Method to modify a creature's vision radius
     * 
     * @param amount
     *        - the amount by which to modify a creature's vision radius
     */
    public void modifyVisionRadius(int amount) {
        visionRadius += amount;
    }
    
    public void modifyDexterity(int amount) {
        dexterity += amount;
    }
    
    /**
     * @return returns the creature's max food
     */
    public int maxFood() {
        return maxFood;
    }
    
    /**
     * @return returns the creature's hunger
     */
    public int food() {
        return food;
    }
    
    /**
     * @return returns the creature's weapon
     */
    public Item weapon() {
        return weapon;
    }
    
    /**
     * @return returns the creature's armor
     */
    public Item armor() {
        return armor;
    }
    
    public Item ring() {
        return ring;
    }
    
    public Item charm() {
        return charm;
    }
    
    public boolean rooted() {
        return rooted;
    }
    
    public void rooted(boolean value) {
        rooted = value;
    }
    
    public boolean invisible() {
        return invisible;
    }
    
    public void invisible(boolean value) {
        invisible = value;
    }
    
    public int stealthRadius() {
        return stealthRadius;
    }
    
    public void modifyStealthRadius(int amount) {
        stealthRadius += amount;
    }
    
    /**
     * @return returns the creature's xp
     */
    public int xp() {
        return xp;
    }
    
    public int regenHpPer1000(){
        return regenHpPer1000;
    }
    
    public int regenManaPer1000(){
        return regenManaPer1000;
    }
    
    public void aquatic(boolean value){
        aquatic = value;
    }
    
    public boolean aquatic(){
        return aquatic;
    }
    
    public void canSwim(boolean value){
        canSwim = value;
    }
    
    public boolean canSwim(){
        return canSwim;
    }
    
    public void canFly(boolean value){
        canFly = value;
    }
    
    public boolean canFly(){
        return canFly;
    }
    
    public int strength() {
        return strength;
    }
    
    public int dexterity() {
        return dexterity;
    }
    
    public void modifyStrength(int amount) {
        strength += amount;
    }
    
    public boolean hasSeenPlayer() {
        return hasSeenPlayer;
    }
    
    public void hasSeenPlayer(boolean value) {
        hasSeenPlayer = value;
    }
    
    public int lastSeenPlayer() {
        return lastSeenPlayer;
    }
    
    public void setLastSeenPlayer(int amount) {
        lastSeenPlayer = amount;
    }
    
    /**
     * Method that returns the name of an item
     * 
     * @param item
     *        - item to return name of
     * @return returns the item's appearance name, or if the creature has
     *         learned the actual name, return the actual name
     */
    public String nameOf(Item item) {
        return ai.getName(item);
    }
    
    /**
     * Method to allow creatures to learn the real name of items
     * 
     * @param item
     *        - item to learn the real name of
     */
    public void learnName(Item item) {
        if (this.isPlayer()) notify("The " + item.appearance() + " is a " + item.name() + "!");
        ai.setName(item, item.name());
    }
    
    /**
     * @return returns the creature's (the player's mainly) cause of death
     */
    public String causeOfDeath() {
        return causeOfDeath;
    }
    
    /**
     * Method to modify a creature's xp
     * 
     * @param amount
     *        - the amount by which to modify a creature's xp
     */
    public void modifyXp(int amount) {
        xp += amount;
        
        notify("You %s %d xp.", amount < 0 ? "lose" : "gain", amount);
        
        while (xp > (int) (Math.pow(level, 1.5) * 20)) {
            level++;
            doAction("advance to level %d", level);
            ai.onGainLevel();
            modifyHp(level * 2, null);
        }
    }
    
    /**
     * @return returns the creature's level
     */
    public int level() {
        return level;
    }
    
    /**
     * @return returns the list of effects currently active on the given
     *         creature
     */
    public List<Effect> effects() {
        return effects;
    }
    
    /**
     * @return returns details about the creature
     */
    public String details() {
        return String.format("     level:%d     attack:%d     defense:%d     hp:%d", level, attackValue(),
                defenseValue(), hp());
    }
    
}
