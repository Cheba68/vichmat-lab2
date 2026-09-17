package approximation.math;

public class NumberFormatter {

    public static String format(double value) {
        if (value == 0.0) {
            return "0";
        }

        if (!Double.isFinite(value)) {
            return Double.toString(value);
        }

        return Double.toString(value);
    }
}