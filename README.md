# SCL-Star

## Getting Started

### Overview

SCL-Star provides tools for learning and comparing Finite State Machines (FSMs) using the L* and SCL* algorithms. The artifact includes a Dockerized environment with all necessary dependencies, a project JAR file, and scripts for running experiments. Results include statistical analyses and plots, facilitating the reproduction of the paper's conclusions.

### Installation

Install Docker using the appropriate method for your platform:

- **Debian & Ubuntu:**  
  ```bash
  sudo apt-get install docker.io
  ```  
  or  
  ```bash
  sudo apt-get install docker-ce
  ```  

- **Arch:**  
  ```bash
  sudo pacman -S docker
  ```  

- **Other platforms:** Refer to the [Docker installation guide](https://docs.docker.com/install/).

#### Install `openjdk:21-oracle`:  
```bash
docker pull openjdk:21-oracle
```

#### Install `docker-buildx` (not required for WSL):  
- **For Ubuntu:**  
  ```bash
  sudo apt install curl
  curl -LO https://github.com/docker/buildx/releases/download/v0.19.3/buildx-v0.19.3.linux-amd64 
  mkdir -p ~/.docker/cli-plugins
  mv buildx-v0.19.3.linux-amd64 ~/.docker/cli-plugins/docker-buildx
  chmod +x ~/.docker/cli-plugins/docker-buildx
  ```

You can download docker-buildx for other platforms [here](https://github.com/docker/buildx/releases)
> **Note:** Windows users are advised to use WSL for Docker.

#### Login to Docker:  
Sign up at [Docker](https://www.docker.com) if you don't have an account, then log in:  
```bash
docker login
```

#### Build the Docker container:  
1. Download and unzip the artifact file.  
2. Navigate to the artifact directory and build the Docker image:  
   ```bash
   docker build --tag scl-star .
   ```  
   If you encounter the following error:
   ```bash
   ERROR: permission denied while trying to connect to the Docker daemon socket
   ```
   You can resolve it by running:
   ```bash
   sudo chmod 666 /var/run/docker.sock
   ```

Run the container and access its shell:

- **Linux:**  
  ```bash
  docker run -v "$(pwd)"/data:/app/data -it --entrypoint=/bin/bash scl-star -i
  ```  
- **Windows (PowerShell):**  
  ```bash
  docker run -v ${pwd}/data:/app/data -it --entrypoint=/bin/bash scl-star -i
  ```  
- **Windows (Git Bash):**  
  ```bash
  docker run -v  "${PWD}/data/":/app/data/ -it --entrypoint bash scl-star -i
  ```  
> **Note:** If you are using mintty, try prefixing the command with `winpty`


<br>

### Java version verification:  
  ```bash
  java -version
  ```  
  Expected output: Java version 21 or later.

---

## Step-by-Step Instructions

### Running Experiments

Execute the experiment:  
Inside the Docker shell, run:  
```bash
java -cp ./libs/learnlib-distribution-0.16.0-dependencies-bundle.jar:./libs/opencsv-5.6.jar:./libs/slf4j-jdk14-1.7.36.jar:./libs/commons-cli-1.4.jar:scl-star.jar main/Experiment
``` 

### Tips for Running the Program:
#### 1. Choosing Equivalence Query:
```bash
Choose Equivalence Query (rndWords recommended): [wp, w, wrnd, rndWords, rndWordsBig, rndWalk]
```
This option determines how the counterexamples are found in LearnLib. For our work, we used `rndWords`. You can enter `rndWords` for this part.

#### 2. Final Check Mode:
```bash
Enable Final Check Mode (disabled recommended): [true/false]
```
We used `false` for the paper, so you can set it to `false` as well.

#### 3. Enter Number of Repetitions:
```bash
Enter Number of Repetitions (3 recommended):
```
We used 3 repetitions in our work. You can use 3, or if you want to reduce the running time, you may set it to 1.

#### 4. Test Type:
```bash
Enter Test Type [Real, P2P, Ring, Star, Bus, Bipartite, Mesh]:
```
Each run generates data points for a single test type, and the data is saved in the following path:  
`/Results/Parameters/`

- For example, to generate data for **Real Tests** (the related figure will be similar to figures 4 and 5 in the paper), choose `Real` for the test type. The generated data will be saved at:  
  `/Results/Parameters/Real Tests/Results.csv`

- For other test types like `P2P`, `Ring`, `Star`, or `Bipartite`, run the program for each type individually. For instance, data for the `Ring` test type will be located at:  
  `/Results/Parameters/Generated Tests/Ring/Results.csv`.  
Once you've generated data for all the desired test types, you'll have enough information to create figures similar to figures 7 and 8 in the paper(discussed in the "Analyzing Results" section on plotting the figures).

I should mention that you can skip running the Mesh and Bus test types.  

#### 5. Minimum Number of States:
```bash
Enter Minimum Number of States (100 recommended)
```
This is the minimum number of states allowed in the data. We used 100, so you can use this value as well.

#### 6. Maximum Number of States:
```bash
Enter Maximum Number of States (30000 recommended)
```
This is the maximum number of states allowed in the data. We used 30,000, but generating tests with such a high number of states may take too long. To reduce the running time, you can set this value to 1,000.

#### 7. Number of Tests for Each Component-Number:
```bash
Enter Number of Tests for each component-number
```
This specifies the number of tests to be run for each "component-number." We used 10, so you can use the same value.

#### 8. Minimum Number of Components:
```bash
Enter Minimum Number of Components (3 recommended)
```
This is the minimum number of components allowed in the data. We used 3, so you can use this value as well.

#### 9. Maximum Number of Components:
```bash
Enter Maximum Number of Components (7 recommended for Bipartite and Mesh, and 9 for others)
```
This is the maximum number of components allowed in the data. We used the following values:  

- **9** for all test types except Bipartite and Mesh.  
- **7** for Bipartite and Mesh.  

You can use these values as well. However, to reduce the processing time:  
- You can set the value to **7** for all test types.  
- For Bipartite and Mesh specifically, you can reduce it further to **5**.

I should mention again that you can skip running the Mesh and Bus test.  

--- 

> **Note:** 
If the learning process takes too long (e.g., if the learning round number in the terminal exceeds 1000), save `Results.csv`, restart the experiment, and merge new data later:  
> - Place the previous `Results.csv` as `1.csv` in the `Merging Tool` folder.  
> - Place the new `Results.csv` as `2.csv`.  
> - Run `Merge Results.ipynb` to generate a merged `Results.csv`.  
> - Move the merged file to `Results/Parameters/Test-Type-That-YouChose`.

### Test types:  
- **Real Tests:** Automatically selected via `ChooseTests.py` (in `src/test/Real Tests/`).  
- **Generated Tests:** Created using `GenerateTests.py` (in `src/test/Generated Tests/`).  

### Analyzing results:  
- Use `ResultsHandler.ipynb` in Jupyter Notebook for:  
  - Statistical analysis (e.g., normality checks, t-tests).  
  - Plots(figure 4, 5, 7, 8 in the paper).  

  At the beginning of the "Plots" section in the notebook, we provide figures for each individual test type (e.g., Figures 4 and 5 in the paper for Real Tests) showing the number of symbols and resets.  
  Following that, we include figures for merged test types (e.g., Figures 7 and 8 in the paper for [P2P, Ring, Star, Bipartite]) also displaying the number of symbols and resets.

- Install Jupyter and dependencies via the [Jupyter installation guide](https://jupyter.org/install) and `requirements.txt`.

### Used Data
- Data for plots in the paper is in `Final Plots Data` folder.  

### Statistical data:  
- The `Synchs Infos` folder contains synchronization analysis.


## Exit the container:  
To close the Docker shell:  
```bash
exit
```

---

## Claims Supported

- Differences in cost and efficiency between L* and SCL*.  
- Performance evaluation under various conditions.  
- Significance testing and data visualization.

---

## Claims Not Supported

- FSMs with larger state spaces or more complex topologies are not tested.

---

## Additional Notes

- Save `Results.csv` and restart if experiments run too long.  
- Runtime varies based on test configurations.  
- Ensure directory mounting permissions are granted.  
- On Windows, `winpty` may be required before Docker commands.  