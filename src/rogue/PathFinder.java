package rogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * The PathFinder class.
 * <br>
 * Uses the A* algorithm for finding an optimal path from one point to another
 */
public class PathFinder {
    
    /** An ArrayList of Point objects **/
    private ArrayList<Point> open;
    /** An ArrayList of Point objects **/
    private ArrayList<Point> closed;
    /**
     * A HashMap of Point, Point objects which details which Point is the parent
     * of another Point object
     **/
    private HashMap<Point, Point> parents;
    /**
     * A HashMap of Point, Integer objects which represents the total cost for a
     * given route
     **/
    private HashMap<Point, Integer> totalCost;
    
    /**
     * The constructor for the PathFinder class
     */
    public PathFinder() {
        this.open = new ArrayList<Point>();
        this.closed = new ArrayList<Point>();
        this.parents = new HashMap<Point, Point>();
        this.totalCost = new HashMap<Point, Integer>();
    }
    
    /**
     * Method that calculates the heuristic cost to go from one Point to another
     * 
     * @param from
     *        - the starting Point object
     * @param to
     *        - the end Point object
     * @return returns the heuristic cost to go from the start Point to the end
     *         Point
     */
    private int heuristicCost(Point from, Point to) {
        return Math.max(Math.abs(from.x - to.x), Math.abs(from.y - to.y));
    }
    
    /**
     * Method that calculates the cost to get to this Point from another Point
     * 
     * @param from
     *        - the Point to start from
     * @return returns the cost to get from the start Point to this Point
     */
    private int costToGetTo(Point from) {
        return parents.get(from) == null ? 0 : (1 + costToGetTo(parents.get(from)));
    }
    
    /**
     * Calculates the total cost to go from one Point to another
     * 
     * @param from
     *        - the start Point
     * @param to
     *        - the end Point
     * @return returns the total cost
     */
    private int totalCost(Point from, Point to) {
        if (totalCost.containsKey(from)) return totalCost.get(from);
        
        int cost = costToGetTo(from) + heuristicCost(from, to);
        totalCost.put(from, cost);
        return cost;
    }
    
    /**
     * Reparents a child Point to a new parent Point
     * 
     * @param child
     *        - the child Point
     * @param parent
     *        - the new parent Point
     */
    private void reParent(Point child, Point parent) {
        parents.put(child, parent);
        totalCost.remove(child);
    }
    
    /**
     * Using the A* algorithm, find the most optimal path
     * 
     * @param creature
     *        - the creature doing the path finding
     * @param start
     *        - the start Point
     * @param end
     *        - the end Point
     * @param maxTries
     *        - the maximum number of tries to come up with the most optimal
     *        path
     * @return returns an ArrayList of Point objects, which makes up the most
     *         optimal path
     */
    public ArrayList<Point> findPath(Creature creature, Point start, Point end, int maxTries) {
        open.clear();
        closed.clear();
        parents.clear();
        totalCost.clear();
        
        open.add(start);
        
        for (int tries = 0; tries < maxTries && open.size() > 0; tries++) {
            Point closest = getClosestPoint(end);
            
            open.remove(closest);
            closed.add(closest);
            
            if (closest.equals(end))
                return createPath(start, closest);
            else
                checkNeighbors(creature, end, closest);
        }
        return null;
    }
    
    /**
     * Method that gets the closest Point to this
     * 
     * @param end
     *        - the Point object you want to get to
     * @return returns the Point object with the lowest cost to get to the end
     *         Point
     */
    private Point getClosestPoint(Point end) {
        Point closest = open.get(0);
        for (Point other : open) {
            if (totalCost(other, end) < totalCost(closest, end)) closest = other;
        }
        return closest;
    }
    
    /**
     * Method to check the neighbors of the creature
     * 
     * @param creature
     *        - the Creature doing the path finding
     * @param end
     *        - the end Point
     * @param closest
     *        - most likely is the Point returned by getClosestPoint(Point)
     */
    private void checkNeighbors(Creature creature, Point end, Point closest) {
        for (Point neighbor : closest.neighbors8()) {
            if (closed.contains(neighbor)
                    || !creature.canEnter(neighbor.x, neighbor.y, neighbor.z) && !neighbor.equals(end))
                continue;
            
            if (open.contains(neighbor))
                reParentNeighborIfNecessary(closest, neighbor);
            else
                reParentNeighbor(closest, neighbor);
        }
    }
    
    /**
     * Method that calls upon the reParent(Point, Point) method
     * <br>
     * Specifically being called for when used on neighboring Points
     * 
     * @param closest
     *        - most likely is the Point being returned by
     *        getClosestPoint(Point)
     * @param neighbor
     *        - the neighbor in question
     */
    private void reParentNeighbor(Point closest, Point neighbor) {
        reParent(neighbor, closest);
        open.add(neighbor);
    }
    
    /**
     * Method that checks whether or not it is necessary to reparent
     * 
     * @param closest
     *        - most likely is the Point being returned by
     *        getClosestPoint(Point)
     * @param neighbor
     *        - the neighbor in question
     */
    private void reParentNeighborIfNecessary(Point closest, Point neighbor) {
        Point originalParent = parents.get(neighbor);
        double currentCost = costToGetTo(neighbor);
        reParent(neighbor, closest);
        double reparentCost = costToGetTo(neighbor);
        
        if (reparentCost < currentCost)
            open.remove(neighbor);
        else
            reParent(neighbor, originalParent);
    }
    
    /**
     * Creates a path from the start Point to the end Point
     * 
     * @param start
     *        - the start Point
     * @param end
     *        - the end Point
     * @return returns an ArrayList of Point objects that sequentially make up
     *         the desired path
     */
    private ArrayList<Point> createPath(Point start, Point end) {
        ArrayList<Point> path = new ArrayList<Point>();
        
        while (!end.equals(start)) {
            path.add(end);
            end = parents.get(end);
        }
        
        Collections.reverse(path);
        return path;
    }
}
