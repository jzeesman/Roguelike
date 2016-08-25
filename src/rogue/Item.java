package rogue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The Item class handles all of the attributes of items
 */
public class Item {
    
    /** The item's glyph **/
    private char glyph;
    /** The item's glyph color **/
    private Color color;
    /** The item's name **/
    private String name;
    
    private String prefix;
    private String suffix;
    /** The item's full name (which includes prefix/suffix) **/
    private String fullName;
    
    /** The item's initial apperance **/
    private String apperance;
    
    /** The item's attack value **/
    private int attackValue;
    /** The item's thrown attack value **/
    private int thrownAttackValue;
    /** The item's ranged attack value **/
    private int rangedAttackValue;
    /** The item's defense value **/
    private int defenseValue;
    /** The item's armor penetration value **/
    private int armorPen;
    
    /** The item's health boost when equipped **/
    private int healthBoost;
    /** The item's health regen boost when equipped **/
    private int healthRegenBoost;
    /** The item's mana boost when equipped **/
    private int manaBoost;
    /** The item's mana regen boost when equipped **/
    private int manaRegenBoost;
    
    private int stealthModifier;
    
    /** Keeps track of whether or not this item is a spear **/
    private boolean isSpear;
    /** Keeps track of whether or not this item is an axe **/
    private boolean isAxe;
    /** Keeps track of whether or not this item is a mace **/
    private boolean isMace;
    private boolean isSword;
    private boolean isDagger;
    private boolean isClub;
    private boolean isRanged;
    private boolean isWand;
    private boolean isFood;
    private boolean isPotion;
    private boolean isScroll;
    
    private boolean isArmor;
    
    private boolean isRing;
    private boolean isCharm;
    
    /** The Effect object that represents the effect an item has upon quaff **/
    private Effect quaffEffect;
    /** The Effect object that represents the effect an item (scroll) has upon reading **/
    private Effect spellEffect;
    
    /** The Spell object that triggers upon cast using the effect from {@linkplain Item#spellEffect} **/
    private Spell spell;
    
    private Effect enchantment;
    
    private boolean effectVisible;
    
    private boolean cursed;
    private boolean cursedVisible;
    
    private int strengthRequirement;
    
    /** A List of spells, used in spellbooks **/
    private List<Spell> writtenSpells;
    
    /** The item's food value **/
    private int foodValue;
    
    /**
     * The general constructor for the Item class
     * 
     * @param glyph
     *        - the glyph that represents the item in the world
     * @param color
     *        - the color of the glyph
     * @param name
     *        - the name of the item
     * @param apperance
     *        - the appearance name of the item
     */
    public Item(char glyph, Color color, String name, String apperance) {
        this.glyph = glyph;
        this.color = color;
        this.name = name;
        this.apperance = apperance;
        attackValue = 0;
        thrownAttackValue = 1;
        rangedAttackValue = 0;
        defenseValue = 0;
        armorPen = 0;
        healthBoost = 0;
        healthRegenBoost = 0;
        manaBoost = 0;
        manaRegenBoost = 0;
        isSpear = false;
        isAxe = false;
        isMace = false;
        isSword = false;
        isClub = false;
        isRanged = false;
        isWand = false;
        isPotion = false;
        isScroll = false;
        isFood = false;
        isArmor = false;
        isRing = false;
        isCharm = false;
        prefix = null;
        suffix = null;
        enchantment = null;
        effectVisible = false;
        cursedVisible = false;
        strengthRequirement = 0;
        stealthModifier = 0;
        writtenSpells = new ArrayList<Spell>();
    }
    
    /**
     * Modifies an item's food value
     * 
     * @param amount
     *        - the amount you want to modify by
     */
    public void modifyFoodValue(int amount) {
        foodValue += amount;
    }
    
    /**
     * @return returns the Item's food value
     */
    public int foodValue() {
        return foodValue;
    }
    
    /**
     * @return returns the Item's attack value
     */
    public int attackValue() {
        return attackValue;
    }
    
    /**
     * Modifies attack value
     * 
     * @param amount
     *        - the amount to modify the attack value by
     */
    public void modifyAttackValue(int amount) {
        attackValue += amount;
    }
    
    /**
     * @return returns the Item's thrown attack value
     */
    public int thrownAttackValue() {
        return thrownAttackValue;
    }
    
    /**
     * Modifies thrown attack value
     * 
     * @param amount
     *        - the amount to modify the thrown attack value by
     */
    public void modifyThrownAttackValue(int amount) {
        thrownAttackValue += amount;
    }
    
    /**
     * @return returns the Item's ranged attack value
     */
    public int rangedAttackValue() {
        return rangedAttackValue;
    }
    
    /**
     * Modifies ranged attack value
     * 
     * @param amount
     *        - the amount to modify the ranged attack value by
     */
    public void modifyRangedAttackValue(int amount) {
        rangedAttackValue += amount;
    }
    
    /**
     * @return returns the Item's defense value
     */
    public int defenseValue() {
        return defenseValue;
    }
    
    /**
     * Modifies defense value
     * 
     * @param amount
     *        - the amount to modify the defense value by
     */
    public void modifyDefenseValue(int amount) {
        defenseValue += amount;
    }
    
    /**
     * @return returns the armor penetration value
     */
    public int armorPen() {
        return armorPen;
    }
    
    /**
     * Modifies armor penetration value
     * 
     * @param amount
     *        - the amount to modify the armor penetration value by
     */
    public void modifyArmorPen(int amount) {
        armorPen += amount;
    }
    
    public int healthBoost() {
        return healthBoost;
    }
    
    public void modifyHealthBoost(int amount) {
        healthBoost += amount;
    }
    
    public int healthRegenBoost() {
        return healthRegenBoost;
    }
    
    public void modifyHealthRegenBoost(int amount) {
        healthRegenBoost += amount;
    }
    
    public int manaBoost() {
        return manaBoost;
    }
    
    public void modifyManaBoost(int amount) {
        manaBoost += amount;
    }
    
    public int manaRegenBoost() {
        return manaRegenBoost;
    }
    
    public void modifyManaRegenBoost(int amount) {
        manaRegenBoost += amount;
    }
    
    /**
     * Sets whether or not the item is a spear
     * 
     * @param value
     *        - whether or not the item is a spear
     */
    public void isSpear(boolean value) {
        isSpear = value;
    }
    
    /**
     * @return returns whether or not the item is a spear
     */
    public boolean isSpear() {
        return isSpear;
    }
    
    public void isRing(boolean value){
        isRing = value;
    }
    
    public boolean isRing(){
        return isRing;
    }
    
    public void isCharm(boolean value){
        isCharm = value;
    }
    
    public boolean isCharm(){
        return isCharm;
    }
    
    /**
     * Sets whether or not the item is an axe
     * 
     * @param value
     *        - whether or not the item is an axe
     */
    public void isAxe(boolean value) {
        isAxe = value;
    }
    
    /**
     * @return returns whether or not the item is an axe
     */
    public boolean isAxe() {
        return isAxe;
    }
    
    /**
     * Sets whether or not the item is a mace
     * 
     * @param value
     *        - whether or not the item is a mace
     */
    public void isMace(boolean value) {
        isMace = value;
    }
    
    /**
     * @return returns whether or not the item is a mace
     */
    public boolean isMace() {
        return isMace;
    }
    
    public void isSword(boolean value){
        isSword = value;
    }
    
    public boolean isSword(){
        return isSword;
    }
    
    public void isDagger(boolean value){
        isDagger = value;
    }
    
    public boolean isDagger(){
        return isDagger;
    }
    
    public void isClub(boolean value){
        isClub = value;
    }
    
    public boolean isClub(){
        return isClub;
    }
    
    public void isRanged(boolean value){
        isRanged = value;
    }
    
    public boolean isRanged(){
        return isRanged;
    }
    
    public void isWand(boolean value){
        isWand = value;
    }
    
    public boolean isWand(){
        return isWand;
    }
    
    public void isFood(boolean value){
        isFood = value;
    }
    
    public boolean isFood(){
        return isFood;
    }
    
    public void isPotion(boolean value){
        isPotion = value;
    }
    
    public boolean isPotion(){
        return isPotion;
    }
    
    public void isScroll(boolean value){
        isScroll = value;
    }
    
    public boolean isScroll(){
        return isScroll;
    }
    
    public int stealthModifier(){
        return stealthModifier;
    }
    
    public void modifyStealthModifier(int amount){
        stealthModifier += amount;
    }
    
    public boolean isWeapon(){
        return (isAxe || isSpear || isMace || isSword || isClub || isRanged);
    }
    
    public void isArmor(boolean value){
        isArmor = value;
    }
    
    public boolean isArmor(){
        return isArmor;
    }
    
    public boolean isAccessory(){
        return (isRing || isCharm);
    }
    
    /**
     * @return returns the effect an item gives upon quaff
     */
    public Effect quaffEffect() {
        return quaffEffect;
    }
    
    /**
     * Sets the quaffEffect of an item
     * 
     * @param effect
     *        - effect to set
     */
    public void setQuaffEffect(Effect effect) {
        this.quaffEffect = effect;
    }
    
    public Effect spellEffect(){
        return spellEffect;
    }
    
    public void setSpellEffect(Effect effect){
        this.spellEffect = effect;
    }
    
    public Spell spell(){
        return spell;
    }
    
    public void setSpell(String name, int manaCost){
        this.spell = new Spell(name, manaCost, spellEffect);
    }
    
    /**
     * @return returns the List of writtenSpells an item has
     */
    public List<Spell> writtenSpells() {
        return writtenSpells;
    }
    
    /**
     * Method to add a written spell to an item
     * 
     * @param name
     *        - name of the spell
     * @param manaCost
     *        - mana cost of the spell
     * @param effect
     *        - effect of the spell
     */
    public void addWrittenSpell(String name, int manaCost, Effect effect) {
        writtenSpells.add(new Spell(name, manaCost, effect));
    }
    
    /**
     * @return returns the Item's glyph
     */
    public char glyph() {
        return glyph;
    }
    
    /**
     * @return returns the Item's color
     */
    public Color color() {
        return color;
    }
    
    /**
     * @return returns the Item's name
     */
    public String name() {
        return name;
    }
    
    /**
     * @return returns an item's appearance (if appearance == null, return the
     *         item's name)
     */
    public String appearance() {
        if (apperance == null){
            if(effectVisible)
                return fullName();
            return name();
        }
        
        return apperance;
    }
    
    public String fullName(){
        return ((isWeapon() || isArmor() || isAccessory()) ? (prefix != null || suffix != null && !(prefix == null && suffix == null) ? fullName : name) : name);
    }
    
    public String prefix(){
        return prefix;
    }
    
    public String suffix(){
        return suffix;
    }
    
    public void setFullName(String prefix, String suffix){
        String temp = "";
        if(prefix != null){
            temp += prefix;
            this.prefix = prefix;
        }
        temp += name;
        if(suffix != null){
            temp += suffix;
            this.suffix = suffix;
        }
        fullName = temp;
    }
    
    public Effect enchantment(){
        return enchantment;
    }
    
    public void setEnchantment(Effect effect){
        this.enchantment = effect;
    }
    
    public void setCursed(boolean value){
        cursed = value;
    }
    
    public boolean isCursed(){
        return cursed;
    }
    
    public void setCursedVisible(boolean value){
        cursedVisible = value;
    }
    
    public boolean cursedVisible(){
        return cursedVisible;
    }
    
    public void modifyStrengthRequirement(int amount){
        strengthRequirement += amount;
    }
    
    public int strengthRequirement(){
        return strengthRequirement;
    }
    
    public void setEffectVisible(boolean value){
        effectVisible = value;
    }
    
    public boolean effectVisible(){
        return effectVisible;
    }
    
    /**
     * @return returns an item's details
     */
    public String details() {
        String details = "";
        
        if (strengthRequirement != 0) details += "   StrReq:" + strengthRequirement;
        
        if (attackValue != 0) details += "   attack:" + attackValue;
        
        if (armorPen != 0) details += "   armorPen:" + armorPen;
        
        if (thrownAttackValue != 0) details += "   throw:" + thrownAttackValue;
        
        if (rangedAttackValue != 0) details += "   ranged:" + rangedAttackValue;
        
        if (defenseValue != 0) details += "   defense:" + defenseValue;
        
        if (foodValue != 0) details += "   food:" + foodValue;
        
        if (healthBoost != 0) details += "   hpBoost:" + healthBoost;
        
        if (healthRegenBoost != 0) details += "   hpRegenBoost:" + healthRegenBoost;
        
        if (manaBoost != 0) details += "   manaBoost:" + manaBoost;
        
        if (manaRegenBoost != 0) details += "   manaRegenBoost:" + manaRegenBoost;
        
        return details;
    }
    
}
