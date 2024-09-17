import javax.swing.*;
import java.awt.*;

public class ResultsPage extends JPanel {
    private JLabel imageLabel1;
    private JLabel imageLabel2;
    private int frameWidth;
    private int frameHeight;

    public ResultsPage(int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0); // Padding
        gbc.fill = GridBagConstraints.BOTH; // Fill both horizontally and vertically
        setBackground(Color.WHITE); // Background color

        // Placeholder labels for images (will be set after tests)
        imageLabel1 = new JLabel();
        imageLabel2 = new JLabel();

        // Set GridBagConstraints for the images
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        add(imageLabel1, gbc);

        gbc.gridy = 1;  // Move the second label below the first
        add(imageLabel2, gbc);
    }

    // Method to set the images with resizing
    public void setImages(ImageIcon img1, ImageIcon img2) {
        // Resize images to fit within the frame dimensions
        Image scaledImg1 = img1.getImage().getScaledInstance(frameWidth - 20, (int)(frameHeight / 2.4) , Image.SCALE_SMOOTH);
        Image scaledImg2 = img2.getImage().getScaledInstance(frameWidth - 20, (int)(frameHeight / 2.4) , Image.SCALE_SMOOTH);

        // Set the resized images
        imageLabel1.setIcon(new ImageIcon(scaledImg1));
        imageLabel2.setIcon(new ImageIcon(scaledImg2));

        // Revalidate and repaint the panel to update the UI
        revalidate();
        repaint();
    }

    public JPanel getPanel() {
        return this;
    }
}
