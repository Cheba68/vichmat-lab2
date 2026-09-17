package approximation.gui;

import approximation.math.NumberFormatter;
import approximation.model.ApproximationResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultRow {

    private final StringProperty name;
    private final StringProperty coefficients;
    private final StringProperty sumSquaredErrors;
    private final StringProperty standardDeviation;
    private final StringProperty rSquared;

    public ResultRow(ApproximationResult result) {

        name = new SimpleStringProperty(
                result.getName()
        );

        coefficients = new SimpleStringProperty(
                formatCoefficients(
                        result.getCoefficients()
                )
        );

        sumSquaredErrors = new SimpleStringProperty(
                NumberFormatter.format(
                        result.getSumSquaredErrors()
                )
        );

        standardDeviation = new SimpleStringProperty(
                NumberFormatter.format(
                        result.getStandardDeviation()
                )
        );

        rSquared = new SimpleStringProperty(
                NumberFormatter.format(
                        result.getRSquared()
                )
        );
    }

    private String formatCoefficients(
            double[] coefficients
    ) {

        StringBuilder result =
                new StringBuilder();

        char coefficientName = 'a';

        for (double coefficient : coefficients) {

            if (result.length() > 0) {
                result.append(", ");
            }

            result.append(coefficientName)
                    .append("=")
                    .append(
                            NumberFormatter.format(
                                    coefficient
                            )
                    );

            coefficientName++;
        }

        return result.toString();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty coefficientsProperty() {
        return coefficients;
    }

    public StringProperty sumSquaredErrorsProperty() {
        return sumSquaredErrors;
    }

    public StringProperty standardDeviationProperty() {
        return standardDeviation;
    }

    public StringProperty rSquaredProperty() {
        return rSquared;
    }
}