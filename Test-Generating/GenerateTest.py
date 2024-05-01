import string
import random
from typing import Final
import pydot
import GenerateComponent


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
            numOfStates = random.randint(self.minStates, self.maxStates)
            graphString = GenerateComponent(synchActions, synchOuts, unsynchActs, numOfStates)
        
        
    def generateUnsynchActs(self, synchActions, synchOuts):    
        unsynchActs = list()
        for i in range(self.numOfUnsynchActs):
            newAct = self.alphabets[random.randint(0, len(self.alphabets) - 1)]
            unsynchActs.append(newAct)
            self.alphabets.remove(newAct)
        
        return(unsynchActs)
    

         
    
    
