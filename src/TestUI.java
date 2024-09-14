import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TestUI extends JPanel {
    private JSpinner testSpinner;
    private JSpinner componentSpinnerMin;
    private JSpinner componentSpinnerMax;
    private JSpinner stateSpinnerMin;
    private JSpinner stateSpinnerMax;
    private JButton runButton;
    private JPanel panel;

    public TestUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.BOTH; // Fill both horizontally and vertically

        // Set up panel properties
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Font settings
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font componentFont = new Font("Arial", Font.PLAIN, 12);
        Font buttonFont = new Font("Arial", Font.BOLD, 14); // Adjusted for better fitting

        // Number of Tests Spinner
        testSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        testSpinner.setFont(componentFont);

        // Align number in the spinner
        JComponent testEditor = testSpinner.getEditor();
        JFormattedTextField testField = ((JSpinner.DefaultEditor) testEditor).getTextField();
        testField.setHorizontalAlignment(JTextField.CENTER); // Center number
        testField.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        JLabel testLabel = new JLabel("Number of Tests for each Component:");
        testLabel.setFont(labelFont);
        add(testLabel, gbc);

        gbc.gridx = 1;
        add(testSpinner, gbc);

        // Component Min/Max Spinners
        componentSpinnerMin = new JSpinner(new SpinnerNumberModel(3, 1, 12, 1));
        componentSpinnerMax = new JSpinner(new SpinnerNumberModel(9, 1, 12, 1));
        componentSpinnerMin.setFont(componentFont);
        componentSpinnerMax.setFont(componentFont);

        JPanel componentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        componentPanel.add(new JLabel("Min:"));
        componentPanel.add(componentSpinnerMin);
        componentPanel.add(Box.createHorizontalStrut(20));
        componentPanel.add(new JLabel("Max:"));
        componentPanel.add(componentSpinnerMax);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel componentLabel = new JLabel("Number of Components (3 to 9 recommended):");
        componentLabel.setFont(labelFont);
        add(componentLabel, gbc);

        gbc.gridx = 1;
        add(componentPanel, gbc);

        // State Min/Max Spinners
        stateSpinnerMin = new JSpinner(new SpinnerNumberModel(100, 0, 30000, 100));
        stateSpinnerMax = new JSpinner(new SpinnerNumberModel(30000, 1, 30000, 100));
        stateSpinnerMin.setFont(componentFont);
        stateSpinnerMax.setFont(componentFont);

        JPanel statePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        statePanel.add(new JLabel("Min:"));
        statePanel.add(stateSpinnerMin);
        statePanel.add(Box.createHorizontalStrut(20));
        statePanel.add(new JLabel("Max:"));
        statePanel.add(stateSpinnerMax);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel stateLabel = new JLabel("Number of States (100 to 30000 recommended):");
        stateLabel.setFont(labelFont);
        add(stateLabel, gbc);

        gbc.gridx = 1;
        add(statePanel, gbc);

        // Run Button
        runButton = new JButton("Run");
        runButton.setFont(new Font("Arial", Font.BOLD, 50));
        runButton.setBackground(new Color(0x007BFF)); // Bootstrap blue
        runButton.setForeground(Color.WHITE);
        runButton.setFocusPainted(false);
        runButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // No border
        runButton.setOpaque(true);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0.01;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 250, 10, 250); // Horizontal padding for centering
        add(runButton, gbc);

        // Action listener for the "Run" button
        runButton.addActionListener(e -> {
            int numberOfTests = (int) testSpinner.getValue();
            int componentMin = (int) componentSpinnerMin.getValue();
            int componentMax = (int) componentSpinnerMax.getValue();
            int stateMin = (int) stateSpinnerMin.getValue();
            int stateMax = (int) stateSpinnerMax.getValue();
            saveTestConfiguration(numberOfTests, componentMin, componentMax, stateMin, stateMax);
        });
    }

    public JPanel getPanel() {
        return this;
    }

    // Method to save test configuration to a file
    private void saveTestConfiguration(int numTests, int componentMin, int componentMax, int stateMin, int stateMax) {
        try {
            FileWriter writer = new FileWriter("test_results.txt", true);
            writer.write("Number of Tests: " + numTests + "\n");
            writer.write("Components: " + componentMin + " - " + componentMax + "\n");
            writer.write("States: " + stateMin + " - " + stateMax + "\n");
            writer.write("---------------------------\n");
            writer.close();
            System.out.println("Test configuration saved to file.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public JButton getRunButton(){
        return runButton;
    }

    public ArrayList<String> getInfo(){
        ArrayList<String> info = new ArrayList<>();
        info.add(String.valueOf(testSpinner.getValue()));
        info.add(String.valueOf(componentSpinnerMin.getValue()));
        info.add(String.valueOf(componentSpinnerMax.getValue()));
        info.add(String.valueOf(stateSpinnerMin.getValue()));
        info.add(String.valueOf(stateSpinnerMax.getValue()));

        return info;
    }
}
