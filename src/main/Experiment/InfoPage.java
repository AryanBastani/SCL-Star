package main.Experiment;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class InfoPage extends JPanel {
    private JComboBox<String> equivalenceQueryCombo;
    private JCheckBox finalCheckModeCheckBox;
    private JSpinner repetitionSpinner;
    private JRadioButton realTestsRadio;
    private JRadioButton p2pTestsRadio;
    private JRadioButton ringTestsRadio;
    private JRadioButton starTestsRadio;
    private JRadioButton busTestsRadio;
    private JRadioButton bipartiteTestsRadio;
    private JRadioButton meshTestsRadio;
    private JSpinner testSpinner;
    private JSpinner componentSpinnerMin;
    private JSpinner componentSpinnerMax;
    private JSpinner stateSpinnerMin;
    private JSpinner stateSpinnerMax;
    private JButton runButton;
    private ButtonGroup testsTypeGroup;

    public InfoPage() {
        // Set layout with padding between components and stretch settings
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch horizontally

        // General panel and font settings
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Increased fonts for larger window
        Font labelFont = new Font("Arial", Font.BOLD, 24);
        Font componentFont = new Font("Arial", Font.PLAIN, 18);

        // Equivalence Query ComboBox
        String[] equivalenceOptions = {"wp", "w", "wrnd", "rndWords", "rndWordsBig", "rndWalk"};
        equivalenceQueryCombo = new JComboBox<>(equivalenceOptions);
        equivalenceQueryCombo.setSelectedItem("rndWords");
        equivalenceQueryCombo.setFont(componentFont);
        ((JLabel) equivalenceQueryCombo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        JLabel equivalenceLabel = new JLabel("Equivalence Query:");
        equivalenceLabel.setFont(labelFont);
        add(equivalenceLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        add(equivalenceQueryCombo, gbc);

        // Final Check Mode CheckBox
        finalCheckModeCheckBox = new JCheckBox("Enable Final Check Mode");
        finalCheckModeCheckBox.setFont(componentFont);
        finalCheckModeCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel finalCheckModeLabel = new JLabel("Final Check Mode:");
        finalCheckModeLabel.setFont(labelFont);
        add(finalCheckModeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        add(finalCheckModeCheckBox, gbc);

        // Repetition Spinner
        repetitionSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
        configureSpinner(repetitionSpinner, componentFont);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel repeatLabel = new JLabel("Number of Repetitions:");
        repeatLabel.setFont(labelFont);
        add(repeatLabel, gbc);

        JPanel repeatPanel = new JPanel(new FlowLayout());
        repeatPanel.add(repetitionSpinner);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        add(repeatPanel, gbc);

        // Tests Type Radio Buttons
        realTestsRadio = new JRadioButton("Real");
        p2pTestsRadio = new JRadioButton("P2P");
        ringTestsRadio = new JRadioButton("Ring");
        starTestsRadio = new JRadioButton("Star");
        busTestsRadio = new JRadioButton("Bus");
        bipartiteTestsRadio = new JRadioButton("Bipartite");
        meshTestsRadio = new JRadioButton("Mesh");
        testsTypeGroup = new ButtonGroup();
        testsTypeGroup.add(realTestsRadio);
        testsTypeGroup.add(p2pTestsRadio);
        testsTypeGroup.add(ringTestsRadio);
        testsTypeGroup.add(starTestsRadio);
        testsTypeGroup.add(busTestsRadio);
        testsTypeGroup.add(bipartiteTestsRadio);
        testsTypeGroup.add(meshTestsRadio);
        realTestsRadio.setSelected(true);
        realTestsRadio.setFont(componentFont);
        p2pTestsRadio.setFont(componentFont);
        ringTestsRadio.setFont(componentFont);
        starTestsRadio.setFont(componentFont);
        busTestsRadio.setFont(componentFont);
        bipartiteTestsRadio.setFont(componentFont);
        meshTestsRadio.setFont(componentFont);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel testsTypeLabel = new JLabel("Tests Type:");
        testsTypeLabel.setFont(labelFont);
        add(testsTypeLabel, gbc);

        // Arrange radio buttons into 2 rows, and center the panel
        JPanel testsTypePanel = new JPanel(new GridBagLayout());
        GridBagConstraints testsGbc = new GridBagConstraints();
        testsGbc.gridx = 0;
        testsGbc.gridy = 0;
        testsGbc.gridwidth = 4; // Span across the center
        testsGbc.insets = new Insets(10, 10, 10, 10);

        JPanel radioPanel = new JPanel(new GridLayout(2, 4)); // 2 rows, 4 columns
        radioPanel.add(realTestsRadio);
        radioPanel.add(p2pTestsRadio);
        radioPanel.add(ringTestsRadio);
        radioPanel.add(starTestsRadio);
        radioPanel.add(busTestsRadio);
        radioPanel.add(bipartiteTestsRadio);
        radioPanel.add(meshTestsRadio);

        testsTypePanel.add(radioPanel, testsGbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        add(testsTypePanel, gbc);

        // Number of Tests Spinner
        testSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        configureSpinner(testSpinner, componentFont);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel numTestsLabel = new JLabel("Number of Tests:");
        numTestsLabel.setFont(labelFont);
        add(numTestsLabel, gbc);
        JPanel testsPanel = new JPanel(new FlowLayout());
        testsPanel.add(testSpinner);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        add(testsPanel, gbc);

        // Component Min/Max Spinners
        componentSpinnerMin = new JSpinner(new SpinnerNumberModel(3, 1, 12, 1));
        componentSpinnerMax = new JSpinner(new SpinnerNumberModel(9, 1, 12, 1));
        configureSpinner(componentSpinnerMin, componentFont);
        configureSpinner(componentSpinnerMax, componentFont);

        // Component Min/Max Spinners with added space between them
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        JLabel componentsLabel = new JLabel("Number of Components:");
        componentsLabel.setFont(labelFont);
        add(componentsLabel, gbc);

        // Create a panel with extra spacing between Min and Max
        JPanel componentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 30 px horizontal gap
        componentPanel.add(new JLabel("Min:"));
        componentPanel.add(componentSpinnerMin);
        componentPanel.add(Box.createHorizontalStrut(60)); // Add extra spacing between Min and Max
        componentPanel.add(new JLabel("Max:"));
        componentPanel.add(componentSpinnerMax);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        add(componentPanel, gbc);

        // State Min/Max Spinners
        stateSpinnerMin = new JSpinner(new SpinnerNumberModel(100, 0, 30000, 100));
        stateSpinnerMax = new JSpinner(new SpinnerNumberModel(30000, 1, 30000, 100));
        configureSpinner(stateSpinnerMin, componentFont);
        configureSpinner(stateSpinnerMax, componentFont);

        // State Min/Max Spinners with added space between them
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        JLabel statesLabel = new JLabel("Number of States:");
        statesLabel.setFont(labelFont);
        add(statesLabel, gbc);

        // Create a panel with extra spacing between Min and Max
        JPanel statePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 30 px horizontal gap
        statePanel.add(new JLabel("Min:"));
        statePanel.add(stateSpinnerMin);
        statePanel.add(Box.createHorizontalStrut(60)); // Add extra spacing between Min and Max
        statePanel.add(new JLabel("Max:"));
        statePanel.add(stateSpinnerMax);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        add(statePanel, gbc);

        // Add listeners to update spinners based on Test Type
        addTestTypeListeners();

        // Run Button
        runButton = new JButton("Run");
        runButton.setFont(new Font("Arial", Font.BOLD, 36));
        runButton.setBackground(new Color(0x007BFF)); // Bootstrap blue
        runButton.setForeground(Color.WHITE);
        runButton.setFocusPainted(false);
        runButton.setOpaque(true);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(20, 300, 20, 300);
        gbc.anchor = GridBagConstraints.CENTER;
        add(runButton, gbc);

        // Action listener for the "Run" button
        runButton.addActionListener(e -> printConfiguration());
    }

    private void addTestTypeListeners() {
        realTestsRadio.addActionListener(e -> setComponentAndStateValues(3, 9, 100, 30000));
        p2pTestsRadio.addActionListener(e -> setComponentAndStateValues(3, 9, 100, 30000));
        ringTestsRadio.addActionListener(e -> setComponentAndStateValues(3, 9, 100, 30000));
        starTestsRadio.addActionListener(e -> setComponentAndStateValues(3, 9, 100, 30000));
        busTestsRadio.addActionListener(e -> setComponentAndStateValues(3, 9, 100, 30000));
        bipartiteTestsRadio.addActionListener(e -> setComponentAndStateValues(3, 7, 100, 30000));
        meshTestsRadio.addActionListener(e -> setComponentAndStateValues(3, 6, 100, 20000));
    }

    private void setComponentAndStateValues(int minComponents, int maxComponents, int minStates, int maxStates) {
        componentSpinnerMin.setValue(minComponents);
        componentSpinnerMax.setValue(maxComponents);
        stateSpinnerMin.setValue(minStates);
        stateSpinnerMax.setValue(maxStates);
    }

    // Method to configure spinner size and center text
    private void configureSpinner(JSpinner spinner, Font font) {
        JComponent editor = spinner.getEditor();
        JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
        tf.setHorizontalAlignment(JTextField.CENTER);  // Center text in spinner
        tf.setFont(font);  // Set custom font size
    }

    private void printConfiguration() {
        String query = (String) equivalenceQueryCombo.getSelectedItem();
        boolean finalCheckMode = finalCheckModeCheckBox.isSelected();
        int repetitions = (int) repetitionSpinner.getValue();
        String testType = getSelectedTestType();
        int numTests = (int) testSpinner.getValue();
        int minComponents = (int) componentSpinnerMin.getValue();
        int maxComponents = (int) componentSpinnerMax.getValue();
        int minStates = (int) stateSpinnerMin.getValue();
        int maxStates = (int) stateSpinnerMax.getValue();

        System.out.println("Equivalence Query: " + query + "\n");
        System.out.println("Final Check Mode: " + finalCheckMode + "\n");
        System.out.println("Repetitions: " + repetitions + "\n");
        System.out.println("Test Type: " + testType + "\n");
        System.out.println("Number of Tests: " + numTests + "\n");
        System.out.println("Min Components: " + minComponents + "\n");
        System.out.println("Max Components: " + maxComponents + "\n");
        System.out.println("Min States: " + minStates + "\n");
        System.out.println("Max States: " + maxStates + "\n");
    }

    private String getSelectedTestType() {
        if (realTestsRadio.isSelected()) return "Real";
        if (p2pTestsRadio.isSelected()) return "P2P";
        if (ringTestsRadio.isSelected()) return "Ring";
        if (starTestsRadio.isSelected()) return "Star";
        if (busTestsRadio.isSelected()) return "Bus";
        if (bipartiteTestsRadio.isSelected()) return "Bipartite";
        if (meshTestsRadio.isSelected()) return "Mesh";
        return "";
    }

    public JPanel getPanel() {
        return this;
    }

    public ArrayList<String> getInfo(){
        ArrayList<String> info = new ArrayList<>();
        info.add((String) equivalenceQueryCombo.getSelectedItem());
        info.add(String.valueOf(finalCheckModeCheckBox.isSelected()));
        info.add(String.valueOf(repetitionSpinner.getValue()));
        info.add(getSelectedTestType());
        info.add(String.valueOf(stateSpinnerMax.getValue()));
        info.add(String.valueOf(stateSpinnerMin.getValue()));
        info.add(String.valueOf(testSpinner.getValue()));
        info.add(String.valueOf(componentSpinnerMin.getValue()));
        info.add(String.valueOf(componentSpinnerMax.getValue()));

        return info;
    }

    public JButton getRunButton(){
        return runButton;
    }

}
