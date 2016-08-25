package rogue.screens;

import java.awt.event.KeyEvent;
import java.util.List;

import asciiPanel.AsciiPanel;

/**
 * The MessageScreen class displays the most recent messages for easy viewing.
 * 
 * @author juleszeesman
 *
 */
public class MessageScreen implements Screen {
    
    /** The list of messages to keep track of **/
    private List<String> messageList;
    
    /**
     * The constructor for the MessageScreen class
     * 
     * @param messageList
     *        - the list of messages to keep track of
     */
    public MessageScreen(List<String> messageList) {
        this.messageList = messageList;
    }
    
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.clear();
        terminal.writeCenter("Message Log", 1);
        int y = 3;
        terminal.write("-- Sorted By Most Recent --", 2, y++);
        for (int i = messageList.size() - 1; i >= 0; i--) {
            if (y < 20) {
                terminal.write(messageList.get(i), 2, y++);
            }
        }
        terminal.writeCenter("-- press any key to continue --", 22);
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }
    
}
