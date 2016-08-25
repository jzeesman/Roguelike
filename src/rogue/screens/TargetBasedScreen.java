package rogue.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import rogue.Creature;
import rogue.Line;
import rogue.Point;
import rogue.World;

public abstract class TargetBasedScreen implements Screen {
    
    protected Creature player;
    protected String caption;
    private int sx;
    private int sy;
    private int x;
    private int y;
    
    public TargetBasedScreen(Creature player, String caption, int sx, int sy) {
        this.player = player;
        this.caption = caption;
        this.sx = sx;
        this.sy = sy;
    }
    
    //&& player.creature(p.x, p.y, player.z) != null
    
    @Override
    public void displayOutput(AsciiPanel terminal) {
        Line line = new Line(sx, sy, sx + x, sy + y);
        for (Point p : line.getPoints()) {
            if (p.x < 0 || p.x >= PlayScreen.screenWidth() || p.y < 0 || p.y >= PlayScreen.screenHeight() + 4) continue;
            
            
            if(p.equals(line.getPoints().get(line.size() - 1))){
                terminal.write('*', p.x, p.y, AsciiPanel.brightRed);
                if(player.creature(player.x + x, player.y + y, player.z) != null)
                    terminal.writeCenter("" + player.creature(player.x + x, player.y + y, player.z).name(), 22);
            }else{                
                terminal.write('*', p.x, p.y, AsciiPanel.brightMagenta);
                terminal.writeCenter(" ", 22);
            }
        }
        
        terminal.clear(' ', 0, PlayScreen.screenHeight() + 3, PlayScreen.screenWidth(), 1);
        terminal.write(caption, 0, PlayScreen.screenHeight() + 3);
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        
        int px = x;
        int py = y;
        
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_H:
                x--;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_L:
                x++;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_J:
                y--;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_K:
                y++;
                break;
            case KeyEvent.VK_Y:
                x--;
                y--;
                break;
            case KeyEvent.VK_U:
                x++;
                y--;
                break;
            case KeyEvent.VK_B:
                x--;
                y++;
                break;
            case KeyEvent.VK_N:
                x++;
                y++;
                break;
            case KeyEvent.VK_ENTER:
                selectWorldCoordinate(player.x + x, player.y + y, sx + x, sy + y);
                return null;
            case KeyEvent.VK_ESCAPE:
                return null;
        }
        
        if (player.x + x >= (World.width() - 1)) {
            x = (World.width() - 1) - player.x;
        } else if (x < 0 && -x > player.x) {
            x = -player.x;
        }
        if (player.y + y >= (World.height() - 1)) {
            y = (World.height() - 1) - player.y;
        } else if (y < 0 && -y > player.y) {
            y = -player.y;
        }
        
        if (!isAcceptable(player.x + x, player.y + y)) {
            x = px;
            y = py;
        }
        
        enterWorldCoordinate(player.x + x, player.y + y, sx + x, sy + y);
        
        return this;
    }
    
    public boolean isAcceptable(int x, int y) {
        return true;
    }
    
    public void enterWorldCoordinate(int x, int y, int screenX, int screenY) {
    }
    
    public void selectWorldCoordinate(int x, int y, int screenX, int screenY) {
    }
    
}
