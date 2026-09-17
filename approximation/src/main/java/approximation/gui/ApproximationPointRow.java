package approximation.gui;

import approximation.model.DataPoint;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ApproximationPointRow {

    private final DoubleProperty x;
    private final DoubleProperty y;
    private final DoubleProperty approximatedY;
    private final DoubleProperty error;

    public ApproximationPointRow(
            DataPoint point,
            double approximatedY,
            double error
    ) {

        this.x = new SimpleDoubleProperty(
                point.getX()
        );

        this.y = new SimpleDoubleProperty(
                point.getY()
        );

        this.approximatedY =
                new SimpleDoubleProperty(
                        approximatedY
                );

        this.error =
                new SimpleDoubleProperty(
                        error
                );
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public DoubleProperty approximatedYProperty() {
        return approximatedY;
    }

    public DoubleProperty errorProperty() {
        return error;
    }
}