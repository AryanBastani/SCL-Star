import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class TestConfigurationUI extends JFrame {
    // Components Declaration
    private JComboBox<String> equivalenceQueryCombo;
    private JCheckBox finalCheckModeCheckBox;
    private JSpinner repetitionSpinner;
    private JRadioButton realTestsRadio;
    private JRadioButton generatedTestsRadio;
    private JButton nextButton;
    private ButtonGroup testsTypeGroup;

    public TestConfigurationUI() {
        // Frame Configuration
        setTitle("Test Configuration");
        setSize(600, 400); // Initial window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout Configuration
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Adjusted margins for component spacing
        gbc.fill = GridBagConstraints.BOTH; // Allow components to grow both horizontally and vertically
        gbc.anchor = GridBagConstraints.WEST;

        // Equivalence Query Section
        String[] equivalenceOptions = {"wp", "w", "wrnd", "rndWords", "rndWordsBig", "rndWalk"};
        equivalenceQueryCombo = new JComboBox<>(equivalenceOptions);
        equivalenceQueryCombo.setSelectedItem("rndWords");

        // Set a smaller preferred size, but allow it to grow responsively
        equivalenceQueryCombo.setPreferredSize(new Dimension(120, equivalenceQueryCombo.getPreferredSize().height)); // Decrease initial width

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5; // Space for the label
        gbc.weighty = 0.1; // Vertical growth weight
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Equivalence Query (recommended: rndWords):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.2;  // Allow growth, but limit how much the JComboBox grows
        gbc.weighty = 0.1;  // Ensure it grows vertically too
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow it to stretch within this limit
        add(equivalenceQueryCombo, gbc);

        // Final Check Mode Section
        finalCheckModeCheckBox = new JCheckBox("Enable Final Check Mode");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.1;  // Ensure vertical growth
        add(new JLabel("Final Check Mode (Disabled is recommended):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;  // Ensure vertical growth
        add(finalCheckModeCheckBox, gbc);

        // Repetition Section
        repetitionSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));

        // Align the text inside the spinner's editor to the left
        JComponent editor = repetitionSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setHorizontalAlignment(JTextField.LEFT); // Align text to the left
        }

        // Wrap the spinner in a JPanel to control its size
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        repetitionSpinner.setPreferredSize(new Dimension(65, 25)); // Set a small width (50px)
        spinnerPanel.add(repetitionSpinner);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        gbc.weighty = 0.1;  // Ensure vertical growth
        add(new JLabel("Number of Repetition for Each Test (recommended: 3):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;  // Ensure vertical growth
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(spinnerPanel, gbc);

        // Tests Type Section
        // Tests Type Section
        realTestsRadio = new JRadioButton("Real Tests");
        generatedTestsRadio = new JRadioButton("Generated Tests");
        testsTypeGroup = new ButtonGroup();
        testsTypeGroup.add(realTestsRadio);
        testsTypeGroup.add(generatedTestsRadio);

// Select "Real Tests" by default
        realTestsRadio.setSelected(true); // This line sets "Real Tests" as the default selection

        JPanel testsTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        testsTypePanel.add(realTestsRadio);
        testsTypePanel.add(generatedTestsRadio);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.weighty = 0.1;  // Ensure vertical growth
        add(new JLabel("Tests Type:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;  // Ensure vertical growth
        add(testsTypePanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;  // Ensure vertical growth
        add(testsTypePanel, gbc);

        // Next Button
        // Next Button
        nextButton = new JButton("Next"); // Initialize the nextButton

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected equivalence query
                String selectedEquivalenceQuery = (String) equivalenceQueryCombo.getSelectedItem();

                // Get final check mode status
                boolean isFinalCheckModeEnabled = finalCheckModeCheckBox.isSelected();

                // Get number of repetitions
                int repetitionCount = (Integer) repetitionSpinner.getValue();

                // Get selected tests type
                String selectedTestType;
                if (realTestsRadio.isSelected()) {
                    selectedTestType = "Real Tests";
                } else  {
                    selectedTestType = "Generated Tests";
                }

                // Now, print or save the collected data
                System.out.println("Equivalence Query: " + selectedEquivalenceQuery);
                System.out.println("Final Check Mode: " + (isFinalCheckModeEnabled ? "Enabled" : "Disabled"));
                System.out.println("Repetitions: " + repetitionCount);
                System.out.println("Tests Type: " + selectedTestType);

                // Optionally save to a file or pass data to the next step
                saveConfigurationToFile(selectedEquivalenceQuery, isFinalCheckModeEnabled, repetitionCount, selectedTestType);

                // Show message
                JOptionPane.showMessageDialog(TestConfigurationUI.this, "Proceeding to the next page...");
            }
        });


        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;  // Give the button more vertical growth space
        gbc.anchor = GridBagConstraints.CENTER;
        add(nextButton, gbc);
    }

    // Method to save configuration data to a file
    private void saveConfigurationToFile(String query, boolean finalCheck, int repetitions, String testType) {
        try {
            FileWriter writer = new FileWriter("test_configuration.txt", true); // Append mode
            writer.write("Equivalence Query: " + query + "\n");
            writer.write("Final Check Mode: " + (finalCheck ? "Enabled" : "Disabled") + "\n");
            writer.write("Repetitions: " + repetitions + "\n");
            writer.write("Tests Type: " + testType + "\n");
            writer.write("---------------------------\n");
            writer.close();
            System.out.println("Configuration saved to file.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TestConfigurationUI ui = new TestConfigurationUI();
            ui.setVisible(true);
        });
    }
}
