# Restaurant Order Management System - Quick Reference

## Project Summary

A complete Java Collections learning project demonstrating when and why to use:
- **ArrayList** for menu items (random access needed)
- **LinkedList** for kitchen queue (FIFO operations)
- **HashMap** for all orders (permanent record, fast lookup)
- **LinkedHashMap** for completed orders (maintain completion sequence)

## Files Included

| File | Purpose |
|------|---------|
| `OrderStatus.java` | Enum: PENDING, IN_KITCHEN, COMPLETED, CANCELLED |
| `MenuItem.java` | Menu item with ID, name, price, category |
| `OrderItem.java` | Line item in order: MenuItem + quantity |
| `Order.java` | Complete order with ArrayList of items |
| `Restaurant.java` | Main class managing all 4 collections |
| `RestaurantMain.java` | Interactive user interface + demo |
| `README.md` | Complete project documentation |
| `COLLECTION_ANALYSIS.md` | Why each collection was chosen |
| `TESTING_GUIDE.md` | Test scenarios and validation |

## Compilation & Execution

```bash
# Compile all files
javac *.java

# Run interactive program
java RestaurantMain

# Run automated demonstration
echo "9" | java RestaurantMain
```

## Key Design Decisions

### ArrayList<MenuItem> menu
```java
// Best for sequential access and display
for (MenuItem item : menu) { ... }
```

### LinkedList<Order> kitchenQueue
```java
// FIFO queue - O(1) add and remove at ends
kitchenQueue.addLast(order);      // O(1)
Order next = kitchenQueue.removeFirst();  // O(1) - NOT O(n)
```

### HashMap<Integer, Order> orders
```java
// PERMANENT RECORD - never remove, only update status
Order order = orders.get(orderId);  // O(1)
order.setStatus(OrderStatus.COMPLETED);  // Still in HashMap
```

### LinkedHashMap<Integer, Order> completedOrders
```java
// Maintains insertion order (completion sequence)
for (Order o : completedOrders.values()) { ... }  // Ordered iteration
```

## Order Lifecycle

```
createOrder()
    ↓ (Status: PENDING, in HashMap)
    
addItemToOrder() - multiple times
    ↓ (Still in HashMap, Status: PENDING)
    
sendToKitchen()
    ↓ (Status: IN_KITCHEN, in HashMap + LinkedList)
    
completeNextOrder()
    ↓ (Status: COMPLETED, in HashMap + LinkedHashMap, removed from LinkedList)
    
OR: cancelOrder()
    ↓ (Status: CANCELLED, in HashMap, removed from queues)
```

## Common Operations

### Create Order
```java
Order order = restaurant.createOrder("Customer Name");
// Order added to HashMap immediately
```

### Add Item to Order
```java
restaurant.addItemToOrder(orderId, menuItemId, quantity);
// Only works for PENDING orders
```

### Send to Kitchen
```java
restaurant.sendToKitchen(orderId);
// Moves order to LinkedList queue, status → IN_KITCHEN
```

### Complete Order
```java
Order completed = restaurant.completeNextOrder();
// Removes from queue, adds to LinkedHashMap, status → COMPLETED
```

### Cancel Order
```java
restaurant.cancelOrder(orderId);
// Updates status to CANCELLED, removes from queues
```

### Find Order
```java
Order order = restaurant.findOrderById(1001);  // O(1) HashMap lookup
```

## Collection Comparison

| Use Case | ArrayList | LinkedList | HashMap | LinkedHashMap |
|----------|-----------|-----------|---------|---------------|
| Random access | ✓ O(1) | ✗ O(n) | N/A | N/A |
| FIFO queue | ✗ O(n) | ✓ O(1) | N/A | N/A |
| Fast lookup | ✗ O(n) | ✗ O(n) | ✓ O(1) | ✓ O(1) |
| Ordered lookup | ✗ | ✗ | ✗ | ✓ maintains order |
| Memory | Low | High | Medium | High |

## Testing the System

**Quick Test:**
```
1. Run program with: java RestaurantMain
2. Choose option 9 (Demonstration)
3. Watch system create, process, and complete orders
4. View statistics showing all collections working together
```

**Manual Test Sequence:**
1. View Menu (option 6)
2. Create multiple orders (option 1)
3. Add items to orders (option 2)
4. Send to kitchen (option 3)
5. Complete orders (option 4)
6. View all orders (option 7)
7. View statistics (option 8)

## Important Rules

### ✓ HashMap is Permanent Record
- ALL orders go in HashMap when created
- NEVER remove orders from HashMap
- Use status field to track what happened
- Allows complete audit trail

### ✓ Order Status Flow
```
PENDING → (IN_KITCHEN → COMPLETED)
        → (CANCELLED anytime)
```

### ✓ Only PENDING Orders Can be Modified
- Cannot add items to IN_KITCHEN orders
- Cannot add items to COMPLETED orders
- Can only cancel PENDING or IN_KITCHEN orders

### ✓ LinkedList Efficiency
- Kitchen queue uses FIFO (First-In-First-Out)
- removeFirst() must be O(1), not O(n)
- LinkedList is perfect, ArrayList is terrible

## Time Complexity Summary

```
Operation                      Best Collection    Time
─────────────────────────────────────────────────────
Find menu item by ID          HashMap            O(1)
Find order by ID              HashMap            O(n) linear search
Process next kitchen order    LinkedList         O(1)
Get completed order sequence  LinkedHashMap      O(n) ordered
Add to menu                   ArrayList          O(1)
Display menu                  ArrayList          O(n)
```

## Output Examples

### Menu Display
```
APPETIZER:
ID: 101 | Bruschetta             | $7.99 | Category: Appetizer
ID: 102 | Calamari               | $9.99 | Category: Appetizer

MAIN COURSE:
ID: 201 | Spaghetti Carbonara    | $14.99 | Category: Main Course
```

### Order Display
```
ORDER ID: 1001 | CUSTOMER: Alice Johnson
Status: In Kitchen | Created: Sun Aug 30 13:17:41 UTC 2026
ORDER ITEMS:
1. Spaghetti Carbonara       x2 | Unit: $14.99 | Subtotal: $29.98
2. Tiramisu                  x1 | Unit: $6.99 | Subtotal: $6.99
TOTAL ITEMS: 2 (Qty: 3) | TOTAL: $36.97
```

### Kitchen Queue
```
KITCHEN QUEUE (3 orders)
1. Order #1001 | Customer: Alice Johnson   | Status: In Kitchen   | Total: $42.95
2. Order #1002 | Customer: Bob Smith       | Status: In Kitchen   | Total: $27.98
3. Order #1003 | Customer: Charlie Brown   | Status: In Kitchen   | Total: $54.95
```

### Statistics
```
RESTAURANT STATISTICS
Menu Items: 13
Total Orders Created: 3
Orders in Kitchen Queue: 1
Completed Orders: 2
Pending Orders: 0
Total Revenue (Completed Orders): $70.93
```

## Learning Path

1. **Start** - Read README.md (overview)
2. **Design** - Read COLLECTION_ANALYSIS.md (why each collection)
3. **Code** - Study source files (OrderStatus → MenuItem → OrderItem → Order → Restaurant)
4. **Test** - Follow TESTING_GUIDE.md (validation scenarios)
5. **Practice** - Modify and extend the system (see Extension Ideas in README)

## Key Takeaways

1. **Different problems need different solutions**
   - Menu = ArrayList (indexed access)
   - Kitchen queue = LinkedList (FIFO efficiency)
   - Orders = HashMap (fast lookup)
   - Completed = LinkedHashMap (ordered access)

2. **Choosing wrong collection costs performance**
   - ArrayList.remove(0) on 1000 items = 1,000,000 operations
   - LinkedList.removeFirst() on 1000 items = 1,000 operations
   - 1000x performance difference!

3. **Design impacts maintenance**
   - HashMap as permanent record = complete audit trail
   - Order status field = no data loss
   - LinkedHashMap = accurate analytics

4. **Type-safe enums > string states**
   - OrderStatus.PENDING prevents invalid values
   - Compiler catches errors
   - Cleaner, more maintainable code

## Troubleshooting

**Program won't compile:**
```bash
# Make sure Java is installed
java -version

# Compile again
javac *.java
```

**"Order not found" errors:**
- Check order ID is correct
- Verify order was actually created
- Use "View all orders" to see what exists

**Kitchen queue appears empty:**
- Orders must have items before sending to kitchen
- Use "Send to kitchen" after adding items

**Total price incorrect:**
- Check quantities are positive
- Verify menu item prices are correct
- Order.calculateTotal() is called after each modification

## Extensions & Improvements

- Add order timestamps and analytics
- Implement multiple kitchen staff (parallel processing)
- Add inventory tracking per menu item
- Create database persistence
- Build GUI interface with JavaFX
- Add customer loyalty program
- Implement discount codes
- Add table management for dine-in

---

**Start here:** Compile with `javac *.java` and run with `java RestaurantMain`
