package rogue;


public class Class {
    
    private int maxHp;
    private int hpRegen;
    private int maxMana;
    private int manaRegen;
    private int attackValue;
    private int defenseValue;
    private int strength;
    private int dexterity;
    private Inventory startingItems;
    
    public Class(int maxHp, int hpRegen, int maxMana, int manaRegen, int attack, int defense, int strength, int dexterity, Item[] items){
        this.maxHp = maxHp;
        this.hpRegen = hpRegen;
        this.maxMana = maxMana;
        this.manaRegen = manaRegen;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.strength = strength;
        this.dexterity = dexterity;
        startingItems = new Inventory(20);
        for(Item i : items){
            if(i != null){
                startingItems.add(i);
            }
        }
    }
    
    public int maxHp(){
        return maxHp;
    }
    
    public int hpRegen(){
        return hpRegen;
    }
    
    public int maxMana(){
        return maxMana;
    }
    
    public int manaRegen(){
        return manaRegen;
    }
    
    public int attackValue(){
        return attackValue;
    }
    
    public int defenseValue(){
        return defenseValue;
    }
    
    public int strength(){
        return strength;
    }
    
    public int dexterity(){
        return dexterity;
    }
    
    public Inventory startingItems(){
        return startingItems;
    }
}
