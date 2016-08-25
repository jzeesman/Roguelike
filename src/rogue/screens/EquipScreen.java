package rogue.screens;

import rogue.Creature;
import rogue.Item;

public class EquipScreen extends InventoryBasedScreen {
    
    public EquipScreen(Creature player) {
        super(player);
    }
    
    @Override
    protected String getVerb() {
        return "wear or wield";
    }
    
    @Override
    protected boolean isAcceptable(Item item) {
        return item.attackValue() > 0 || item.defenseValue() > 0 || item.isRing() || item.isCharm();
    }
    
    @Override
    protected Screen use(Item item) {
        player.equip(item);
        return null;
    }
    
}
