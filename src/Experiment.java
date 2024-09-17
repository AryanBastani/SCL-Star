import javax.swing.*;
import java.io.*;
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
                    ArrayList<String> info = new ArrayList<>();
                    info.addAll(firstPageInfo);
                    info.add(secondPageInfo.getLast());
                    info.add(secondPageInfo.get(secondPageInfo.size() - 2));
                    info.add(secondPageInfo.get(0));
                    info.add(String.valueOf(true));
                    int minNumOfComponents = Integer.parseInt(secondPageInfo.get(1));
                    int maxNumOfComponents = Integer.parseInt(secondPageInfo.get(2));
                    for(int numOfComponents = minNumOfComponents;
                        numOfComponents<=maxNumOfComponents; numOfComponents++) {

                        if (firstPageInfo.getLast().equals("Real Tests")) {
                            try {
                                FileWriter myWriter = new FileWriter("Real-Tests/Props.txt");
                                myWriter.write(numOfComponents + "\n");
                                myWriter.write(String.valueOf(Integer.parseInt(secondPageInfo.getFirst()) * 1000));
                                myWriter.close();

                               runFile("python", "Real-Tests/ChooseTests.py");
                            } catch (IOException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }
                        } else {

                        }
                        try {
                            FsmLearner learner = new FsmLearner(info);
                            learner.learn();
                            info.set(info.size() - 1, String.valueOf(false));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        runFile("python", "JupyterRunner.py");
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }

                });
            });
        });
    }

    private static void runFile(String type, String filePath) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(type, filePath);

        // Redirect error stream to avoid missing any issues
        builder.redirectErrorStream(true);

        Process process = builder.start();

        // Capture the output from the process
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Ensure the process completes
        try {
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
