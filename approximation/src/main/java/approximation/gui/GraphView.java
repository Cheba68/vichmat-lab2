package approximation.gui;

import java.util.List;

import approximation.approximation.ApproximationFunction;
import approximation.model.DataPoint;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class GraphView {

    public static LineChart<Number, Number> createGraph(
            List<DataPoint> points,
            List<ApproximationFunction> functions
    ) {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("x");
        yAxis.setLabel("y");

        LineChart<Number, Number> chart =
                new LineChart<>(xAxis, yAxis);

        chart.setTitle("Аппроксимация методом наименьших квадратов");

        double minX = points.get(0).getX();
        double maxX = points.get(0).getX();

        for (DataPoint point : points) {
            minX = Math.min(minX, point.getX());
            maxX = Math.max(maxX, point.getX());
        }

        double margin = (maxX - minX) * 0.1;

        if (margin == 0) {
            margin = 1;
        }

        double startX = minX - margin;
        double endX = maxX + margin;

        int numberOfPoints = 200;

        double step =
                (endX - startX)
                        / (numberOfPoints - 1);

        for (ApproximationFunction function : functions) {

            XYChart.Series<Number, Number> series =
                    new XYChart.Series<>();

            series.setName(function.getName());

            for (int i = 0; i < numberOfPoints; i++) {

                double x = startX + i * step;
                double y = function.evaluate(x);

                if (Double.isFinite(y)) {

                    series.getData().add(
                            new XYChart.Data<>(x, y)
                    );
                }
            }

            chart.getData().add(series);
        }

        XYChart.Series<Number, Number> pointsSeries =
                new XYChart.Series<>();

        pointsSeries.setName("Исходные точки");

        for (DataPoint point : points) {

            pointsSeries.getData().add(
                    new XYChart.Data<>(
                            point.getX(),
                            point.getY()
                    )
            );
        }

        chart.getData().add(pointsSeries);

        chart.setPrefHeight(500);

        return chart;
    }
}