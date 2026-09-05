import java.util.ArrayList;
import java.util.List;


public class Order {
    private int orderId;
    private String customerName;
    private ArrayList<OrderItem> items;
    private double total;
    private OrderStatus status;
    private long createdTime;
    private long completedTime;


    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.status = OrderStatus.PENDING;
        this.createdTime = System.currentTimeMillis();
        this.completedTime = 0;
    }


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

    public void setStatus(OrderStatus status) {
        this.status = status;
        if (status == OrderStatus.COMPLETED) {
            this.completedTime = System.currentTimeMillis();
        }
    }


    public void addItem(OrderItem item) {
        if (item != null && item.getQuantity() > 0) {
            items.add(item);
            calculateTotal();
        }
    }


    public boolean removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            calculateTotal();
            return true;
        }
        return false;
    }


    public boolean removeItemByMenuId(int menuItemId) {
        boolean removed = items.removeIf(orderItem -> orderItem.getItem().getId() == menuItemId);
        if (removed) {
            calculateTotal();
        }
        return removed;
    }


    public void calculateTotal() {
        this.total = 0.0;
        for (OrderItem item : items) {
            this.total += item.calculateSubtotal();
        }
    }


    public int getItemCount() {
        return items.size();
    }


    public int getTotalQuantity() {
        int totalQty = 0;
        for (OrderItem item : items) {
            totalQty += item.getQuantity();
        }
        return totalQty;
    }


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


    @Override
    public String toString() {
        return String.format("Order #%d | Customer: %-15s | Status: %-12s | Total: $%.2f | Items: %d",
                             orderId, customerName, status.getDisplayName(), total, getItemCount());
    }
}
