package rogue;

import java.applet.Applet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import asciiPanel.AsciiPanel;
import rogue.screens.Screen;
import rogue.screens.StartScreen;

/**
 * The AppletMain class runs the game when launched as an Applet
 */
public class AppletMain extends Applet implements KeyListener{
    
    /** Generated serial version UID **/
    private static final long serialVersionUID = -161280856282164997L;
    
    /** AsciiPanel object **/
    private AsciiPanel terminal;
    /** Screen object **/
    private Screen screen;
    
    /**
     * The main constructor for the AppletMain class
     */
    public AppletMain(){
        super();
        terminal = new AsciiPanel();
        add(terminal);
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
    }
    
    @Override
    public void init(){
        super.init();
        this.setSize(terminal.getWidth() + 20, terminal.getHeight() + 20);
    }
    
    @Override
    public void repaint(){
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        screen = screen.respondToUserInput(e);
        repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e){
    }
    
    @Override
    public void keyTyped(KeyEvent e){
    }
    
}
