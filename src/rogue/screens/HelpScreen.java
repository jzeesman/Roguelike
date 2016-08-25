package rogue.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class HelpScreen implements Screen {
    
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.clear();
        terminal.writeCenter("Roguelike Help", 1);
        terminal.write("Descend into the Catacombs, find the king's soul, and return to", 1, 3);
        terminal.write("the surface to win. Use whatever you can to avoid death.", 1, 4);
        
        int y = 5;
        terminal.write("-- Movement --", 2, y++);
        terminal.write("[k] up, [j] down, [h] left, [l] right", 2, y++);
        terminal.write("[y] up-left, [u] up-right", 2, y++);
        terminal.write("[b] down-left, [n] down-right", 2, y++);
        terminal.write("[<] or [>] up/down stair", 2, y++);
        terminal.write("-- Actions --", 2, y++);
        terminal.write("[q] quaff a potion", 2, y++);
        terminal.write("[w] wear or wield", 2, y++);
        terminal.write("[e] eat, [r] read", 2, y++);
        terminal.write("[t] throw, [d] drop", 2, y++);
        terminal.write("[f] fire a ranged weapon", 2, y++);
        terminal.write("[g] or [,] to pick up", 2, y++);
        terminal.write("[;] look around, [m] to look at notifications", 2, y++);
        terminal.write("[x] examine your items", 2, y++);
        terminal.write("[/] help, ['] view your stats", 2, y++);
        
        terminal.writeCenter("-- press any key to continue --", 22);
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }
    
}
