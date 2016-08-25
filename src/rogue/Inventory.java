package rogue;

/**
 * The Inventory class keeps track of the player (and other creature's) items
 */
public class Inventory {
    
    /** Item array to keep track of a creature's inventory **/
    private Item[] items;
    
    /**
     * General constructor for the Inventory class
     * 
     * @param max
     *        - the maximum number of inventory slots
     */
    public Inventory(int max) {
        items = new Item[max];
    }
    
    /**
     * Method to add an item to the first open slot in an inventory
     * 
     * @param item
     *        - the item to add
     */
    public void add(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = item;
                break;
            }
        }
    }
    
    /**
     * Method to remove an item from an inventory
     * 
     * @param item
     *        - the item to remove
     */
    public void remove(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item) {
                items[i] = null;
                return;
            }
        }
    }
    
    /**
     * Method that determines whether or not the inventory is full
     * 
     * @return returns true if the inventory is full, otherwise false
     */
    public boolean isFull() {
        int size = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                size++;
            }
        }
        return size == items.length;
    }
    
    /**
     * @return returns the list of items
     *         <br>
     *         (a.k.a. an inventory)
     */
    public Item[] getItems() {
        return items;
    }
    
    /**
     * @param i
     *        - the slot number of the inventory
     * @return returns the item in the specific slot
     */
    public Item get(int i) {
        return items[i];
    }
    
    /**
     * Method to check whether a creature's inventory contains a certain item
     * 
     * @param item
     *        - item to look for
     * @return returns true if the inventory contains the item, otherwise false
     */
    public boolean contains(Item item) {
        for (Item i : items) {
            if (i == item) return true;
        }
        return false;
    }
    
}
