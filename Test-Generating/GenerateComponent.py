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
        self.statesInputFlow = [0] * numOfStates
    
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
    
    def satisfyFlowForActs(self, acts, stateId):
        for currentState in range(self.numOfStates):
            for act in acts:
                if(self.transitions[currentState][act][0] == stateId and currentState != stateId):
                    self.statesInputFlow[stateId] += 1
    
    def satisfyInputFlow(self, stateId):
        self.statesInputFlow[stateId] = 0
        self.satisfyFlowForActs(self.unsynchActs, stateId)
        self.satisfyFlowForActs(self.synchActions, stateId)

    
    def isEveryStateReachable(self):
        for state in range(self.numOfStates):
            self.satisfyInputFlow(state)

        if(0 in self.statesInputFlow):
            return(False)
        else:
            return(True)
        
    def findEnoughReachableState(self, sourceState, acts):
        for act in acts:
            sinkState = self.transitions[sourceState][act][0]
            if(sourceState == sinkState):
                return(sinkState, act)
            elif(self.statesInputFlow[sinkState] > 1):
                return(sinkState, act)
        return(-1, -1)

        
    def makeReachable(self, stateID):
        sourceState = random.randint(0, self.numOfStates - 1)
        enoughReachableState = -1
        actToEnoughState = -1
        while(True):
            while(self.statesInputFlow[sourceState] == 0):
                sourceState = random.randint(0, self.numOfStates - 1)

            enoughReachableState, actToEnoughState = self.findEnoughReachableState(sourceState, self.synchActions)
            if(enoughReachableState != -1):
                break
            enoughReachableState, actToEnoughState = self.findEnoughReachableState(sourceState, self.unsynchActs)
            if(enoughReachableState != -1):
                break
            
        self.statesInputFlow[enoughReachableState] -= 1
        self.transitions[sourceState][actToEnoughState][0] = stateID
        self.statesInputFlow[stateID] += 1
                

    def makeStatesReachable(self):
        for stateId in range(self.numOfStates):
            if(self.statesInputFlow[stateId] == 0):
                self.makeReachable(stateId)
        
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
        
        while ((not self.isGraphMinimum()) or (not self.isEveryStateReachable())):
            while(not self.isGraphMinimum()):       
                self.refactorGraph()
            while(not self.isEveryStateReachable()):
                self.makeStatesReachable()
            
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


            