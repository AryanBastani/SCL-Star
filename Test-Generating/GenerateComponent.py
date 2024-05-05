import string
import random
from typing import Final
import pydot

class ComponentGenerator:
    def __init__(self, synchActions, synchOuts, unsynchActs, numOfStates):
        self.synchActions: Final[list] = synchActions
        self.synchOuts: Final[list] = synchOuts
        self.unsynchActs: Final[list] = unsynchActs
        self.numOfStates: Final[int] = numOfStates
        self.IsMinimum: Final[int] = 1
        self.IsNotMin: Final[int] = 0
        self.transitions = list(dict())
        self.graph = ''
        self.causeOfNotMin = ''
    
    def refactorGraph(self):
        return
    
    def didActJustLoop(self, act):
        
    
    def checkMinForActs(self, acts):
        for act in acts:
            if(self.didActJustLoop(act)):
                self.causeOfNotMin = act
                return(False)
        return(True)
            
    def isGraphMinimum(self):
        if(not self.checkMinForActs(self.unsynchActs)):
            return(False)
        if(not self.checkMinForActs(self.synchActions)):
            return(False)
        
        return(True)
    
    def generate(self):
        for stateNum in range(self.numOfStates):
            for synchNum in range(len(self.synchActions)):
                self.generateLine(stateNum, self.synchActions[synchNum], self.synchOuts[synchNum]) 
            for unsynchNum in range(len(self.unsynchActs)):
                self.generateLine(stateNum, self.unsynchActs[unsynchNum], random.randint(0, 1))
                
        if not self.isGraphMinimum():       
            self.refactorGraph()
            
        return(self.graph)

            
    def generateLine(self, stateNum, action, actionOut):
        self.graph += 's' + str(stateNum) + ' -> '
        self.graph += 's' + str(random.randint(0, self.numOfStates - 1))
        self.graph += ' [label="' + action + '  /  ' + str(actionOut) + '"];\n'

            