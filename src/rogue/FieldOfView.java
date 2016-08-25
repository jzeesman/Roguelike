package rogue;

/**
 * FieldOfView class handles the math behind what a creature can see from a
 * given location
 */
public class FieldOfView {
    
    /** The world in which to check the fov **/
    private World world;
    /** The z level on which to check the fov **/
    private int depth;
    
    /** 2D boolean array keeping track of what tiles are/aren't visible from a given position **/
    private boolean[][] visible;
    
    /** 3D Tile array keeping track of tiles **/
    private Tile[][][] tiles;
    
    /**
     * General constructor for the FieldOfView class
     * 
     * @param world
     *        - the world in which your field of view is in
     */
    public FieldOfView(World world) {
        this.world = world;
        this.visible = new boolean[World.width()][World.height()];
        this.tiles = new Tile[World.width()][World.height()][World.depth()];
        
        for (int x = 0; x < World.width(); x++) {
            for (int y = 0; y < World.height(); y++) {
                for (int z = 0; z < World.depth(); z++) {
                    tiles[x][y][z] = Tile.UNKNOWN;
                }
            }
        }
    }
    
    /**
     * Update method for the FieldOfView class
     * 
     * @param wx
     *        -
     * @param wy
     *        -
     * @param wz
     *        -
     * @param r
     *        -
     */
    public void update(int wx, int wy, int wz, int r) {
        depth = wz;
        visible = new boolean[World.width()][World.height()];
        
        for (int x = -r; x < r; x++) {
            for (int y = -r; y < r; y++) {
                if (x * x + y * y > r * r) continue;
                
                if (wx + x < 0 || wx + x >= World.width() || wy + y < 0 || wy + y >= World.height()) continue;
                
                for (Point p : new Line(wx, wy, wx + x, wy + y)) {
                    Tile tile = world.tile(p.x, p.y, wz);
                    visible[p.x][p.y] = true;
                    tiles[p.x][p.y][wz] = tile;
                    
                    if ((!tile.isGround() && !tile.isWater()) && tile != Tile.STAIRS_DOWN && tile != Tile.STAIRS_UP) break;
                }
            }
        }
    }
    
    /**
     * Method returns a tile at a given coordinate
     * 
     * @param x
     *        - x coordinate to check
     * @param y
     *        - y coordinate to check
     * @param z
     *        - z level to check
     * @return returns the tile at the given coordinate
     */
    public Tile tile(int x, int y, int z) {
        return tiles[x][y][z];
    }
    
    /**
     * Method checks if a given tile is visible to the player
     * 
     * @param x
     *        - x coordinate to check
     * @param y
     *        - y coordinate to check
     * @param z
     *        - z level to check
     * @return returns whether or not the tile is visible
     */
    public boolean isVisible(int x, int y, int z) {
        return z == depth && x >= 0 && y >= 0 && x < visible.length && y < visible[0].length && visible[x][y];
    }
    
}
