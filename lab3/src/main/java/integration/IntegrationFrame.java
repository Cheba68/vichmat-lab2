package integration;

import integration.function.Functions;
import integration.function.MathFunction;
import integration.improper.ImproperIntegralHandler;
import integration.improper.ImproperIntegralResult;
import integration.method.IntegrationMethod;
import integration.method.RectangleMethod;
import integration.method.SimpsonMethod;
import integration.method.TrapezoidMethod;
import integration.util.InputValidator;
import integration.util.NumberParser;
import integration.util.ResultFormatter;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;

public class IntegrationFrame extends JFrame {

    private final JComboBox<String> functionBox;
    private final JComboBox<String> methodBox;

    private final ImproperIntegralHandler integralHandler;

    private final GraphPanel graphPanel;

    private final JTextArea messageArea;

    private final JTextField aField;
    private final JTextField bField;
    private final JTextField epsilonField;

    private final JButton calculateButton;

    private final JLabel resultLabel;
    private final JLabel errorLabel;
    private final JLabel nLabel;
    private final JLabel statusLabel;

    public IntegrationFrame() {

        setTitle("Численное интегрирование");
        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(900, 650)
        );

        setLocationRelativeTo(null);

        integralHandler =
        new ImproperIntegralHandler();

        graphPanel =
        new GraphPanel();

        /*
         * Основная панель.
         */
        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        /*
         * Панель параметров.
         */
        JPanel parametersPanel =
                new JPanel(
                        new GridBagLayout()
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(5, 5, 5, 5);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        /*
         * Функция.
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
        parametersPanel.add(
                new JLabel("Функция:"),
                gbc
        );

        functionBox =
        new JComboBox<>();

functionBox.addItem(
        "Выберите функцию"
);

for (MathFunction function
        : Functions.getAvailableFunctions()) {

    functionBox.addItem(
            function.getName()
    );
}
        gbc.gridx = 1;
        gbc.weightx = 1.0;

        parametersPanel.add(
                functionBox,
                gbc
        );

        /*
         * Метод.
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;

        parametersPanel.add(
                new JLabel("Метод:"),
                gbc
        );

        methodBox =
                new JComboBox<>();

        methodBox.addItem("Метод левых прямоугольников");
        methodBox.addItem("Метод правых прямоугольников");
        methodBox.addItem("Метод средних прямоугольников");

        methodBox.addItem(
                "Метод трапеций"
        );

        methodBox.addItem(
                "Метод Симпсона"
        );

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        parametersPanel.add(
                methodBox,
                gbc
        );

        /*
         * Левая граница.
         */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;

        parametersPanel.add(
                new JLabel("Левая граница a:"),
                gbc
        );

        aField =
                new JTextField("0");

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        parametersPanel.add(
                aField,
                gbc
        );

        /*
         * Правая граница.
         */
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;

        parametersPanel.add(
                new JLabel("Правая граница b:"),
                gbc
        );

        bField =
                new JTextField("1");

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        parametersPanel.add(
                bField,
                gbc
        );

        /*
         * Точность.
         */
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;

        parametersPanel.add(
                new JLabel("Точность ε:"),
                gbc
        );

        epsilonField =
                new JTextField("0,0001");

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        parametersPanel.add(
                epsilonField,
                gbc
        );

        /*
         * Кнопка.
         */
        calculateButton =
                new JButton("Вычислить");

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;

        parametersPanel.add(
                calculateButton,
                gbc
        );

        mainPanel.add(
                parametersPanel,
                BorderLayout.NORTH
        );

        /*
         * Результаты.
         */
        JPanel resultPanel =
                new JPanel(
                        new GridLayout(
                                5,
                                1,
                                5,
                                5
                        )
                );

        resultPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Результат"
                )
        );

        resultLabel =
                new JLabel(
                        "Значение: —"
                );

        errorLabel =
                new JLabel(
                        "Погрешность: —"
                );

        nLabel =
                new JLabel(
                        "Число разбиений: —"
                );

        statusLabel =
                new JLabel(
                        "Статус: —"
                );

        messageArea =
        new JTextArea();

messageArea.setEditable(false);
messageArea.setLineWrap(true);
messageArea.setWrapStyleWord(true);

messageArea.setText(
        "Здесь будет отображаться информация "
                + "о выполнении вычисления."
);

        resultPanel.add(resultLabel);
        resultPanel.add(errorLabel);
        resultPanel.add(nLabel);
        resultPanel.add(statusLabel);
        resultPanel.add(
        new JScrollPane(messageArea)
);

        JPanel centerPanel =
        new JPanel(
                new BorderLayout(
                        10,
                        10
                )
        );

centerPanel.add(
        resultPanel,
        BorderLayout.NORTH
);

centerPanel.add(
        graphPanel,
        BorderLayout.CENTER
);

mainPanel.add(
        centerPanel,
        BorderLayout.CENTER
);

        setContentPane(mainPanel);

        pack();

        setLocationRelativeTo(null);

        calculateButton.addActionListener(
        e -> calculate()
);
    }

    private void calculate() {

        clearPreviousResult();

    try {

        int functionIndex =
                functionBox.getSelectedIndex();

        if (functionIndex <= 0) {

            throw new IllegalArgumentException(
                    "Выберите функцию."
            );
        }

        MathFunction function =
                Functions
                        .getAvailableFunctions()
                        .get(functionIndex - 1);

        String aText =
                aField.getText().trim();

        String bText =
                bField.getText().trim();

        String epsilonText =
                epsilonField.getText().trim();

        BigDecimal a =
                NumberParser.parse(aText);
            
        BigDecimal b =
                NumberParser.parse(bText);
            
        BigDecimal epsilon =
                NumberParser.parse(epsilonText);
            
        IntegrationMethod method =
                createMethod(
                        methodBox.getSelectedIndex()
                );

        InputValidator.validateAll(
                function,
                method,
                a,
                b,
                epsilon
        );

        ImproperIntegralResult result =
                integralHandler.integrate(
                        function,
                        method,
                        a.doubleValue(),
                        b.doubleValue(),
                        epsilon.doubleValue()
                );
        ImproperIntegralResult result0 = integralHandler.integrate(function, createMethod(0), a.doubleValue(), b.doubleValue(), epsilon.doubleValue());
        ImproperIntegralResult result1 = integralHandler.integrate(function, createMethod(1), a.doubleValue(), b.doubleValue(), epsilon.doubleValue());
        ImproperIntegralResult result2 = integralHandler.integrate(function, createMethod(2), a.doubleValue(), b.doubleValue(), epsilon.doubleValue());
        ImproperIntegralResult result3 = integralHandler.integrate(function, createMethod(3), a.doubleValue(), b.doubleValue(), epsilon.doubleValue());
        ImproperIntegralResult result4 = integralHandler.integrate(function, createMethod(4), a.doubleValue(), b.doubleValue(), epsilon.doubleValue());
        
        System.out.println(result0);
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println(result4);
        

        messageArea.setText(
                result.getMessage()
        );

        if (result.getStatus()
        == ImproperIntegralResult.Status.DIVERGENT) {

    resultLabel.setText(
            "Значение: —"
    );

    errorLabel.setText(
            "Погрешность: —"
    );

    nLabel.setText(
            "Число разбиений: —"
    );

    statusLabel.setText(
            "Статус: Интеграл не существует."
    );

    graphPanel.setFunction(
        null,
        0.0,
        1.0
);

} else {

    resultLabel.setText(
            "Значение: "
                    + ResultFormatter.format(
                            result.getValue()
                    )
    );

    errorLabel.setText(
            "Погрешность: "
                    + ResultFormatter.format(
                            result.getError()
                    )
    );

    nLabel.setText(
            "Число разбиений: "
                    + result.getN()
    );

    if (result.getStatus()
        == ImproperIntegralResult.Status.SYMMETRIC_CANCELLATION) {

    statusLabel.setText(
            "Статус: Обнаружен разрыв. "
                    + "Симметричный участок сокращён."
    );

} else {

    statusLabel.setText(
            "Статус: "
                    + result.getStatus()
    );
}
}

        graphPanel.setFunction(
        function,
        a.doubleValue(),
        b.doubleValue()
);

    } catch (IllegalArgumentException e) {

        showError(
                e.getMessage()
        );

    } catch (Exception e) {

        showError(
                "Не удалось выполнить вычисление: "
                        + e.getMessage()
        );
    }
}

private IntegrationMethod createMethod(int index) {

    switch (index) {

        case 0:
            return new RectangleMethod(
                    RectangleMethod.Type.LEFT
            );

        case 1:
            return new RectangleMethod(
                    RectangleMethod.Type.RIGHT
            );

        case 2:
            return new RectangleMethod(
                    RectangleMethod.Type.MIDDLE
            );

        case 3:
            return new TrapezoidMethod();

        case 4:
            return new SimpsonMethod();

        default:
            throw new IllegalArgumentException(
                    "Неизвестный метод интегрирования."
            );
    }
}

private void showError(
        String message
) {

    messageArea.setText(
            message
    );

    JOptionPane.showMessageDialog(
            this,
            message,
            "Ошибка ввода",
            JOptionPane.ERROR_MESSAGE
    );
}

    public JComboBox<String> getFunctionBox() {
        return functionBox;
    }

    public JComboBox<String> getMethodBox() {
        return methodBox;
    }

    public JTextField getAField() {
        return aField;
    }

    public JTextField getBField() {
        return bField;
    }

    public JTextField getEpsilonField() {
        return epsilonField;
    }

    public JButton getCalculateButton() {
        return calculateButton;
    }

    public JLabel getResultLabel() {
        return resultLabel;
    }

    public JLabel getErrorLabel() {
        return errorLabel;
    }

    public JLabel getNLabel() {
        return nLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    private void clearPreviousResult() {

    resultLabel.setText(
            "Значение: —"
    );

    errorLabel.setText(
            "Погрешность: —"
    );

    nLabel.setText(
            "Число разбиений: —"
    );

    statusLabel.setText(
            "Статус: Выполняется..."
    );

    messageArea.setText(
            "Выполняется вычисление..."
    );

    graphPanel.setFunction(
            null,
            0.0,
            1.0
    );
}
}