package rogue.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class ClassScreen implements Screen {
    
    private static String className;

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.writeCenter("-- Please Select A Class --", 1);
        int y = 8;
        terminal.write(" [1]  Knight", 5, y++);
        terminal.write(" [2]  Paladin", 5, y++);
        terminal.write(" [3]  Mage", 5, y++);
        terminal.write(" [4]  Rogue", 5, y++);
        terminal.write(" [5]  Ranger", 5, y++);
        terminal.write(" [6]  Deprived", 5, y++);
        terminal.writeCenter("-- choose a class to continue --", 22);
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_1) className = "knight";
        if (key.getKeyCode() == KeyEvent.VK_2) className = "paladin";
        if (key.getKeyCode() == KeyEvent.VK_3) className = "mage";
        if (key.getKeyCode() == KeyEvent.VK_4) className = "rogue";
        if (key.getKeyCode() == KeyEvent.VK_5) className = "ranger";
        if (key.getKeyCode() == KeyEvent.VK_6) className = "deprived";
        return (key.getKeyCode() == KeyEvent.VK_1 || key.getKeyCode() == KeyEvent.VK_2
                || key.getKeyCode() == KeyEvent.VK_3 || key.getKeyCode() == KeyEvent.VK_4
                || key.getKeyCode() == KeyEvent.VK_5 || key.getKeyCode() == KeyEvent.VK_6) ? new NameScreen() : this;
    }
    
    public static String className() {
        return className;
    }
    
}
