import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private TestConfigurationUI firstPage;
    private TestUI secondPage;
    private LoadingPage loadingPage;
    private ResultsPage resultsPage;

    private ArrayList<String> firstPageInfo = new ArrayList<>();
    private ArrayList<String> secondPageInfo = new ArrayList<>();

    public MainFrame() {
        // Set up the frame
        setTitle("Test Configuration");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = (int) (1000);  // 80% of screen width
        int frameHeight = (int) (800);  // 80% of screen height
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
        loadingPage = new LoadingPage();
        resultsPage = new ResultsPage(frameWidth, frameHeight);

        // Add the two pages to the main panel
        mainPanel.add(firstPage.getPanel(), "FirstPage");
        mainPanel.add(secondPage.getPanel(), "SecondPage");
        mainPanel.add(loadingPage.getPanel(), "LoadingPage");
        mainPanel.add(resultsPage.getPanel(), "ResultsPage");

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
// Set listener for the "Next" button on the second page
    public void setSecondNextButtonListener(Runnable listener) {
        secondPage.getRunButton().addActionListener(e -> {
            cardLayout.show(mainPanel, "LoadingPage");  // Switch to the loading page immediately

            // Use a SwingWorker for background processing
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    listener.run();  // Run the long-running task in the background
                    return null;
                }

                @Override
                protected void done() {
                    // After background task finishes, show two images

                    // Load the images
                    ImageIcon img1 = new ImageIcon("Results/Plots/Resets.png");  // Replace with actual paths
                    ImageIcon img2 = new ImageIcon("Results/Plots/Symbols.png");  // Replace with actual paths

                    // Set the images in the pictures page
                    resultsPage.setImages(img1, img2);

                    // Switch to the pictures page after the task is done
                    cardLayout.show(mainPanel, "ResultsPage");
                }
            };

            worker.execute();  // Start the background task
        });
    }

}
