import string
import random
from typing import Final
import pydot

class GenerateComponent:
    def __init__(self, synchActions, synchOuts, unsynchActs, numOfStates):
        self.synchActions: Final[list] = synchActions
        self.synchOuts: Final[list] = synchOuts
        self.unsynchActs: Final[list] = unsynchActs
        self.numOfStates: Final[int] = numOfStates
        self.graph = ''
        self.generate()
        return(self.graph)
    
    
    def generate(self):
        numOfStates = random.randint(self.minStates, self.maxStates)
        graph = ''
        for stateNum in range(numOfStates):
            for synchNum in range(len(self.synchActions)):
                self.generateLine(stateNum, self.synchActions[synchNum], self.synchOuts[synchNum]) 
            for unsynchNum in range(len(self.unsynchActs)):
                self.generateLine(stateNum, self.unsynchActs[synchNum], string(random.randint(0, 1)))

            
    def generateLine(self, stateNum, action, actionOut):
        self.graph += 's' + string(stateNum) + ' -> '
        self.graph += 's' + string(random.randint(0, self.numOfStates - 1))
        self.graph += ' [label="' + action + '  /  ' + actionOut + '"];\n'

            