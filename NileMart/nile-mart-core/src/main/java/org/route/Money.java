package org.route;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/** Money helpers: 2 decimals, HALF_UP (GEN-01, GEN-04). */
public final class Money {
    private Money() {
    }

    public static BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public static String format(BigDecimal value) {
        return String.format(Locale.US, "%,.2f", round(value));
    }
}
