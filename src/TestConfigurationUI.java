import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class TestConfigurationUI extends JPanel {
    private JComboBox<String> equivalenceQueryCombo;
    private JCheckBox finalCheckModeCheckBox;
    private JSpinner repetitionSpinner;
    private JRadioButton realTestsRadio;
    private JRadioButton generatedTestsRadio;
    private JButton nextButton;
    private ButtonGroup testsTypeGroup;
    private JPanel panel;

    public TestConfigurationUI() {
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

        // Equivalence Query ComboBox
        String[] equivalenceOptions = {"wp", "w", "wrnd", "rndWords", "rndWordsBig", "rndWalk"};
        equivalenceQueryCombo = new JComboBox<>(equivalenceOptions);
        equivalenceQueryCombo.setSelectedItem("rndWords");
        equivalenceQueryCombo.setFont(componentFont);
        ((JLabel) equivalenceQueryCombo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER); // Center text

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.1; // Minimal vertical space
        JLabel equivalenceLabel = new JLabel("Equivalence Query (recommended: rndWords):");
        equivalenceLabel.setFont(labelFont);
        add(equivalenceLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.1; // Minimal vertical space
        add(equivalenceQueryCombo, gbc);

        // Final Check Mode CheckBox
        finalCheckModeCheckBox = new JCheckBox("Enable Final Check Mode");
        finalCheckModeCheckBox.setFont(componentFont);
        finalCheckModeCheckBox.setHorizontalAlignment(SwingConstants.CENTER); // Center the checkbox text

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.1; // Minimal vertical space
        JLabel finalCheckLabel = new JLabel("Final Check Mode (Disabled is recommended):");
        finalCheckLabel.setFont(labelFont);
        add(finalCheckLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.1; // Minimal vertical space
        add(finalCheckModeCheckBox, gbc);

        // Repetition Spinner
        repetitionSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
        repetitionSpinner.setFont(componentFont);

        // Center the number in the spinner
        JComponent editor = repetitionSpinner.getEditor();
        JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
        tf.setHorizontalAlignment(JTextField.CENTER); // Align number to center
        tf.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size of number

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.1; // Minimal vertical space
        JLabel repetitionLabel = new JLabel("Number of Repetition for Each Test (recommended: 3):");
        repetitionLabel.setFont(labelFont);
        add(repetitionLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.1; // Minimal vertical space
        add(repetitionSpinner, gbc);

        // Tests Type Radio Buttons
        realTestsRadio = new JRadioButton("Real Tests");
        generatedTestsRadio = new JRadioButton("Generated Tests");
        testsTypeGroup = new ButtonGroup();
        testsTypeGroup.add(realTestsRadio);
        testsTypeGroup.add(generatedTestsRadio);
        realTestsRadio.setSelected(true);  // Default
        realTestsRadio.setFont(componentFont);
        generatedTestsRadio.setFont(componentFont);

        // Center the radio buttons vertically in the panel
        JPanel testsTypePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout to center vertically
        GridBagConstraints testsTypeGBC = new GridBagConstraints();
        testsTypeGBC.gridx = 0;
        testsTypeGBC.gridy = 0;
        testsTypeGBC.anchor = GridBagConstraints.CENTER; // Center the content
        testsTypePanel.add(realTestsRadio, testsTypeGBC);

        testsTypeGBC.gridx = 1;
        testsTypePanel.add(generatedTestsRadio, testsTypeGBC);

        testsTypePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Optional border for visual separation

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.weighty = 0.1;  // Ensure vertical growth
        add(new JLabel("Tests Type:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;  // Ensure vertical growth
        add(testsTypePanel, gbc);

// Next Button
        nextButton = new JButton("Next");

        nextButton.setFont(new Font("Arial", Font.BOLD, 50)); // Font size
        nextButton.setPreferredSize(new Dimension(20, 10)); // Fixed size for button
        nextButton.setBackground(new Color(0x007BFF)); // Bootstrap blue
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Padding
        nextButton.setOpaque(true);

// Constraints for centering the button horizontally without changing gridwidth
        gbc.gridx = 0; // Keep it in the second column
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 0.01; // Allow horizontal expansion
        gbc.weighty = 0.1; // Minimal vertical space
        gbc.anchor = GridBagConstraints.CENTER; // Center the button within the available space
        gbc.insets = new Insets(50, 250, 10, 250); // Add horizontal padding to center it manually

        add(nextButton, gbc);

    }

    public JButton getNextButton() {
        return nextButton;
    }

    public JPanel getPanel() {
        return this;
    }

    public ArrayList<String> getInfo(){
        ArrayList<String> info = new ArrayList<>();
        info.add((String) equivalenceQueryCombo.getSelectedItem());
        info.add(String.valueOf(finalCheckModeCheckBox.isSelected()));
        info.add(String.valueOf(repetitionSpinner.getValue()));
        if (realTestsRadio.isSelected())
            info.add("Real Tests");
        else
            info.add("Generated Tests");

        return info;
    }

    // Method to save configuration data to a file
    private void saveConfigurationToFile(String query, boolean finalCheck, int repetitions, String testType) {
        try {
            FileWriter writer = new FileWriter("test_configuration.txt", true);
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
}
