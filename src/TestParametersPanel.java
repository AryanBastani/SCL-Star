import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

class TestParametersPanel extends JPanel {
    private JSpinner testSpinner;
    private JSpinner componentSpinnerMin;
    private JSpinner componentSpinnerMax;
    private JSpinner stateSpinnerMin;
    private JSpinner stateSpinnerMax;
    private JButton runButton;
    private JPanel panel;

    public TestParametersPanel() {
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel for "Number of Tests"
        testSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        alignSpinnerTextLeft(testSpinner);
        JPanel componentPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); // Increase space between Min and Max
        componentPanel1.add(testSpinner);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        add(new JLabel("Number of Tests for each Component:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        add(componentPanel1, gbc);

        // Panel for "Number of Components"
        componentSpinnerMin = new JSpinner(new SpinnerNumberModel(3, 1, 12, 1));
        componentSpinnerMax = new JSpinner(new SpinnerNumberModel(9, 1, 12, 1));
        alignSpinnerTextLeft(componentSpinnerMin);
        alignSpinnerTextLeft(componentSpinnerMax);
        JPanel componentPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); // Increase space between Min and Max
        componentPanel2.add(new JLabel("Min:"));
        componentPanel2.add(componentSpinnerMin);
        componentPanel2.add(Box.createHorizontalStrut(20)); // Add space between Min and Max
        componentPanel2.add(new JLabel("Max:"));
        componentPanel2.add(componentSpinnerMax);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Number of Components (3 to 9 recommended):"), gbc);

        gbc.gridx = 1;
        add(componentPanel2, gbc);

        // Panel for "Number of States"
        stateSpinnerMin = new JSpinner(new SpinnerNumberModel(100, 0, 30000, 100));
        stateSpinnerMax = new JSpinner(new SpinnerNumberModel(30000, 1, 30000, 100));
        alignSpinnerTextLeft(stateSpinnerMin);
        alignSpinnerTextLeft(stateSpinnerMax);
        JPanel statePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); // Increase space between Min and Max
        statePanel.add(new JLabel("Min:"));
        statePanel.add(stateSpinnerMin);
        statePanel.add(Box.createHorizontalStrut(20)); // Add space between Min and Max
        statePanel.add(new JLabel("Max:"));
        statePanel.add(stateSpinnerMax);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Number of States (100 to 30000 recommended):"), gbc);

        gbc.gridx = 1;
        add(statePanel, gbc);

        // "Run" button
        runButton = new JButton("Run");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weighty = 0.5;  // Pushes the button toward the bottom
        add(runButton, gbc);

        // Action Listener for button
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberOfTests = (int) testSpinner.getValue();
                int componentMin = (int) componentSpinnerMin.getValue();
                int componentMax = (int) componentSpinnerMax.getValue();
                int stateMin = (int) stateSpinnerMin.getValue();
                int stateMax = (int) stateSpinnerMax.getValue();

                // Save the configuration and show result
                saveConfigurationToFile(numberOfTests, componentMin, componentMax, stateMin, stateMax);

                // Display result in a new window
                JFrame resultFrame = new JFrame("Result");
                resultFrame.setSize(400, 300);
                resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JLabel resultLabel = new JLabel("<html>Number of Tests: " + numberOfTests +
                        "<br>Components: " + componentMin + " to " + componentMax +
                        "<br>States: " + stateMin + " to " + stateMax + "</html>");
                resultFrame.add(resultLabel);
                resultFrame.setVisible(true);
            }
        });
    }

    private void saveConfigurationToFile(int numberOfTests, int componentMin, int componentMax, int stateMin, int stateMax) {
        try {
            FileWriter writer = new FileWriter("test_parameters.txt", true); // Append mode
            writer.write("Number of Tests: " + numberOfTests + "\n");
            writer.write("Components: " + componentMin + " to " + componentMax + "\n");
            writer.write("States: " + stateMin + " to " + stateMax + "\n");
            writer.write("---------------------------\n");
            writer.close();
            System.out.println("Configuration saved to file.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void alignSpinnerTextLeft(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setHorizontalAlignment(JTextField.LEFT); // Align text to the left
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}