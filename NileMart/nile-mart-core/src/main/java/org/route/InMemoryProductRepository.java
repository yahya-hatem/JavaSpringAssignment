package org.route;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> store = new LinkedHashMap<>();

    @PostConstruct
    void loadSeedData() {
        seed("Smartphone X5 128GB", "PH-X5-128", "16000.00", "Electronics", 12);
        seed("Fast Charger 20W", "ACC-CHG-20W", "650.00", "Electronics", 40);
        seed("Wireless Earbuds", "ACC-BUDS-6", "1100.00", "Electronics", 8);
        seed("Cotton Galabeya", "FSH-GAL-001", "900.00", "Fashion", 25);
        seed("Non-stick Frying Pan 28cm", "HOM-PAN-28", "1200.00", "Home & Kitchen", 3);
        seed("Wooden Spice Rack", "HOM-SPC-RCK", "750.00", "Home & Kitchen", 10);
        seed("Sun Cream SPF50", "BTY-SUN-50", "350.00", "Beauty", 0);
        seed("The Cairo Trilogy", "BK-CAI-TRI", "450.00", "Books", 15);
    }

    private void seed(String name, String sku, String price, String category, int stock) {
        save(Product.builder()
                .name(name)
                .sku(sku)
                .price(new BigDecimal(price))
                .category(category)
                .isActive(true)
                .stockQuantity(stock)
                .build());
    }

    @PreDestroy
    void close() {
        System.out.println("Catalog closed");
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(store.get(sku));
    }

    @Override
    public Product save(Product product) {
        store.put(product.getSku(), product);
        return product;
    }
}
