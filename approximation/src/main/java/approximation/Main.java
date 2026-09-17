package approximation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import approximation.approximation.ApproximationFunction;
import approximation.approximation.ApproximationManager;
import approximation.gui.ApproximationPointRow;
import approximation.gui.GraphView;
import approximation.gui.ResultRow;
import approximation.io.DataReader;
import approximation.math.FormulaFormatter;
import approximation.math.NumberFormatter;
import approximation.math.Statistics;
import approximation.model.ApproximationError;
import approximation.model.ApproximationResult;
import approximation.model.DataPoint;
import approximation.validation.DataValidator;
import approximation.validation.ValidationException;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

    private final ObservableList<DataPoint> points =
            FXCollections.observableArrayList();

    private final VBox errorsContainer =
        new VBox(5);

    private final TableView<DataPoint> table =
            new TableView<>(points);

    private final ObservableList<ResultRow> resultRows =
        FXCollections.observableArrayList();

    private TableView<ResultRow> resultTable;

    private Label approximationTableLabel;

    private LineChart<Number, Number> graph;

    private final VBox graphContainer =
        new VBox();

    private final ObservableList<ApproximationPointRow> approximationRows =
        FXCollections.observableArrayList();

private TableView<ApproximationPointRow> approximationTable;

    private Label bestResultLabel;
    private Label bestFormulaLabel;
    private Label pearsonLabel;
    private Label rSquaredMessageLabel;

    private final ApproximationManager approximationManager =
        new ApproximationManager();

    private void showInfo(String message) {

    Alert alert = new Alert(
            Alert.AlertType.INFORMATION
    );

    alert.setTitle("Информация");
    alert.setHeaderText(null);
    alert.setContentText(message);

    alert.showAndWait();
}
    
    private void showError(String message) {

    Alert alert = new Alert(
            Alert.AlertType.ERROR
    );

    alert.setTitle("Ошибка");
    alert.setHeaderText(null);
    alert.setContentText(message);

    alert.showAndWait();
}

    @Override
    public void start(Stage stage) {

        Label title = new Label(
                "Аппроксимация методом наименьших квадратов"
        );

        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableColumn<DataPoint, Number> xColumn =
                new TableColumn<>("X");

        xColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getX())
        );

        TableColumn<DataPoint, Number> yColumn =
                new TableColumn<>("Y");

        yColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getY())
        );

        xColumn.setPrefWidth(150);
        yColumn.setPrefWidth(150);

        table.getColumns().addAll(xColumn, yColumn);
        table.setPrefHeight(400);

        resultTable = createResultTable();

        approximationTable = createApproximationTable();

        approximationTableLabel = new Label(
        "Значения лучшей аппроксимации"
);

approximationTableLabel.setStyle(
        "-fx-font-size: 15px; -fx-font-weight: bold;"
);

        bestResultLabel = new Label(
        "Лучшая аппроксимация: —"
);

pearsonLabel = new Label();

rSquaredMessageLabel = new Label();
rSquaredMessageLabel.setStyle(
        "-fx-font-size: 14px;"
);

pearsonLabel.setStyle(
        "-fx-font-size: 14px;"
);

bestResultLabel.setStyle(
        "-fx-font-size: 16px; -fx-font-weight: bold;"
);

bestFormulaLabel = new Label(
        "Формула: —"
    );
    
    bestFormulaLabel.setStyle(
        "-fx-font-size: 15px;"
    );
    
    

        TextField xField = new TextField();
        xField.setPromptText("X");

        TextField yField = new TextField();
        yField.setPromptText("Y");

        Button addButton = new Button("Добавить");

        addButton.setOnAction(event -> {

    if (points.size() >= DataValidator.MAX_POINTS) {
        showError(
                "Нельзя добавить больше 12 точек."
        );
        return;
    }

    try {

        double x = Double.parseDouble(
                xField.getText().trim().replace(',', '.')
        );

        double y = Double.parseDouble(
                yField.getText().trim().replace(',', '.')
        );

        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            showError(
                    "X и Y должны быть конечными числами."
            );
            return;
        }

        for (DataPoint point : points) {

            if (Double.compare(point.getX(), x) == 0) {
                showError(
                        "Точка с X = " + x +
                        " уже существует."
                );
                return;
            }
        }

        points.add(new DataPoint(x, y));

        xField.clear();
        yField.clear();

    } catch (NumberFormatException e) {

        showError(
                "X и Y должны быть числами."
        );
    }
});

Button openFileButton =
        new Button("Открыть файл");

        openFileButton.setOnAction(event -> {

    FileChooser fileChooser =
            new FileChooser();

    fileChooser.setTitle(
            "Выберите файл с исходными точками"
    );

    fileChooser.setInitialDirectory(
        new File(
                "/home/vladimir/vichmat/approximation/src/main/java/approximation/"
        )
);

    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                    "Текстовые файлы",
                    "*.txt"
            )
    );

    File file =
            fileChooser.showOpenDialog(stage);

    if (file == null) {
        return;
    }

    try {

        List<DataPoint> loadedPoints =
        DataReader.readFromFile(
                Path.of(file.getAbsolutePath())
        );

if (loadedPoints.size() > DataValidator.MAX_POINTS) {
    throw new ValidationException(
            "В файле не может быть больше 12 точек."
    );
}

points.clear();
points.addAll(loadedPoints);

        System.out.println(
                "Файл успешно загружен."
        );

        System.out.println(
                "Количество точек: "
                        + points.size()
        );

    } catch (IOException e) {

        showError(
                "Ошибка чтения файла: "
                        + e.getMessage()
        );

    } catch (IllegalArgumentException e) {

        showError(
                e.getMessage()
        );

    } catch (ValidationException e) {

        showError(
                e.getMessage()
        );
    }
});

        Button deleteButton = new Button("Удалить");

        deleteButton.setOnAction(event -> {

            DataPoint selected =
                    table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                points.remove(selected);
            }
        });

        Button clearButton = new Button("Очистить");

        Button calculateButton = new Button("Рассчитать");

        calculateButton.setOnAction(e -> {

    try {

        DataValidator.validateForCalculation(points);

        List<ApproximationResult> results =
                approximationManager.calculateAll(points);

        errorsContainer.getChildren().clear();

if (!approximationManager.getErrors().isEmpty()) {

    Label errorsTitle =
            new Label("Недоступные методы:");

    errorsContainer.getChildren().add(
            errorsTitle
    );

    for (ApproximationError error :
            approximationManager.getErrors()) {

        Label errorLabel =
                new Label(
                        error.getMethodName()
                                + ": "
                                + error.getMessage()
                );

        errorsContainer.getChildren().add(
                errorLabel
        );
    }
}

        errorsContainer.getChildren().clear();

for (ApproximationError error :
        approximationManager.getErrors()) {

    Label errorLabel = new Label(
            error.getMethodName()
                    + ": "
                    + error.getMessage()
    );

    errorsContainer.getChildren().add(
            errorLabel
    );
}

        for (ApproximationError error :
        approximationManager.getErrors()) {

    System.out.println(
            error.getMethodName()
                    + ": "
                    + error.getMessage()
    );
}

        resultRows.clear();

for (ApproximationResult result : results) {
    resultRows.add(
            new ResultRow(result)
    );
}

System.out.println();
System.out.println(" РЕЗУЛЬТАТЫ АППРКСИМАЦИИ ");

for (ApproximationResult result : results) {

    System.out.println();
    System.out.println("Метод: " + result.getName());

    System.out.print("Коэффициенты: ");

    double[] coefficients = result.getCoefficients();

    char coefficientName = 'a';

    for (double coefficient : coefficients) {

        System.out.print(
                coefficientName
                        + "="
                        + NumberFormatter.format(coefficient)
                        + " "
        );

        coefficientName++;
    }

    System.out.println();

    System.out.println(
            "S = "
                    + NumberFormatter.format(
                            result.getSumSquaredErrors()
                    )
    );

    System.out.println(
            "СКО = "
                    + NumberFormatter.format(
                            result.getStandardDeviation()
                    )
    );

    System.out.println(
            "R² = "
                    + NumberFormatter.format(
                            result.getRSquared()
                    )
    );

    double[] approximatedY =
            result.getApproximatedY();

    double[] errors =
            result.getErrors();

    System.out.println(
            "xᵢ        yᵢ        φ(xᵢ)        εᵢ"
    );

    for (int i = 0; i < points.size(); i++) {

        System.out.println(
                NumberFormatter.format(points.get(i).getX())
                        + "    "
                        + NumberFormatter.format(points.get(i).getY())
                        + "    "
                        + NumberFormatter.format(approximatedY[i])
                        + "    "
                        + NumberFormatter.format(errors[i])
        );
    }

    System.out.println("-----------------------------------");
}

        ApproximationResult best =
                approximationManager.findBest(results);

        double pearson =
        Statistics.calculatePearsonCorrelation(points);

pearsonLabel.setText(
        "Коэффициент корреляции Пирсона: "
                + NumberFormatter.format(pearson)
);

        approximationRows.clear();

double[] approximatedY =
        best.getApproximatedY();

double[] errors =
        best.getErrors();

for (int i = 0; i < points.size(); i++) {

    approximationRows.add(
            new ApproximationPointRow(
                    points.get(i),
                    approximatedY[i],
                    errors[i]
            )
    );
}

List<ApproximationFunction> availableFunctions =
        new ArrayList<>();

for (ApproximationResult result : results) {
    availableFunctions.add(
            approximationManager.findFunction(result)
    );
}

graph = GraphView.createGraph(
        points,
        availableFunctions
);

graphContainer.getChildren().clear();
graphContainer.getChildren().add(graph);

        bestResultLabel.setText(
        "Лучшая аппроксимация: "
                + best.getName()
                + "    СКО = "
                + NumberFormatter.format(
                        best.getStandardDeviation()
                )
                + "    R² = "
                + NumberFormatter.format(
                        best.getRSquared()
                )
);

bestFormulaLabel.setText(
        "Формула: "
                + FormulaFormatter.format(best)
);

double rSquared =
        best.getRSquared();

rSquaredMessageLabel.setText(
        Statistics.getRSquaredMessage(rSquared)
);

    } catch (ValidationException ex) {

        showError(ex.getMessage());

    } catch (IllegalArgumentException ex) {

        showError(ex.getMessage());
    }
});

        clearButton.setOnAction(event ->
                points.clear()
        );

        HBox inputPanel = new HBox(
        10,
        xField,
        yField,
        addButton,
        deleteButton,
        clearButton,
        calculateButton,
        openFileButton
);

        VBox topPanel = new VBox(
                15,
                title,
                inputPanel
        );

        topPanel.setPadding(new Insets(15));

        BorderPane root = new BorderPane();

VBox tablesPanel = new VBox(
        10,
        table,
        resultTable,
        errorsContainer,
        bestResultLabel,
        bestFormulaLabel,
        rSquaredMessageLabel,
        pearsonLabel,
        approximationTableLabel,
        approximationTable,
        graphContainer
);

tablesPanel.setPadding(
        new Insets(0, 15, 15, 15)
);

ScrollPane scrollPane = new ScrollPane(tablesPanel);

scrollPane.setFitToWidth(true);
scrollPane.setFitToHeight(false);

scrollPane.setHbarPolicy(
        ScrollPane.ScrollBarPolicy.AS_NEEDED
);

scrollPane.setVbarPolicy(
        ScrollPane.ScrollBarPolicy.AS_NEEDED
);

root.setTop(topPanel);
root.setCenter(scrollPane);

Scene scene = new Scene(root, 1000, 800);

        stage.setTitle(
                "Метод наименьших квадратов"
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private TableView<ResultRow> createResultTable() {

    TableView<ResultRow> table =
            new TableView<>();

    TableColumn<ResultRow, String> nameColumn =
            new TableColumn<>("Метод");

    nameColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue()
                            .nameProperty()
    );

    TableColumn<ResultRow, String> coefficientsColumn =
            new TableColumn<>("Коэффициенты");

    coefficientsColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue()
                            .coefficientsProperty()
    );

    TableColumn<ResultRow, String> sColumn =
            new TableColumn<>("S");

    sColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue()
                            .sumSquaredErrorsProperty()
    );

    TableColumn<ResultRow, String> deviationColumn =
            new TableColumn<>("СКО");

    deviationColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue()
                            .standardDeviationProperty()
    );

    TableColumn<ResultRow, String> rSquaredColumn =
            new TableColumn<>("R²");

    rSquaredColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue()
                            .rSquaredProperty()
    );

    nameColumn.setPrefWidth(150);
coefficientsColumn.setPrefWidth(450);
sColumn.setPrefWidth(220);
deviationColumn.setPrefWidth(220);
rSquaredColumn.setPrefWidth(220);

    table.getColumns().addAll(
            nameColumn,
            coefficientsColumn,
            sColumn,
            deviationColumn,
            rSquaredColumn
    );

    table.setItems(resultRows);

    table.setPrefHeight(350);

    return table;
}

private TableView<ApproximationPointRow> createApproximationTable() {

    TableView<ApproximationPointRow> table =
            new TableView<>();

    TableColumn<ApproximationPointRow, Number> xColumn =
            new TableColumn<>("xᵢ");

    xColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue().xProperty()
    );

    TableColumn<ApproximationPointRow, Number> yColumn =
            new TableColumn<>("yᵢ");

    yColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue().yProperty()
    );

    TableColumn<ApproximationPointRow, Number> approximatedYColumn =
            new TableColumn<>("φ(xᵢ)");

    approximatedYColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue().approximatedYProperty()
    );

    TableColumn<ApproximationPointRow, Number> errorColumn =
            new TableColumn<>("εᵢ");

    errorColumn.setCellValueFactory(
            cellData ->
                    cellData.getValue().errorProperty()
    );

    table.getColumns().addAll(
            xColumn,
            yColumn,
            approximatedYColumn,
            errorColumn
    );

    table.setItems(approximationRows);

    table.setPrefHeight(250);

    return table;
}
}