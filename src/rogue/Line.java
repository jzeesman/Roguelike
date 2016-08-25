package rogue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Line class contains most of the math to do with FieldOfView, trajectory,
 * and path finding
 */
public class Line implements Iterable<Point> {
    
    /** A List of Point objects **/
    private List<Point> points;
    
    /**
     * General constructor for the Line class
     * 
     * @param x0
     *        - x coordinate of starting point
     * @param y0
     *        - y coordinate of starting point
     * @param x1
     *        - x coordinate of end point
     * @param y1
     *        - y coordinate of end point
     */
    public Line(int x0, int y0, int x1, int y1) {
        points = new ArrayList<Point>();
        
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        
        while (true) {
            points.add(new Point(x0, y0, 0));
            
            if (x0 == x1 && y0 == y1) break;
            
            int e2 = err * 2;
            if (e2 > -dx) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
    
    public Iterator<Point> iterator() {
        return points.iterator();
    }
    
    /**
     * Method returns
     * 
     * @return returns the List of Point objects
     */
    public List<Point> getPoints() {
        return points;
    }
    
    /**
     * @return returns the size of the List of Point objects
     */
    public int size() {
        return points.size();
    }
}
