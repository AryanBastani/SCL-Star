import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.api.SUL;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.DefaultQuery;
import de.learnlib.api.statistic.StatisticSUL;
import de.learnlib.driver.util.MealySimulatorSUL;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.filter.statistic.oracle.CounterOracle;
import de.learnlib.filter.statistic.sul.ResetCounterSUL;
import de.learnlib.filter.statistic.sul.SymbolCounterSUL;
import de.learnlib.oracle.equivalence.RandomWMethodEQOracle;
import de.learnlib.oracle.equivalence.RandomWordsEQOracle;
import de.learnlib.oracle.equivalence.WMethodEQOracle;
import de.learnlib.oracle.equivalence.WpMethodEQOracle;
import de.learnlib.oracle.equivalence.mealy.RandomWalkEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.commons.util.Pair;
import net.automatalib.serialization.InputModelDeserializer;
import net.automatalib.serialization.dot.DOTParsers;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.visualization.VisualizationHelper;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Run_experiment {
    public static String FILE_NAME = "FILE_NAME";
    public static String STATES = "STATES";
    public static String INPUTS = "INPUTS";
    public static String LSTAR = "LSTAR";
    public static String LIP = "LIP";
    public static String SCLSTAR = "SCLSTAR";
    public static String MQ_SYM = "_MQ_SYM";
    public static String MQ_RST = "_MQ_RST";
    public static String EQ_SYM = "_EQ_SYM";
    public static String EQ_RST = "_EQ_RST";
    public static String TOTAL_SYM = "_TOTAL_SYM";
    public static String TOTAL_RST = "_TOTAL_RST";
    public static String EQs = "_EQs";
    public static String COMPONENTS = "_COMPONENTS" ;
    public static String ROUNDS = "_ROUNDS";
    public static String MEM_SHIP_COUNT = "_MQs";
    public static String EQ_QUERY_COUNT = "_EQ_QUERY_COUNT";
    public static String CACHE = "CACHE";
    public static String[] data;

    public static String [] benchmarks;
    private static Boolean CACHE_ENABLE = true;

    private static String RESULTS_PATH;
//    private static Logger logger;
    private static CSVProperties csvProperties;
    private static Experimentproperties experimentProperties ;
    private static String benchmarks_base_dir;
    private static Logger logger;

    public static final String SRC_DIR = "src_dir";
    public static final String EQUIVALENCE_METHOD = "eq";
    public static final String EXPERIMENT_REPEAT = "repeat";


    public static void main(String[] args) throws IOException {
        csvProperties = CSVProperties.getInstance();
        experimentProperties = Experimentproperties.getInstance();
        try {
            // create the command line parser
            CommandLineParser parser = new DefaultParser();

            // create the Options
            Options options = createOptions();

            // automatically generate the help statement
            HelpFormatter formatter = new HelpFormatter();

            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            boolean isGenratedTests = false;
            boolean isNastedTests = false;

            System.out.println("Please choose a benchmarck to run(Enter 1 or 2 or ... or 5):");
            System.out.println("\t1- CL-Star-Benchmarks\n\t2- SmallTest-Benchmarks");
            System.out.println("\t3- Mealy-benchmarks\n\t4- Real-Tests\n\t5- Generated-Benchmarks");

            String file_path = "";
            Scanner myObj = new Scanner(System.in);
            String benckmarkId = myObj.nextLine();
            if(benckmarkId.equals("1"))
                file_path = "data/CL_Benchmarks.txt";
            else if(benckmarkId.equals("2"))
                file_path = "data/SmallTest_Benchmarks.txt";
            else if(benckmarkId.equals("3"))
                file_path = "data/Mealy_Benchmarks.txt";
            else if(benckmarkId.equals("4")) {
                file_path = "data/Reals.txt";
                isGenratedTests = true;
                isNastedTests = true;
            }
            else{
                isGenratedTests = true;
                System.out.println("Please choose the type of generated-benchmark (Enter 1 or 2 or ... or 8):");
                System.out.println("\t1- Point-To-Point\n\t2- Mesh\n\t3- Star\n\t4- Ring");
                System.out.println("\t5- Tree\n\t6- Bus\n\t7- Hybrid\n\t8- All types" );
                benckmarkId = myObj.nextLine();
                file_path = "Test-Generating/data/";
                if(benckmarkId.equals("1"))
                    file_path += "Point-To-Point.txt";
                else if(benckmarkId.equals("2"))
                    file_path += "Mesh.txt";
                else if(benckmarkId.equals("3"))
                    file_path += "Star.txt";
                else if(benckmarkId.equals("4"))
                    file_path += "Ring.txt";
                else if(benckmarkId.equals("5"))
                    file_path += "Tree.txt";
                else if(benckmarkId.equals("6"))
                    file_path += "Bus.txt";
                else if(benckmarkId.equals("7"))
                    file_path += "Hybrid.txt";
                else{
                    file_path = "data/Generated_Benchmarks.txt";
                    isNastedTests = true;
                }
            }
            /*
            if (line.hasOption(SRC_DIR)) {
                file_path = line.getOptionValue(SRC_DIR);
            } else {
                file_path = experimentProperties.getProp("benchmarks_file");
            }*/
            String equivalence_method;
            if (line.hasOption(EQUIVALENCE_METHOD)) {
                equivalence_method = line.getOptionValue(EQUIVALENCE_METHOD);
            } else {
                equivalence_method = experimentProperties.getProp("eq_query");
            }
            int repeat;
            if (line.hasOption(EXPERIMENT_REPEAT)) {
                repeat = Integer.parseInt(line.getOptionValue(EXPERIMENT_REPEAT));
            } else {
                repeat = Integer.parseInt(experimentProperties.getProp(EXPERIMENT_REPEAT));
            }

//        initial the experiment properties
            benchmarks_base_dir = experimentProperties.getProp("benchmarks_base_dir");
            RESULTS_PATH = experimentProperties.getProp("result_path");
            File f = new File(file_path);
            BufferedReader br = new BufferedReader(new FileReader(f));

            // initial a results file
            Utils.writeDataHeader(RESULTS_PATH, csvProperties.getResults_header());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime now = LocalDateTime.now();
//            logger = Logger.getLogger(dtf.format(now).toString());
//        FileHandler fh;
//        try {
//            String path = "logs/" + dtf.format(now) + ".log";
//            fh = new FileHandler(path);
//            logger.addHandler(fh);
//            SimpleFormatter formatter1 = new SimpleFormatter();
//            fh.setFormatter(formatter1);
//
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

            int dataLen = csvProperties.getIndex("DATA_LEN");

            String c;
            ProductMealy productMealy = null;

            File inputFolder = new File("Results/FSMs/INPUTs");
            Utils.clearFolder(inputFolder);
            new File("Results/FSMs/INPUTS").mkdirs();

            int inputCounter = 0;

            File sclFolder = new File("Results/FSMs/SCL-Star");
            Utils.clearFolder(sclFolder);
            new File("Results/FSMs/SCL-Star").mkdirs();

            File lFolder = new File("Results/FSMs/L-Star");
            Utils.clearFolder(lFolder);
            if(benckmarkId.equals("1")) {
                new File("Results/FSMs/L-Star").mkdirs();
            }

            File clFolder = new File("Results/FSMs/CL-Star");
            Utils.clearFolder(clFolder);
            new File("Results/FSMs/CL-Star").mkdirs();

            if(isGenratedTests && isNastedTests) {
                while (br.ready()) {
                    c = br.readLine();
                    File f2 = new File(c);
                    BufferedReader br2 = new BufferedReader(new FileReader(f2));
                    data = new String[dataLen];
                    data[csvProperties.getIndex(FILE_NAME)] = c;
                    productMealy = null;
                    while (br2.ready()) {
                        c = br2.readLine();
                        CompactMealy<String, Word<String>> currentTarget;
                        File file = new File(c);
                        try {
                            currentTarget = Utils.getInstance().loadMealyMachineFromDot(file);
                        } catch (Exception e) {
                            System.out.println(file);
                            System.out.println("problem in loading file");
                            System.out.println(e.toString());
                            System.out.println(c);
                            continue;
                        }
                        if (productMealy == null) {
                            productMealy = new ProductMealy(currentTarget);
                        } else productMealy.mergeFSMs(currentTarget);
                    }
                    assert productMealy != null;
                    CompactMealy<String, Word<String>> target = productMealy.getMachine();

                    inputCounter++;
                    new File("Results/FSMs/CL-Star/For input" + inputCounter).mkdirs();
                    new File("Results/FSMs/SCL-Star/For input" + inputCounter).mkdirs();
                    new File("Results/FSMs/L-Star/For input" + inputCounter).mkdirs();

                    try {
                        FileWriter inputWriter = new FileWriter("Results/FSMs/INPUTs/input" + inputCounter + "txt");
                        Utils.printMachine(target, false, inputWriter);
                        inputWriter.close();
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }


                    //logger.info("#States: " + target.size());
                    data[csvProperties.getIndex(STATES)] = Integer.toString(target.size());
                    data[csvProperties.getIndex(INPUTS)] = Integer.toString(target.numInputs());
                    Alphabet<String> alphabet = target.getInputAlphabet();

                    for (int rep = 0; rep < repeat; rep++) {
                        //   Shuffle the alphabet
                        String[] alphArr = alphabet.toArray(new String[alphabet.size()]);
                        Collections.shuffle(Arrays.asList(alphArr));
                        alphabet = Alphabets.fromArray(alphArr);
                        data[csvProperties.getIndex(CACHE)] = CACHE_ENABLE.toString();

                        Boolean final_check_mode = Boolean.valueOf(experimentProperties.getProp("final_check_mode"));
                        learnProductMealy(target, alphabet, equivalence_method, final_check_mode, inputCounter, rep + 1);

                        //             RUN SCL*
                        @Nullable CompactMealy result = null;
                        result = learnMealyInParts(target, alphabet, equivalence_method, "rndWords", final_check_mode, rep + 1, inputCounter, benckmarkId);

                        if (result == null) {
                            System.out.println("the  SUL is not learned completely (CL-Star)");
                        } else {
                            Utils.writeDataLineByLine(RESULTS_PATH, data);
                        }
                    }
                }
            }
            else if(isGenratedTests){
                while (br.ready()) {
                    c = br.readLine();
                    CompactMealy<String, Word<String>> currentTarget;
                    File file = new File(c);
                    data = new String[dataLen];
                    data[csvProperties.getIndex(FILE_NAME)] = c;
                    try {
                        currentTarget = Utils.getInstance().loadMealyMachineFromDot(file);
                    } catch (Exception e) {
                        System.out.println(file);
                        System.out.println("problem in loading file");
                        System.out.println(e.toString());
                        System.out.println(c);
                        continue;
                    }
                    if (productMealy == null) {
                        productMealy = new ProductMealy(currentTarget);
                    } else productMealy.mergeFSMs(currentTarget);
                }
                assert productMealy != null;
                CompactMealy<String, Word<String>> target = productMealy.getMachine();

                inputCounter++;
                new File("Results/FSMs/CL-Star/For input" + inputCounter).mkdirs();
                new File("Results/FSMs/SCL-Star/For input" + inputCounter).mkdirs();
                new File("Results/FSMs/L-Star/For input" + inputCounter).mkdirs();

                try {
                    FileWriter inputWriter = new FileWriter("Results/FSMs/INPUTs/input" + inputCounter + "txt");
                    Utils.printMachine(target, false, inputWriter);
                    inputWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }


                //logger.info("#States: " + target.size());
                data[csvProperties.getIndex(STATES)] = Integer.toString(target.size());
                data[csvProperties.getIndex(INPUTS)] = Integer.toString(target.numInputs());
                Alphabet<String> alphabet = target.getInputAlphabet();

                for (int rep = 0; rep < repeat; rep++) {
                    //   Shuffle the alphabet
                    String[] alphArr = alphabet.toArray(new String[alphabet.size()]);
                    Collections.shuffle(Arrays.asList(alphArr));
                    alphabet = Alphabets.fromArray(alphArr);
                    data[csvProperties.getIndex(CACHE)] = CACHE_ENABLE.toString();

                    Boolean final_check_mode = Boolean.valueOf(experimentProperties.getProp("final_check_mode"));
                    learnProductMealy(target, alphabet, equivalence_method, final_check_mode, inputCounter, rep + 1);

                    //             RUN SCL*
                    @Nullable CompactMealy result = null;
                    result = learnMealyInParts(target, alphabet, equivalence_method, "rndWords", final_check_mode, rep + 1, inputCounter, benckmarkId);

                    if (result == null) {
                        System.out.println("the  SUL is not learned completely (CL-Star)");
                    } else {
                        Utils.writeDataLineByLine(RESULTS_PATH, data);
                    }
                }
            }
            else {
                while (br.ready()) {
                    inputCounter++;
                    new File("Results/FSMs/CL-Star/For input" + inputCounter).mkdirs();
                    new File("Results/FSMs/SCL-Star/For input" + inputCounter).mkdirs();
                    if (benckmarkId.equals("1"))
                        new File("Results/FSMs/L-Star/For input" + inputCounter).mkdirs();
                    c = br.readLine();
                    data = new String[dataLen];
                    File file = new File(c);
                    data[csvProperties.getIndex(FILE_NAME)] = c;
                    CompactMealy<String, Word<String>> target;
                    try {
                        target = Utils.getInstance().loadMealyMachineFromDot(file);
                    } catch (Exception e) {
                        System.out.println(file);
                        System.out.println("problem in loading file");
                        System.out.println(e.toString());
                        System.out.println(c);
                        continue;
                    }

                    try {
                        FileWriter inputWriter = new FileWriter("Results/FSMs/INPUTs/input" + inputCounter + "txt");
                        Utils.printMachine(target, false, inputWriter);
                        inputWriter.close();
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }


                    //logger.info("#States: " + target.size());
                    data[csvProperties.getIndex(STATES)] = Integer.toString(target.size());
                    data[csvProperties.getIndex(INPUTS)] = Integer.toString(target.numInputs());
                    Alphabet<String> alphabet = target.getInputAlphabet();


                    for (int rep = 0; rep < repeat; rep++) {
                        //   Shuffle the alphabet
                        String[] alphArr = alphabet.toArray(new String[alphabet.size()]);
                        Collections.shuffle(Arrays.asList(alphArr));
                        alphabet = Alphabets.fromArray(alphArr);
                        data[csvProperties.getIndex(CACHE)] = CACHE_ENABLE.toString();

                        //             RUN L*
                        Boolean final_check_mode = Boolean.valueOf(experimentProperties.getProp("final_check_mode"));
                        learnProductMealy(target, alphabet, equivalence_method, final_check_mode, inputCounter, rep + 1);

                        //             RUN SCL*
                        @Nullable CompactMealy result = null;
                        result = learnMealyInParts(target, alphabet, equivalence_method, "rndWords", final_check_mode, rep + 1, inputCounter, benckmarkId);

                        if (result == null) {
                            System.out.println("the  SUL is not learned completely (CL-Star)");
                        } else {
                            Utils.writeDataLineByLine(RESULTS_PATH, data);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getStackTrace()[0].getLineNumber());
        }
    }

    public static CompactMealy learnMealyInParts(CompactMealy mealyss, Alphabet<String> alphabet, String eq_method, String partial_eq_method, boolean test_mode, int rep, int inCounter, String benchmarkId){

        Utils.getInstance();
        // SUL simulator
        SUL<String, Word<String>> sulSim = new MealySimulatorSUL<>(mealyss, Utils.OMEGA_SYMBOL);

        //////////////////////////////////
        // Setup objects related to MQs //
        //////////////////////////////////

        // Counters for MQs
        StatisticSUL<String, Word<String>> mq_sym = new SymbolCounterSUL<>("MQ", sulSim);
        StatisticSUL<String, Word<String>> mq_rst = new ResetCounterSUL<>("MQ", mq_sym);

        // SUL for counting queries wraps sul
        SUL<String, Word<String>> mq_sul = mq_rst;

        // use caching to avoid duplicate queries
        // SULs for associating the IncrementalMealyBuilder 'mq_builder' to MQs
        if(CACHE_ENABLE){
            mq_sul = SULCache.createDAGCache(alphabet, mq_rst);
        }

        MembershipOracle<String, Word<Word<String>>> mqOracle = new SULOracle<String, Word<String>>(mq_sul);
        CounterOracle membShipCounter = new CounterOracle(mqOracle, "MQ");

        //////////////////////////////////
        // Setup objects related to EQs //
        //////////////////////////////////

        // Counters for EQs
        StatisticSUL<String, Word<String>> eq_sym = new SymbolCounterSUL<>("EQ", sulSim);
        StatisticSUL<String, Word<String>> eq_rst = new ResetCounterSUL<>("EQ", eq_sym);

        // SUL for counting queries wraps sul
        SUL<String, Word<String>> eq_sul = eq_rst;

        // SULs for associating the IncrementalMealyBuilder 'builder' to EQs
        if (CACHE_ENABLE){
            eq_sul = SULCache.createDAGCache(alphabet, eq_rst);
        }

        EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> eqOracle = null;
        EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> partialEqOracle = null;
        partialEqOracle = buildEqOracle(eq_sul, partial_eq_method);
        eqOracle = buildEqOracle(eq_sul, eq_method);

        EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> testEqOracle = null;
        testEqOracle = buildEqOracle(eq_sul, "wp");
        @Nullable CompactMealy sclResult;
        try {
        FileWriter sclWriter = new FileWriter("Results/FSMs/SCL-Star/For input" + inCounter + "/Run for the " + rep + "st time.txt");
        SclStar sclStar = new SclStar(alphabet, membShipCounter, eqOracle, partialEqOracle);
        if (!test_mode ){
            sclResult = sclStar.run(mealyss, eq_sym, null, rep, sclWriter);
        }
        else{
//        create check eq oracle for random search
//            SUL<String, Word<String>> testSul = new MealySimulatorSUL<>(mealyss, Utils.OMEGA_SYMBOL);
//            MembershipOracle<String, Word<Word<String>>> testOracleForEQoracle = new SULOracle<>(testSul);
//            EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> testEqOracle =
//                    new WpMethodEQOracle<>(testOracleForEQoracle, 2);
            sclResult = sclStar.run(mealyss, eq_sym, testEqOracle, rep, sclWriter);
        }

            Utils.printMachine(sclResult, true, sclWriter);
            sclWriter.close();

            data[csvProperties.getIndex(LIP+ROUNDS)] = String.valueOf(sclStar.getRound_counter().getCount());
            data[csvProperties.getIndex(LIP+MQ_RST)] = Utils.ExtractValue(mq_rst.getStatisticalData().getSummary());
            data[csvProperties.getIndex(LIP+MQ_SYM)] = Utils.ExtractValue(mq_sym.getStatisticalData().getSummary());
            data[csvProperties.getIndex(LIP+EQ_RST)] = Utils.ExtractValue(eq_rst.getStatisticalData().getSummary());
            data[csvProperties.getIndex(LIP+EQ_SYM)] = Utils.ExtractValue(eq_sym.getStatisticalData().getSummary());
            data[csvProperties.getIndex(LIP+EQs)] = String.valueOf(sclStar.getEq_counter().getCount());
            data[csvProperties.getIndex(LIP+TOTAL_RST)] = String.valueOf(Long.parseLong(Utils.ExtractValue(mq_rst.getStatisticalData().getSummary()))+ Long.parseLong(Utils.ExtractValue(eq_rst.getStatisticalData().getSummary())));
            data[csvProperties.getIndex(LIP+TOTAL_SYM)] = String.valueOf(Long.parseLong(Utils.ExtractValue(mq_sym.getStatisticalData().getSummary()))+ Long.parseLong(Utils.ExtractValue(eq_sym.getStatisticalData().getSummary())));
            data[csvProperties.getIndex(LIP+COMPONENTS)] = String.valueOf(sclStar.getSigmaFamily().size());
            data[csvProperties.getIndex(LIP+MEM_SHIP_COUNT)] = String.valueOf(membShipCounter.getCount());
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }

//        try {
//            FileWriter clWriter = new FileWriter("Results/FSMs/CL-Star/For input" + inCounter + "/Run for the " + rep + "st time.txt");
//        ClStar Mealy_LIP = new ClStar(alphabet, mqOracle, eqOracle, partialEqOracle, logger);
//        @Nullable CompactMealy clResult;
//        if (!test_mode ){
//            clResult = Mealy_LIP.run(eq_sym, null, clWriter);
//        }
//        else{
////        create check eq oracle for random search
////            SUL<String, Word<String>> testSul = new MealySimulatorSUL<>(mealyss, Utils.OMEGA_SYMBOL);
////            MembershipOracle<String, Word<Word<String>>> testOracleForEQoracle = new SULOracle<>(testSul);
////            EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> testEqOracle =
////                    new WpMethodEQOracle<>(testOracleForEQoracle, 2);
//            clResult = Mealy_LIP.run(eq_sym, testEqOracle, clWriter);
//        }
//            Utils.printMachine(clResult, false, clWriter);
//            clWriter.close();
//        }
//        catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//            return null;
//        }


//        logger.info("Rounds: " + sclStar.getRound_counter().getCount());
//        logger.info("#EQs: " + sclStar.getEq_counter().getCount());
//        logger.info(mq_rst.getStatisticalData().toString());
//        logger.info(mq_sym.getStatisticalData().toString());
//        logger.info(eq_rst.getStatisticalData().toString());
//        logger.info(eq_sym.getStatisticalData().toString());
//        // statistics array

        // learning statistics


        // profiling
        //SimpleProfiler.logResults();
        return sclResult;
    }

    public static void learnProductMealy(CompactMealy mealyss, Alphabet<String> alphabet, String eq_method, boolean test_mode, int inCounter, int rep){

        Utils.getInstance();
        // SUL simulator
        SUL<String, Word<String>> sulSim = new MealySimulatorSUL<>(mealyss, Utils.OMEGA_SYMBOL);

        //////////////////////////////////
        // Setup objects related to MQs //
        //////////////////////////////////

        // Counters for MQs
        StatisticSUL<String, Word<String>> mq_sym = new SymbolCounterSUL<>("MQ", sulSim);
        StatisticSUL<String, Word<String>> mq_rst = new ResetCounterSUL<>("MQ", mq_sym);

        // SUL for counting queries wraps sul
        SUL<String, Word<String>> mq_sul = mq_rst;

        // use caching to avoid duplicate queries

        // SULs for associating the IncrementalMealyBuilder 'mq_builder' to MQs
        if (CACHE_ENABLE){
            mq_sul = SULCache.createDAGCache(alphabet, mq_rst);
        }

        MembershipOracle<String, Word<Word<String>>> mqOracle = new SULOracle<String, Word<String>>(mq_sul);
        CounterOracle membShipCounter = new CounterOracle(mqOracle, "MQ");


        //////////////////////////////////
        // Setup objects related to EQs //
        //////////////////////////////////

        // Counters for EQs
        StatisticSUL<String, Word<String>> eq_sym = new SymbolCounterSUL<>("EQ", sulSim);
        StatisticSUL<String, Word<String>> eq_rst = new ResetCounterSUL<>("EQ", eq_sym);

        // SUL for counting queries wraps sul
        SUL<String, Word<String>> eq_sul = eq_rst;

        // SULs for associating the IncrementalMealyBuilder 'cbuilder' to EQs
        if(CACHE_ENABLE){
            eq_sul = SULCache.createDAGCache(alphabet, eq_rst);
        }

        EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> eqOracle = null;
        EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> testEqOracle = null;
        eqOracle = buildEqOracle(eq_sul, eq_method);
        testEqOracle = buildEqOracle(eq_sul, "wp");


        Experiment experiment = learningLStarM(alphabet, mealyss, membShipCounter, eqOracle);
        CompactMealy<String, Word<String>> h = (CompactMealy<String, Word<String>>) experiment.getFinalHypothesis();
        if (test_mode){
            @Nullable DefaultQuery<String, Word<Word<String>>> ce = testEqOracle.findCounterExample(h,alphabet);
            if (ce!=null){
                System.out.println();
                System.out.println("************  incomplete lstar learning  **********");
                System.out.println(data[csvProperties.getIndex(FILE_NAME)]);
                System.out.println();
            }
        }
        try {
            FileWriter lWriter = new FileWriter("Results/FSMs/L-Star/For input" + inCounter + "/Run for the " + rep + "st time.txt");
            Utils.printMachine(h, false, lWriter);
            lWriter.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



        // learning statistics
//        logger.info("Rounds: " + experiment.getRounds().getCount());
//        logger.info(mq_rst.getStatisticalData().toString());
//        logger.info(mq_sym.getStatisticalData().toString());
//        logger.info(eq_rst.getStatisticalData().toString());
//        logger.info(eq_sym.getStatisticalData().toString());


//        // statistics array
        data[csvProperties.getIndex(LSTAR+MQ_RST)] = Utils.ExtractValue(mq_rst.getStatisticalData().getSummary());
        data[csvProperties.getIndex(LSTAR+MQ_SYM)] = Utils.ExtractValue(mq_sym.getStatisticalData().getSummary());
        data[csvProperties.getIndex(LSTAR+EQ_RST)] = Utils.ExtractValue(eq_rst.getStatisticalData().getSummary());
        data[csvProperties.getIndex(LSTAR+EQ_SYM)] = Utils.ExtractValue(eq_sym.getStatisticalData().getSummary());
        data[csvProperties.getIndex(LSTAR+EQs)] = String.valueOf(experiment.getRounds().getCount());
        data[csvProperties.getIndex(LSTAR+TOTAL_RST)] = String.valueOf(Long.parseLong(Utils.ExtractValue(mq_rst.getStatisticalData().getSummary()))+ Long.parseLong(Utils.ExtractValue(eq_rst.getStatisticalData().getSummary())));
        data[csvProperties.getIndex(LSTAR+TOTAL_SYM)] = String.valueOf(Long.parseLong(Utils.ExtractValue(mq_sym.getStatisticalData().getSummary()))+ Long.parseLong(Utils.ExtractValue(eq_sym.getStatisticalData().getSummary())));
        data[csvProperties.getIndex(LSTAR+MEM_SHIP_COUNT)] = String.valueOf(membShipCounter.getCount());


        // profiling
        SimpleProfiler.logResults();
    }

    private static Experiment<MealyMachine<?, String,?, Word<String>>> learningLStarM(Alphabet<String> alphabet,
                                                                                      CompactMealy<String, Word<String>> mealyss,
                                                                                      MembershipOracle<String, Word<Word<String>>> mqOracle,
                                                                                      EquivalenceOracle<? super MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> eqOracle){

        ExtensibleLStarMealyBuilder<String, Word<String>> builder = new ExtensibleLStarMealyBuilder<String, Word<String>>();
        builder.setAlphabet(alphabet);
        builder.setOracle(mqOracle);


        ExtensibleLStarMealy<String, Word<String>> learner = builder.create();

        // The experiment will execute the main loop of active learning
        Experiment.MealyExperiment<String, Word<String>> experiment = new Experiment.MealyExperiment<String, Word<String>>(learner, eqOracle,
                alphabet);

        experiment.run();
        return experiment;
    }


    private static EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> buildEqOracle(
            SUL<String, Word<String>> eq_sul, String eq_method) {
        MembershipOracle<String, Word<Word<String>>> oracleForEQoracle = new SULOracle<>(eq_sul);

        EquivalenceOracle<MealyMachine<?, String, ?, Word<String>>, String, Word<Word<String>>> eqOracle;
        double restartProbability;
        int maxSteps, maxTests, maxLength, minLength, maxDepth, minimalSize, rndLength, bound;
        long rnd_long;
        boolean resetStepCount;
        long tstamp = System.currentTimeMillis();
        Random rnd_seed = new Random(tstamp);

        LearnLibProperties learn_props = LearnLibProperties.getInstance();

        switch (eq_method) {
            case "rndWalk":
                // create RandomWalkEQOracle
                restartProbability = learn_props.getRndWalk_restartProbability();
                maxSteps = learn_props.getRndWalk_maxSteps();
                resetStepCount = learn_props.getRndWalk_resetStepsCount();

                eqOracle = new RandomWalkEQOracle<String, Word<String>>(eq_sul, // sul
                        restartProbability, // reset SUL w/ this probability before a step
                        maxSteps, // max steps (overall)
                        resetStepCount, // reset step count after counterexample
                        rnd_seed // make results reproducible
                );
//                logger.info("EquivalenceOracle: RandomWalkEQOracle(" + restartProbability + "," + maxSteps + ","
//                        + resetStepCount + ")");
                break;
            case "rndWords":
                // create RandomWordsEQOracle
                maxTests = learn_props.getRndWords_maxTests();
                maxLength = learn_props.getRndWords_maxLength();
                minLength = learn_props.getRndWords_minLength();
                rnd_long = rnd_seed.nextLong();
                rnd_seed.setSeed(rnd_long);
//                System.out.println("max test");
//                System.out.println(maxTests);

                eqOracle = new RandomWordsEQOracle<>(oracleForEQoracle, minLength, maxLength, maxTests, rnd_seed);
//                logger.info("EquivalenceOracle: RandomWordsEQOracle(" + minLength + ", " + maxLength + ", " + maxTests
//                        + ", " + rnd_long + ")");
                break;

            case "rndWordsBig":
                // create RandomWordsEQOracle
                maxTests = 2000;
                maxLength = 200;
                minLength = learn_props.getRndWords_minLength();
                rnd_long = rnd_seed.nextLong();
                rnd_seed.setSeed(rnd_long);

                eqOracle = new RandomWordsEQOracle<>(oracleForEQoracle, minLength, maxLength, maxTests, rnd_seed);
//                logger.info("EquivalenceOracle: RandomWordsEQOracle(" + minLength + ", " + maxLength + ", " + maxTests
//                        + ", " + rnd_long + ")");
                break;
            case "wp":
                maxDepth = learn_props.getW_maxDepth();
                eqOracle = new WpMethodEQOracle<>(oracleForEQoracle, maxDepth);
//                logger.info("EquivalenceOracle: WpMethodEQOracle(" + maxDepth + ")");
                break;
            case "w":
                maxDepth = learn_props.getW_maxDepth();
                eqOracle = new WMethodEQOracle<>(oracleForEQoracle, maxDepth);
//                logger.info("EquivalenceOracle: WMethodQsizeEQOracle(" + maxDepth + ")");
                break;
            case "wrnd":
                minimalSize = learn_props.getWhyp_minLen();
                rndLength = learn_props.getWhyp_rndLen();
                bound = learn_props.getWhyp_bound();
                rnd_long = rnd_seed.nextLong();
                rnd_seed.setSeed(rnd_long);

                eqOracle = new RandomWMethodEQOracle<>(oracleForEQoracle, minimalSize, rndLength, bound, rnd_seed, 1);
//                logger.info("EquivalenceOracle: RandomWMethodEQOracle(" + minimalSize + "," + rndLength + "," + bound
//                        + "," + rnd_long + ")");
                break;
            default:
                maxDepth = 2;
                eqOracle = new WMethodEQOracle<>(oracleForEQoracle, maxDepth);
//                logger.info("EquivalenceOracle: WMethodEQOracle(" + maxDepth + ")");
                break;
        }
        return eqOracle;//        return new WpMethodEQOracle<>(oracleForEQoracle, 4);
    }


    private static CompactMealy<String, Word<String>> LoadMealy(File fsm_file) {
        // TODO Auto-generated method stub
        InputModelDeserializer<String, CompactMealy<String, Word<String>>> parser_1 = DOTParsers
                .mealy(MEALY_EDGE_WORD_STR_PARSER);
        CompactMealy<String, Word<String>> mealy = null;
        String file_name = fsm_file.getName();
        if (file_name.endsWith("txt")) {
            try {
                mealy = Utils.getInstance().loadMealyMachine(fsm_file);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return mealy;
        } else if (file_name.endsWith("dot")) {
            try {
                mealy = parser_1.readModel(fsm_file).model;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return mealy;
        }

        return null;
    }

    public static final Function<Map<String, String>, Pair<@Nullable String, @Nullable Word<String>>> MEALY_EDGE_WORD_STR_PARSER = attr -> {
        final String label = attr.get(VisualizationHelper.EdgeAttrs.LABEL);
        if (label == null) {
            return Pair.of(null, null);
        }

        final String[] tokens = label.split("/");

        if (tokens.length != 2) {
            return Pair.of(null, null);
        }

        Word<String> token2 = Word.epsilon();
        token2 = token2.append(tokens[1]);
        return Pair.of(tokens[0], token2);
    };

    private static Options createOptions() {
        Options options = new Options();
        options.addOption(SRC_DIR, true, "Input directory");
        options.addOption(EQUIVALENCE_METHOD, true, "Equivalence method, options: wp, w, wrnd, rndWords, rndWordsBig, rndWalk");
        options.addOption(EXPERIMENT_REPEAT, true, "Number of repeating the experiment");
        return options;
    }

}
