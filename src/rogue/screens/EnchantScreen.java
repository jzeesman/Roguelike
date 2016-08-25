package rogue.screens;

import rogue.Creature;
import rogue.Item;


public class EnchantScreen extends InventoryBasedScreen {
    
    private Item scroll;
    
    public EnchantScreen(Creature player, Item scroll){
        super(player);
        this.scroll = scroll;
    }
    
    @Override
    protected String getVerb() {
        return "enchant";
    }
    
    @Override
    protected boolean isAcceptable(Item item) {
        if(scroll.name().contains("curse")) return (item.isWeapon() || item.isArmor() || item.isAccessory());
        if(scroll.name().contains("enchantment")) return (item.isWeapon() || item.isArmor());
        if(scroll.name().contains("identify")) return ((item.writtenSpells().isEmpty() && item.spellEffect() == null && !item.isFood()) || item.isWeapon() || item.isArmor() || item.isAccessory() || item.quaffEffect() != null);
        return true;
    }
    
    @Override
    protected Screen use(Item item) {
        if(scroll.fullName().contains("curse")){
            if(item.isCursed()) player.notify("The curse has been lifted from %s!", player.nameOf(item));
            item.setCursed(false);
            item.setCursedVisible(false);
        }
        if(scroll.fullName().contains("enchantment")){
            player.notify("The %s has been enchanted!", player.nameOf(item));
            item.modifyAttackValue((item.attackValue() / 2));
            item.modifyDefenseValue((item.defenseValue() / 2));
            item.modifyRangedAttackValue((item.rangedAttackValue() / 2));
            item.modifyStrengthRequirement(-1);
        }
        if(scroll.fullName().contains("identify")){
            player.learnName(item);
            item.setCursedVisible(true);
            item.setEffectVisible(true);
            player.notify("The %s has been identified!", item.fullName());
        }
        return null;
    }
    
}
