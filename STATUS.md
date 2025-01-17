## STATUS File  

**Badge(s) Applied For:**  
- Available  
- Functional  

---

### Justification  

#### **Available**  
- The artifact is openly accessible on a public GitHub repository and Zenodo.  
- The repository includes all required materials to reproduce the results described in the paper, including source code, experiment scripts, pre-generated results, and analysis tools.  
- Although the repository does not have a DOI, it is publicly accessible and actively maintained, ensuring long-term availability.  
- Hosting on GitHub allows for version control and updates, providing transparency and traceability for the artifact's evolution.  

#### **Functional**  
- **Documentation:**  
  - The README file provides comprehensive, step-by-step instructions for installation, experiment execution, and result analysis.  
  - It includes detailed explanations of experimental parameters, such as equivalence query types, state ranges, and test types, ensuring that users can replicate and validate the experiments described in the paper.  

- **Completeness:**  
  - The artifact contains all necessary components, including a Dockerized environment for consistency, experiment scripts, statistical analysis tools, and visualization notebooks.  
  - Pre-generated results files (e.g., `Results.csv`) are included to facilitate quick validation of the claims without the need for re-running all experiments.  

- **Exercisability:**  
  - The artifact provides a seamless setup and execution process via Docker containers, ensuring compatibility across major platforms (Linux, macOS, and Windows with WSL).  
  - Users can execute experiments by following simple commands, with customizable parameters to replicate specific results or explore new configurations.  
  - The provided tools, such as `ResultsHandler.ipynb`, allow users to analyze and visualize results efficiently, producing figures comparable to those in the paper.  

- **Verification and Validation:**  
  - The experiments produce results consistent with those reported in the paper, supporting its claims about the differences in cost and efficiency between L* and SCL*.  
  - Statistical analysis scripts and pre-generated data validate key findings, such as performance trends across various FSM topologies and configurations.  

---

### Additional Notes  
- The artifact is well-documented and designed for ease of use, enabling users to focus on experimentation and analysis without technical barriers.  
- Known limitations, such as runtime constraints for larger FSMs, are transparently addressed in the README file, ensuring realistic expectations.  
- By providing a pre-configured environment and detailed instructions, the artifact significantly lowers the barrier to reproducibility and verification.  