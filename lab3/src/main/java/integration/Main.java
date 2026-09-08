package integration;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            IntegrationFrame frame =
                    new IntegrationFrame();

            frame.setVisible(true);
        });
    }
}