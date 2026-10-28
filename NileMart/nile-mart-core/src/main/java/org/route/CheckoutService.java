package org.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ProductService productService;
    private final ShippingQuoteService shippingQuoteService;
    private final TaxCalculator taxCalculator;
    private final OrderRegistry orderRegistry;
    private final List<NotificationSender> notificationSenders;

    public Order placeOrder(ShoppingCart cart, Governorate governorate) {
        // 1. reject an empty cart (ORD-02)
        if (cart.isEmpty()) {
            throw new StoreException("Cannot check out an empty cart.");
        }

        // 2. re-check the stock of every line before changing anything (ORD-03)
        Map<String, Integer> lines = cart.getLines();
        for (Map.Entry<String, Integer> line : lines.entrySet()) {
            Product product = productService.findBySku(line.getKey());
            if (product.getStockQuantity() < line.getValue()) {
                throw new StoreException("Not enough stock for " + product.getName()
                        + ": only " + product.getStockQuantity() + " available.");
            }
        }

        // 3. totals
        BigDecimal subtotal = cart.subtotal();
        BigDecimal vat = taxCalculator.calculate(subtotal);
        BigDecimal shipping = shippingQuoteService.quote(governorate, subtotal);
        BigDecimal total = Money.round(subtotal.add(vat).add(shipping));

        // 4. reduce stock for all lines or none (ORD-06)
        List<Map.Entry<String, Integer>> reduced = new ArrayList<>();
        try {
            for (Map.Entry<String, Integer> line : lines.entrySet()) {
                productService.reduceStock(line.getKey(), line.getValue());
                reduced.add(line);
            }
        } catch (StoreException e) {
            for (Map.Entry<String, Integer> done : reduced) {
                productService.restoreStock(done.getKey(), done.getValue());
            }
            throw e;
        }

        // 5. save the order
        Order order = new Order(orderRegistry.nextOrderNumber(), cart.getCustomer(), governorate,
                lines, subtotal, vat, shipping, total);
        orderRegistry.save(order);

        // 6. notify through every sender
        String message = "Order " + order.orderNumber() + " confirmed. Total "
                + Money.format(total) + " EGP";
        for (NotificationSender sender : notificationSenders) {
            sender.send(order.customer(), message);
        }

        // 7. clear the cart
        cart.clear();
        return order;
    }
}
