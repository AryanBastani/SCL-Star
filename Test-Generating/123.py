import string
import random
from typing import Final
import pydot



class GenerateTest:
    def __init__(self):
        self.alphabets = string.ascii_letters
        self.numOfUnsynchActs: Final[int] = 2
        self.minStates: Final[int] = 5
        self.maxStates: Final[int] = 9
        self.componentCounter = 0

    def generateSynchComponents(self, synchActions, numOfComponents):
        synchOuts = list()
        for synchAct in synchActions:
            synchOuts.append(random.randint(0,1))
        for i in range(numOfComponents):
            unsynchActs = self.generateUnsynchActs(synchActions, synchOuts)
            self.generateComponent(synchActions, synchOuts, unsynchActs)
        
        
    def generateUnsynchActs(self, synchActions, synchOuts):    
        unsynchActs = list()
        for i in range(self.numOfUnsynchActs):
            newAct = self.alphabets[random.randint(0, len(self.alphabets) - 1)]
            unsynchActs.append(newAct)
            self.alphabets.remove(newAct)
        
        return(unsynchActs)

    def generateComponent(self, synchActions, synchOuts, unsynchActs):
        componentCounter += 1
        numOfStates = random.randint(self.minStates, self.maxStates)
        graph = ''
        for stateNum in range(numOfStates):
            for synchNum in range(len(synchActions)):
            # generateLine(graph, stateNum, synchActions[synchNum], synchOuts[synchOuts]) 

            
    #def generateLine(graph, stateNum, action, synchOut):
        # graph += 's' + string(stateNum) + ' -> s' + random.randint()
            #pydot.Edge
            #graph.add_edge(pydot.Edge(node_a, node_b))

            # Save the source as a DOT file
            #graph.write_raw("my_graph.dot")
    

         
    
    
