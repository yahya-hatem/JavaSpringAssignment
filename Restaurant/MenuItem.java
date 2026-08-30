/**
 * Represents a menu item in the restaurant.
 * Stores immutable information about a dish: ID, name, price, and category.
 */
public class MenuItem {
    private int id;
    private String name;
    private double price;
    private String category;

    /**
     * Constructor for MenuItem
     * @param id Unique identifier for the menu item
     * @param name Name of the dish
     * @param price Price of the dish
     * @param category Category (e.g., "Appetizer", "Main", "Dessert", "Beverage")
     */
    public MenuItem(int id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %-20s | $%.2f | Category: %s", 
                             id, name, price, category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MenuItem menuItem = (MenuItem) obj;
        return id == menuItem.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
