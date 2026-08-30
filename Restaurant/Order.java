import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant order placed by a customer.
 * Contains a collection of OrderItems, tracks status, and manages the order total.
 */
public class Order {
    private int orderId;
    private String customerName;
    private ArrayList<OrderItem> items;
    private double total;
    private OrderStatus status;
    private long createdTime;
    private long completedTime;

    /**
     * Constructor for Order
     * @param orderId Unique order identifier
     * @param customerName Name of the customer placing the order
     */
    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.status = OrderStatus.PENDING;
        this.createdTime = System.currentTimeMillis();
        this.completedTime = 0;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getCompletedTime() {
        return completedTime;
    }

    /**
     * Update the order status
     * @param status The new OrderStatus
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
        if (status == OrderStatus.COMPLETED) {
            this.completedTime = System.currentTimeMillis();
        }
    }

    /**
     * Add an item to the order
     * @param item The OrderItem to add
     */
    public void addItem(OrderItem item) {
        if (item != null && item.getQuantity() > 0) {
            items.add(item);
            calculateTotal();
        }
    }

    /**
     * Remove an item from the order by its index
     * @param index The index of the item to remove
     * @return true if item was removed, false otherwise
     */
    public boolean removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            calculateTotal();
            return true;
        }
        return false;
    }

    /**
     * Remove all items with a specific MenuItem ID
     * @param menuItemId The ID of the menu item to remove
     * @return true if any items were removed, false otherwise
     */
    public boolean removeItemByMenuId(int menuItemId) {
        boolean removed = items.removeIf(orderItem -> orderItem.getItem().getId() == menuItemId);
        if (removed) {
            calculateTotal();
        }
        return removed;
    }

    /**
     * Calculate the total cost of the order by summing all subtotals
     */
    public void calculateTotal() {
        this.total = 0.0;
        for (OrderItem item : items) {
            this.total += item.calculateSubtotal();
        }
    }

    /**
     * Get the number of items (line items) in the order
     * @return Number of different order items
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Get the total quantity of all items
     * @return Sum of all quantities
     */
    public int getTotalQuantity() {
        int totalQty = 0;
        for (OrderItem item : items) {
            totalQty += item.getQuantity();
        }
        return totalQty;
    }

    /**
     * Display the complete order information
     */
    public void displayOrder() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ORDER ID: " + orderId + " | CUSTOMER: " + customerName);
        System.out.println("Status: " + status.getDisplayName() + " | Created: " + new java.util.Date(createdTime));
        System.out.println("=".repeat(70));

        if (items.isEmpty()) {
            System.out.println("(No items in order)");
        } else {
            System.out.println("ORDER ITEMS:");
            System.out.println("-".repeat(70));
            for (int i = 0; i < items.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, items.get(i).toString());
            }
        }

        System.out.println("-".repeat(70));
        System.out.printf("TOTAL ITEMS: %d (Qty: %d) | TOTAL: $%.2f%n", 
                         getItemCount(), getTotalQuantity(), total);
        System.out.println("=".repeat(70));
    }

    /**
     * Get a compact string representation of the order
     */
    @Override
    public String toString() {
        return String.format("Order #%d | Customer: %-15s | Status: %-12s | Total: $%.2f | Items: %d",
                             orderId, customerName, status.getDisplayName(), total, getItemCount());
    }
}
