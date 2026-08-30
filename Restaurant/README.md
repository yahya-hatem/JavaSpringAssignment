# Restaurant Order Management System

## Project Overview

This is a complete console-based Restaurant Order Management System built in Java, designed to teach and demonstrate the proper use of Java Collections (ArrayList, LinkedList, HashMap, and LinkedHashMap) for different data management tasks.

## Learning Objectives Achieved

✓ **ArrayList** - Efficiently manage menu items with random access capability  
✓ **LinkedList** - Implement a FIFO kitchen order queue with O(1) enqueue/dequeue  
✓ **HashMap** - Maintain a permanent record of all orders for instant O(1) lookup by ID  
✓ **LinkedHashMap** - Track completed orders in the order they were completed  
✓ **Custom Objects** - Work with MenuItem, OrderItem, and Order objects within collections  
✓ **Enum** - Use OrderStatus enum for type-safe order state management  
✓ **Collections Operations** - Add, remove, search, iterate, and transform collection data  

---

## Class Structure

### 1. **OrderStatus** (Enum)
Represents the four possible states of an order:
- `PENDING` - Order created, awaiting kitchen processing
- `IN_KITCHEN` - Order sent to kitchen, currently being prepared
- `COMPLETED` - Order prepared and ready for customer
- `CANCELLED` - Order cancelled by customer or restaurant

```java
public enum OrderStatus {
    PENDING("Pending"),
    IN_KITCHEN("In Kitchen"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");
}
```

### 2. **MenuItem**
Represents a single item on the restaurant menu.

**Attributes:**
- `id` - Unique menu item identifier (int)
- `name` - Dish name (String)
- `price` - Item price (double)
- `category` - Category like "Appetizer", "Main Course", etc. (String)

**Key Methods:**
- Constructors with full parameter initialization
- Getters and setters for all attributes
- `toString()` - Formatted display with aligned columns
- `equals()` and `hashCode()` - Support HashMap operations

### 3. **OrderItem**
Represents a single line item in a customer's order (pairs a MenuItem with quantity).

**Attributes:**
- `item` - Reference to a MenuItem
- `quantity` - How many of this item ordered (int)

**Key Methods:**
- `getItem()` - Returns the MenuItem
- `getQuantity()` - Returns quantity ordered
- `calculateSubtotal()` - Returns price × quantity
- `toString()` - Formatted display showing item details and subtotal

### 4. **Order**
Represents a complete customer order containing multiple OrderItems.

**Attributes:**
- `orderId` - Unique order identifier (int)
- `customerName` - Name of customer placing order (String)
- `items` - **ArrayList<OrderItem>** - Dynamic list of items in order
- `total` - Current order total (double)
- `status` - Current OrderStatus
- `createdTime` - Timestamp when order was created (long)
- `completedTime` - Timestamp when order was completed (long)

**Key Methods:**
- `addItem(OrderItem)` - Add item to order and recalculate total
- `removeItem(int index)` - Remove item by index
- `removeItemByMenuId(int)` - Remove all items with given menu ID
- `calculateTotal()` - Sum all subtotals
- `getItemCount()` - Return number of line items
- `getTotalQuantity()` - Return sum of all quantities
- `setStatus(OrderStatus)` - Update status and timestamp if completed
- `displayOrder()` - Pretty-print complete order details

**Why ArrayList for items?**
- Need to iterate over items multiple times (calculating total, displaying)
- Need indexed access (remove by index)
- Order of items may matter for display
- LinkedList unnecessary overhead; items list is small

### 5. **Restaurant**
Main class managing the entire order management system. Demonstrates strategic collection usage.

#### Collections Used:

| Collection | Purpose | Why This Type? |
|-----------|---------|---|
| `ArrayList<MenuItem> menu` | Store all menu items | Random access O(1) for finding items, easy iteration for display |
| `LinkedList<Order> kitchenQueue` | FIFO queue of orders being processed | Efficient O(1) addLast() and removeFirst(); simulates real kitchen queue |
| `HashMap<Integer, Order> orders` | **Permanent record** of ALL orders ever created | Fast O(1) lookup by orderId; persists completed/cancelled orders with status flag |
| `LinkedHashMap<Integer, Order> completedOrders` | Completed orders in completion order | Maintains insertion order; useful for analytics and kitchen performance tracking |

#### Key Methods:

**Menu Management:**
- `addMenuItemItem(MenuItem)` - Add item to menu
- `removeMenuItem(int)` - Remove by ID
- `findMenuItemById(int)` - Quick menu lookup
- `displayMenu()` - Show all items organized by category

**Order Creation & Lookup:**
- `createOrder(String)` - Create new order, add to HashMap immediately
- `findOrderById(int)` - O(1) lookup using HashMap
- `displayOrder(int)` - Display specific order details

**Order Modification:**
- `addItemToOrder(int, int, int)` - Add item by order ID and menu item ID
- `removeItemFromOrder(int, int)` - Remove from order
- Note: Only PENDING orders can be modified

**Kitchen Queue Operations:**
- `sendToKitchen(int)` - Move order from PENDING → IN_KITCHEN, add to LinkedList queue
- `completeNextOrder()` - Remove first from queue (FIFO), update status, add to LinkedHashMap
- `cancelOrder(int)` - Update status to CANCELLED, remove from kitchen queue if needed
- `displayKitchenQueue()` - Show current queue order

**Reporting & Analytics:**
- `displayAllOrders()` - Show all orders regardless of status
- `displayCompletedOrders()` - Show completed orders in completion order (LinkedHashMap maintains order)
- `displayStatistics()` - Revenue, order counts, queue status

---

## Why Each Collection Type?

### ArrayList for Menu
```java
ArrayList<MenuItem> menu;
```
**Why?** Menu items are referenced by ID frequently. We need O(1) random access to check if an item exists before adding it to an order. ArrayList provides this with simple iteration over small collection.

### LinkedList for Kitchen Queue
```java
LinkedList<Order> kitchenQueue;
```
**Why?** A kitchen queue is FIFO (First-In-First-Out). LinkedList's `addLast()` and `removeFirst()` are O(1) operations, making it perfect for queue operations. This is far more efficient than ArrayList where `remove(0)` would be O(n).

```java
// Efficient O(1) operations
kitchenQueue.addLast(order);      // Add to back of line
Order next = kitchenQueue.removeFirst();  // Process next in line
```

### HashMap for All Orders
```java
HashMap<Integer, Order> orders;
```
**Why?** We need O(1) lookup speed regardless of how many orders exist. HashMap provides this. The assignment specifies this HashMap is the **permanent record** - orders are NEVER removed, only their status is updated. This is crucial for:
- Quickly finding an order by ID
- Preserving history of all orders
- Status field indicates what happened to order

```java
Order order = orders.get(orderId);  // O(1) lookup!
if (order.getStatus() == OrderStatus.COMPLETED) { ... }
```

### LinkedHashMap for Completed Orders
```java
LinkedHashMap<Integer, Order> completedOrders;
```
**Why?** LinkedHashMap maintains **insertion order** (the order in which items were added). This is perfect for tracking completed orders in the sequence they were finished. This enables:
- Accurate order completion sequence reporting
- Performance metrics on which orders completed when
- Visual display showing completion order

---

## Important Design Rules

### ✓ The HashMap Rule (CRITICAL)
The HashMap is the **permanent record**:
- ALL orders go into the HashMap when created
- Orders are **NEVER removed** from the HashMap
- Completed or cancelled orders remain in the HashMap
- The `OrderStatus` field tells you what happened to that order

```java
// CORRECT: Keep order in HashMap, just change status
orders.put(orderId, order);
order.setStatus(OrderStatus.COMPLETED);

// WRONG: Never do this!
// orders.remove(orderId);  // ❌ WRONG
```

### ✓ Order Status Flow
```
PENDING → IN_KITCHEN → COMPLETED
   ↓
CANCELLED (from PENDING or IN_KITCHEN)
```

An order moves through these states:
1. Created as PENDING
2. Sent to kitchen → IN_KITCHEN (added to LinkedList queue)
3. Processed → COMPLETED (moved to LinkedHashMap)
4. OR cancelled at any point → CANCELLED

### ✓ Only PENDING Orders Can Be Modified
- Cannot add items to IN_KITCHEN, COMPLETED, or CANCELLED orders
- This prevents modification of orders already being prepared

---

## Usage Example

```java
// Initialize restaurant
Restaurant restaurant = new Restaurant("Java Bistro");

// Add menu items (ArrayList)
MenuItem pasta = new MenuItem(201, "Spaghetti", 14.99, "Main Course");
restaurant.addMenuItemItem(pasta);

// Create an order (added to HashMap immediately)
Order order = restaurant.createOrder("John Smith");

// Add items to order
restaurant.addItemToOrder(order.getOrderId(), 201, 2);  // 2x Spaghetti

// Send to kitchen (moved to LinkedList queue, status → IN_KITCHEN)
restaurant.sendToKitchen(order.getOrderId());

// Complete order (removed from queue, moved to LinkedHashMap, status → COMPLETED)
Order completed = restaurant.completeNextOrder();

// Query completed orders in completion order
restaurant.displayCompletedOrders();  // LinkedHashMap maintains order

// Find any order by ID (fast O(1) HashMap lookup)
Order found = restaurant.findOrderById(1001);
```

---

## Compilation and Execution

### Compile
```bash
javac *.java
```

### Run Interactive Program
```bash
java RestaurantMain
```

### Run Automated Demonstration
```bash
echo "9" | java RestaurantMain
```

The program will:
1. Initialize the menu with 13 sample items
2. Create 3 orders for different customers
3. Add items to each order
4. Send orders to kitchen
5. Complete orders
6. Display statistics

---

## Sample Output

```
======================================================================
JAVA BISTRO - MAIN MENU
======================================================================
1. Create a new order
2. Add item to order
3. Send order to kitchen
4. Complete order from kitchen
5. Cancel order
6. View menu
7. View all orders
8. View statistics
9. Run demonstration
0. Exit
======================================================================

======================================================================
KITCHEN QUEUE (3 orders)
======================================================================
1. Order #1001 | Customer: Alice Johnson   | Status: In Kitchen   | Total: $42.95 | Items: 3
2. Order #1002 | Customer: Bob Smith       | Status: In Kitchen   | Total: $27.98 | Items: 2
3. Order #1003 | Customer: Charlie Brown   | Status: In Kitchen   | Total: $54.95 | Items: 2
======================================================================

======================================================================
COMPLETED ORDERS (2 total)
======================================================================
1. Order #1001 | Customer: Alice Johnson   | Status: Completed    | Total: $42.95 | Items: 3
2. Order #1002 | Customer: Bob Smith       | Status: Completed    | Total: $27.98 | Items: 2
======================================================================
```

---

## Key Learning Points

1. **Different Collections for Different Purposes**
   - Don't just use ArrayList for everything
   - Match collection type to actual access patterns
   - LinkedList excellent for queues; bad for random access
   - HashMap excellent for lookups; maintains no order

2. **Status Enums Over Strings**
   - Type-safe, preventing invalid values
   - Compiler catches mistakes
   - More readable and maintainable

3. **Immutable Permanent Records**
   - HashMap holds all orders ever created
   - Only status changes, never removal
   - Enables complete business audit trail

4. **Insertion Order Preservation**
   - LinkedHashMap maintains order for analytics
   - Important for kitchen performance metrics
   - Shows which orders completed when

5. **Custom Objects in Collections**
   - Collections can hold any object type
   - Objects manage their own state
   - Enables rich domain modeling

---

## Extension Ideas

- Add price calculation with tax and discounts
- Implement order timing metrics (how long in queue?)
- Add menu item inventory tracking
- Implement customer loyalty/order history
- Add special diet restrictions
- Implement order notes and modifications
- Add serialization to save orders to disk
- Create a GUI interface
- Add database backend

---

## Conclusion

This project demonstrates when to use specific Java collections and why. The key insight is:

> **Use the right data structure for the right job. ArrayList for indexed access, LinkedList for queue operations, HashMap for fast lookups, LinkedHashMap for ordered lookups.**
