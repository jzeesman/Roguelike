package rogue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import asciiPanel.AsciiPanel;
import rogue.screens.ClassScreen;
import rogue.screens.EnchantScreen;
import rogue.screens.PlayScreen;

/**
 * StuffFactory class is responsible for spawning things from creatures, to
 * items (weapons, armors, food, potions, and spellbooks)
 */
public class StuffFactory {
    
    // ===============================================================================
    
    // TODO: Create new weapons
    
    // Wands - have spell charges
    
    // ===============================================================================
    
    // TODO: Create new armor
    // Armor has a strength requirement?
    // "Heavier" armor causes more hunger per turn?
    
    // Robes - boost maxMana++, manaRegen++, magicPower++?
    // --- create magicPower stat?
    // Spiked armor - thorns?
    
    // ===============================================================================
    
    // TODO: Create new ring enchantments
    
    // ===============================================================================
    
    // TODO: Create new charm enchantments
    
    // ===============================================================================
    
    // TODO: Create new items
    
    // Scrolls - potions for spells
    // --- 1 time use, unidentified
    
    // Scroll Effects
    // --- Discord - enemy friendly fire
    // --- Magic Mapping - reveal map in a large radius
    // --- Negation - removes & prevents "magic"
    // ------ i.e. confused, invisible, levitating, etc.
    // --- Protect Armor - immune to acid degredation & future curses
    // --- Protect Weapon - immune to acid degredation & future curses
    // --- Recharging - increases wand charges (implement wands)
    // --- Sanctuary - creates a 3x3 of runes protecting you from melee attacks
    // ------ unaffected by negation. are they temporary or permanent?
    
    // Spells from spellbooks, but one-time usage
    
    // ===============================================================================
    
    // TODO: Create new spells/spellbooks
    
    // ===============================================================================
    
    // TODO: Create new enemies
    
    // Skeletons
    // --- Soldiers (medium dmg, medium hp, start with armor & sword, melee only)
    // --- Archers (weak, low hp, start with a bow, ranged only?)
    // --- Mages (weak, low hp, start with a spellbook, magic only, until out of mana?)
    
    // ===============================================================================
    
    // TODO: Create new potions
    
    // Potion of Hallucination
    // --- gas cloud or single target?
    // --- implement hallucination effect
    // Potion of Incineration
    // --- AoE or single target?
    // Potion of Detect Magic
    // Potion Fire Immunity
    // --- implement fire
    // Potion of Levitation
    // --- implement holes/rivers
    // Potion of Confusion (swap movement controls?)
    // --- gas cloud or single target?
    // Potion of Descent
    // --- creates large hole
    // --- implement ability to fall through holes
    
    // ===============================================================================
    
    // TODO: Create new effects
    
    // Hallucination - glyphs shuffle each turn?
    // Fire - fire spread
    // --- implement fire/fire spread
    // Invisibility - temporarily disables mob tracking?
    // --- doesn't work on mobs with 6th sense
    // Detect Magic - soft-id inventory/world items & 6th sense world items
    // --- blue = benefit, red = cursed
    // Fire Immunity
    // Levitation - float, immune to falling, etc.
    // Confusion - movement keys are scrambled/AI hunt() set to wander()
    
    // ===============================================================================
    
    /** Map connecting generic potion names to colors **/
    private Map<String, Color> potionColors;
    /** List of the generic potion names **/
    private List<String> potionAppearances;
    private List<String> scrollAppearances;
    /** The world in which to spawn items/creatures **/
    private World world;
    
    /**
     * The general constructor for the StuffFactory class
     * 
     * @param world
     *        - the world in which the StuffFactory will spawn creatures
     */
    public StuffFactory(World world) {
        this.world = world;
        
        setUpPotionAppearances();
        setUpScrollAppearances();
    }
    
    /**
     * Method for setting up the randomly shuffled generic potion names
     */
    private void setUpPotionAppearances() {
        potionColors = new HashMap<String, Color>();
        potionColors.put("red potion", AsciiPanel.brightRed);
        potionColors.put("yellow potion", AsciiPanel.brightYellow);
        potionColors.put("green potion", AsciiPanel.brightGreen);
        potionColors.put("cyan potion", AsciiPanel.brightCyan);
        potionColors.put("blue potion", AsciiPanel.brightBlue);
        potionColors.put("magenta potion", AsciiPanel.brightMagenta);
        potionColors.put("dark potion", AsciiPanel.brightBlack);
        potionColors.put("grey potion", AsciiPanel.white);
        potionColors.put("light potion", AsciiPanel.brightWhite);
        
        potionAppearances = new ArrayList<String>(potionColors.keySet());
        Collections.shuffle(potionAppearances);
    }
    
    private void setUpScrollAppearances() {
        scrollAppearances = new ArrayList<String>();
        String letters = "abcdefghijklmnopqrstuvwxyz";
        
        for (int i = 0; i < 10; i++) {
            String word = "";
            for (int j = 0; j < 7; j++) {
                Random r = new Random();
                int q = r.nextInt(26);
                word += letters.substring(q, q + 1);
            }
            String text = "scroll of " + word;
            scrollAppearances.add(text);
        }
        
        Collections.shuffle(scrollAppearances);
    }
    
    // ===============================================================================
    // Creatures
    // ===============================================================================
    
    /**
     * Method for creating the player
     * 
     * @param messages
     *        - the list of messages that will be sent to the player throughout
     *        the game
     * @param fov
     *        - the FieldOfView object linked to the player, which helps figure
     *        out the player's fov
     * @return returns the player
     */
    public Creature newPlayer(List<String> messages, FieldOfView fov) {
        Creature player = new Creature(world, "you", '@', AsciiPanel.brightWhite, ClassScreen.className());
        world.addAtEmptyLocation(player, 0);
        new PlayerAi(player, messages, fov);
        
        for(Item i : player.inventory().getItems()){
            if(i != null && (i.isPotion() || i.isScroll())){
                player.learnName(i);
            }
        }
        
        return player;
    }
    
    /**
     * Method for creating a new fungus creature
     * 
     * @param depth
     *        - the depth at which to create the fungus
     * @return returns the newly created fungus creature
     */
    public Creature newFungus(int depth) {
        Creature fungus = new Creature(world, "fungus", 'f', AsciiPanel.green, 10, 0, 0, 0, 0, 0);
        world.addAtEmptyLocation(fungus, depth);
        new FungusAi(fungus, this);
        return fungus;
    }
    
    /**
     * Method for creating a new bat creature
     * 
     * @param depth
     *        - the depth at which to create the bat
     * @return returns the newly created bat creature
     */
    public Creature newBat(int depth) {
        Creature bat = new Creature(world, "bat", 'b', AsciiPanel.brightBlue, 15, 0, 5, 0, 0, 0);
        world.addAtEmptyLocation(bat, depth);
        new BatAi(bat);
        return bat;
    }
    
    /**
     * Method for creating a new zombie creature
     * 
     * @param depth
     *        - the depth at which to create the zombie
     * @param player
     *        - the player which the zombies will aggro
     * @return returns the newly created zombie creature
     */
    public Creature newZombie(int depth, Creature player) {
        Creature zombie = new Creature(world, "zombie", 'z', AsciiPanel.white, 50, 0, 8, 10, 2, 1);
        world.addAtEmptyLocation(zombie, depth);
        new ZombieAi(zombie, player);
        return zombie;
    }
    
    /**
     * Method for creating a new goblin creature
     * 
     * @param depth
     *        - the depth at which to create the goblin
     * @param player
     *        - the player which the goblin will aggro
     * @return returns the newly created goblin creature
     */
    public Creature newGoblin(int depth, Creature player) {
        // TODO: Add randomizer for goblin class
        // TODO: Create new goblin using class randomizer
        Creature goblin = new Creature(world, "goblin", 'g', AsciiPanel.brightGreen, 66, 15, 12, 4, 3, 4);
        new GoblinAi(goblin, player);
        goblin.equip(newRandomWeapon(depth));
        goblin.equip(newRandomArmor(depth));
        world.addAtEmptyLocation(goblin, depth);
        return goblin;
    }
    
    public Creature newWeepingAngel(int depth, Creature player) {
        Creature weepingAngel = new Creature(world, "weeping angel", 'Æ', AsciiPanel.brightWhite, 100, 0, 0, 999, 0, 0);
        new WeepingAngelAi(weepingAngel, player);
        world.addAtEmptyLocation(weepingAngel, depth);
        return weepingAngel;
    }
    
    public Creature newTroll(int depth, Creature player) {
        Creature troll = new Creature(world, "troll", 't', AsciiPanel.brightGreen, 100, 0, 15, 10, 8, 1);
        new TrollAi(troll, player);
        troll.modifyRegenHpPer1000(200);
        world.addAtEmptyLocation(troll, depth);
        return troll;
    }
    
    public Creature newEel(int depth, Creature player){
        Creature eel = new Creature(world, "eel", 'e', AsciiPanel.green, 150, 0, 20, 15, 6, 0);
        new EelAi(eel, player);
        world.addAtEmptyLocation(eel, depth);
        return eel;
    }
    
    // ===============================================================================
    // Items :: General
    // ===============================================================================
    
    /**
     * Method for creating the victory item
     * 
     * @param depth
     *        - the depth at which to spawn said item
     * @return returns a newly generated victory item: "king's soul"
     */
    public Item newVictoryItem(int depth) {
        Item item = new Item('*', AsciiPanel.brightWhite, "king's soul", null);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    /**
     * Method for creating a new "rock" item
     * 
     * @param depth
     *        - the depth at which to spawn said item
     * @return returns the newly created "rock" item
     */
    public Item newRock(int depth) {
        Item rock = new Item(',', AsciiPanel.yellow, "rock", null);
        rock.modifyThrownAttackValue(3);
        world.addAtEmptyLocation(rock, depth);
        return rock;
    }
    
    // ===============================================================================
    // Items :: Food
    // ===============================================================================
    
    public Item newApple() {
        Item apple = new Item(';', AsciiPanel.red, "apple", null);
        apple.modifyFoodValue(100);
        apple.isFood(true);
        return apple;
    }
    
    /**
     * Method for creating a new "apple" item
     * 
     * @param depth
     *        - the depth at which to spawn said item
     * @return returns the newly created "apple" item
     */
    public Item newApple(int depth) {
        Item apple = new Item(';', AsciiPanel.red, "apple", null);
        apple.modifyFoodValue(100);
        apple.isFood(true);
        world.addAtEmptyLocation(apple, depth);
        return apple;
    }
    
    public Item newBread() {
        Item bread = new Item(';', AsciiPanel.yellow, "bread", null);
        bread.modifyFoodValue(400);
        bread.isFood(true);
        return bread;
    }
    
    /**
     * Method for creating a new "bread" item
     * 
     * @param depth
     *        - the depth at which to spawn said item
     * @return returns the newly created "bread" item
     */
    public Item newBread(int depth) {
        Item bread = new Item(';', AsciiPanel.yellow, "bread", null);
        bread.modifyFoodValue(400);
        bread.isFood(true);
        world.addAtEmptyLocation(bread, depth);
        return bread;
    }
    
    public Item newMeat() {
        Item meat = new Item(';', AsciiPanel.magenta, "meat", null);
        meat.modifyFoodValue(200);
        meat.isFood(true);
        return meat;
    }
    
    /**
     * Method for creating a new "meat" item
     * 
     * @param depth
     *        - the depth at which to spawn said item
     * @return returns the newly created "meat" item
     */
    public Item newMeat(int depth) {
        Item meat = new Item(';', AsciiPanel.magenta, "meat", null);
        meat.modifyFoodValue(200);
        meat.isFood(true);
        world.addAtEmptyLocation(meat, depth);
        return meat;
    }
    
    // ===============================================================================
    // Items :: Weapons
    // ===============================================================================
    
    public Item newDagger() {
        Item item = new Item(')', AsciiPanel.white, "rusty dagger", null);
        item.modifyAttackValue(4);
        item.modifyThrownAttackValue(4);
        item.modifyStrengthRequirement(2);
        item.isDagger(true);
        return item;
    }
    
    /**
     * Method for creating a new "dagger" item
     * 
     * @param depth
     *        - the depth at which to spawn said weapon
     * @return returns the newly created "dagger" item
     */
    public Item newDagger(int depth) {
        Item item = new Item(')', AsciiPanel.white, "dagger", null);
        item.modifyAttackValue(4);
        item.modifyThrownAttackValue(4);
        item.modifyStrengthRequirement(2);
        item.isDagger(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newSword(){
        Item item = new Item(')', AsciiPanel.brightWhite, "sword", null);
        item.modifyAttackValue(10);
        item.modifyThrownAttackValue(3);
        item.modifyStrengthRequirement(3);
        item.isSword(true);
        return item;
    }
    
    /**
     * Method for creating a new "sword" item
     * 
     * @param depth
     *        - the depth at which to spawn said weapon
     * @return returns the newly created "sword" item
     */
    public Item newSword(int depth) {
        Item item = new Item(')', AsciiPanel.brightWhite, "sword", null);
        item.modifyAttackValue(10);
        item.modifyThrownAttackValue(3);
        item.modifyStrengthRequirement(3);
        item.isSword(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newStaff() {
        Item item = new Item(')', AsciiPanel.yellow, "old staff", null);
        item.modifyAttackValue(3);
        item.modifyDefenseValue(3);
        item.modifyThrownAttackValue(3);
        item.modifyStrengthRequirement(1);
        item.isClub(true);
        return item;
    }
    
    /**
     * Method for creating a new "staff" item
     * 
     * @param depth
     *        - the depth at which to spawn said weapon
     * @return returns the newly created "staff" item
     */
    public Item newStaff(int depth) {
        Item item = new Item(')', AsciiPanel.yellow, "staff", null);
        item.modifyAttackValue(4);
        item.modifyDefenseValue(2);
        item.modifyThrownAttackValue(3);
        item.modifyStrengthRequirement(1);
        item.isClub(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newBow() {
        Item item = new Item(')', AsciiPanel.yellow, "worn bow", null);
        item.modifyAttackValue(1);
        item.modifyRangedAttackValue(4);
        item.modifyStrengthRequirement(1);
        item.isRanged(true);
        return item;
    }
    
    /**
     * Method for creating a new "bow" item
     * 
     * @param depth
     *        - the depth at which to spawn said weapon
     * @return returns the newly created "bow" item
     */
    public Item newBow(int depth) {
        Item item = new Item(')', AsciiPanel.yellow, "bow", null);
        item.modifyAttackValue(1);
        item.modifyRangedAttackValue(5);
        item.modifyStrengthRequirement(1);
        item.isRanged(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newMace() {
        Item item = new Item(')', AsciiPanel.white, "rusty mace", null);
        item.modifyAttackValue(4);
        item.modifyArmorPen(2);
        item.modifyThrownAttackValue(2);
        item.modifyStrengthRequirement(4);
        item.isMace(true);
        return item;
    }
    
    public Item newMace(int depth) {
        Item item = new Item(')', AsciiPanel.white, "mace", null);
        item.modifyAttackValue(5);
        item.modifyArmorPen(2);
        item.modifyThrownAttackValue(2);
        item.modifyStrengthRequirement(5);
        item.isMace(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newSpear(int depth) {
        Item item = new Item(')', AsciiPanel.white, "spear", null);
        item.modifyAttackValue(6);
        item.modifyThrownAttackValue(4);
        item.isSpear(true);
        item.modifyStrengthRequirement(4);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newAxe(int depth) {
        Item item = new Item(')', AsciiPanel.white, "axe", null);
        item.modifyAttackValue(8);
        item.modifyThrownAttackValue(4);
        item.modifyStrengthRequirement(5);
        item.isAxe(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newHalberd(int depth) {
        Item item = new Item(')', AsciiPanel.brightGreen, "halberd", null);
        item.modifyAttackValue(5);
        item.modifyThrownAttackValue(3);
        item.modifyStrengthRequirement(7);
        item.isSpear(true);
        item.isAxe(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newClub(int depth) {
        Item item = new Item(')', AsciiPanel.yellow, "club", null);
        item.modifyAttackValue(12);
        item.modifyThrownAttackValue(3);
        item.modifyStrengthRequirement(8);
        item.isClub(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    // ===============================================================================
    // Items :: Armor
    // ===============================================================================
    
    public Item newTunic() {
        Item item = new Item('[', AsciiPanel.green, "dirty tunic", null);
        item.modifyDefenseValue(1);
        item.modifyStrengthRequirement(1);
        return item;
    }
    
    /**
     * Method for creating a new "tunic" item
     * 
     * @param depth
     *        - the depth at which to spawn said armor
     * @return returns the newly created "light armor" item
     */
    public Item newTunic(int depth) {
        Item item = new Item('[', AsciiPanel.green, "tunic", null);
        item.modifyDefenseValue(2);
        item.modifyStrengthRequirement(1);
        item.isArmor(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newChainmail() {
        Item item = new Item('[', AsciiPanel.white, "rusty chainmail", null);
        item.modifyDefenseValue(3);
        item.modifyStrengthRequirement(2);
        item.modifyStealthModifier(2);
        item.isArmor(true);
        return item;
    }
    
    /**
     * Method for creating a new "chainmail" item
     * 
     * @param depth
     *        - the depth at which to spawn said armor
     * @return returns the newly created "medium armor" item
     */
    public Item newChainmail(int depth) {
        Item item = new Item('[', AsciiPanel.white, "chainmail", null);
        item.modifyDefenseValue(4);
        item.modifyStrengthRequirement(3);
        item.modifyStealthModifier(2);
        item.isArmor(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newPlatemail() {
        Item item = new Item('[', AsciiPanel.brightWhite, "battered platemail", null);
        item.modifyDefenseValue(5);
        item.modifyStrengthRequirement(4);
        item.modifyStealthModifier(3);
        item.isArmor(true);
        return item;
    }
    
    /**
     * Method for creating a new "platemail" item
     * 
     * @param depth
     *        - the depth at which to spawn said armor
     * @return returns the newly created "heavy armor" item
     */
    public Item newPlatemail(int depth) {
        Item item = new Item('[', AsciiPanel.brightWhite, "platemail", null);
        item.modifyDefenseValue(6);
        item.modifyStrengthRequirement(6);
        item.modifyStealthModifier(3);
        item.isArmor(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newRobes() {
        Item item = new Item('[', AsciiPanel.brightBlue, "tattered robes", null);
        item.modifyDefenseValue(1);
        item.modifyManaBoost(10);
        item.modifyManaRegenBoost(50);
        item.modifyStrengthRequirement(1);
        item.isArmor(true);
        return item;
    }
    
    public Item newRobes(int depth) {
        Item item = new Item('[', AsciiPanel.brightBlue, "robes", null);
        item.modifyDefenseValue(1);
        item.modifyManaBoost(20);
        item.modifyManaRegenBoost(75);
        item.modifyStrengthRequirement(1);
        item.isArmor(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    // ===============================================================================
    // Items :: Rings
    // ===============================================================================
    
    public Item newRing() {
        Item item = new Item('ø', AsciiPanel.brightBlue, "ring", null);
        item.isRing(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        return item;
    }
    
    public Item newRing(int depth) {
        Item item = new Item('ø', AsciiPanel.brightBlue, "ring", null);
        item.isRing(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    // ===============================================================================
    // Items :: Charms
    // ===============================================================================
    
    public Item newCharm() {
        Item item = new Item('ø', AsciiPanel.brightGreen, "charm", null);
        item.isCharm(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        return item;
    }
    
    public Item newCharm(int depth) {
        Item item = new Item('ø', AsciiPanel.brightGreen, "charm", null);
        item.isCharm(true);
        Enchantment e = new Enchantment(item);
        e.generateEnchantedItem(item);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    // ===============================================================================
    // Items :: Potions
    // ===============================================================================
    
    public Item newPotionOfHealth() {
        String appearance = potionAppearances.get(0);
        Item item = new Item('!', potionColors.get(appearance), "health potion", appearance);
        item.setQuaffEffect(new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.hp() == creature.maxHp()) return;
                
                creature.modifyHp(15, null);
                creature.doAction(item, "look healthier");
                creature.learnName(item);
            }
        });
        
        item.isPotion(true);
        return item;
    }
    
    /**
     * Method for creating a new "health potion" item
     * 
     * @param depth
     *        - the depth at which to spawn said potion
     * @return returns the newly created "health potion" item
     */
    public Item newPotionOfHealth(int depth) {
        String appearance = potionAppearances.get(0);
        Item item = new Item('!', potionColors.get(appearance), "health potion", appearance);
        item.setQuaffEffect(new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.hp() == creature.maxHp()) return;
                
                creature.modifyHp(15, null);
                creature.doAction(item, "look healthier");
                creature.learnName(item);
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newPotionOfInvisibility(){
        String appearance = potionAppearances.get(1);
        Item item = new Item('!', potionColors.get(appearance), "invisibility potion", appearance);
        item.setQuaffEffect(new Effect(5){
            
            @SuppressWarnings("unused")
            int vision;
            boolean endEarly = false;
            
            public void start(Creature creature){
                creature.doAction("disappear into the shadows");
                creature.invisible(true);
                vision = creature.visionRadius();
                creature.modifyVisionRadius(-(creature.visionRadius() - 3));
            }
            
            public void update(Creature creature){
                if(!creature.invisible() && duration > 1) {
                    duration = 0;
                    endEarly = true;
                }
                super.update(creature);
            }
            
            public void end(Creature creature){
                if(creature.invisible() && endEarly){
                    creature.invisible(false);
                }
                creature.doAction("reappear from the shadows");
                duration = 1;
            }
        });
        
        item.isPotion(true);
        return item;
    }
    
    public Item newPotionOfInvisibility(int depth){
        String appearance = potionAppearances.get(1);
        Item item = new Item('!', potionColors.get(appearance), "invisibility potion", appearance);
        item.setQuaffEffect(new Effect(5){
            
            @SuppressWarnings("unused")
            int vision;
            boolean endEarly = false;
            
            public void start(Creature creature){
                creature.doAction("disappear into the shadows");
                creature.invisible(true);
                vision = creature.visionRadius();
                creature.modifyVisionRadius(-(creature.visionRadius() - 3));
            }
            
            public void update(Creature creature){
                if(!creature.invisible() && duration > 1) {
                    duration = 0;
                    endEarly = true;
                }
                super.update(creature);
            }
            
            public void end(Creature creature){
                if(creature.invisible() && endEarly){
                    creature.invisible(false);
                }
                creature.doAction("reappear from the shadows");
                duration = 1;
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newPotionOfMana() {
        String appearance = potionAppearances.get(2);
        Item item = new Item('!', potionColors.get(appearance), "mana potion", appearance);
        item.setQuaffEffect(new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.mana() == creature.maxMana()) return;
                
                creature.modifyMana(25);
                creature.doAction(item, "look revitalized");
                creature.learnName(item);
            }
        });
        
        item.isPotion(true);
        return item;
    }
    
    /**
     * Method for creating a new "mana potion" item
     * 
     * @param depth
     *        - the depth at which to spawn said potion
     * @return returns the newly created "mana potion" item
     */
    public Item newPotionOfMana(int depth) {
        String appearance = potionAppearances.get(2);
        Item item = new Item('!', potionColors.get(appearance), "mana potion", appearance);
        item.setQuaffEffect(new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.mana() == creature.maxMana()) return;
                
                creature.modifyMana(25);
                creature.doAction(item, "look revitalized");
                creature.learnName(item);
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newPotionOfPoison() {
        String appearance = potionAppearances.get(3);
        Item item = new Item('!', potionColors.get(appearance), "poison potion", appearance);
        item.setQuaffEffect(new Effect(20) {
            
            public void start(Creature creature) {
                creature.doAction(item, "look sick");
                creature.learnName(item);
            }
            
            public void update(Creature creature) {
                super.update(creature);
                creature.modifyHp(-1, "Died of poison.");
            }
        });
        
        item.isPotion(true);
        return item;
    }
    
    /**
     * Method for creating a new "poison potion" item
     * 
     * @param depth
     *        - the depth at which to spawn said potion
     * @return returns the newly created "poison potion" item
     */
    public Item newPotionOfPoison(int depth) {
        String appearance = potionAppearances.get(3);
        Item item = new Item('!', potionColors.get(appearance), "poison potion", appearance);
        item.setQuaffEffect(new Effect(20) {
            
            public void start(Creature creature) {
                creature.doAction(item, "look sick");
                creature.learnName(item);
            }
            
            public void update(Creature creature) {
                super.update(creature);
                creature.modifyHp(-1, "Died of poison.");
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    /**
     * Method for creating a new "warrior's potion" item
     * 
     * @param depth
     *        - the depth at which to spawn said potion
     * @return returns the newly created "warrior's potion" item
     */
    public Item newPotionOfWarrior(int depth) {
        String appearance = potionAppearances.get(4);
        Item item = new Item('!', potionColors.get(appearance), "warrior's potion", appearance);
        item.setQuaffEffect(new Effect(20) {
            
            public void start(Creature creature) {
                creature.modifyAttackValue(5);
                creature.modifyDefenseValue(5);
                creature.doAction(item, "suddenly look more fierce");
                creature.learnName(item);
            }
            
            public void end(Creature creature) {
                creature.modifyAttackValue(-5);
                creature.modifyDefenseValue(-5);
                creature.doAction(item, "suddenly look less fierce");
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    /**
     * Method for creating a new "glowing potion" item
     * 
     * @param depth
     *        - the depth at which to spawn said potion
     * @return returns the newly created "glowing potion" item
     */
    public Item newPotionOfLight(int depth) {
        String appearance = potionAppearances.get(5);
        Item item = new Item('!', potionColors.get(appearance), "potion of illumination", appearance);
        item.setQuaffEffect(new Effect(30) {
            
            public void start(Creature creature) {
                creature.modifyVisionRadius(4);
                creature.doAction(item, "begin to emit an aura of light");
                creature.learnName(item);
            }
            
            public void end(Creature creature) {
                creature.modifyVisionRadius(-4);
                creature.doAction(item, "the aura fades");
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    /**
     * Method for creating a new "blindness potion" item
     * 
     * @param depth
     *        - the depth at which to spawn said potion
     * @return returns the newly created "blindness potion" item
     */
    public Item newPotionOfBlindness(int depth) {
        String appearance = potionAppearances.get(6);
        Item item = new Item('!', potionColors.get(appearance), "blindness potion", appearance);
        item.setQuaffEffect(new Effect(20) {
            
            public void start(Creature creature) {
                creature.modifyVisionRadius(-4);
                creature.doAction(item, "vision becomes impaired");
                creature.learnName(item);
            }
            
            public void end(Creature creature) {
                creature.modifyVisionRadius(4);
                creature.doAction(item, "vision returns to normal");
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newPotionOfStrength(int depth) {
        String appearance = potionAppearances.get(7);
        Item item = new Item('!', potionColors.get(appearance), "strength potion", appearance);
        item.setQuaffEffect(new Effect(20) {
            
            public void start(Creature creature) {
                creature.modifyStrength(3);
                creature.doAction(item, "suddenly look stronger");
                creature.learnName(item);
            }
            
            public void end(Creature creature) {
                creature.modifyStrength(-3);
                creature.doAction(item, "suddenly look less strong");
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newPotionOfDetection(int depth) {
        String appearance = potionAppearances.get(8);
        Item item = new Item('!', potionColors.get(appearance), "detection potion", appearance);
        item.setQuaffEffect(new Effect(20) {
            
            public void start(Creature creature) {
                creature.modifyDetectCreatures(1);
                creature.doAction("get a sixth sense.");
                creature.learnName(item);
            }
            
            public void end(Creature creature) {
                creature.modifyDetectCreatures(-1);
                creature.doAction("lose the sixth sense.");
            }
        });
        
        item.isPotion(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    // ===============================================================================
    // Items :: Scrolls
    // ===============================================================================
    
    public Item newScrollOfEnchantment(int depth) {
        String appearance = scrollAppearances.get(0);
        Item item = new Item('/', AsciiPanel.brightYellow, "scroll of enchantment", appearance);
        item.setSpellEffect(new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.isPlayer()) {
                    PlayScreen.setSubscreen(new EnchantScreen(creature, item));
                }
            }
            
            public void update(Creature creature) {
                super.update(creature);
            }
        });
        
        item.isScroll(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newScrollOfIdentify(int depth) {
        String appearance = scrollAppearances.get(1);
        Item item = new Item('/', AsciiPanel.brightYellow, "scroll of identify", appearance);
        item.setSpellEffect(new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.isPlayer()) {
                    PlayScreen.setSubscreen(new EnchantScreen(creature, item));
                }
                creature.learnName(item);
            }
            
            public void update(Creature creature) {
                super.update(creature);
            }
        });
        
        item.isScroll(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newScrollOfRemoveCurse(int depth) {
        String appearance = scrollAppearances.get(2);
        Item item = new Item('/', AsciiPanel.brightYellow, "scroll of remove curse", appearance);
        item.setSpellEffect(new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.isPlayer()) {
                    PlayScreen.setSubscreen(new EnchantScreen(creature, item));
                }
            }
            
            public void update(Creature creature) {
                super.update(creature);
            }
        });
        
        item.isScroll(true);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    // ===============================================================================
    // Items :: Spellbooks
    // ===============================================================================
    
    public Item newWhiteMagesSpellbook() {
        Item item = new Item('+', AsciiPanel.brightWhite, "paladin's spellbook", null);
        item.addWrittenSpell("minor heal", 4, new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.hp() == creature.maxHp()) return;
                
                creature.modifyHp(20, null);
                creature.doAction("look healthier");
            }
        });
        
        item.addWrittenSpell("slow heal", 12, new Effect(50) {
            
            public void update(Creature creature) {
                super.update(creature);
                creature.modifyHp(2, null);
            }
        });
        
        return item;
    }
    
    /**
     * Method for creating a new "white mage's spellbook" item
     * 
     * @param depth
     *        - the depth at which to spawn said spellbook
     * @return returns the newly created "white mage's spellbook" item
     */
    public Item newWhiteMagesSpellbook(int depth) {
        Item item = new Item('+', AsciiPanel.brightWhite, "white mage's spellbook", null);
        item.addWrittenSpell("minor heal", 4, new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.hp() == creature.maxHp()) return;
                
                creature.modifyHp(20, null);
                creature.doAction("look healthier");
            }
        });
        
        item.addWrittenSpell("major heal", 8, new Effect(1) {
            
            public void start(Creature creature) {
                if (creature.hp() == creature.maxHp()) return;
                
                creature.modifyHp(50, null);
                creature.doAction("look healthier");
            }
        });
        
        item.addWrittenSpell("slow heal", 12, new Effect(50) {
            
            public void update(Creature creature) {
                super.update(creature);
                creature.modifyHp(2, null);
            }
        });
        
        item.addWrittenSpell("inner strength", 16, new Effect(50) {
            
            public void start(Creature creature) {
                creature.modifyAttackValue(2);
                creature.modifyDefenseValue(2);
                creature.modifyVisionRadius(1);
                creature.modifyRegenHpPer1000(10);
                creature.modifyRegenManaPer1000(-10);
                creature.doAction("seem to glow with inner strength");
            }
            
            public void update(Creature creature) {
                super.update(creature);
                if (Math.random() < 0.25) creature.modifyHp(1, null);
            }
            
            public void end(Creature creature) {
                creature.modifyAttackValue(-2);
                creature.modifyDefenseValue(-2);
                creature.modifyVisionRadius(-1);
                creature.modifyRegenHpPer1000(-10);
                creature.modifyRegenManaPer1000(10);
                creature.doAction("inner strength fades");
            }
        });
        
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    /**
     * Method for creating a new "blue mage's spellbook" item
     * 
     * @param depth
     *        - the depth at which to spawn said spellbook
     * @return returns the newly created "blue mage's spellbook" item
     */
    public Item newBlueMagesSpellbook(int depth) {
        Item item = new Item('+', AsciiPanel.brightBlue, "blue mage's spellbook", null);
        
        item.addWrittenSpell("blood to mana", 1, new Effect(1) {
            
            public void start(Creature creature) {
                int amount = Math.min(creature.hp() - 1, creature.maxMana() - creature.mana());
                creature.modifyHp(-amount, "Killed by a blood to mana spell.");
                creature.modifyMana(amount);
            }
        });
        
        item.addWrittenSpell("blink", 6, new Effect(1) {
            
            public void start(Creature creature) {
                creature.doAction("fade out");
                
                int mx = 0;
                int my = 0;
                
                do {
                    mx = (int) (Math.random() * 11) - 5;
                    my = (int) (Math.random() * 11) - 5;
                } while (!creature.canEnter(creature.x + mx, creature.y + my, creature.z)
                        && creature.canSee(creature.x + mx, creature.y + my, creature.z));
                
                creature.moveBy(mx, my, 0);
                
                creature.doAction("fade in");
            }
        });
        
        item.addWrittenSpell("summon bats", 11, new Effect(1) {
            
            public void start(Creature creature) {
                for (int ox = -1; ox < 2; ox++) {
                    for (int oy = -1; oy < 2; oy++) {
                        int nx = creature.x + ox;
                        int ny = creature.y + oy;
                        if (ox == 0 && oy == 0 || creature.creature(nx, ny, creature.z) != null) continue;
                        
                        Creature bat = newBat(0);
                        
                        if (!bat.canEnter(nx, ny, creature.z)) {
                            world.remove(bat);
                            continue;
                        }
                        
                        bat.x = nx;
                        bat.y = ny;
                        bat.z = creature.z;
                        
                        creature.summon(bat);
                    }
                }
            }
        });
        
        item.addWrittenSpell("detect creatures", 16, new Effect(75) {
            
            public void start(Creature creature) {
                creature.doAction("gains a sixth sense");
                creature.modifyDetectCreatures(1);
            }
            
            public void end(Creature creature) {
                creature.doAction("loses their sixth sense");
                creature.modifyDetectCreatures(-1);
            }
        });
        
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newRedMagesSpellbook(){
        Item item = new Item('+', AsciiPanel.brightRed, "apprentice's spellbook", null);
        
        item.addWrittenSpell("frostbolt", 4, new Effect(4){
            public void start(Creature creature){
                creature.doAction("freeze in place");
                creature.modifyHp(-6, "Killed by a frostbolt.");
                creature.rooted(true);
            }
            
            public void update(Creature creature){
                super.update(creature);
            }
            
            public void end(Creature creature){
                creature.doAction("unfreeze");
                creature.rooted(false);
            }
        });
        
        item.addWrittenSpell("ink jet", 2, new Effect(4){
            
            int vision;
            
            public void start(Creature creature){
                creature.doAction("become blinded");
                creature.modifyHp(-3, "Killed by ink jet.");
                vision = creature.visionRadius();
                creature.modifyVisionRadius(-(creature.visionRadius() - 2));
            }
            
            public void update(Creature creature){
                super.update(creature);
            }
            
            public void end(Creature creature){
                creature.doAction("recover from blindness");
                creature.modifyVisionRadius((vision - 2));
            }
        });
        
        return item;
    }
    
    public Item newRedMagesSpellbook(int depth){
        Item item = new Item('+', AsciiPanel.brightRed, "red mage's spellbook", null);
        
        item.addWrittenSpell("fireball", 8, new Effect(4){
            
            boolean onFire = false;
            
            public void start(Creature creature){
                for(int ox = -1; ox < 2; ox++){
                    for(int oy = -1; oy < 2; oy++){
                        Creature c = world.creature(creature.x + ox, creature.y + oy, creature.z);
                        c.doAction("get hit by a fireball");
                        c.modifyHp(-15, "Killed by a fireball.");
                    }
                }
                if(Math.random() < 0.4){
                    onFire = true;
                }
            }
            
            public void update(Creature creature){
                if(onFire) {
                    creature.doAction("suffer burn damage");
                    creature.modifyHp(-2, "Burned to death.");
                }
                super.update(creature);
            }
            
            public void end(Creature creature){
                onFire = false;
            }
            
        });
        
        item.addWrittenSpell("frostbolt", 6, new Effect(5){
            public void start(Creature creature){
                creature.doAction("freeze in place");
                creature.modifyHp(-10, "Killed by a frostbolt.");
                creature.rooted(true);
            }
            
            public void update(Creature creature){
                super.update(creature);
            }
            
            public void end(Creature creature){
                creature.doAction("unfreeze");
                creature.rooted(false);
            }
        });
        
        item.addWrittenSpell("ink jet", 4, new Effect(5){
            
            int vision;
            
            public void start(Creature creature){
                creature.doAction("become blinded");
                creature.modifyHp(-4, "Killed by ink jet.");
                vision = creature.visionRadius();
                creature.modifyVisionRadius(-(creature.visionRadius() - 2));
            }
            
            public void update(Creature creature){
                super.update(creature);
            }
            
            public void end(Creature creature){
                creature.doAction("recover from blindness");
                creature.modifyVisionRadius((vision - 2));
            }
        });
        
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    public Item newBlackMageSpellbook(int depth){
        Item item = new Item('+', AsciiPanel.brightBlack, "black mage's spellbook", null);
        
        item.addWrittenSpell("invisibility", 10, new Effect(5){
            
            int vision;
            boolean endEarly = false;
            
            public void start(Creature creature){
                creature.doAction("disappear into the shadows");
                creature.invisible(true);
                vision = creature.visionRadius();
                creature.modifyVisionRadius(-(creature.visionRadius() - 3));
            }
            
            public void update(Creature creature){
                if(!creature.invisible() && duration > 1) {
                    duration = 0;
                    endEarly = true;
                }
                super.update(creature);
            }
            
            public void end(Creature creature){
                if(creature.invisible() && endEarly){
                    creature.invisible(false);
                }
                creature.doAction("reappear from the shadows");
                duration = 1;
            }
        });
        
        world.addAtEmptyLocation(item, depth);
        return item;
    }
    
    // ---------------------
    // ---- Randomizers ----
    // ---------------------
    
    /**
     * Method for creating a new random weapon
     * 
     * @param depth
     *        - the depth at which to spawn said weapon
     * @return returns the newly created random weapon
     */
    public Item newRandomWeapon(int depth) {
        switch ((int) (Math.random() * 8)) {
            case 0:
                return newDagger(depth);
            case 1:
                return newSword(depth);
            case 2:
                return newBow(depth);
            case 3:
                return newStaff(depth);
            case 4:
                return newMace(depth);
            case 5:
                return newSpear(depth);
            case 6:
                return newAxe(depth);
            case 7:
                return newHalberd(depth);
            default:
                return newClub(depth);
        }
    }
    
    /**
     * Method for creating a new random armor
     * 
     * @param depth
     *        - the depth at which to spawn said armor
     * @return returns the newly created random armor
     */
    public Item newRandomArmor(int depth) {
        switch ((int) (Math.random() * 3)) {
            case 0:
                return newTunic(depth);
            case 1:
                return newChainmail(depth);
            case 2:
                return newPlatemail(depth);
            default:
                return newRobes(depth);
        }
    }
    
    public Item newRandomRing() {
        switch ((int) (Math.random() * 3)) {
            case 0:
                return newRing();
            case 1:
                return newRing();
            case 2:
                return newRing();
            default:
                return newRing();
        }
    }
    
    public Item newRandomRing(int depth) {
        switch ((int) (Math.random() * 3)) {
            case 0:
                return newRing(depth);
            case 1:
                return newRing(depth);
            case 2:
                return newRing(depth);
            default:
                return newRing(depth);
        }
    }
    
    public Item newRandomCharm() {
        switch ((int) (Math.random() * 3)) {
            case 0:
                return newCharm();
            case 1:
                return newCharm();
            case 2:
                return newCharm();
            default:
                return newCharm();
        }
    }
    
    public Item newRandomCharm(int depth) {
        switch ((int) (Math.random() * 3)) {
            case 0:
                return newCharm(depth);
            case 1:
                return newCharm(depth);
            case 2:
                return newCharm(depth);
            default:
                return newCharm(depth);
        }
    }
    
    public Item newRandomScroll(int depth) {
        switch ((int) (Math.random() * 2)) {
            case 0:
                return newScrollOfEnchantment(depth);
            case 1:
                return newScrollOfIdentify(depth);
            default:
                return newScrollOfRemoveCurse(depth);
        }
    }
    
    /**
     * Method for creating a new random potion
     * 
     * @param depth
     *        - the depth at which to spawn said potion
     * @return returns the newly created random potion
     */
    public Item newRandomPotion(int depth) {
        switch ((int) (Math.random() * 8)) {
            case 0:
                return newPotionOfHealth(depth);
            case 1:
                return newPotionOfInvisibility(depth);
            case 2:
                return newPotionOfMana(depth);
            case 3:
                return newPotionOfPoison(depth);
            case 4:
                return newPotionOfLight(depth);
            case 5:
                return newPotionOfBlindness(depth);
            case 6:
                return newPotionOfWarrior(depth);
            case 7:
                return newPotionOfStrength(depth);
            default:
                return newPotionOfDetection(depth);
        }
    }
    
    /**
     * Method for creating a new random spellbook
     * 
     * @param depth
     *        - the depth at which to spawn said spellbook
     * @return returns the newly created random spellbook
     */
    public Item newRandomSpellbook(int depth) {
        switch ((int) (Math.random() * 3)) {
            case 0:
                return newWhiteMagesSpellbook(depth);
            case 1:
                return newBlueMagesSpellbook(depth);
            default:
                return newRedMagesSpellbook(depth);
        }
    }
}
