import javax.swing.*;
import java.awt.*;

public class LawnMowerGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lawn Mower Emulator");

            int rows = 10; // Define the number of rows
            int cols = 10; // Define the number of columns

            LawnGrid lawnGrid = new LawnGrid(rows, cols); // Pass rows and cols to LawnGrid
            LawnMower mower = new LawnMower(rows, cols);  // Pass rows and cols to LawnMower
            MowerMediator mediator = new MowerMediator(lawnGrid, mower);

            JPanel controlPanel = new JPanel();
            JButton startButton = new JButton("Start");
            JButton stopButton = new JButton("Stop");

            startButton.addActionListener(e -> mediator.startMowing());
            stopButton.addActionListener(e -> mediator.stopMowing());

            controlPanel.add(startButton);
            controlPanel.add(stopButton);

            frame.setLayout(new BorderLayout());
            frame.add(lawnGrid, BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.SOUTH);

            frame.setSize(600, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
