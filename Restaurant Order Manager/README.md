# Restaurant Order Management System

A comprehensive Java console application demonstrating proper use of Java Collections (ArrayList, LinkedList, HashMap, and LinkedHashMap) in a real-world restaurant order management scenario.

## Project Overview

This system simulates a restaurant's order management workflow, featuring:
- **Menu Management** using ArrayList for storing menu items
- **Kitchen Queue** using LinkedList for FIFO order processing
- **Order Lookup** using HashMap for O(1) order retrieval by ID
- **Completion History** using LinkedHashMap to preserve completion order
- **Order Status Tracking** using an OrderStatus enum

## Class Structure

### 1. **OrderStatus.java** (Enum)
Defines the possible states of an order:
- `PENDING` - Order created but not yet in kitchen
- `IN_KITCHEN` - Order being prepared
- `COMPLETED` - Order finished and ready
- `CANCELLED` - Order cancelled

### 2. **MenuItem.java**
Represents a single menu item with:
- **Attributes**: id, name, price, category
- **Methods**:
  - Constructors with full initialization
  - Getters and setters for all attributes
  - `toString()` for formatted display

### 3. **OrderItem.java**
Represents a menu item with its quantity in an order:
- **Attributes**: MenuItem item, int quantity
- **Methods**:
  - `getItem()` - returns the MenuItem
  - `getQuantity()` - returns the quantity ordered
  - `calculateSubtotal()` - returns price × quantity
  - `toString()` - formatted display with price breakdown

### 4. **Order.java**
Represents a complete customer order:
- **Attributes**: 
  - `orderId` - unique identifier
  - `customerName` - customer's name
  - `items` - ArrayList<OrderItem>
  - `total` - calculated total price
  - `status` - current OrderStatus
- **Methods**:
  - `addItem(MenuItem, quantity)` - adds item to order
  - `removeItem(index)` - removes item by index
  - `calculateTotal()` - recalculates order total
  - `updateStatus(OrderStatus)` - changes order status
  - `displayOrder()` - formatted order display

### 5. **Restaurant.java** (Core Business Logic)
Manages all restaurant operations and collections:

#### Collections Used:
```java
ArrayList<MenuItem> menu              // All menu items
LinkedList<Order> kitchenQueue        // Orders in kitchen (FIFO)
HashMap<Integer, Order> orders        // All orders (permanent record)
LinkedHashMap<Integer, Order> completedOrders  // Completed orders (ordered)
```

#### Menu Management:
- `addMenuItemToMenu(MenuItem)` - adds item to menu
- `findMenuItemById(int)` - searches menu items
- `displayMenu()` - shows all menu items

#### Order Management:
- `createOrder(String)` - creates new PENDING order
- `addItemToOrder(orderId, menuItemId, quantity)` - adds items to PENDING orders
- `removeItemFromOrder(orderId, index)` - removes items from PENDING orders
- `sendToKitchen(orderId)` - moves order from PENDING to IN_KITCHEN
- `processNextOrder()` - moves order from IN_KITCHEN to COMPLETED
- `cancelOrder(orderId)` - cancels PENDING or IN_KITCHEN orders
- `findOrderById(orderId)` - quick lookup via HashMap
- `displayOrder(orderId)` - shows specific order details
- `displayAllOrders()` - shows all orders (HashMap values)
- `displayKitchenQueue()` - shows queue (LinkedList)
- `displayCompletedOrders()` - shows completed orders (LinkedHashMap - preserves order)
- `displayStatistics()` - shows order count by status

### 6. **Main.java** (User Interface)
Interactive console menu with:
- Menu display
- Order creation and item management
- Kitchen queue management
- Order cancellation
- Statistics display
- Pre-populated sample menu (12 items)

## Collection Usage Explanation

### ArrayList<MenuItem>
**Why ArrayList for menu?**
- Menu items are static, rarely removed
- Random access needed (display all)
- Simple iteration sufficient
- Best practice for collections of immutable data

### LinkedList<Order> (Kitchen Queue)
**Why LinkedList for kitchen queue?**
- FIFO order processing (add to end, remove from front)
- `add()` and `remove()` operations are O(1)
- Perfect for queue-like structures
- Eliminates ArrayList shifting overhead

### HashMap<Integer, Order> (All Orders)
**Why HashMap for order lookup?**
- Permanent record of ALL orders (never deleted)
- Need fast lookup by order ID: O(1) average
- HashMap is the database for this application
- Orders remain even after completion/cancellation
- Status field tells us order's current state

### LinkedHashMap<Integer, Order> (Completed Orders)
**Why LinkedHashMap for completed orders?**
- Shows which orders completed first (insertion order)
- Preserves temporal sequence
- Historical/audit trail of completions
- Subset of orders from HashMap that status == COMPLETED

## Key Rules Implemented

1. **FIFO Kitchen Queue**
   - Orders added to end via `add()`
   - Orders removed from front via `remove()`
   - LinkedList maintains order automatically

2. **Immutable HashMap**
   - Orders NEVER removed from HashMap
   - Completed/cancelled orders stay in HashMap
   - OrderStatus field indicates current state

3. **Order Lifecycle**
   ```
   PENDING → (sendToKitchen) → IN_KITCHEN → (processNextOrder) → COMPLETED
             ↘ (cancelOrder) → CANCELLED (stays in HashMap)
   ```

4. **Validation**
   - Items can only be added to PENDING orders
   - Orders must have items before kitchen
   - Cannot cancel COMPLETED orders
   - Cannot process empty queue

## Usage Instructions

### Compilation
```bash
javac *.java
```

### Running
```bash
java Main
```

### Example Workflow
1. View Menu (see available items with IDs)
2. Create Order (enter customer name)
3. Add items (enter menu item ID and quantity)
4. Send to Kitchen (order changes to IN_KITCHEN)
5. Process Orders (kitchen processes queue FIFO)
6. View Completed/All Orders (see results)
7. Cancel Order (optional - only PENDING or IN_KITCHEN)

## Sample Menu
- **Appetizers**: Garlic Bread (101), Mozzarella Sticks (102), Caesar Salad (103)
- **Mains**: Spaghetti Carbonara (201), Grilled Salmon (202), Beef Burger (203), Margherita Pizza (204)
- **Desserts**: Tiramisu (301), Chocolate Cake (302), Cheesecake (303)
- **Beverages**: Soft Drink (401), Coffee (402), Wine (403)

## Learning Outcomes

By completing this project, you will:
- ✓ Understand when to use ArrayList, LinkedList, HashMap, LinkedHashMap
- ✓ Practice implementing FIFO queues with LinkedList
- ✓ Use HashMap for fast lookups and permanent record-keeping
- ✓ Preserve insertion order with LinkedHashMap
- ✓ Work with enums for type-safe status values
- ✓ Implement complex business logic across multiple classes
- ✓ Design user-friendly console interfaces
- ✓ Practice validation and error handling

## Time Complexity Analysis

| Operation | Collection | Complexity |
|-----------|-----------|-----------|
| Add menu item | ArrayList | O(1) amortized |
| Add to queue | LinkedList | O(1) |
| Remove from queue | LinkedList | O(1) |
| Find order by ID | HashMap | O(1) average |
| Display all orders | HashMap | O(n) |
| View queue order | LinkedList | O(1) per item |
| Preserve completion history | LinkedHashMap | O(1) + ordered |

## Potential Enhancements

1. File persistence (save/load orders to file)
2. Order timing (track preparation times)
3. Multiple kitchen zones (separate queues by dish type)
4. Customer notifications
5. Inventory management
6. Revenue reporting
7. Staff assignment tracking
8. Priority orders (VIP customers)

## File Listing

```
OrderStatus.java          - Enum for order states
MenuItem.java             - Menu item class
OrderItem.java            - Order item class
Order.java                - Order class
Restaurant.java           - Core business logic
Main.java                 - Console UI and main entry point
README.md                 - This documentation
```

---

**Assignment Requirements Met:**
- ✓ ArrayList for menu storage
- ✓ LinkedList for kitchen FIFO queue
- ✓ HashMap for all-orders permanent record
- ✓ LinkedHashMap for completed orders (ordered)
- ✓ OrderStatus enum with all required states
- ✓ All required methods implemented
- ✓ Proper validation and error handling
- ✓ Demonstrates why each collection is appropriate
