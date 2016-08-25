package rogue;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import asciiPanel.AsciiPanel;
import rogue.screens.Screen;
import rogue.screens.StartScreen;

/**
 * The ApplicationMain class runs the game when run as a Java Application
 */
public class ApplicationMain extends JFrame implements KeyListener {
    
    /** Generated serial version UID **/
    private static final long serialVersionUID = 1633385179150976889L;
    
    /** AsciiPanel object **/
    private AsciiPanel terminal;
    /** Screen object **/
    private Screen screen;
    
    /**
     * The main constructor for the ApplicationMain class
     */
    public ApplicationMain() {
        super();
        terminal = new AsciiPanel();
        add(terminal);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
    }
    
    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    /**
     * The main method for the game
     * 
     * @param args
     *        - any additional command line arguments
     */
    public static void main(String[] args) {
        ApplicationMain app = new ApplicationMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }
}
