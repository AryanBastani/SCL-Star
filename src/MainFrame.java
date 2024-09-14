import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private TestConfigurationUI firstPage;
    private TestUI secondPage;

    public MainFrame() {
        // Set up the frame
        setTitle("Test Configuration");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = (int) (screenSize.width * 0.7);  // 80% of screen width
        int frameHeight = (int) (screenSize.height * 0.7);  // 80% of screen height
        setSize(frameWidth, frameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);  // Consistent background color

        // Create first page and second page
        firstPage = new TestConfigurationUI();
        secondPage = new TestUI();  // Assuming TestUI is similarly updated for a cohesive look

        // Add the two pages to the main panel
        mainPanel.add(firstPage.getPanel(), "FirstPage");
        mainPanel.add(secondPage.getPanel(), "SecondPage");

        // Add main panel to the frame
        add(mainPanel);

        // Set up next button action to switch to the second page
        firstPage.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "SecondPage");  // Switch to second page
            }
        });

        // Styling the main frame
        getContentPane().setBackground(Color.WHITE);  // Consistent background color
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));  // Set a minimum size for better usability
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
