package rogue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class World {
    
    /** A 3D Tile array marking which tiles are in each location **/
    private Tile[][][] tiles;
    /** A 3D Item array marking which tiles in the world have items **/
    private Item[][][] items;
    
    /**
     * A List of Creature objects that contains all of the creatures in the
     * world
     **/
    private List<Creature> creatures;
    
    /** The width of the world **/
    private static int width;
    /** The height of the world **/
    private static int height;
    /** The depth of the world **/
    private static int depth;
    
    /**
     * General constructor for the World class
     * 
     * @param tiles
     *        - the set of tiles that make up the world
     */
    public World(Tile[][][] tiles) {
        this.tiles = tiles;
        World.width = tiles.length;
        World.height = tiles[0].length;
        World.depth = tiles[0][0].length;
        this.items = new Item[width][height][depth];
        creatures = new ArrayList<Creature>();
    }
    
    /**
     * Method belonging to the world, that tells all creatures to update after
     * every turn
     */
    public void update() {
        List<Creature> toUpdate = new ArrayList<Creature>(creatures);
        for (Creature creature : toUpdate) {
            creature.update();
        }
    }
    
    /**
     * Method used to get a creature at a specific location
     * 
     * @param x
     *        - x coordinate of the tile to search
     * @param y
     *        - y coordinate of the tile to search
     * @param z
     *        - the z level on which to search
     * @return returns the creature at the specific location if there is one,
     *         otherwise NULL
     */
    public Creature creature(int x, int y, int z) {
        for (Creature c : creatures) {
            if (c.x == x && c.y == y && c.z == z) return c;
        }
        return null;
    }
    
    /**
     * Method that allows creatures to dig through walls, turning them into
     * floor
     * 
     * @param x
     *        - the x coordinate of the tile to dig
     * @param y
     *        - the y coordinate of the tile to dig
     * @param z
     *        - the z level on which to dig
     */
    public void dig(int x, int y, int z) {
        if (tile(x, y, z).isDiggable()) tiles[x][y][z] = Tile.FLOOR;
    }
    
    /**
     * Adds a given creature at a random empty location
     * 
     * @param creature
     *        - the creature to be added
     * @param depth
     *        - the z level on which to add the creature
     */
    public void addAtEmptyLocation(Creature creature, int depth) {
        int x;
        int y;
        
        if(!creature.aquatic() && !creature.canFly()){
            do {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } while (!tile(x, y, depth).isGround() || creature(x, y, depth) != null);
        }else{
            do {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } while (!tile(x, y, depth).isWater() || creature(x, y, depth) != null);
        }
        
        creature.x = x;
        creature.y = y;
        creature.z = depth;
        creatures.add(creature);
    }
    
    /**
     * Adds a given item at a random empty location
     * 
     * @param item
     *        - the item to be added
     * @param depth
     *        - the z level on which to add the creature
     */
    public void addAtEmptyLocation(Item item, int depth) {
        int x;
        int y;
        
        do {
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        } while (((!tile(x, y, depth).isGround() && !tile(x, y, depth).isWater()) && tile(x, y,  depth) != Tile.STAIRS_UP && tile(x, y,  depth) != Tile.STAIRS_DOWN) || item(x, y, depth) != null);
        
        items[x][y][depth] = item;
        
    }
    
    /**
     * Method to put an item onto the ground
     * 
     * @param item
     *        - the item to be placed
     * @param x
     *        - the x coordinate to start with
     * @param y
     *        - the y coordinate to start with
     * @param z
     *        - the z level to start with
     * @return returns true if it can be placed, otherwise false
     */
    public boolean addAtEmptySpace(Item item, int x, int y, int z) {
        if (item == null) return false;
        
        List<Point> points = new ArrayList<Point>();
        List<Point> checked = new ArrayList<Point>();
        
        points.add(new Point(x, y, z));
        
        while (!points.isEmpty()) {
            Point p = points.remove(0);
            checked.add(p);
            
            if ((!tile(p.x, p.y, p.z).isGround() && !tile(p.x, p.y, p.z).isWater()) || tile(p.x, p.y, p.z) == Tile.STAIRS_DOWN
                    || tile(p.x, p.y, p.z) == Tile.STAIRS_UP)
                continue;
            
            if (items[p.x][p.y][p.z] == null) {
                items[p.x][p.y][p.z] = item;
                Creature c = this.creature(p.x, p.y, p.z);
                if (c != null) c.notify("A %s lands between your feet.", c.nameOf(item));
                return true;
            } else {
                List<Point> neighbors = p.neighbors8();
                neighbors.removeAll(checked);
                points.addAll(neighbors);
            }
        }
        return false;
    }
    
    /**
     * Removes a creature from the universal list of creatures
     * 
     * @param other
     *        - creature to be removed
     */
    public void remove(Creature other) {
        creatures.remove(other);
    }
    
    /**
     * Method to remove an item from the world
     * 
     * @param item
     *        - item to be removed
     */
    public void remove(Item item) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    if (items[x][y][z] == item) {
                        items[x][y][z] = null;
                        return;
                    }
                }
            }
        }
    }
    
    /**
     * Removes item from the ground
     * 
     * @param x
     *        - x coordinate of tile to remove item from
     * @param y
     *        - y coordinate of tile to remove item from
     * @param z
     *        - z level of tile to remove item from
     */
    public void remove(int x, int y, int z) {
        items[x][y][z] = null;
    }
    
    /**
     * Returns the tile at the given coordinates
     * 
     * @param x
     *        - the x coordinate to search at
     * @param y
     *        - the y coordinate to search at
     * @param z
     *        - the z level to check the tile on
     * @return the tile itself
     */
    public Tile tile(int x, int y, int z) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth)
            return Tile.BOUNDS;
        else
            return tiles[x][y][z];
    }
    
    /**
     * Returns the item at the given coordinates
     * 
     * @param x
     *        - the x coordinate to search at
     * @param y
     *        - the y coordinate to search at
     * @param z
     *        - the z level to check the tile on
     * @return the item itself
     */
    public Item item(int x, int y, int z) {
        return items[x][y][z];
    }
    
    /**
     * Method to add a Creature to the world after the world has already been
     * populated with Creatures
     * 
     * @param pet
     *        - creature to be added
     */
    public void add(Creature pet) {
        creatures.add(pet);
    }
    
    /**
     * Returns the glyph of the tile at the given coordinates
     * 
     * @param x
     *        - the x coordinate of the tile
     * @param y
     *        - the y coordinate of the tile
     * @param z
     *        - the z level to check the tile on
     * @return a glyph following this order of precedence:
     *         <ol>
     *         <li>creature (if present)</li>
     *         <li>item (if present)</li>
     *         <li>tile</li>
     */
    public char glyph(int x, int y, int z) {
        Creature creature = creature(x, y, z);
        if (creature != null) return creature.glyph();
        
        if (item(x, y, z) != null) return item(x, y, z).glyph();
        
        return tile(x, y, z).glyph();
    }
    
    /**
     * Returns the color of the tile at the given coordinates
     * 
     * @param x
     *        - the x coordinate of the tile
     * @param y
     *        - the y coordinate of the tile
     * @param z
     *        - the z level to check the tile on
     * @return a color following this order of precedence:
     *         <ol>
     *         <li>creature (if present)</li>
     *         <li>item (if present)</li>
     *         <li>tile</li>
     */
    public Color color(int x, int y, int z) {
        Creature creature = creature(x, y, z);
        if (creature != null) return creature.color();
        
        if (item(x, y, z) != null) return item(x, y, z).color();
        
        return tile(x, y, z).color();
    }
    
    /**
     * @return returns the width of the world
     */
    public static int width() {
        return width;
    }
    
    /**
     * @return returns the height of the world
     */
    public static int height() {
        return height;
    }
    
    /**
     * @return returns the depth of the world
     */
    public static int depth() {
        return depth;
    }
}
