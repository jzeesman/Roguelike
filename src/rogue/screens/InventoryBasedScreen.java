package rogue.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import rogue.Creature;
import rogue.Item;

public abstract class InventoryBasedScreen implements Screen {
    
    protected Creature player;
    private String letters;
    
    protected abstract String getVerb();
    
    protected abstract boolean isAcceptable(Item item);
    
    protected abstract Screen use(Item item);
    
    public InventoryBasedScreen(Creature player) {
        this.player = player;
        this.letters = "abcdefghijklmnopqrstuvwxyz";
    }
    
    @Override
    public void displayOutput(AsciiPanel terminal) {
        ArrayList<String> lines = getList();
        
        int y = 23 - lines.size();
        int x = 4;
        
        if (lines.size() > 0) terminal.clear(' ', x, y, 20, lines.size());
        
        for (String line : lines) {
            terminal.write(line, x, y++);
        }
        
        terminal.clear(' ', 0, 23, 80, 1);
        terminal.write("What would you like to " + getVerb() + "? -- [escape] to close menu --", 1, 23);
        
        terminal.repaint();
    }
    
    /**
     * Makes a list of all the acceptable items and letter for each
     * corresponding inventory slot
     */
    private ArrayList<String> getList() {
        ArrayList<String> lines = new ArrayList<String>();
        Item[] inventory = player.inventory().getItems();
        
        for (int i = 0; i < inventory.length; i++) {
            Item item = inventory[i];
            
            if (item == null || !isAcceptable(item)) continue;
            
            String line = letters.charAt(i) + " - " + item.glyph() + " " + player.nameOf(item);
            
            if (item == player.weapon() || item == player.armor() || item == player.ring() || item == player.charm()) line += " (equipped)";
            
            if (item.isCursed() && item.cursedVisible()) line += " (cursed)";
            
            if(item.strengthRequirement() > 0) line += (" [" + item.strengthRequirement() + "]");
            
            if(item.foodValue() > 0) line += (" (" + item.foodValue() + ")");
            
            lines.add(line);
        }
        return lines;
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        char c = key.getKeyChar();
        
        Item[] items = player.inventory().getItems();
        
        if (letters.indexOf(c) > -1 && items.length > letters.indexOf(c) && items[letters.indexOf(c)] != null
                && isAcceptable(items[letters.indexOf(c)]))
            return use(items[letters.indexOf(c)]);
        else if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
            return null;
        else
            return this;
    }
    
}
