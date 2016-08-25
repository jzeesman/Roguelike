package rogue.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import asciiPanel.AsciiPanel;
import rogue.Creature;
import rogue.FieldOfView;
import rogue.Item;
import rogue.StuffFactory;
import rogue.Tile;
import rogue.World;
import rogue.WorldBuilder;

/**
 * The PlayScreen class represents the main world screen
 */
public class PlayScreen implements Screen {
    
    /** The world in which everything takes place **/
    private World world;
    /** The creature that represents the player **/
    private Creature player;
    
    /** The screen's width **/
    private static int screenWidth;
    /** The screen's height **/
    private static int screenHeight;
    
    /** The list of messages to pass to the player **/
    private List<String> messages;
    
    /** The list of messages to store in the MessageScreen **/
    private List<String> messageList;
    
    /** FieldOfView object used in {@linkplain #displayOutput(AsciiPanel)} **/
    private FieldOfView fov;
    
    /** Screen object that represents any subscreen that must be displayed **/
    private static Screen subscreen;
    
    private boolean shouldUpdate = false;
    
    /**
     * The general constructor for the PlayScreen class
     */
    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 20;
        messages = new ArrayList<String>();
        messageList = new ArrayList<String>();
        createWorld();
        fov = new FieldOfView(world);
        
        StuffFactory factory = new StuffFactory(world);
        createCreatures(factory);
        createItems(factory);
    }
    
    /**
     * Method used to create creatures<br>
     * (including the player)
     * 
     * @param factory
     *        - the StuffFactory object through which the creatures will be
     *        created
     */
    private void createCreatures(StuffFactory factory) {
        player = factory.newPlayer(messages, fov);
        
        for (int z = 0; z < World.depth(); z++) {
            for (int i = 0; i < 3; i++) {
//                factory.newFungus(z);
            }
            for (int i = 0; i < 8; i++) {
//                factory.newBat(z);
            }
            for (int i = 0; i < 3; i++) {
//                factory.newZombie(z, player);
            }
            for (int i = 0; i < 6; i++) {
//                factory.newGoblin(z, player);
            }
            for (int i = 0; i < 2; i++) {
//                if (Math.random() * 100 < 15) factory.newWeepingAngel(z, player);
            }
            for (int i = 0; i < 3; i++) {
//                factory.newTroll(z, player);
            }
            for (int i = 0; i < 3; i++) {
                // TODO: Add water generation
                // factory.newEel(z, player);
            }
        }
    }
    
    /**
     * Method used to create items<br>
     * 
     * @param factory
     *        - the StuffFactory object through which the creatures will be
     *        created
     */
    private void createItems(StuffFactory factory) {
        for (int z = 0; z < World.depth(); z++) {
            for (int i = 0; i < World.width() * World.height() / 100; i++) {
                factory.newRock(z);
            }
            for (int i = 0; i < 4; i++) {
                factory.newApple(z);
            }
            for (int i = 0; i < 3; i++) {
                factory.newMeat(z);
            }
            for (int i = 0; i < 3; i++) {
                factory.newBread(z);
            }
            for (int i = 0; i < 3; i++) {
                factory.newRandomWeapon(z);
            }
            for (int i = 0; i < 4; i++) {
                factory.newRandomArmor(z);
            }
            for (int i = 0; i < 3; i++) {
                factory.newRandomScroll(z);
            }
            for (int i = 0; i < 4; i++) {
                factory.newRandomPotion(z);
            }
            for (int i = 0; i < 5; i++) {
                factory.newRandomRing(z);
            }
            for (int i = 0; i < 5; i++) {
                factory.newRandomCharm(z);
            }
            for (int i = 0; i < 1; i++) {
                if (z % 3 == 0) factory.newRandomSpellbook(z);
            }
        }
        factory.newVictoryItem(World.depth() - 1);
    }
    
    /**
     * Sets this object's World variable to a new WorldBuilder which generates
     * caves, and returns its tiles to be used by the original world object
     */
    private void createWorld() {
        world = new WorldBuilder(80, 51, 13).makeCaves().build();
    }
    
    /**
     * Method telling us how far along the X axis we should scroll
     */
    public int getScrollX() {
        return Math.max(0, Math.min(player.x - screenWidth / 2, World.width() - screenWidth));
    }
    
    /**
     * Method telling us how far along the Y axis we should scroll
     */
    public int getScrollY() {
        return Math.max(0, Math.min(player.y - screenHeight / 2, World.height() - screenHeight));
    }
    
    /**
     * @return returns true if the player is trying to go upstairs when on the
     *         first floor
     */
    private boolean userIsTryingToExit() {
        return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
    }
    
    /**
     * @return returns a WinScreen if userIsTryingToExit() == true, and they
     *         have the "king's soul", otherwise returns a LoseScreen
     */
    private Screen userExits() {
        for (Item item : player.inventory().getItems()) {
            if (item != null && item.name().equals("king's soul")) return new WinScreen();
        }
        player.modifyHp(0, "Died while cowardly fleeing the catacombs.");
        return new LoseScreen(player);
    }
    
    /**
     * Method used to display the world tiles onto the PlayScreen's terminal<br>
     * This takes a left and top to know which section of the world it should
     * display
     * 
     * @param terminal
     *        - the terminal on which to display the tiles
     * @param left
     *        - the left-most x coordinate of the world that is visible on the
     *        screen
     * @param top
     *        - the top-most y coordinate of the world that is visible on the
     *        screen
     */
    private void displayTiles(AsciiPanel terminal, int left, int top) {
        fov.update(player.x, player.y, player.z, player.visionRadius());
        
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;
                
                if (player.canSee(wx, wy, player.z)) {
                    terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
                } else {
                    terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray);
                }
            }
        }
    }
    
    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();
        
        displayTiles(terminal, left, top);
        
        terminal.write(player.glyph(), player.x - left, player.y - top);
        
        String stats = String.format(" %3d/%3d hp  %d/%d mana  %8s  stealth %d", player.hp(), player.maxHp(),
                player.mana(), player.maxMana(), hunger(), Math.max(0, player.stealthRadius()));
        terminal.write(stats, 1, 23);
        
        String level = String.format(" Depth: %d", player.z + 1);
        terminal.write(level, 1, 1);
        
        displayMessages(terminal, messages);
        
        if (subscreen != null) subscreen.displayOutput(terminal);
    }
    
    /**
     * Method that determines what message to display regarding the player's
     * hunger
     * 
     * @return returns the player's food/maxFood along with a general status
     *         message if applicable
     */
    private String hunger() {
        if (player.food() < player.maxFood() * 0.1)
            return player.food() + "/" + player.maxFood() + " hunger [Starving]";
        else if (player.food() < player.maxFood() * 0.2)
            return player.food() + "/" + player.maxFood() + " hunger [Hungry]";
        else if (player.food() > player.maxFood() * 0.9)
            return player.food() + "/" + player.maxFood() + " hunger [Stuffed]";
        else if (player.food() > player.maxFood() * 0.8)
            return player.food() + "/" + player.maxFood() + " hunger [Full]";
        else
            return player.food() + "/" + player.maxFood() + " hunger";
    }
    
    /**
     * Method used to display messages onto the screen
     * 
     * @param terminal
     *        - the terminal on which to display the messages
     * @param messages
     *        - the messages which to display
     */
    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            messageList.add(messages.get(i));
            if (top + i < 24) terminal.writeCenter(messages.get(i), top + i);
        }
        messages.clear();
    }
    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int level = player.level();
        
        if (subscreen != null) {
            subscreen = subscreen.respondToUserInput(key);
        } else {
            switch (key.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_H:
                    player.moveBy(-1, 0, 0);
                    shouldUpdate = true;
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_L:
                    player.moveBy(1, 0, 0);
                    shouldUpdate = true;
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_K:
                    player.moveBy(0, -1, 0);
                    shouldUpdate = true;
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_J:
                    player.moveBy(0, 1, 0);
                    shouldUpdate = true;
                    break;
                case KeyEvent.VK_Y:
                    player.moveBy(-1, -1, 0);
                    shouldUpdate = true;
                    break;
                case KeyEvent.VK_U:
                    player.moveBy(1, -1, 0);
                    shouldUpdate = true;
                    break;
                case KeyEvent.VK_B:
                    player.moveBy(-1, 1, 0);
                    shouldUpdate = true;
                    break;
                case KeyEvent.VK_N:
                    player.moveBy(1, 1, 0);
                    shouldUpdate = true;
                    break;
                case KeyEvent.VK_D:
                    subscreen = new DropScreen(player);
                    break;
                case KeyEvent.VK_E:
                    subscreen = new EatScreen(player);
                    break;
                case KeyEvent.VK_W:
                    subscreen = new EquipScreen(player);
                    break;
                case KeyEvent.VK_X:
                    subscreen = new ExamineScreen(player);
                    break;
                case KeyEvent.VK_SLASH:
                    subscreen = new HelpScreen();
                    break;
                case KeyEvent.VK_SEMICOLON:
                    subscreen = new LookScreen(player, "Looking...", player.x - getScrollX(), player.y - getScrollY());
                    break;
                case KeyEvent.VK_T:
                    subscreen = new ThrowScreen(player, player.x - getScrollX(), player.y - getScrollY());
                    break;
                case KeyEvent.VK_F:
                    if (player.weapon() == null || player.weapon().rangedAttackValue() == 0)
                        player.notify("You don't have a ranged weapon equiped.");
                    else
                        subscreen = new FireWeaponScreen(player, player.x - getScrollX(), player.y - getScrollY());
                    break;
                case KeyEvent.VK_Q:
                    subscreen = new QuaffScreen(player);
                    break;
                case KeyEvent.VK_R:
                    subscreen = new ReadScreen(player, player.x - getScrollX(), player.y - getScrollY());
                    break;
                case KeyEvent.VK_M:
                    subscreen = new MessageScreen(messageList);
                    break;
                case KeyEvent.VK_QUOTE:
                    subscreen = new StatsScreen(player);
                    break;
            }
            
            switch (key.getKeyChar()) {
                case 'g':
                case ',':
                    player.pickup();
                    shouldUpdate = true;
                    break;
                case '<':
                    if (userIsTryingToExit())
                        return userExits();
                    else
                        player.moveBy(0, 0, -1);
                    break;
                case '>':
                    player.moveBy(0, 0, 1);
                    break;
            }
        }
        
        if (player.level() > level) subscreen = new LevelUpScreen(player, player.level() - level);
        
        if (subscreen == null && shouldUpdate == true) world.update();
        
        if (player.hp() < 1) return new LoseScreen(player);
        
        shouldUpdate = false;
        
        return this;
    }
    
    public static void setSubscreen(Screen screen) {
        subscreen = screen;
    }
    
    public static int screenWidth() {
        return screenWidth;
    }
    
    public static int screenHeight() {
        return screenHeight;
    }
    
}
