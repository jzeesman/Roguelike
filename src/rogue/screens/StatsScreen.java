package rogue.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import rogue.Creature;


public class StatsScreen implements Screen {
    
    private Creature player;
    
    public StatsScreen(Creature player){
        this.player = player;
    }
    
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.clear();
        terminal.writeCenter("Your Info", 1);
                
        String weapon = (player.weapon() != null ? player.weapon().name() : "----");
        String armor = (player.armor() != null ? player.armor().name() : "----");
        String ring = (player.ring() != null ? player.ring().fullName() : "----");
        String charm = (player.charm() != null ? player.charm().fullName() : "----");
        
        int y = 3;
        terminal.write(String.format("-- %s --", NameScreen.playerName()), 2, y++); // y = 3
        terminal.write("Level: " + player.level(), 2, y++); // y = 4
        terminal.write("XP: " + player.xp(), 2, y++); // y = 5
        terminal.write("HP Regen: 1 per " + (int) Math.ceil(1000/player.regenHpPer1000()) + " turns", 2, y++); // y = 6
        terminal.write("Mana Regen: 1 per " + (int) Math.ceil(1000/player.regenManaPer1000()) + " turns", 2, y++); // y = 7
        terminal.write("-- Stats --", 2, y++); // y = 8
        terminal.write("Str: " + player.strength(), 2, y++); // y = 9
        terminal.write("Dex: " + player.dexterity(), 2, y++); // y = 10
        terminal.write("Attack: " + player.attackValue(), 2, y++); // y = 11
        terminal.write("Defense: " + player.defenseValue(), 2, y++); // y = 12
        terminal.write("Vision Radius: " + player.visionRadius(), 2, y++); // y = 13
        terminal.write("-- Equipment --", 2, y++); // y = 14
        terminal.write("Weapon: " + weapon, 2, y++); // y = 15
        terminal.write("Armor: " + armor, 2, y++); // y = 16
        terminal.write("Ring: " + ring, 2, y++); // y = 17
        terminal.write("Charm: " + charm, 2, y++); // y = 18
        
        terminal.writeCenter("-- press any key to continue --", 22);
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }
    
}
