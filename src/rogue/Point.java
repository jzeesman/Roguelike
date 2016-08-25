package rogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Point class has x, y, and z coordinates, but doesn't have anything
 * attached to it, like a tile.
 * <br>
 * It is a way of abstracting a given location in the world, without worrying
 * about tiles, items, creatures, etc.
 */
public class Point {
    
    /** The x coordinate of the Point **/
    public int x;
    /** The y coordinate of the Point **/
    public int y;
    /** The z coordinate of the Point **/
    public int z;
    
    /**
     * General constructor for the Point class
     * 
     * @param x
     *        - the x coordinate
     * @param y
     *        - the y coordinate
     * @param z
     *        - the z coordinate
     */
    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Point)) return false;
        Point other = (Point) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        if (z != other.z) return false;
        return true;
    }
    
    /**
     * Method that returns a point list of all of the 8 neighboring points
     * 
     * @return a list of the 8 neighboring points
     */
    public List<Point> neighbors8() {
        List<Point> points = new ArrayList<Point>();
        
        for (int ox = -1; ox < 2; ox++) {
            for (int oy = -1; oy < 2; oy++) {
                if (ox == 0 && oy == 0) continue;
                
                points.add(new Point(x + ox, y + oy, z));
            }
        }
        
        // We shuffle the points so we don't introduce bias.
        // Otherwise the upper left point would always be checked first, and the
        // lower right last, which may lead to odd things.
        Collections.shuffle(points);
        return points;
    }
}
