# Restaurant Order Management System - Testing Guide

## Testing Strategies

This guide provides comprehensive test scenarios to validate the Restaurant Order Management System.

---

## Test Scenario 1: Basic Order Creation and Processing

### Objective
Verify that orders flow correctly through all states: PENDING → IN_KITCHEN → COMPLETED

### Steps
1. Create order for "John Smith"
2. Add items to order
3. Send to kitchen (verify status changes to IN_KITCHEN)
4. Complete order (verify status changes to COMPLETED)

### Validation
```
✓ Order appears in HashMap with correct ID
✓ Order status is PENDING initially
✓ Order added to LinkedList kitchen queue
✓ Order status becomes IN_KITCHEN
✓ Order removed from LinkedList queue
✓ Order status becomes COMPLETED
✓ Order appears in LinkedHashMap
✓ Order remains in HashMap (permanent record)
```

### Expected Output
```
✓ New order created: Order #1001 for John Smith
✓ Added 2x Spaghetti Carbonara to Order #1001
✓ Order #1001 sent to kitchen. Queue size: 1
✓ Order #1001 completed. Remaining in queue: 0
```

---

## Test Scenario 2: Menu Item Management

### Objective
Verify ArrayList correctly stores and retrieves menu items

### Steps
1. Add 5 different menu items to ArrayList
2. Search for each by ID
3. Display complete menu grouped by category
4. Remove one item
5. Verify item no longer in menu

### Validation Checklist
```
✓ All items added to ArrayList successfully
✓ findMenuItemById() returns correct item with O(1) perception
✓ Menu displays grouped by category
✓ Menu counts correct number of items
✓ Removed item no longer found by search
✓ MenuItem toString() shows formatted output
```

### Test Data
```java
MenuItem m1 = new MenuItem(101, "Appetizer A", 5.99, "Appetizer");
MenuItem m2 = new MenuItem(102, "Appetizer B", 6.99, "Appetizer");
MenuItem m3 = new MenuItem(201, "Main A", 14.99, "Main Course");
MenuItem m4 = new MenuItem(202, "Main B", 15.99, "Main Course");
MenuItem m5 = new MenuItem(301, "Dessert A", 5.99, "Dessert");
```

---

## Test Scenario 3: Kitchen Queue FIFO Order

### Objective
Verify LinkedList maintains FIFO order and processes in correct sequence

### Steps
1. Create 5 orders
2. Add items to each
3. Send ALL orders to kitchen
4. Verify queue shows correct order
5. Complete orders one by one
6. Verify each is removed from front (FIFO)

### Validation
```
✓ Kitchen queue shows orders in send order
✓ First order sent is first to be completed
✓ removeFirst() removes in FIFO order (not random)
✓ Queue size decreases correctly
✓ Order removed from LinkedList stays in HashMap
```

### Expected Behavior
```
Send orders: 1001, 1002, 1003, 1004, 1005
Kitchen queue: [1001, 1002, 1003, 1004, 1005]

Complete order 1:
Queue: [1002, 1003, 1004, 1005]

Complete order 2:
Queue: [1003, 1004, 1005]

// Orders 1001, 1002 remain in HashMap with COMPLETED status
```

---

## Test Scenario 4: HashMap Permanent Record

### Objective
Verify HashMap maintains ALL orders regardless of status, and never removes completed orders

### Steps
1. Create 3 orders
2. Complete 1 order
3. Cancel 1 order
4. Leave 1 pending
5. Display all orders
6. Verify all 3 still in HashMap
7. Check status fields show what happened

### Validation Checklist
```
✓ HashMap.size() shows all 3 orders
✓ Completed order still in HashMap
✓ Cancelled order still in HashMap
✓ findOrderById() returns order regardless of status
✓ Order status field correctly shows COMPLETED, CANCELLED, PENDING
✓ No orders removed from HashMap
```

### Test Code
```java
// After running scenario
Restaurant r = new Restaurant("Test");
Order o1 = r.createOrder("Alice");
Order o2 = r.createOrder("Bob");
Order o3 = r.createOrder("Charlie");

// ... add items, send to kitchen, complete/cancel ...

// All should still exist
assert r.findOrderById(o1.getOrderId()) != null;
assert r.findOrderById(o2.getOrderId()) != null;
assert r.findOrderById(o3.getOrderId()) != null;

// Size should be 3
assertEquals(3, r.getAllOrders().size());
```

---

## Test Scenario 5: LinkedHashMap Completion Order

### Objective
Verify LinkedHashMap maintains insertion order of completed orders

### Steps
1. Create 3 orders: A, B, C
2. Send all to kitchen
3. Complete them out of order: C, A, B
4. Display completed orders
5. Verify they appear in completion order: C, A, B (not creation order)

### Validation
```
✓ Completed orders display in order they were completed
✓ Order of completion is preserved across sessions
✓ LinkedHashMap iteration yields orders in completion sequence
✓ Not affected by original order creation order
```

### Scenario
```
Created:  1001→Alice, 1002→Bob, 1003→Charlie
Kitchen:  [1001, 1002, 1003]
Completed: 1003 first
          then 1001
          then 1002

Display shows: 1003, 1001, 1002 (completion order)
```

---

## Test Scenario 6: Order Item Management

### Objective
Verify Order's ArrayList of OrderItems works correctly

### Steps
1. Create order
2. Add 5 items with different quantities
3. Display order (shows all items)
4. Calculate total (sum of subtotals)
5. Remove middle item
6. Verify total recalculated
7. Modify quantity of remaining item

### Validation
```
✓ ArrayList holds all OrderItems
✓ Order displays items with correct formatting
✓ Total calculates as sum of (price × quantity)
✓ Removing item removes only that item
✓ Total recalculates after removal
✓ getTotalQuantity() sums all quantities
✓ getItemCount() returns number of line items
```

### Test Data
```
Order for John Smith:
  Item 1: Pasta × 2 @ $14.99 = $29.98
  Item 2: Wine × 1 @ $8.99 = $8.99
  Item 3: Bread × 3 @ $2.99 = $8.97
  Item 4: Dessert × 1 @ $5.99 = $5.99
  Item 5: Coffee × 2 @ $2.99 = $5.98
  ─────────────────────────────
  Subtotal: $59.91
  Items: 5 | Qty: 9
```

---

## Test Scenario 7: Order Status Validation

### Objective
Verify OrderStatus enum prevents invalid status transitions and behaviors

### Steps
1. Create order (verify starts as PENDING)
2. Try to add item to IN_KITCHEN order (should fail)
3. Try to cancel COMPLETED order (should fail)
4. Send order to kitchen (verify status updates)
5. Verify enum provides correct display names

### Validation
```
✓ New orders always start as PENDING
✓ Cannot add items to non-PENDING orders
✓ Cannot cancel COMPLETED orders
✓ Status displays human-readable name (not raw enum)
✓ All status values work correctly
✓ Status transitions are logical
```

### Test Cases
```java
Order order = restaurant.createOrder("Test");
assertEquals(OrderStatus.PENDING, order.getStatus());

// Try invalid operations
assertFalse(restaurant.sendToKitchen(-1));  // Invalid ID
assertFalse(restaurant.cancelOrder(-1));     // Invalid ID

// Valid flow
assertTrue(restaurant.sendToKitchen(order.getOrderId()));
assertEquals(OrderStatus.IN_KITCHEN, order.getStatus());

assertTrue(restaurant.cancelOrder(order.getOrderId()));
assertEquals(OrderStatus.CANCELLED, order.getStatus());
```

---

## Test Scenario 8: Data Integrity

### Objective
Verify system maintains data consistency across collections

### Steps
1. Create order and send to kitchen
2. Verify order appears in:
   - HashMap (orders)
   - LinkedList (kitchenQueue)
3. Complete order
4. Verify order appears in:
   - HashMap (orders) - still there!
   - LinkedHashMap (completedOrders)
   - NOT in LinkedList (removed)
5. Verify status shows COMPLETED

### Validation
```
✓ Order in HashMap with current status
✓ Order in appropriate queue (kitchen or completed)
✓ Order removed from old queue when moved
✓ Order never removed from HashMap
✓ Status field accurate in all references
```

### Data Flow Diagram
```
createOrder()
    ↓
Order added to HashMap
    ↓
sendToKitchen()
    ├→ HashMap status = IN_KITCHEN
    └→ Order added to LinkedList queue
    ↓
completeNextOrder()
    ├→ Order removed from LinkedList
    ├→ HashMap status = COMPLETED
    └→ Order added to LinkedHashMap
```

---

## Test Scenario 9: Edge Cases

### Objective
Verify system handles unusual situations correctly

### Test Cases

**Empty Collections:**
```java
// Kitchen queue operations on empty queue
restaurant.completeNextOrder();  // Should say "empty" not crash
restaurant.displayKitchenQueue(); // Should say "empty"
```

**Duplicate Items in Order:**
```java
// Add same menu item twice - should have two separate OrderItems
restaurant.addItemToOrder(orderId, menuItem101, 2);
restaurant.addItemToOrder(orderId, menuItem101, 3);  // 5 total? or 2 line items?
// Should create 2 line items: "2x Item" and "3x Item"
```

**Invalid IDs:**
```java
restaurant.findOrderById(9999);        // Returns null
restaurant.addItemToOrder(9999, 1, 1); // Fails gracefully
restaurant.sendToKitchen(9999);        // Fails gracefully
```

**Large Order:**
```java
// Order with 50 items
for (int i = 0; i < 50; i++) {
    restaurant.addItemToOrder(orderId, (101 + i), 1);
}
// Verify total calculates correctly
// Verify display handles large order well
```

**Order Lifecycle:**
```java
// Create order, add item
// Don't send to kitchen, cancel instead
restaurant.cancelOrder(orderId);
assertEquals(OrderStatus.CANCELLED, order.getStatus());

// Verify in HashMap but not in any queue
assertTrue(hashMapContains(orderId));
assertFalse(kitchenQueueContains(orderId));
```

---

## Test Scenario 10: Performance Verification

### Objective
Verify collections perform with expected time complexity

### Measurement: Order Lookup Speed

```java
// Create 1000 orders
for (int i = 0; i < 1000; i++) {
    Order order = restaurant.createOrder("Customer " + i);
    restaurant.addItemToOrder(order.getOrderId(), 201, 1);
}

// Measure lookup time
long startTime = System.nanoTime();
for (int i = 0; i < 10000; i++) {
    Order order = restaurant.findOrderById(1500);  // Random order
}
long endTime = System.nanoTime();

// Should be O(1) - consistent time regardless of which order
System.out.println("Lookup time: " + (endTime - startTime) / 10000.0 + " ns");
// Expected: 1000-5000 ns per lookup (constant time)
// NOT exponential with order count
```

---

## Automated Test Implementation

```java
public class RestaurantTest {
    private Restaurant restaurant;
    
    @Before
    public void setup() {
        restaurant = new Restaurant("Test Restaurant");
        initializeMenu();
    }
    
    @Test
    public void testOrderCreation() {
        Order order = restaurant.createOrder("Alice");
        assertNotNull(order);
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNotNull(restaurant.findOrderById(order.getOrderId()));
    }
    
    @Test
    public void testKitchenQueueFIFO() {
        Order o1 = restaurant.createOrder("Customer 1");
        Order o2 = restaurant.createOrder("Customer 2");
        
        restaurant.addItemToOrder(o1.getOrderId(), 201, 1);
        restaurant.addItemToOrder(o2.getOrderId(), 201, 1);
        
        restaurant.sendToKitchen(o1.getOrderId());
        restaurant.sendToKitchen(o2.getOrderId());
        
        Order completed1 = restaurant.completeNextOrder();
        assertEquals(o1.getOrderId(), completed1.getOrderId());
        
        Order completed2 = restaurant.completeNextOrder();
        assertEquals(o2.getOrderId(), completed2.getOrderId());
    }
    
    @Test
    public void testHashMapPermanentRecord() {
        Order order = restaurant.createOrder("Test");
        restaurant.addItemToOrder(order.getOrderId(), 201, 1);
        restaurant.sendToKitchen(order.getOrderId());
        restaurant.completeNextOrder();
        
        // Still in HashMap
        assertNotNull(restaurant.findOrderById(order.getOrderId()));
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }
    
    private void initializeMenu() {
        restaurant.addMenuItemItem(new MenuItem(201, "Pasta", 14.99, "Main"));
        restaurant.addMenuItemItem(new MenuItem(301, "Dessert", 5.99, "Dessert"));
    }
}
```

---

## Quick Validation Checklist

Use this checklist to verify system correctness:

- [ ] ArrayList stores and retrieves menu items correctly
- [ ] LinkedList maintains FIFO kitchen queue order
- [ ] HashMap provides O(1) order lookup by ID
- [ ] LinkedHashMap maintains completed order sequence
- [ ] No orders removed from HashMap (permanent record)
- [ ] Order status transitions correctly
- [ ] Cannot modify non-PENDING orders
- [ ] Totals calculate correctly
- [ ] All edge cases handled without crashes
- [ ] Output formatting is clean and readable

---

## Manual Testing Session

Start the interactive program:
```bash
java RestaurantMain
```

Follow this sequence:
1. **View Menu** (option 6) - See menu grouped by category
2. **Create Order** (option 1) - Create order for "John Doe"
3. **Add Items** (option 2) - Add several items
4. **View Order** (option 7) - See all orders
5. **Send to Kitchen** (option 3) - Send order to kitchen
6. **View Kitchen Queue** (option 3 again) - See queue
7. **Complete Order** (option 4) - Process order
8. **View Statistics** (option 8) - See all metrics
9. **Run Demo** (option 9) - See full demonstration

After each step, verify output matches expectations!
