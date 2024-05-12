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
        self.transitions = [dict()] * numOfStates
        self.graph = ''
        self.causeOfNotMin = ''
    
    def refactorGraph(self):
        shouldChangeState = random.randint(0, self.numOfStates - 1)
        
        destState = random.randint(0, self.numOfStates - 2)
        if(destState >= shouldChangeState):
            destState += 1
            
        preOut = self.transitions[shouldChangeState][self.causeOfNotMin][1]
        self.transitions[shouldChangeState][self.causeOfNotMin] = (destState, preOut)
    
    def didActJustLoop(self, act):
        for state in range(self.numOfStates):
            if(self.transitions[state][act][0] != state):
                return(False)
        return(True)
    
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
        
    def generateAll(self, isForTransitions):
        for stateNum in range(self.numOfStates):
            for synchNum in range(len(self.synchActions)):
                if(isForTransitions):
                    self.generateTransition(stateNum, self.synchActions[synchNum], self.synchOuts[synchNum]) 
                else:
                    self.generateLine(stateNum, self.synchActions[synchNum], self.synchOuts[synchNum])
            for unsynchNum in range(len(self.unsynchActs)):
                if(isForTransitions):
                    self.generateTransition(stateNum, self.unsynchActs[unsynchNum], random.randint(0, 1))
                else:
                    self.generateLine(stateNum, self.unsynchActs[unsynchNum], random.randint(0, 1))
    
    def generate(self):
        self.generateAll(isForTransitions = True)
        
        while (True):
            if not self.isGraphMinimum():       
                self.refactorGraph()
            else:
                break
            
        self.generateGraphStr()
        return(self.graph)

    def generateTransition(self, stateNum, action, actionOut):
        self.transitions[stateNum][action] = [random.randint(0, self.numOfStates - 1), actionOut]
            
    def generateLine(self, stateNum, action, actionOut):
        self.graph += 's' + str(stateNum) + ' -> '
        self.graph += 's' + str(random.randint(0, self.numOfStates - 1))
        self.graph += ' [label="' + action + '  /  ' + str(actionOut) + '"];\n'
        
    def generateGraphStr(self):
        self.generateAll(isForTransitions = False)


            