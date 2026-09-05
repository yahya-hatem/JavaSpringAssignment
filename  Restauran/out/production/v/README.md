# Restaurant Order Management System
## Java 8 Features Project

A complete console-based Restaurant Order Management System built in Java, demonstrating proper use of Java Collections and Java 8 features.

---

## 📁 Project Structure

```
Restaurant_Project/
├── README.md                           # This file
├── src/                                # Source Code
│   ├── RestaurantMain.java            # Main entry point
│   ├── Restaurant.java                # Core management class
│   ├── Order.java                     # Order model
│   ├── OrderItem.java                 # Order line item
│   ├── MenuItem.java                  # Menu item model
│   └── OrderStatus.java               # Enum for order states
├── analysis/                          # Analysis Documents
│   ├── Java8_Features_Analysis_English.md   # English analysis
│   └── Java8_Features_Analysis.md           # Arabic analysis
└── docs/                              # Additional documentation
```

---

## 🎯 Project Overview

### Purpose
This project demonstrates:
- **Java Collections Framework** (ArrayList, LinkedList, HashMap, LinkedHashMap)
- **Java 8 Features** (Lambda Expressions, forEach, Streams API, Functional Interfaces)
- **Object-Oriented Programming** Principles
- **Design Patterns** (Singleton, Factory)

### Key Features
✅ Create and manage restaurant menu  
✅ Create and process customer orders  
✅ Manage kitchen queue (FIFO)  
✅ Track completed orders  
✅ Generate statistics and reports  
✅ Interactive command-line interface  

---

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher installed
- Command line/Terminal access

### Compilation

```bash
# Navigate to src directory
cd src

# Compile all Java files
javac *.java

# Run the application
java RestaurantMain
```

### Alternative (Single Command)

```bash
javac src/*.java && java -cp src RestaurantMain
```

---

## 📋 Class Structure

### 1. **OrderStatus** (Enum)
Represents the four possible states of an order:
- `PENDING` - Order created, awaiting kitchen processing
- `IN_KITCHEN` - Order sent to kitchen, currently being prepared
- `COMPLETED` - Order prepared and ready for customer
- `CANCELLED` - Order cancelled

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
- `id` - Unique menu item identifier
- `name` - Dish name
- `price` - Item price
- `category` - Category (Appetizer, Main Course, Dessert, Beverage)

### 3. **OrderItem**
Represents a single line item in a customer's order.

**Attributes:**
- `item` - Reference to a MenuItem
- `quantity` - How many of this item ordered

### 4. **Order**
Represents a complete customer order containing multiple OrderItems.

**Attributes:**
- `orderId` - Unique order identifier
- `customerName` - Name of customer
- `items` - ArrayList of OrderItems
- `total` - Order total
- `status` - Current OrderStatus
- `createdTime` - Timestamp of creation
- `completedTime` - Timestamp of completion

### 5. **Restaurant**
Main class managing the entire order management system.

**Collections Used:**
| Collection | Purpose | Why This Type? |
|-----------|---------|---|
| `ArrayList<MenuItem>` | Menu items | Random access O(1), easy iteration |
| `LinkedList<Order>` | Kitchen queue | Efficient FIFO operations O(1) |
| `HashMap<Integer, Order>` | All orders | Fast O(1) lookup by ID |
| `LinkedHashMap<Integer, Order>` | Completed orders | Maintains insertion order |

### 6. **RestaurantMain**
Interactive main program with menu-driven interface.

---

## ☕ Java 8 Features Demonstrated

### 1. Lambda Expressions
```java
menu.removeIf(item -> item.getId() == itemId);
orders.values().forEach(order -> System.out.println(order.toString()));
```

### 2. forEach() Method
```java
for (MenuItem item : menu) {
    // Process item
}
```

### 3. removeIf() Predicate
```java
items.removeIf(orderItem -> orderItem.getItem().getId() == menuItemId);
```

### 4. Map.computeIfAbsent()
```java
itemsByCategory.computeIfAbsent(item.getCategory(), 
    k -> new ArrayList<>()).add(item);
```

### 5. Enum with Constructor
```java
public enum OrderStatus {
    PENDING("Pending"),
    IN_KITCHEN("In Kitchen"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
```

### 6. Integer.hashCode()
```java
@Override
public int hashCode() {
    return Integer.hashCode(id);
}
```

---

## 📊 Usage Examples

### Create a New Order
```
Enter choice (1-9): 1
Enter customer name: Alice Johnson
✓ New order created: Order #1001 for Alice Johnson
```

### Add Items to Order
```
Enter choice (1-9): 2
Enter order ID: 1001
Enter menu item ID: 201
Enter quantity: 2
✓ Added 2x Spaghetti Carbonara to Order #1001
```

### Send Order to Kitchen
```
Enter choice (1-9): 3
Enter order ID: 1001
✓ Order #1001 sent to kitchen. Queue size: 1
```

### Complete Order
```
Enter choice (1-9): 4
Complete the first order in queue? (y/n): y
✓ Order #1001 completed
```

### View Statistics
```
Enter choice (1-9): 8
================================================================================
RESTAURANT STATISTICS
================================================================================
Menu Items: 12
Total Orders Created: 5
Orders in Kitchen Queue: 2
Completed Orders: 2
Pending Orders: 1
Total Revenue (Completed Orders): $89.97
================================================================================
```

---

## 🎓 Learning Outcomes

After studying this project, you will understand:

1. **Java Collections Framework**
   - When to use ArrayList vs LinkedList vs HashMap
   - Time complexity of operations (O(1) vs O(n))
   - Iteration methods and performance implications

2. **Java 8 Features**
   - Lambda Expressions for functional programming
   - forEach() and removeIf() for collection operations
   - Map API improvements (computeIfAbsent)
   - Enum type safety

3. **Object-Oriented Design**
   - Class hierarchy and relationships
   - Encapsulation and access modifiers
   - Method design and responsibilities

4. **Software Architecture**
   - Model-View-Controller concepts
   - Separation of concerns
   - Data management patterns

---

## 📚 Documentation

### Analysis Documents
- **Java8_Features_Analysis_English.md** - Comprehensive Java 8 features analysis in English
- **Java8_Features_Analysis.md** - Complete analysis in Arabic

### Key Topics Covered
- Lambda Expressions with detailed examples
- Stream API introduction
- Map API improvements
- Functional Interfaces
- Method References
- Enum design patterns
- Recommended improvements for Java 8 adoption

---

## 🔧 Advanced Features (Recommended Enhancements)

### 1. Stream API for Statistics
```java
public double getTotalRevenue() {
    return orders.values().stream()
        .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
        .mapToDouble(Order::getTotal)
        .sum();
}
```

### 2. Optional for Safe Lookups
```java
public Optional<MenuItem> findMenuItemById(int itemId) {
    return menu.stream()
        .filter(item -> item.getId() == itemId)
        .findFirst();
}
```

### 3. Method References
```java
orders.values().forEach(System.out::println);
menu.forEach(System.out::println);
```

### 4. Collectors for Grouping
```java
public Map<String, List<MenuItem>> groupMenuByCategory() {
    return menu.stream()
        .collect(Collectors.groupingBy(MenuItem::getCategory));
}
```

---

## 🐛 Testing Recommendations

### Unit Tests to Create
- [ ] MenuItem equality and hashCode
- [ ] Order total calculation
- [ ] Order status transitions
- [ ] Kitchen queue FIFO operations
- [ ] Menu item removal
- [ ] Order cancellation

### Test Scenarios
- [ ] Create order with multiple items
- [ ] Calculate total with different quantities
- [ ] Process order through all states
- [ ] Handle invalid order IDs
- [ ] Manage kitchen queue operations

---

## 📝 Notes

- The project uses Java 8+ features
- Some methods use `String.repeat()` which requires Java 11+
- For strict Java 8 compatibility, replace with traditional string concatenation
- All collections are properly initialized and managed
- No external dependencies required

---

## 🎯 Improvement Checklist

- [ ] Refactor to use Stream API extensively
- [ ] Implement Optional for null safety
- [ ] Add comprehensive unit tests
- [ ] Implement method references throughout
- [ ] Add data persistence (file I/O)
- [ ] Create database integration
- [ ] Add GUI with JavaFX or Swing
- [ ] Implement multi-threading for kitchen operations
- [ ] Add payment processing
- [ ] Create REST API

---

## 📞 Support & Questions

For detailed analysis of Java 8 features used in this project, refer to:
- `analysis/Java8_Features_Analysis_English.md` - Complete English documentation
- `analysis/Java8_Features_Analysis.md` - Complete Arabic documentation

---

## ✅ Checklist for Running Project

- [x] All Java files present
- [x] Proper folder structure
- [x] Documentation complete
- [x] Analysis files included
- [x] Ready to compile and run
- [x] Examples provided

---

## 📄 File Summary

| File | Purpose | Lines |
|------|---------|-------|
| RestaurantMain.java | Main entry point | 265 |
| Restaurant.java | Core management | 365 |
| Order.java | Order model | 178 |
| OrderItem.java | Order line item | 60 |
| MenuItem.java | Menu item model | 77 |
| OrderStatus.java | Order state enum | 26 |
| **Total Source Code** | | **971** |

---

## 🚀 Next Steps

1. **Compile and run** the application
2. **Review** the source code
3. **Read** the Java 8 analysis documents
4. **Understand** each Java 8 feature in context
5. **Experiment** by making modifications
6. **Implement** recommended enhancements

---

**Project Version:** 1.0  
**Java Version:** Java 8+  
**Status:** Complete and Ready to Use  
**Last Updated:** September 2026  

---

## 📖 How to Read the Documentation

1. Start with this **README.md** for overview
2. Review the **source code** in `src/` directory
3. Read **Java8_Features_Analysis_English.md** for detailed analysis
4. Study individual Java files and correlate with analysis
5. Try implementing recommended enhancements

---

**Happy Learning! 🎓**
