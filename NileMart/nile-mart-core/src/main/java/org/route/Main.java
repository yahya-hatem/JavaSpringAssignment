package org.route;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private final ApplicationContext context;
    private final ProductService productService;
    private final CheckoutService checkoutService;
    private final Map<Integer, ShoppingCart> carts = new LinkedHashMap<>();
    private final Scanner in = new Scanner(System.in);
    private int nextCartNumber = 1;

    private Main(ApplicationContext context) {
        this.context = context;
        this.productService = context.getBean(ProductService.class);
        this.checkoutService = context.getBean(CheckoutService.class);
    }

    public static void main(String[] args) {
        // try-with-resources closes the context on exit, so the @PreDestroy methods run
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            new Main(context).run();
        }
    }

    private void run() {
        while (true) {
            printMenu();
            if (!in.hasNextLine()) {
                break;
            }
            String choice = in.nextLine().trim();
            if (choice.equals("0")) {
                break;
            }
            try {
                switch (choice) {
                    case "1" -> showProducts();
                    case "2" -> createCart();
                    case "3" -> addToCart();
                    case "4" -> showCart();
                    case "5" -> checkOut();
                    default -> System.out.println("Invalid option.");
                }
            } catch (StoreException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========== NILEMART ==========");
        System.out.println("1. Show products");
        System.out.println("2. Create a cart");
        System.out.println("3. Add a product to a cart");
        System.out.println("4. Show a cart");
        System.out.println("5. Check out a cart");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private void showProducts() {
        System.out.printf("%-14s %-30s %-16s %12s %6s%n", "SKU", "Name", "Category", "Price (EGP)", "Stock");
        for (Product p : productService.findAll()) {
            System.out.printf("%-14s %-30s %-16s %12s %6d%n", p.getSku(), p.getName(),
                    p.getCategory(), Money.format(p.getPrice()), p.getStockQuantity());
        }
    }

    private void createCart() {
        String name = ask("Customer full name: ");
        String phone = ask("Phone: ");
        String email = ask("Email (leave empty for none): ");
        if (name.isBlank() || phone.isBlank()) {
            throw new StoreException("Name and phone are required.");
        }
        Customer customer = new Customer(name, phone, email.isBlank() ? null : email);

        ShoppingCart cart = context.getBean(ShoppingCart.class); // new cart from the container
        cart.initialize(nextCartNumber++, customer);
        carts.put(cart.getCartNumber(), cart);
        System.out.println("Cart #" + cart.getCartNumber() + " created for " + name + ".");
    }

    private void addToCart() {
        ShoppingCart cart = findCart();
        String sku = ask("Product SKU: ").toUpperCase();
        int qty = askInt("Quantity: ");
        cart.addItem(sku, qty);
        System.out.println("Added " + qty + " x " + sku + " to cart #" + cart.getCartNumber() + ".");
    }

    private void showCart() {
        ShoppingCart cart = findCart();
        Customer c = cart.getCustomer();
        System.out.println("Cart #" + cart.getCartNumber() + " - " + c.fullName() + " (" + c.phone() + ")");
        if (cart.isEmpty()) {
            System.out.println("  (empty)");
            return;
        }
        cart.getLines().forEach((sku, qty) -> System.out.printf("  %-14s %-30s x%-3d %12s%n",
                sku, productService.findBySku(sku).getName(), qty,
                Money.format(productService.getPrice(sku, qty))));
        System.out.println("  Subtotal: " + Money.format(cart.subtotal()) + " EGP");
    }

    private void checkOut() {
        ShoppingCart cart = findCart();
        Governorate[] all = Governorate.values();
        for (int i = 0; i < all.length; i++) {
            System.out.printf("%2d. %s%n", i + 1, all[i].getDisplayName());
        }
        int n = askInt("Governorate number: ");
        if (n < 1 || n > all.length) {
            throw new StoreException("Invalid governorate number.");
        }
        Order order = checkoutService.placeOrder(cart, all[n - 1]);

        System.out.println("--- Order " + order.orderNumber() + " ---");
        System.out.println("Subtotal: " + Money.format(order.subtotal()) + " EGP");
        System.out.println("VAT:      " + Money.format(order.vat()) + " EGP");
        System.out.println("Shipping: " + Money.format(order.shippingFee()) + " EGP");
        System.out.println("Total:    " + Money.format(order.total()) + " EGP");
    }

    private ShoppingCart findCart() {
        int number = askInt("Cart number: ");
        ShoppingCart cart = carts.get(number);
        if (cart == null) {
            throw new StoreException("Cart #" + number + " does not exist.");
        }
        return cart;
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return in.hasNextLine() ? in.nextLine().trim() : "";
    }

    private int askInt(String prompt) {
        try {
            return Integer.parseInt(ask(prompt));
        } catch (NumberFormatException e) {
            throw new StoreException("Please enter a valid number.");
        }
    }
}
