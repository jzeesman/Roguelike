package rogue.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class NameScreen implements Screen {
    
    private static String name = "";
    
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.writeCenter("Name The Hero!", 1);
        terminal.writeCenter("-- Please Enter Your Name --", 5);
        if (name != null && name.length() > 0) terminal.writeCenter("[" + name + "]", 14);
        else terminal.writeCenter("[insert hero name here]", 14);
        terminal.writeCenter("-- press [enter] to continue --", 22);
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if ((key.getKeyCode() == KeyEvent.VK_BACK_SPACE || key.getKeyCode() == KeyEvent.VK_DELETE) && name.length() > 0) {
            String temp = name.substring(0, name.length() - 1);
            name = temp;
        }else{
            if((key.getKeyChar() >= 'a' && key.getKeyChar() <= 'z') || (key.getKeyChar() >= 'A' && key.getKeyChar() <= 'Z') || key.getKeyChar() == ' '){
                char c = key.getKeyChar();
                name += c;
            }
        }
        return (key.getKeyCode() == KeyEvent.VK_ENTER && name.length() > 0 ? new PlayScreen() : this);
    }
    
    public static String playerName() {
        return name;
    }
    
}
