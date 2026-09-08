package integration;

import integration.function.MathFunction;
import java.awt.*;
import javax.swing.*;

public class GraphPanel extends JPanel {

    private MathFunction function;

    private double[] discontinuityPoints;

    private double a;
    private double b;

    private double minY;
    private double maxY;

    public GraphPanel() {

        setBackground(Color.WHITE);

        setBorder(
                BorderFactory.createTitledBorder(
                        "График функциии"
                )
        );

        setPreferredSize(
                new Dimension(800, 400)
        );
    }

public void setFunction(
        MathFunction function,
        double a,
        double b
) {

    this.function = function;
    this.a = a;
    this.b = b;

    if (function != null) {

        this.discontinuityPoints =
                function.getDiscontinuityPoints();

    } else {

        this.discontinuityPoints = null;
    }

    repaint();
}

    @Override
    protected void paintComponent(
            Graphics graphics
    ) {

        super.paintComponent(graphics);

        if (function == null) {
            return;
        }

        Graphics2D g =
                (Graphics2D) graphics.create();

        try {

            drawGraph(g);

        } finally {

            g.dispose();
        }
    }

    private void drawGraph(
            Graphics2D g
    ) {

        int width = getWidth();
        int height = getHeight();

        int leftMargin = 60;
        int rightMargin = 20;
        int topMargin = 20;
        int bottomMargin = 40;

        int graphWidth =
                width - leftMargin - rightMargin;

        int graphHeight =
                height - topMargin - bottomMargin;

        /*
         * Определяем диапазон Y.
         */
        double minY =
                Double.POSITIVE_INFINITY;

        double maxY =
                Double.NEGATIVE_INFINITY;

        int samples = 1000;

        for (int i = 0; i <= samples; i++) {

            double x =
                    a + (b - a) * i / samples;

            double y =
        function.evaluate(x);

if (!Double.isFinite(y)
        || Math.abs(y) > 1e6) {
    continue;
}

minY =
        Math.min(minY, y);

maxY =
        Math.max(maxY, y);
        }

        if (!Double.isFinite(minY)
                || !Double.isFinite(maxY)) {

            g.drawString(
                    "Невозможно построить график.",
                    leftMargin,
                    topMargin + 20
            );

            return;
        }

        /*
         * Если функция почти постоянная,
         * немного расширяем диапазон.
         */
        if (Math.abs(maxY - minY) < 1e-12) {

            minY -= 1.0;
            maxY += 1.0;
        }

        /*
         * Добавляем небольшой запас сверху
         * и снизу.
         */
        double padding =
                0.05 * (maxY - minY);

        minY -= padding;
        maxY += padding;

        this.minY = minY;
        this.maxY = maxY;

        /*
         * Оси.
         */
        drawAxes(
                g,
                leftMargin,
                topMargin,
                graphWidth,
                graphHeight,
                minY,
                maxY
        );

        drawDiscontinuities(
        g,
        leftMargin,
        topMargin,
        graphWidth,
        graphHeight
);

        /*
         * График.
         */
        drawFunction(
                g,
                leftMargin,
                topMargin,
                graphWidth,
                graphHeight,
                minY,
                maxY,
                samples
        );
    }

    private void drawAxes(
        Graphics2D g,
        int left,
        int top,
        int width,
        int height,
        double minY,
        double maxY
) {

    int xAxisY =
            coordinateY(
                    0.0,
                    top,
                    height,
                    minY,
                    maxY
            );

    int yAxisX =
            coordinateX(
                    0.0,
                    left,
                    width
            );

    /*
     * Ось X.
     */
    if (xAxisY >= top
            && xAxisY <= top + height) {

        g.drawLine(
                left,
                xAxisY,
                left + width,
                xAxisY
        );

        g.drawString(
                "X",
                left + width - 15,
                xAxisY - 5
        );
    }

    /*
     * Ось Y.
     */
    if (yAxisX >= left
            && yAxisX <= left + width) {

        g.drawLine(
                yAxisX,
                top,
                yAxisX,
                top + height
        );

        g.drawString(
                "Y",
                yAxisX + 5,
                top + 15
        );
    }

    

    /*
     * Подписи по X.
     */
    drawXTicks(
            g,
            left,
            top,
            width,
            height
    );

    /*
     * Подписи по Y.
     */
    drawYTicks(
            g,
            left,
            top,
            width,
            height,
            minY,
            maxY
    );
}

private void drawXTicks(
        Graphics2D g,
        int left,
        int top,
        int width,
        int height
) {

    int axisY =
            coordinateY(
                    0.0,
                    top,
                    height,
                    minY,
                    maxY
            );

    if (axisY < top
            || axisY > top + height) {

        axisY = top + height;
    }

    int count = 10;

    for (int i = 0; i <= count; i++) {

        double x =
                a + (b - a) * i / count;

        int screenX =
                coordinateX(
                        x,
                        left,
                        width
                );

        g.drawLine(
                screenX,
                axisY - 3,
                screenX,
                axisY + 3
        );

        String text =
                String.format(
                        "%.3f",
                        x
                );

        g.drawString(
                text,
                screenX - 15,
                axisY + 18
        );
    }
}

private void drawYTicks(
        Graphics2D g,
        int left,
        int top,
        int width,
        int height,
        double minY,
        double maxY
) {

    int axisX =
            coordinateX(
                    0.0,
                    left,
                    width
            );

    if (axisX < left
            || axisX > left + width) {

        axisX = left;
    }

    int count = 8;

    for (int i = 0; i <= count; i++) {

        double y =
                minY
                        + (maxY - minY)
                        * i / count;

        int screenY =
                coordinateY(
                        y,
                        top,
                        height,
                        minY,
                        maxY
                );

        g.drawLine(
                axisX - 3,
                screenY,
                axisX + 3,
                screenY
        );

        String text =
                String.format(
                        "%.3f",
                        y
                );

        g.drawString(
                text,
                axisX + 5,
                screenY + 5
        );
    }
}

private void drawDiscontinuities(
        Graphics2D g,
        int left,
        int top,
        int width,
        int height
) {

    if (discontinuityPoints == null) {
        return;
    }

    for (double point : discontinuityPoints) {

        if (point < a || point > b) {
            continue;
        }

        int screenX =
                coordinateX(
                        point,
                        left,
                        width
                );

        /*
         * Отмечаем точку разрыва
         * вертикальной линией.
         */
        g.drawLine(
                screenX,
                top,
                screenX,
                top + height
        );

        String text =
        "разрыв x = "
                + String.format(
                        "%.4f",
                        point
                );

int textWidth =
        g.getFontMetrics().stringWidth(text);

int textX =
        screenX + 5;

if (textX + textWidth > left + width) {
    textX =
            screenX - textWidth - 5;
}

g.drawString(
        text,
        textX,
        top + 20
);
    }
}

    private void drawFunction(
            Graphics2D g,
            int left,
            int top,
            int width,
            int height,
            double minY,
            double maxY,
            int samples
    ) {

        int previousX = 0;
        int previousY = 0;

        boolean hasPrevious = false;

        for (int i = 0; i <= samples; i++) {

            double x =
                    a + (b - a) * i / samples;

            double y =
                    function.evaluate(x);

            /*
             * Разрыв или Infinity.
             */
            if (!Double.isFinite(y)) {

                hasPrevious = false;
                continue;
            }

            /*
             * Не позволяем огромным значениям
             * разрушить масштаб графика.
             */
            if (Math.abs(y) > 1e100) {

                hasPrevious = false;
                continue;
            }

            int screenX =
                    coordinateX(
                            x,
                            left,
                            width
                    );

            int screenY =
                    coordinateY(
                            y,
                            top,
                            height,
                            minY,
                            maxY
                    );

            if (hasPrevious) {

                g.drawLine(
                        previousX,
                        previousY,
                        screenX,
                        screenY
                );
            }

            previousX = screenX;
            previousY = screenY;

            hasPrevious = true;
        }
    }

    private int coordinateX(
            double x,
            int left,
            int width
    ) {

        if (b == a) {
            return left;
        }

        return left +
                (int) (
                        (x - a)
                                / (b - a)
                                * width
                );
    }

    private int coordinateY(
            double y,
            int top,
            int height,
            double minY,
            double maxY
    ) {

        return top + height -
                (int) (
                        (y - minY)
                                / (maxY - minY)
                                * height
                );
    }

    private double getMinY() {
    return minY;
}

private double getMaxY() {
    return maxY;
}
}