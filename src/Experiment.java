import javax.swing.*;
import java.util.ArrayList;

public class Experiment {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);

            // First page: Register an event listener for the first page's "Next" button
            mainFrame.setNextButtonListener(() -> {
                ArrayList<String> firstPageInfo = mainFrame.getFirstPageInfo();
                System.out.println("First Page Info: " + firstPageInfo);

                // Second page: Register a new listener for the second page's "Next" button
                mainFrame.setSecondNextButtonListener(() -> {
                    ArrayList<String> secondPageInfo = mainFrame.getSecondPageInfo();
                    System.out.println("Second Page Info: " + secondPageInfo);

                    // After second page processing, you can proceed with other logic
                });
            });
        });
    }
}
