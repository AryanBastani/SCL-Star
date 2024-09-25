import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Experiment {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);

                mainFrame.setRunButtonListener(() -> {
                    ArrayList<String> infoPageInfo = mainFrame.getInfoPageInfo();
                    writeStatesInfo(Integer.parseInt(infoPageInfo.get(5)), Integer.parseInt(infoPageInfo.get(4)));
                    ArrayList<String> info = new ArrayList<>();
                    info.addAll(infoPageInfo);
                    info.removeLast();
                    info.removeLast();
                    info.add(String.valueOf(true));
                    int minNumOfComponents = Integer.parseInt(infoPageInfo.get(7));
                    int maxNumOfComponents = Integer.parseInt(infoPageInfo.get(8));
                    for(int numOfComponents = minNumOfComponents;
                        numOfComponents<=maxNumOfComponents; numOfComponents++) {

                        if (infoPageInfo.get(3).equals("Real Tests")) {
                            try {
                                FileWriter myWriter = new FileWriter("Real-Tests/Props.txt");
                                myWriter.write(numOfComponents + "\n");
                                myWriter.write(String.valueOf(Integer.parseInt(infoPageInfo.get(6)) * 1000));
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

    private static void writeStatesInfo(int minNumOfStates, int maxNumOfStates){
        try {
            FileWriter myWriter = new FileWriter("data/States Info.txt");
            myWriter.write(minNumOfStates + "\n");
            myWriter.write(String.valueOf(maxNumOfStates));
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
