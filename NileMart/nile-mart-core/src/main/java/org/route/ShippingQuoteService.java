package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingQuoteService {

    private final ShippingCalculator zoneCalculator;
    private final ShippingCalculator freeCalculator;
    private final BigDecimal freeShippingThreshold;

    public ShippingQuoteService(ShippingCalculator zoneCalculator,
                                @Qualifier("freeShipping") ShippingCalculator freeCalculator,
                                @Value("${store.free-shipping-threshold}") BigDecimal freeShippingThreshold) {
        this.zoneCalculator = zoneCalculator;
        this.freeCalculator = freeCalculator;
        this.freeShippingThreshold = freeShippingThreshold;
    }

    /** SHIP-02: free when subtotal >= threshold and the zone is eligible. */
    public BigDecimal quote(Governorate g, BigDecimal subtotal) {
        boolean free = subtotal.compareTo(freeShippingThreshold) >= 0
                && g.getZone().isFreeShippingEligible();
        return free ? freeCalculator.calculate(g) : zoneCalculator.calculate(g);
    }
}
