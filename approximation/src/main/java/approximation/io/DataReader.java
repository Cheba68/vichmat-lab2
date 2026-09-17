package approximation.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import approximation.model.DataPoint;

public class DataReader {

    public static List<DataPoint> readFromFile(
            Path file
    ) throws IOException {

        List<DataPoint> points = new ArrayList<>();

        List<String> lines = Files.readAllLines(file);

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");

            if (parts.length != 2) {
                throw new IllegalArgumentException(
                        "Ошибка в строке " + (i + 1)
                                + ": необходимо указать X и Y."
                );
            }

            try {

                double x = Double.parseDouble(
                        parts[0].replace(',', '.')
                );

                double y = Double.parseDouble(
                        parts[1].replace(',', '.')
                );

                points.add(
                        new DataPoint(x, y)
                );

            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "Ошибка в строке " + (i + 1)
                                + ": X и Y должны быть числами."
                );
            }
        }

        return points;
    }
}