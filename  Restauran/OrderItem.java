/**
 * Represents a single line item in an order.
 * Pairs a MenuItem with its quantity for that specific order.
 */
public class OrderItem {
    private MenuItem item;
    private int quantity;

    /**
     * Constructor for OrderItem
     * @param item The menu item being ordered
     * @param quantity How many of this item are ordered
     */
    public OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Get the menu item
     * @return The MenuItem object
     */
    public MenuItem getItem() {
        return item;
    }

    /**
     * Get the quantity of this item
     * @return The quantity ordered
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity for this order item
     * @param quantity New quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculate the subtotal for this order item (price × quantity)
     * @return The subtotal as a double
     */
    public double calculateSubtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%-25s x%d | Unit: $%.2f | Subtotal: $%.2f",
                             item.getName(),
                             quantity,
                             item.getPrice(),
                             calculateSubtotal());
    }
}
