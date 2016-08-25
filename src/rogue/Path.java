package rogue;

import java.util.List;

/**
 * The Path class contains the final result of the PathFinder class
 */
public class Path {
    
    /** The PathFinder object **/
    private static PathFinder pf = new PathFinder();
    
    /** The List of Point objects that make up the path **/
    private List<Point> points;
    
    /**
     * The constructor for the Path class
     * 
     * @param creature
     *        - the creature who will be doing the path finding
     * @param x
     *        - the x coordinate of the target
     * @param y
     *        - the y coordinate of the target
     */
    public Path(Creature creature, int x, int y) {
        points = pf.findPath(creature, new Point(creature.x, creature.y, creature.z), new Point(x, y, creature.z), 300);
    }
    
    /**
     * @return returns the List of Point objects that make up the path
     */
    public List<Point> points() {
        return points;
    }
}
