package rogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Enchantment {
    
    private Effect effect;
    
    private Item item;
    
    // ===============================================================================
    // Enchantment List
    // ===============================================================================
    
    // Weapon Enchantments
    
    private static List<String> goodWeaponPrefixes;
    private static List<String> badWeaponPrefixes;
    
    private static List<String> goodWeaponSuffixes;
    private static List<String> badWeaponSuffixes;
    
    // Armor Enchantments
    
    private static List<String> goodArmorPrefixes;
    private static List<String> badArmorPrefixes;
    
    private static List<String> goodArmorSuffixes;
    private static List<String> badArmorSuffixes;
    
    // Ring Enchantments
    
    private static List<String> goodRingSuffixes;
    private static List<String> badRingSuffixes;
    
    // Charm Enchantments
    
    private static List<String> goodCharmSuffixes;
    private static List<String> badCharmSuffixes;
    
    // ===============================================================================
    // Effect List
    // ===============================================================================
    
    // Weapon Effects
    
    private Map<String, Effect> goodWeaponPrefixEffects;
    private Map<String, Effect> badWeaponPrefixEffects;
    
    private Map<String, Effect> goodWeaponSuffixEffects;
    private Map<String, Effect> badWeaponSuffixEffects;
    
    // Armor Effects
    
    private Map<String, Effect> goodArmorPrefixEffects;
    private Map<String, Effect> badArmorPrefixEffects;
    
    private Map<String, Effect> goodArmorSuffixEffects;
    private Map<String, Effect> badArmorSuffixEffects;
    
    // Ring Effects
    
    private Map<String, Effect> goodRingSuffixEffects;
    private Map<String, Effect> badRingSuffixEffects;
    
    // Charm Effects
    
    private Map<String, Effect> goodCharmSuffixEffects;
    private Map<String, Effect> badCharmSuffixEffects;
    
    public Enchantment(Item item) {
        initAll();
        fillLists();
        fillEnchantments();
        this.item = item;
    }
    
    private void initAll() {
        goodWeaponPrefixes = new ArrayList<String>();
        badWeaponPrefixes = new ArrayList<String>();
        goodWeaponSuffixes = new ArrayList<String>();
        badWeaponSuffixes = new ArrayList<String>();
        goodArmorPrefixes = new ArrayList<String>();
        badArmorPrefixes = new ArrayList<String>();
        goodArmorSuffixes = new ArrayList<String>();
        badArmorSuffixes = new ArrayList<String>();
        goodRingSuffixes = new ArrayList<String>();
        badRingSuffixes = new ArrayList<String>();
        goodCharmSuffixes = new ArrayList<String>();
        badCharmSuffixes = new ArrayList<String>();
        
        goodWeaponPrefixEffects = new HashMap<String, Effect>();
        badWeaponPrefixEffects = new HashMap<String, Effect>();
        goodWeaponSuffixEffects = new HashMap<String, Effect>();
        badWeaponSuffixEffects = new HashMap<String, Effect>();
        goodArmorPrefixEffects = new HashMap<String, Effect>();
        badArmorPrefixEffects = new HashMap<String, Effect>();
        goodArmorSuffixEffects = new HashMap<String, Effect>();
        badArmorSuffixEffects = new HashMap<String, Effect>();
        goodRingSuffixEffects = new HashMap<String, Effect>();
        badRingSuffixEffects = new HashMap<String, Effect>();
        goodCharmSuffixEffects = new HashMap<String, Effect>();
        badCharmSuffixEffects = new HashMap<String, Effect>();
    }
    
    private void fillLists() {
        
        // Weapon
        
        goodWeaponPrefixes.add("blazing "); // chance to ignite enemy
        goodWeaponPrefixes.add("venomous "); // chance to poison enemy
        goodWeaponPrefixes.add("deadly "); // chance to instant kill enemy
        
        badWeaponPrefixes.add("dull "); // reduces attack of wielder
        badWeaponPrefixes.add("clumsy "); // chance to drop item
        
        goodWeaponSuffixes.add(" of burning"); // chance to ignite enemy
        goodWeaponSuffixes.add(" of poison"); // chance to poison enemy
        goodWeaponSuffixes.add(" of instant death"); // chance to instant kill enemy
        
        badWeaponSuffixes.add(" of weakness"); // reduces attack of wielder
        badWeaponSuffixes.add(" of clumsiness"); // chance to drop item
        
        // Armor
        
        goodArmorPrefixes.add("sturdy "); // increases defense of wearer
        goodArmorPrefixes.add("healthy "); // increases max hp of wearer
        
        badArmorPrefixes.add("flimsy "); // decreases defense of wearer
        
        goodArmorSuffixes.add(" of protection"); // increases defense of wearer
        goodArmorSuffixes.add(" of vigor"); // increases max hp of wearer
        
        badArmorSuffixes.add(" of frailty"); // decreases defense of wearer
        
        // Ring
        
        goodRingSuffixes.add(" of restoration"); // regen
        goodRingSuffixes.add(" of stealth"); // increases stealth (slightly)
        goodRingSuffixes.add(" of agility"); // increases dexterity
        
        badRingSuffixes.add(" of cowardice"); // great increase defense, costs attack
        badRingSuffixes.add(" of foolish bravery"); // great increase attack, costs defense
        badRingSuffixes.add(" of dimwitted strength"); // great increase strength, costs mana
        
        // Charm
        
        goodCharmSuffixes.add(" of power"); // attack (small increase)
        goodCharmSuffixes.add(" of fortitude"); // defense (small increase)
        goodCharmSuffixes.add(" of vision"); // slight increased vision radius
        goodCharmSuffixes.add(" of magic"); // mana boost
        
        badCharmSuffixes.add(" of hunger"); // hp boost, increased hunger
        badCharmSuffixes.add(" of darkness"); // slightly reduce vision slightly increase stealth
        badCharmSuffixes.add(" of shadows"); // greatly increases stealth costs vision
        badCharmSuffixes.add(" of archery"); // greatly increases dexterity at the cost of attack
        
        
        
    }
    
    // TODO: Remember, enchantments aren't just effects
    // You can make an enchantment a simple item modifier
    
    private void fillEnchantments() {
        // ===============================================================================
        // Good Weapon Prefixes
        // ===============================================================================
        
        // chance of igniting target
        // targets the victim
        goodWeaponPrefixEffects.put("blazing ", new Effect(5) {
            
            public void start(Creature creature) {
                if(Math.random() * 100 < 60)
                    creature.doAction("burn");
                else
                    duration = 0;
            }
            
            public void update(Creature creature) {
                super.update(creature);
                creature.doAction("take burn damage");
                creature.modifyHp(-3, "Burned to death.");
            }
        });
        
        // chance of poison
        // targets the victim
        goodWeaponPrefixEffects.put("venomous ", new Effect(5) {
            
            public void start(Creature creature) {
                if(Math.random() * 100 < 60)
                    creature.doAction("look sick");
                else
                    duration = 0;
            }
            
            public void update(Creature creature) {
                super.update(creature);
                creature.doAction("take poison damage");
                creature.modifyHp(-1, "Died of poison.");
            }
        });
        
        // chance of instant kill
        // targets the victim
        goodWeaponPrefixEffects.put("deadly ", new Effect(1) {
            
            public void start(Creature creature) {
                if (Math.random() * 100 < 1) {
                    creature.doAction("dies instantly");
                    creature.modifyHp(-1000, "Killed by an instant death enchantment.");
                }
            }
        });
        
        // ===============================================================================
        // Bad Weapon Prefixes
        // ===============================================================================
        
        // does less damage
        // targets the wearer
        badWeaponPrefixEffects.put("dull ", new Effect(1) {
            
            public void start(Creature creature) {
                creature.modifyAttackValue(-2);
            }
            
            public void update(Creature creature) {
                if(creature.weapon() != null){
                    if (!creature.weapon().equals(item)) {
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature) {
                creature.modifyAttackValue(2);
                duration = 1;
            }
        });
        
        // player can randomly drop this item
        // targets the wearer
        badWeaponPrefixEffects.put("clumsy ", new Effect(1) {
            
            public void update(Creature creature) {
                if(creature.weapon() != null){
                    if (!creature.weapon().equals(item)) {
                        super.update(creature);
                    } else {
                        if (Math.random() * 100 < 3) {
                            creature.drop(item);
                        }
                    }
                }
            }
            
            public void end(Creature creature){
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Good Weapon Suffixes
        // ===============================================================================
        
        // chance of igniting target
        // targets the victim
        goodWeaponSuffixEffects.put(" of burning", new Effect(5) {
            
            public void start(Creature creature) {
                if(Math.random() * 100 < 60)
                    creature.doAction("burn");
                else
                    duration = 0;
            }
            
            public void update(Creature creature) {
                super.update(creature);
                creature.doAction("take burn damage");
                creature.modifyHp(-3, "Burned to death.");
            }
        });
        // chance of poison
        // targets the victim
        goodWeaponSuffixEffects.put(" of poison", new Effect(5) {
            
            public void start(Creature creature) {
                if(Math.random() * 100 < 60)
                    creature.doAction("look sick");
                else
                    duration = 0;
            }
            
            public void update(Creature creature) {
                super.update(creature);
                creature.doAction("take poison damage");
                creature.modifyHp(-1, "Died of poison.");
            }
        });
        
        // chance of instant kill
        // targets the victim
        goodWeaponSuffixEffects.put(" of instant death", new Effect(1) {
            
            public void start(Creature creature) {
                if (Math.random() * 100 < 1) {
                    creature.doAction("dies instantly");
                    creature.modifyHp(-1000, "Killed by an instant death enchantment.");
                }
            }
        });
        
        // ===============================================================================
        // Bad Weapon Suffixes
        // ===============================================================================
        
        // does less damage
        // targets the wearer
        badWeaponSuffixEffects.put(" of weakness", new Effect(1) {
            
            public void start(Creature creature) {
                creature.modifyAttackValue(-2);
            }
            
            public void update(Creature creature) {
                if(creature.weapon() != null){
                    if (!creature.weapon().equals(item)) {
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature) {
                creature.modifyAttackValue(2);
                duration = 1;
            }
        });
        
        // player can randomly drop this item
        badWeaponSuffixEffects.put(" of clumsiness", new Effect(1) {
            
            public void update(Creature creature) {
                if(creature.weapon() != null){
                    if (!creature.weapon().equals(item)) {
                        super.update(creature);
                    } else {
                        if (Math.random() * 100 < 3) {
                            creature.drop(item);
                        }
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Good Armor Prefixes
        // ===============================================================================
        
        // increased defense
        // targets the wearer
        goodArmorPrefixEffects.put("sturdy ", new Effect(1) {
            
            public void start(Creature creature) {
                creature.modifyDefenseValue(2);
            }
            
            public void update(Creature creature) {
                if(creature.armor() != null){
                    if (!creature.armor().equals(item)) {
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature) {
                creature.modifyDefenseValue(-2);
                duration = 1;
            }
        });
        
        // health bonus
        goodArmorPrefixEffects.put("healthy ", new Effect(1) {
            
            public void start(Creature creature) {
                creature.modifyMaxHp(25);
            }
            
            public void update(Creature creature) {
                if(creature.armor() != null){
                    if (!creature.armor().equals(item)) {
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature) {
                creature.modifyMaxHp(-25);
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Bad Armor Prefixes
        // ===============================================================================
        
        // decreased defense
        // targets the wearer
        badArmorPrefixEffects.put("flimsy ", new Effect(1) {
            
            public void start(Creature creature) {
                creature.modifyDefenseValue(-2);
            }
            
            public void update(Creature creature) {
                if(creature.armor() != null){
                    if (!creature.armor().equals(item)) {
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature) {
                creature.modifyDefenseValue(2);
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Good Armor Suffixes
        // ===============================================================================
        
        // increased defense
        // targets the wearer
        goodArmorSuffixEffects.put(" of protection", new Effect(1) {
            
            public void start(Creature creature) {
                creature.modifyDefenseValue(2);
            }
            
            public void update(Creature creature) {
                if(creature.armor() != null){
                    if (!creature.armor().equals(item)) {
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature) {
                creature.modifyDefenseValue(-2);
                duration = 1;
            }
        });
        
        // health bonus
        // targets the wearer
        goodArmorSuffixEffects.put(" of vigor", new Effect(1) {
            
            public void start(Creature creature) {
                creature.modifyMaxHp(25);
            }
            
            public void update(Creature creature) {
                if(creature.armor() != null){
                    if (!creature.armor().equals(item)) {
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature) {
                creature.modifyMaxHp(-25);
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Bad Armor Suffixes
        // ===============================================================================
        
        // defense decrease
        // targets the wearer
        badArmorSuffixEffects.put(" of frailty", new Effect(1) {
            
            public void start(Creature creature) {
                creature.modifyDefenseValue(-2);
            }
            
            public void update(Creature creature) {
                if(creature.armor() != null){
                    if (!creature.armor().equals(item)) {
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature) {
                creature.modifyDefenseValue(2);
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Good Ring Suffixes
        // ===============================================================================
        
        // increases hp regen
        // targets wearer
        goodRingSuffixEffects.put(" of restoration", new Effect(1){
            public void start(Creature creature){
                creature.modifyRegenHpPer1000(20);
                creature.modifyRegenManaPer1000(10);
            }
            
            public void update(Creature creature){
                if(creature.ring() != null){
                    if(!creature.ring().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyRegenHpPer1000(-20);
                creature.modifyRegenManaPer1000(-10);
                duration = 1;
            }
        });
        
        // increases stealth
        // targets wearer?
        goodRingSuffixEffects.put(" of stealth", new Effect(1){
            public void start(Creature creature){
                creature.modifyStealthRadius(-1);
            }
            
            public void update(Creature creature){
                if(creature.ring() != null){
                    if(!creature.ring().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyStealthRadius(1);
                duration = 1;
            }
        });
        
        goodRingSuffixEffects.put(" of agility", new Effect(1){
            public void start(Creature creature){
                creature.modifyDexterity(2);
            }
            
            public void update(Creature creature){
                if(creature.ring() != null){
                    if(!creature.ring().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyDexterity(-2);
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Bad Ring Suffixes
        // ===============================================================================
        
        // increases defense at the cost of attack
        // targets wearer
        badRingSuffixEffects.put(" of cowardice", new Effect(1){
            public void start(Creature creature){
                creature.modifyAttackValue(-3);
                creature.modifyDefenseValue(3);
            }
            
            public void update(Creature creature){
                if(creature.ring() != null){
                    if(!creature.ring().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyAttackValue(3);
                creature.modifyDefenseValue(-3);
                duration = 1;
            }
        });
        
        // increases attack at the cost of defense
        // targets wearer
        badRingSuffixEffects.put(" of foolish bravery", new Effect(1){
            public void start(Creature creature){
                creature.modifyAttackValue(3);
                creature.modifyDefenseValue(-3);
            }
            
            public void update(Creature creature){
                if(creature.ring() != null){
                    if(!creature.ring().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyAttackValue(-3);
                creature.modifyDefenseValue(3);
                duration = 1;
            }
        });
        
        // increases strength at the cost of mana
        // targets wearer
        badRingSuffixEffects.put(" of dimwitted strength", new Effect(1){
            public void start(Creature creature){
                creature.modifyStrength(3);
                creature.modifyMaxMana(-25);
            }
            
            public void update(Creature creature){
                if(creature.ring() != null){
                    if(!creature.ring().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyStrength(-3);
                creature.modifyMaxMana(25);
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Good Charm Suffixes
        // ===============================================================================
        
        // increases attack
        // targets wearer
        goodCharmSuffixEffects.put(" of power", new Effect(1){
            public void start(Creature creature){
                creature.modifyAttackValue(3);
            }
            
            public void update(Creature creature){
                if(creature.charm() != null){
                    if(!creature.charm().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyAttackValue(-3);
                duration = 1;
            }
        });
        
        // increases defense
        // targets wearer
        goodCharmSuffixEffects.put(" of fortitude", new Effect(1){
            public void start(Creature creature){
                creature.modifyDefenseValue(3);
            }
            
            public void update(Creature creature){
                if(creature.charm() != null){
                    if(!creature.charm().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyDefenseValue(-3);
                duration = 1;
            }
        });
        
        // increases vision radius
        // targets wearer
        goodCharmSuffixEffects.put(" of vision", new Effect(1){
            public void start(Creature creature){
                creature.modifyVisionRadius(2);
            }
            
            public void update(Creature creature){
                if(creature.charm() != null){
                    if(!creature.charm().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyVisionRadius(-2);
                duration = 1;
            }
        });
        
        // increases max mana
        // targets wearer
        goodCharmSuffixEffects.put(" of magic", new Effect(1){
            public void start(Creature creature){
                creature.modifyMaxMana(15);
            }
            
            public void update(Creature creature){
                if(creature.charm() != null){
                    if(!creature.charm().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyMaxMana(-15);
                duration = 1;
            }
        });
        
        // ===============================================================================
        // Bad Charm Suffixes
        // ===============================================================================
        
        // increases health, become hungrier faster
        // targets wearer
        badCharmSuffixEffects.put(" of hunger", new Effect(1){
            public void start(Creature creature){
                creature.modifyMaxHp(50);
            }
            
            public void update(Creature creature){
                if(creature.charm() != null){
                    if(!creature.charm().equals(item)){
                        super.update(creature);
                    }
                    creature.modifyFood(-4);
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyMaxHp(-50);
                duration = 1;
            }
        });
        
        // slightly reduced vision, increased stealth
        // targets wearer?
        badCharmSuffixEffects.put(" of darkness", new Effect(1){
            public void start(Creature creature){
                creature.modifyVisionRadius(-1);
                creature.modifyStealthRadius(-1);
            }
            
            public void update(Creature creature){
                if(creature.charm() != null){
                    if(!creature.charm().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyVisionRadius(1);
                creature.modifyStealthRadius(1);
                duration = 1;
            }
        });
        
        // greatly increases stealth, greatly costs vision
        // targets wearer?
        badCharmSuffixEffects.put(" of shadows", new Effect(1){
            public void start(Creature creature){
                creature.modifyVisionRadius(-3);
                creature.modifyStealthRadius(-3);
            }
            
            public void update(Creature creature){
                if(creature.charm() != null){
                    if(!creature.charm().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyVisionRadius(3);
                creature.modifyStealthRadius(-3);
                duration = 1;
            }
        });
        
        badCharmSuffixEffects.put(" of archery", new Effect(1){
            public void start(Creature creature){
                creature.modifyDexterity(5);
                creature.modifyAttackValue(-3);
            }
            
            public void update(Creature creature){
                if(creature.charm() != null){
                    if(!creature.charm().equals(item)){
                        super.update(creature);
                    }
                }else{
                    super.update(creature);
                }
            }
            
            public void end(Creature creature){
                creature.modifyDexterity(-5);
                creature.modifyAttackValue(3);
                duration = 1;
            }
        });
        
    }
    
    public void generateEnchantedItem(Item item) {
        generateRandomEnchantment(item);
        item.setEnchantment(this.effect);
    }
    
    private void generateRandomEnchantment(Item item) {
        Random r = new Random();
        if (item.attackValue() != 0 && !item.isRing() && !item.isCharm()) {
            if (item.defenseValue() == 0 || item.attackValue() > item.defenseValue()) {
                double rand = Math.random();
                if (rand > 0.6) {
                    // 33% of normal
                    return;
                } else if (rand > 0.3) {
                    // 33% of upgrade
                    double rand2 = Math.random();
                    if (rand2 > 0.5) {
                        // 50% of good weapon prefix
                        int i = r.nextInt(goodWeaponPrefixes.size());
                        String key = goodWeaponPrefixes.get(i);
                        item.setFullName(key, null);
                        this.effect = goodWeaponPrefixEffects.get(key);
                    } else {
                        // 50% of good weapon suffix
                        int i = r.nextInt(goodWeaponSuffixes.size());
                        String key = goodWeaponSuffixes.get(i);
                        item.setFullName(null, key);
                        this.effect = goodWeaponSuffixEffects.get(key);
                    }
                } else {
                    // 33% of downgrade
                    double rand2 = Math.random();
                    if (rand2 > 0.5) {
                        // 50% of bad weapon prefix
                        int i = r.nextInt(badWeaponPrefixes.size());
                        String key = badWeaponPrefixes.get(i);
                        item.setFullName(key, null);
                        this.effect = badWeaponPrefixEffects.get(key);
                        double rand3 = Math.random();
                        if(rand3 < 0.2){
                            item.setCursed(true);
                        }
                    } else {
                        // 50% of bad weapon suffix
                        int i = r.nextInt(badWeaponSuffixes.size());
                        String key = badWeaponSuffixes.get(i);
                        item.setFullName(null, key);
                        this.effect = badWeaponSuffixEffects.get(key);
                        double rand3 = Math.random();
                        if(rand3 < 0.2){
                            item.setCursed(true);
                        }
                    }
                }
            }
        } else if (item.defenseValue() != 0 && !item.isRing() && !item.isCharm()) {
            double rand = Math.random();
            if (rand > 0.7) {
                // 33% of normal
                return;
            } else if (rand > 0.3) {
                // 33% of upgrade
                double rand2 = Math.random();
                if (rand2 > 0.5) {
                    // 50% of good armor prefix
                    int i = r.nextInt(goodArmorPrefixes.size());
                    String key = goodArmorPrefixes.get(i);
                    item.setFullName(key, null);
                    this.effect = goodArmorPrefixEffects.get(key);
                } else {
                    // 50% of good armor suffix
                    int i = r.nextInt(goodArmorSuffixes.size());
                    String key = goodArmorSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = goodArmorSuffixEffects.get(key);
                }
            } else {
                // 33% of downgrade
                double rand2 = Math.random();
                if (rand2 > 0.5) {
                    // 50% of bad armor prefix
                    int i = r.nextInt(badArmorPrefixes.size());
                    String key = badArmorPrefixes.get(i);
                    item.setFullName(key, null);
                    this.effect = badArmorPrefixEffects.get(key);
                    double rand3 = Math.random();
                    if(rand3 < 0.2){
                        item.setCursed(true);
                    }
                } else {
                    // 50% of bad armor suffix
                    int i = r.nextInt(badArmorSuffixes.size());
                    String key = badArmorSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = badArmorSuffixEffects.get(key);
                    double rand3 = Math.random();
                    if(rand3 < 0.2){
                        item.setCursed(true);
                    }
                }
            }
        } else if(item.isRing()){
            double rand = Math.random();
            double p1 = goodRingSuffixes.size() / (goodRingSuffixes.size() + badRingSuffixes.size());
            double p2 = badRingSuffixes.size() / (goodRingSuffixes.size() + badRingSuffixes.size());
            if(rand > (Math.max(p1, p2))){
                if(p1 > p2 || p1 == p2){
                    int i = r.nextInt(goodRingSuffixes.size());
                    String key = goodRingSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = goodRingSuffixEffects.get(key);
                }else{
                    int i = r.nextInt(badRingSuffixes.size());
                    String key = badRingSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = badRingSuffixEffects.get(key);
                    double rand2 = Math.random();
                    if(rand2 > 0.4){
                        item.setCursed(true);
                    }
                }
            } else if(rand > (Math.min(p1, p2))){
                if(p1 < p2 || p1 == p2){
                    int i = r.nextInt(goodRingSuffixes.size());
                    String key = goodRingSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = goodRingSuffixEffects.get(key);
                }else{
                    int i = r.nextInt(badRingSuffixes.size());
                    String key = badRingSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = badRingSuffixEffects.get(key);
                    double rand2 = Math.random();
                    if(rand2 > 0.4){
                        item.setCursed(true);
                    }
                }
            }
        } else if(item.isCharm()){
            double rand = Math.random();
            double p1 = goodCharmSuffixes.size() / (goodCharmSuffixes.size() + badCharmSuffixes.size());
            double p2 = badCharmSuffixes.size() / (goodCharmSuffixes.size() + badCharmSuffixes.size());
            if(rand > (Math.max(p1, p2))){
                if(p1 > p2 || p1 == p2){
                    int i = r.nextInt(goodCharmSuffixes.size());
                    String key = goodCharmSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = goodCharmSuffixEffects.get(key);
                }else{
                    int i = r.nextInt(badCharmSuffixes.size());
                    String key = badCharmSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = badCharmSuffixEffects.get(key);
                    double rand2 = Math.random();
                    if(rand2 > 0.4){
                        item.setCursed(true);
                    }
                }
            } else if(rand > (Math.min(p1, p2))){
                if(p1 < p2 || p1 == p2){
                    int i = r.nextInt(goodCharmSuffixes.size());
                    String key = goodCharmSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = goodCharmSuffixEffects.get(key);
                }else{
                    int i = r.nextInt(badCharmSuffixes.size());
                    String key = badCharmSuffixes.get(i);
                    item.setFullName(null, key);
                    this.effect = badCharmSuffixEffects.get(key);
                    double rand2 = Math.random();
                    if(rand2 > 0.4){
                        item.setCursed(true);
                    }
                }
            }
        }
    }
    
    public static List<String> getGoodWeaponPrefixes() {
        return goodWeaponPrefixes;
    }
    
    public static List<String> getBadWeaponPrefixes() {
        return badWeaponPrefixes;
    }
    
    public static List<String> getGoodWeaponSuffixes() {
        return goodWeaponSuffixes;
    }
    
    public static List<String> getBadWeaponSuffixes() {
        return badWeaponSuffixes;
    }
    
    public static List<String> getGoodArmorPrefixes() {
        return goodArmorPrefixes;
    }
    
    public static List<String> getBadArmorPrefixes() {
        return badArmorPrefixes;
    }
    
    public static List<String> getGoodArmorSuffixes() {
        return goodArmorSuffixes;
    }
    
    public static List<String> getBadArmorSuffixes() {
        return badArmorSuffixes;
    }
    
    public static List<String> getGoodRingSuffixes(){
        return goodRingSuffixes;
    }
    
    public static List<String> getBadRingSuffixes(){
        return badRingSuffixes;
    }
    
    public static List<String> getGoodCharmSuffixes(){
        return goodCharmSuffixes;
    }
    
    public static List<String> getBadCharmSuffixes(){
        return badCharmSuffixes;
    }
    
}
