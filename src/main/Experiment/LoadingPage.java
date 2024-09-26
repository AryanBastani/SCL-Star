package main.Experiment;

import javax.swing.*;
import java.awt.*;

public class LoadingPage extends JPanel {
    private JLabel loadingLabel;
    private JLabel messageLabel;
    private Timer animationTimer;
    private int animationFrame = 0;

    public LoadingPage() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.BOTH; // Fill both horizontally and vertically
        setBackground(Color.WHITE); // Background color

        // Font settings
        Font labelFont = new Font("Arial", Font.BOLD, 24);
        Font messageFont = new Font("Arial", Font.PLAIN, 18);

        // Create a label to display the loading animation
        loadingLabel = new JLabel("● ● ●", SwingConstants.CENTER);  // Placeholder for loading dots
        loadingLabel.setFont(labelFont);
        loadingLabel.setForeground(Color.BLUE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.9;
        add(loadingLabel, gbc);

        // Label for message "Running the tests"
        messageLabel = new JLabel("Running the tests", SwingConstants.CENTER);
        messageLabel.setFont(messageFont);
        messageLabel.setForeground(Color.BLACK);

        gbc.gridy = 1;
        gbc.weighty = 0.1;
        add(messageLabel, gbc);

        // Set up timer for the animation
        animationTimer = new Timer(500, e -> updateLoadingAnimation());
        animationTimer.start();
    }

    // Updates the loading animation (cycling through 3 frames)
    private void updateLoadingAnimation() {
        String[] frames = {"●  ○  ○", "○  ●  ○", "○  ○  ●"};
        loadingLabel.setText(frames[animationFrame]);
        animationFrame = (animationFrame + 1) % frames.length;
    }

    public JPanel getPanel() {
        return this;
    }

}

