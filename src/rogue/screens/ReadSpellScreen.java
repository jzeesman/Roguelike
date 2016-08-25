package rogue.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import rogue.Creature;
import rogue.Item;
import rogue.Spell;

public class ReadSpellScreen implements Screen {
    
    protected Creature player;
    private String letters;
    private Item item;
    private int sx;
    private int sy;
    
    public ReadSpellScreen(Creature player, int sx, int sy, Item item) {
        this.player = player;
        this.letters = "abcdefghijklmnopqrstuvwxyz";
        this.item = item;
        this.sx = sx;
        this.sy = sy;
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
        terminal.write("What would you like to read?", 2, 23);
        
        terminal.repaint();
    }
    
    private ArrayList<String> getList() {
        ArrayList<String> lines = new ArrayList<String>();
        
        for (int i = 0; i < item.writtenSpells().size(); i++) {
            Spell spell = item.writtenSpells().get(i);
            
            String line = letters.charAt(i) + " - " + spell.name() + " (" + spell.manaCost() + " mana)";
            
            lines.add(line);
        }
        return lines;
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        char c = key.getKeyChar();
        
        if (letters.indexOf(c) > -1 && item.writtenSpells().size() > letters.indexOf(c) && item.writtenSpells().get(letters.indexOf(c)) != null) {
            return use(item.writtenSpells().get(letters.indexOf(c)));
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            return null;
        } else {
            return this;
        }
    }
    
    protected Screen use(Spell spell){
        return new CastSpellScreen(player, "", sx, sy, spell);
    }
    
}
