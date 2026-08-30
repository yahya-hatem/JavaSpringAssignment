import java.util.Scanner;

/**
 * Main demonstration program for the Restaurant Order Management System
 * This program demonstrates:
 * - Building and managing a menu (ArrayList)
 * - Creating and processing orders (HashMap)
 * - Managing the kitchen queue (LinkedList)
 * - Tracking completed orders (LinkedHashMap)
 */
public class RestaurantMain {
    private static Restaurant restaurant;
    private static Scanner scanner;

    public static void main(String[] args) {
        restaurant = new Restaurant("Java Bistro");
        scanner = new Scanner(System.in);

        // Initialize the menu with sample items
        initializeMenu();

        // Run the interactive menu
        runRestaurantMenu();

        scanner.close();
    }

    /**
     * Initialize the restaurant with sample menu items
     */
    private static void initializeMenu() {
        System.out.println("=".repeat(70));
        System.out.println("Welcome to " + restaurant.getRestaurantName());
        System.out.println("Initializing menu...");
        System.out.println("=".repeat(70));

        // Appetizers
        restaurant.addMenuItemItem(new MenuItem(101, "Bruschetta", 7.99, "Appetizer"));
        restaurant.addMenuItemItem(new MenuItem(102, "Calamari", 9.99, "Appetizer"));
        restaurant.addMenuItemItem(new MenuItem(103, "Mozzarella Sticks", 5.99, "Appetizer"));

        // Main Courses
        restaurant.addMenuItemItem(new MenuItem(201, "Spaghetti Carbonara", 14.99, "Main Course"));
        restaurant.addMenuItemItem(new MenuItem(202, "Grilled Salmon", 18.99, "Main Course"));
        restaurant.addMenuItemItem(new MenuItem(203, "Beef Stroganoff", 16.99, "Main Course"));
        restaurant.addMenuItemItem(new MenuItem(204, "Vegetarian Pasta", 12.99, "Main Course"));

        // Desserts
        restaurant.addMenuItemItem(new MenuItem(301, "Tiramisu", 6.99, "Dessert"));
        restaurant.addMenuItemItem(new MenuItem(302, "Chocolate Lava Cake", 7.99, "Dessert"));
        restaurant.addMenuItemItem(new MenuItem(303, "Panna Cotta", 5.99, "Dessert"));

        // Beverages
        restaurant.addMenuItemItem(new MenuItem(401, "Espresso", 2.99, "Beverage"));
        restaurant.addMenuItemItem(new MenuItem(402, "Iced Tea", 2.49, "Beverage"));
        restaurant.addMenuItemItem(new MenuItem(403, "Red Wine", 8.99, "Beverage"));

        System.out.println("✓ Menu initialized with 12 items\n");
    }

    /**
     * Interactive menu system
     */
    private static void runRestaurantMenu() {
        boolean running = true;

        while (running) {
            displayMainMenu();
            System.out.print("Enter choice (1-9): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleCreateOrder();
                    break;
                case "2":
                    handleAddItemToOrder();
                    break;
                case "3":
                    handleSendToKitchen();
                    break;
                case "4":
                    handleCompleteOrder();
                    break;
                case "5":
                    handleCancelOrder();
                    break;
                case "6":
                    restaurant.displayMenu();
                    break;
                case "7":
                    restaurant.displayAllOrders();
                    break;
                case "8":
                    restaurant.displayStatistics();
                    break;
                case "9":
                    runDemonstration();
                    break;
                case "0":
                    running = false;
                    System.out.println("\nThank you for using Java Bistro!");
                    break;
                default:
                    System.out.println("✗ Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Display the main menu options
     */
    private static void displayMainMenu() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("JAVA BISTRO - MAIN MENU");
        System.out.println("-".repeat(70));
        System.out.println("1. Create a new order");
        System.out.println("2. Add item to order");
        System.out.println("3. Send order to kitchen");
        System.out.println("4. Complete order from kitchen");
        System.out.println("5. Cancel order");
        System.out.println("6. View menu");
        System.out.println("7. View all orders");
        System.out.println("8. View statistics");
        System.out.println("9. Run demonstration");
        System.out.println("0. Exit");
        System.out.println("-".repeat(70));
    }

    /**
     * Handle order creation
     */
    private static void handleCreateOrder() {
        System.out.print("\nEnter customer name: ");
        String customerName = scanner.nextLine().trim();
        if (!customerName.isEmpty()) {
            restaurant.createOrder(customerName);
        } else {
            System.out.println("✗ Customer name cannot be empty");
        }
    }

    /**
     * Handle adding item to order
     */
    private static void handleAddItemToOrder() {
        System.out.print("\nEnter order ID: ");
        int orderId = getIntInput();
        
        System.out.print("Enter menu item ID: ");
        int menuItemId = getIntInput();
        
        System.out.print("Enter quantity: ");
        int quantity = getIntInput();

        if (quantity > 0) {
            restaurant.addItemToOrder(orderId, menuItemId, quantity);
        } else {
            System.out.println("✗ Quantity must be greater than 0");
        }
    }

    /**
     * Handle sending order to kitchen
     */
    private static void handleSendToKitchen() {
        System.out.print("\nEnter order ID: ");
        int orderId = getIntInput();
        restaurant.sendToKitchen(orderId);
        restaurant.displayKitchenQueue();
    }

    /**
     * Handle completing order
     */
    private static void handleCompleteOrder() {
        restaurant.displayKitchenQueue();
        System.out.print("\nComplete the first order in queue? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("y")) {
            Order completed = restaurant.completeNextOrder();
            if (completed != null) {
                completed.displayOrder();
            }
        }
    }

    /**
     * Handle canceling order
     */
    private static void handleCancelOrder() {
        System.out.print("\nEnter order ID to cancel: ");
        int orderId = getIntInput();
        restaurant.cancelOrder(orderId);
    }

    /**
     * Get an integer input from user
     */
    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid input");
            return -1;
        }
    }

    /**
     * Run an automated demonstration of the system
     */
    private static void runDemonstration() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("RUNNING AUTOMATED DEMONSTRATION");
        System.out.println("=".repeat(70));

        // Create several orders
        System.out.println("\n--- STEP 1: Creating Orders ---");
        Order order1 = restaurant.createOrder("Alice Johnson");
        Order order2 = restaurant.createOrder("Bob Smith");
        Order order3 = restaurant.createOrder("Charlie Brown");

        // Add items to orders
        System.out.println("\n--- STEP 2: Adding Items to Orders ---");
        restaurant.addItemToOrder(order1.getOrderId(), 201, 2);  // 2x Spaghetti
        restaurant.addItemToOrder(order1.getOrderId(), 301, 1);  // 1x Tiramisu
        restaurant.addItemToOrder(order1.getOrderId(), 401, 2);  // 2x Espresso

        restaurant.addItemToOrder(order2.getOrderId(), 202, 1);  // 1x Salmon
        restaurant.addItemToOrder(order2.getOrderId(), 403, 1);  // 1x Wine

        restaurant.addItemToOrder(order3.getOrderId(), 204, 3);  // 3x Veg Pasta
        restaurant.addItemToOrder(order3.getOrderId(), 302, 2);  // 2x Lava Cake

        // Display orders
        System.out.println("\n--- STEP 3: Displaying Orders ---");
        restaurant.displayOrder(order1.getOrderId());
        restaurant.displayOrder(order2.getOrderId());

        // Send orders to kitchen
        System.out.println("\n--- STEP 4: Sending Orders to Kitchen ---");
        restaurant.sendToKitchen(order1.getOrderId());
        restaurant.sendToKitchen(order2.getOrderId());
        restaurant.sendToKitchen(order3.getOrderId());
        restaurant.displayKitchenQueue();

        // Complete some orders
        System.out.println("\n--- STEP 5: Completing Orders from Kitchen ---");
        for (int i = 0; i < 2; i++) {
            Order completed = restaurant.completeNextOrder();
            if (completed != null) {
                System.out.println("Completed: " + completed.toString());
            }
        }

        // Display statistics
        System.out.println("\n--- STEP 6: Order Statistics ---");
        restaurant.displayAllOrders();
        restaurant.displayCompletedOrders();
        restaurant.displayKitchenQueue();
        restaurant.displayStatistics();

        System.out.println("\n--- DEMONSTRATION COMPLETE ---\n");
    }
}
