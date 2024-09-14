import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private TestConfigurationUI firstPage;
    private TestUI secondPage;

    private ArrayList<String> firstPageInfo = new ArrayList<>();
    private ArrayList<String> secondPageInfo = new ArrayList<>();

    public MainFrame() {
        // Set up the frame
        setTitle("Test Configuration");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = (int) (screenSize.width * 0.8);  // 80% of screen width
        int frameHeight = (int) (screenSize.height * 0.8);  // 80% of screen height
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

        // Styling the main frame
        getContentPane().setBackground(Color.WHITE);  // Consistent background color
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));  // Set a minimum size for better usability
    }

    // Method to get the information from the first page
    public ArrayList<String> getFirstPageInfo() {
        return firstPage.getInfo();
    }

    // Method to get the information from the second page
    public ArrayList<String> getSecondPageInfo() {
        return secondPage.getInfo();
    }

    // Set listener for the "Next" button on the first page
    public void setNextButtonListener(Runnable listener) {
        firstPage.getNextButton().addActionListener(e -> {
            cardLayout.show(mainPanel, "SecondPage");  // Switch to the second page
            listener.run();
        });
    }

    // Set listener for the "Next" button on the second page
    public void setSecondNextButtonListener(Runnable listener) {
        secondPage.getRunButton().addActionListener(e -> {
            listener.run();  // Run listener after second page "Next" is pressed
        });
    }
}
