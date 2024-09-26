import nbformat
from nbclient import NotebookClient

# Load the notebook file
with open("src/main/ResultsHandler/ResultsHandler.ipynb") as f:
    nb = nbformat.read(f, as_version=4)

# Execute the notebook
client = NotebookClient(nb)
client.execute()