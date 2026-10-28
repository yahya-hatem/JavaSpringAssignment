package org.route;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Prototype scope: every getBean(ShoppingCart.class) returns a new, separate cart. */
@Component
@Scope("prototype")
public class ShoppingCart {

    private final ProductService productService;
    private final int maxQuantityPerProduct;
    private final Map<String, Integer> lines = new LinkedHashMap<>(); // SKU -> quantity

    @Getter
    private int cartNumber;
    @Getter
    private Customer customer;

    public ShoppingCart(ProductService productService,
                        @Value("${store.max-quantity-per-product}") int maxQuantityPerProduct) {
        this.productService = productService;
        this.maxQuantityPerProduct = maxQuantityPerProduct;
    }

    public void initialize(int cartNumber, Customer customer) {
        this.cartNumber = cartNumber;
        this.customer = customer;
    }

    public void addItem(String sku, int qty) {
        if (qty <= 0) {
            throw new StoreException("Quantity must be at least 1.");
        }
        Product product = productService.findBySku(sku);
        if (!product.isActive()) {
            throw new StoreException(product.getName() + " is not available.");
        }
        int stock = product.getStockQuantity();
        if (stock == 0) {                                              // CART-01
            throw new StoreException(product.getName() + " is out of stock.");
        }
        int newQty = lines.getOrDefault(sku, 0) + qty;                 // CART-02
        if (newQty > maxQuantityPerProduct) {                          // CART-03
            throw new StoreException("A line's quantity cannot exceed " + maxQuantityPerProduct
                    + " (cart would have " + newQty + " x " + product.getName() + ").");
        }
        if (newQty > stock) {                                          // CART-03
            throw new StoreException("Only " + stock + " available for " + product.getName() + ".");
        }
        lines.put(sku, newQty);
    }

    public BigDecimal subtotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> line : lines.entrySet()) {
            sum = sum.add(productService.getPrice(line.getKey(), line.getValue()));
        }
        return Money.round(sum);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public void clear() {
        lines.clear();
    }

    public Map<String, Integer> getLines() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(lines));
    }
}
