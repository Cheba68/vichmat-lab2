package integration.util;

import java.math.BigDecimal;

public final class ResultFormatter {

    private ResultFormatter() {
    }

    public static String format(double value) {

        if (Double.isNaN(value)) {
            return "NaN";
        }

        if (Double.isInfinite(value)) {
            return value > 0
                    ? "+∞"
                    : "-∞";
        }

        return BigDecimal
                .valueOf(value)
                .stripTrailingZeros()
                .toPlainString();
    }
}