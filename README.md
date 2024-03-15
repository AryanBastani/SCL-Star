# Decomposed Automata Learning
This repository contains the source codes and results of "Compositional Learning for Interleaving Parallel Automata".
This project is a result of the Mater's research of Faezeh Labbaf at the [Tehran Institute for Advanced Studies(TeIAS)](https://teias.institute/) under the supervision of Hossein Hojjat and Mohammad Mousavi.


## Replicating Experiments
To replicate this experiment, the `Run_experiment` class must be run. This class compares L* and CL* algorithms. It takes a list of FSM files, and learn them with both algorithms.
The learnLib, openCSV, common-cli and slf4j libraries needs to be installed to properly run the experiments. The jar files of these libraries can be found in [\libs](\libs) directory.

The cost of algorithm in terms of number of input symbols and resets for each fsm will be stored in [/data/Results.csv]('/data/Results.csv')
You can reproduce tables 1 and 2, and figures 2-4 in the paper using the data/Results.csv or the colab notebook [here](https://colab.research.google.com/drive/167KaoKKp1GfT24PGRujtBhNYmf33X789?usp=sharing)

The class "Run_Experiments" can be run with the following command:
```
        java -cp ./libs/learnlib-distribution-0.16.0-dependencies-bundle.jar:./libs/opencsv-5.6.jar:./libs/slf4j-jdk14-1.7.36.jar:./libs/commons-cli-1.4.jar:cl-star.jar Run_experiment
```
The input parameters of this class are listed below:
- __-src_dir__: Directory of list of input FSMs. Default: data/experiment_benchmarks.txt
- __-eq__: Equivalence method, options: wp, w, wrnd, rndWords, rndWordsBig, rndWalk. Default: rndWords
- __-repeat__: number of repeating the experiment. Default:3

Other parameters of the experiment can be modified in [`.experimentProps`](/.experimentProps) file. the experiment properties are as followed:

- __benchmarks_base_dir__: the base directory where benchmarks are in it.
- __result_path__: address of the file that results will be written in it.
- __final_check_mode__: a boolean indicating that whether run the experiments with an extra deterministic equivalence query or not.

## Experiment Results
All the experiment results containing the csv file output of results, the summarised results (statistical analysis) and plots are in [`\Results`](Results) directory. 
the statistical analysis and visualizations are performed in python (the source code is in [Decomposed_Learning_Results.ipynb](/Experiments/Decomposed_Learning_Results.ipynb) file).

The data and plots mentioned before is the results of running the experiment on 100 FSMs consisting of a minimum of two and a maximum of nine components in this
case study.
. Our subject systems have a minimum of 300 states and
a maximum of 3840, and their average number of states is 1278.2 with a standard deviation 847.

## Learn Single FSM 
The "Learn_single_FSM" class is used to learn one single FSM with both algorithms.
It will print out the learning costs for both algorithms in shell

The class "Learn_single_FSM" can be run with the following command:
```
        java -cp ./libs/learnlib-distribution-0.16.0-dependencies-bundle.jar:./libs/opencsv-5.6.jar:./libs/slf4j-jdk14-1.7.36.jar:./libs/commons-cli-1.4.jar:cl-star.jar Learn_single_FSM
```
The input parameters of this class are listed below:
- __-src_dir__: Directory of list of input FSMs. Default: data/experiment_benchmarks.txt
- __-eq__: Equivalence method, options: wp, w, wrnd, rndWords, rndWordsBig, rndWalk. Default: rndWords

The input FSM file should be a .dot file which has a similar content as data/sample_fsm.dot file.
Each line in .dot file represents a transition in the FSM, and the initial state is always S0.

## Rebuild the Project

To rebuild the project, run following commands:
```
    javac -d build -cp "libs/*" -Xlint:unchecked src/*.java
    cd build
    jar cvf ../cl-star.jar *.class
    cd ..
```
The jar file of the project will be at ./cl-star.jar
