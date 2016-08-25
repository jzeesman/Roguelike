package rogue;


public class Room {
    
    public int x1, x2, y1, y2;
    private int z;
    
    public int width, height;
    
    public Point center;
    
    public Room(int x, int y, int z, int width, int height){
        this.x1 = x;
        this.x2 = x + width;
        this.y1 = y;
        this.y2 = y + height;
        this.z = z;
        this.width = width;
        this.height = height;
        center = new Point((int) ((x1+x2) / 2), (int) ((y1+y2) / 2), z);
    }
    
    public boolean intersects(Room room){
        return (x1 <= room.x2 && x2 >= room.x1 && y1 <= room.y2 && room.y2 >= room.y1);
    }
    
}
