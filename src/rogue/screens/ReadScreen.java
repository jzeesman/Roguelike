package rogue.screens;

import rogue.Creature;
import rogue.Item;

public class ReadScreen extends InventoryBasedScreen {
    
    private int sx;
    private int sy;
    
    public ReadScreen(Creature player, int sx, int sy) {
        super(player);
        this.sx = sx;
        this.sy = sy;
    }
    
    @Override
    protected String getVerb() {
        return "read";
    }
    
    @Override
    protected boolean isAcceptable(Item item) {
        return !item.writtenSpells().isEmpty() || item.spellEffect() != null;
    }
    
    @Override
    protected Screen use(Item item) {
        if(!item.writtenSpells().isEmpty())
            return new ReadSpellScreen(player, sx, sy, item);
        if(item.name().contains("identify") || item.name().contains("enchant") || item.name().contains("curse")){
            player.inventory().remove(item);
            return new EnchantScreen(player, item);
        }
        player.inventory().remove(item);
        return new CastSpellScreen(player, "", sx, sy, item.spell());
    }
    
}
