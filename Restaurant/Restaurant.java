import java.util.*;

/**
 * Main Restaurant class that manages the order management system.
 * Demonstrates the use of different Java collections for different purposes:
 * - ArrayList: Menu items (random access needed)
 * - LinkedList: Kitchen queue (FIFO operations, efficient removals)
 * - HashMap: All orders (fast lookup by ID, permanent storage)
 * - LinkedHashMap: Completed orders (maintains insertion order of completion)
 */
public class Restaurant {
    private String restaurantName;
    
    // Collections
    private ArrayList<MenuItem> menu;                              // Menu items
    private LinkedList<Order> kitchenQueue;                        // Orders waiting to be processed
    private HashMap<Integer, Order> orders;                        // ALL orders (permanent record)
    private LinkedHashMap<Integer, Order> completedOrders;        // Completed orders in completion order
    
    private int nextOrderId;

    /**
     * Constructor for Restaurant
     * @param name The name of the restaurant
     */
    public Restaurant(String name) {
        this.restaurantName = name;
        this.menu = new ArrayList<>();
        this.kitchenQueue = new LinkedList<>();
        this.orders = new HashMap<>();
        this.completedOrders = new LinkedHashMap<>();
        this.nextOrderId = 1001;
    }

    // ==================== MENU MANAGEMENT ====================

    /**
     * Add a menu item to the restaurant menu
     * Using ArrayList allows efficient random access and sequential display
     * @param item The MenuItem to add
     */
    public void addMenuItemItem(MenuItem item) {
        menu.add(item);
        System.out.println("✓ Added to menu: " + item.getName());
    }

    /**
     * Remove a menu item by ID
     * @param itemId The ID of the item to remove
     * @return true if removed, false if not found
     */
    public boolean removeMenuItem(int itemId) {
        return menu.removeIf(item -> item.getId() == itemId);
    }

    /**
     * Find a menu item by ID
     * @param itemId The ID to search for
     * @return The MenuItem if found, null otherwise
     */
    public MenuItem findMenuItemById(int itemId) {
        for (MenuItem item : menu) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    /**
     * Display all menu items organized by category
     */
    public void displayMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("MENU - " + restaurantName);
        System.out.println("=".repeat(70));

        if (menu.isEmpty()) {
            System.out.println("(Menu is empty)");
            return;
        }

        // Group items by category
        Map<String, List<MenuItem>> itemsByCategory = new TreeMap<>();
        for (MenuItem item : menu) {
            itemsByCategory.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
        }

        // Display each category
        for (Map.Entry<String, List<MenuItem>> entry : itemsByCategory.entrySet()) {
            System.out.println("\n" + entry.getKey().toUpperCase() + ":");
            System.out.println("-".repeat(70));
            for (MenuItem item : entry.getValue()) {
                System.out.println(item.toString());
            }
        }
        System.out.println("=".repeat(70));
    }

    // ==================== ORDER CREATION & MANAGEMENT ====================

    /**
     * Create a new order for a customer
     * @param customerName The name of the customer
     * @return The newly created Order
     */
    public Order createOrder(String customerName) {
        Order newOrder = new Order(nextOrderId++, customerName);
        // Add to the permanent HashMap record immediately
        orders.put(newOrder.getOrderId(), newOrder);
        System.out.println("✓ New order created: Order #" + newOrder.getOrderId() + " for " + customerName);
        return newOrder;
    }

    /**
     * Find an order by its ID
     * HashMap provides O(1) lookup time regardless of how many orders exist
     * @param orderId The order ID to search for
     * @return The Order if found, null otherwise
     */
    public Order findOrderById(int orderId) {
        return orders.get(orderId);
    }

    /**
     * Add an item to an existing order
     * @param orderId The order ID
     * @param menuItemId The menu item ID
     * @param quantity The quantity to add
     * @return true if successful, false if order or menu item not found
     */
    public boolean addItemToOrder(int orderId, int menuItemId, int quantity) {
        Order order = findOrderById(orderId);
        MenuItem item = findMenuItemById(menuItemId);

        if (order == null) {
            System.out.println("✗ Order #" + orderId + " not found");
            return false;
        }
        if (item == null) {
            System.out.println("✗ Menu item #" + menuItemId + " not found");
            return false;
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("✗ Cannot add items to order #" + orderId + " (Status: " + order.getStatus() + ")");
            return false;
        }

        order.addItem(new OrderItem(item, quantity));
        System.out.println("✓ Added " + quantity + "x " + item.getName() + " to Order #" + orderId);
        return true;
    }

    /**
     * Remove an item from an order
     * @param orderId The order ID
     * @param itemIndex The index of the item to remove
     * @return true if successful, false otherwise
     */
    public boolean removeItemFromOrder(int orderId, int itemIndex) {
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("✗ Order #" + orderId + " not found");
            return false;
        }
        if (order.removeItem(itemIndex)) {
            System.out.println("✓ Item removed from Order #" + orderId);
            return true;
        }
        return false;
    }

    /**
     * Display details of a specific order
     * @param orderId The order ID to display
     */
    public void displayOrder(int orderId) {
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("✗ Order #" + orderId + " not found");
        } else {
            order.displayOrder();
        }
    }

    // ==================== KITCHEN QUEUE MANAGEMENT ====================

    /**
     * Send an order to the kitchen queue
     * LinkedList is ideal for queues - efficient enqueue and dequeue operations
     * The order's status is updated to IN_KITCHEN
     * @param orderId The order ID to send to kitchen
     * @return true if successful, false if order not found or not pending
     */
    public boolean sendToKitchen(int orderId) {
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("✗ Order #" + orderId + " not found");
            return false;
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("✗ Order #" + orderId + " is already " + order.getStatus());
            return false;
        }
        if (order.getItemCount() == 0) {
            System.out.println("✗ Order #" + orderId + " has no items");
            return false;
        }

        // Add to kitchen queue and update status
        kitchenQueue.addLast(order);  // Efficient O(1) operation
        order.setStatus(OrderStatus.IN_KITCHEN);
        System.out.println("✓ Order #" + orderId + " sent to kitchen. Queue size: " + kitchenQueue.size());
        return true;
    }

    /**
     * Process (complete) the next order in the kitchen queue
     * LinkedList's removeFirst() is efficient O(1) operation
     * The order is moved to completedOrders LinkedHashMap
     * Note: Order remains in HashMap (permanent record)
     * @return The completed Order, or null if queue is empty
     */
    public Order completeNextOrder() {
        if (kitchenQueue.isEmpty()) {
            System.out.println("✗ Kitchen queue is empty");
            return null;
        }

        // Remove from front of queue (FIFO)
        Order completedOrder = kitchenQueue.removeFirst();
        completedOrder.setStatus(OrderStatus.COMPLETED);
        
        // Add to completed orders (LinkedHashMap maintains insertion order)
        completedOrders.put(completedOrder.getOrderId(), completedOrder);
        
        System.out.println("✓ Order #" + completedOrder.getOrderId() + " completed. " +
                          "Remaining in queue: " + kitchenQueue.size());
        return completedOrder;
    }

    /**
     * Cancel a pending order
     * @param orderId The order ID to cancel
     * @return true if successful, false if order not found or not pending
     */
    public boolean cancelOrder(int orderId) {
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("✗ Order #" + orderId + " not found");
            return false;
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            System.out.println("✗ Cannot cancel completed order #" + orderId);
            return false;
        }

        // If order is in kitchen queue, remove it
        if (order.getStatus() == OrderStatus.IN_KITCHEN) {
            kitchenQueue.remove(order);  // O(n) operation for LinkedList, but necessary
        }

        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("✓ Order #" + orderId + " cancelled");
        return true;
    }

    /**
     * Display the current kitchen queue
     */
    public void displayKitchenQueue() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("KITCHEN QUEUE (" + kitchenQueue.size() + " orders)");
        System.out.println("=".repeat(70));

        if (kitchenQueue.isEmpty()) {
            System.out.println("(Kitchen queue is empty)");
        } else {
            int position = 1;
            for (Order order : kitchenQueue) {
                System.out.printf("%d. %s%n", position++, order.toString());
            }
        }
        System.out.println("=".repeat(70));
    }

    // ==================== ORDER HISTORY & STATISTICS ====================

    /**
     * Display all orders ever created (PENDING, IN_KITCHEN, COMPLETED, CANCELLED)
     * HashMap provides fast iteration without ordering
     */
    public void displayAllOrders() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ALL ORDERS (" + orders.size() + " total)");
        System.out.println("=".repeat(70));

        if (orders.isEmpty()) {
            System.out.println("(No orders yet)");
        } else {
            orders.values().forEach(order -> System.out.println(order.toString()));
        }
        System.out.println("=".repeat(70));
    }

    /**
     * Display completed orders in the order they were completed
     * LinkedHashMap maintains insertion order, so this shows completion order
     */
    public void displayCompletedOrders() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("COMPLETED ORDERS (" + completedOrders.size() + " total)");
        System.out.println("=".repeat(70));

        if (completedOrders.isEmpty()) {
            System.out.println("(No completed orders yet)");
        } else {
            int position = 1;
            for (Order order : completedOrders.values()) {
                System.out.printf("%d. %s%n", position++, order.toString());
            }
        }
        System.out.println("=".repeat(70));
    }

    /**
     * Display summary statistics
     */
    public void displayStatistics() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("RESTAURANT STATISTICS");
        System.out.println("=".repeat(70));
        
        System.out.println("Menu Items: " + menu.size());
        System.out.println("Total Orders Created: " + orders.size());
        System.out.println("Orders in Kitchen Queue: " + kitchenQueue.size());
        System.out.println("Completed Orders: " + completedOrders.size());
        
        // Calculate stats
        int pendingCount = 0;
        double totalRevenue = 0;
        
        for (Order order : orders.values()) {
            if (order.getStatus() == OrderStatus.PENDING) {
                pendingCount++;
            }
            if (order.getStatus() == OrderStatus.COMPLETED) {
                totalRevenue += order.getTotal();
            }
        }
        
        System.out.println("Pending Orders: " + pendingCount);
        System.out.printf("Total Revenue (Completed Orders): $%.2f%n", totalRevenue);
        System.out.println("=".repeat(70));
    }

    /**
     * Get the restaurant name
     */
    public String getRestaurantName() {
        return restaurantName;
    }
}
