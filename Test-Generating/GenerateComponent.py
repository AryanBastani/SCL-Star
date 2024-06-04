import string
import random
from typing import Final
from decimal import Decimal
import pydot

class ComponentGenerator:
    def __init__(self, synchActions, synchOuts, unsynchActs, numOfStates):
        self.synchActions: Final[list] = synchActions
        self.synchOuts: Final[list] = synchOuts
        self.unsynchActs: Final[list] = unsynchActs
        self.numOfStates: Final[int] = numOfStates
        self.IsMinimum: Final[int] = 1
        self.IsNotMin: Final[int] = 0
        self.transitions = [dict() for i in range(numOfStates)]
        self.graph = ''
        self.causeOfNotMin = ''
        self.isReachable = [False] * numOfStates

    def expandBfsQueue(self, actions, u, queue):
        for act in actions:
            v = self.transitions[u][act][0]
            if(not self.isReachable[v]):
                self.isReachable[v] = True
                queue.append(v)

    def bfs(self, startState):
        queue = []

        self.isReachable[startState] = True
        queue.append(startState)

        while(len(queue) != 0):
            u = queue.pop()
            
            self.expandBfsQueue(self.synchActions, u, queue)
            self.expandBfsQueue(self.unsynchActs, u, queue)

            
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
    
    
    def isEveryStateReachable(self):
        self.isReachable = [False] * self.numOfStates
        self.bfs(0)

        if(False in self.isReachable):
            return(False)
        else:
            return(True)
        
    def findEnoughReachableState(self, sourceState, acts, stateId):
        for act in acts:
            sinkState = self.transitions[sourceState][act][0]
            self.transitions[sourceState][act][0] = stateId
            numOfPrevReachables = self.isReachable.count(True)
            self.isReachable = [False] * self.numOfStates
            self.bfs(0)
            if(self.isReachable.count(True) > numOfPrevReachables):
                return(sinkState)
            self.transitions[sourceState][act][0] = sinkState
            self.bfs(0)
        return(-1)

        
    def makeReachable(self, stateID):
        for sourceState in range(self.numOfStates):
            if(not self.isReachable[sourceState]):
                continue
            sinkState = self.findEnoughReachableState(sourceState, self.synchActions, stateID)
            if(sinkState == -1):
                sinkState = self.findEnoughReachableState(sourceState, self.unsynchActs, stateID)
            if(sinkState != -1):
                break
        assert sinkState != -1
            
                

    def makeStatesReachable(self):
        for stateId in range(self.numOfStates):
            if(not self.isReachable[stateId]):
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
        while(not self.isEveryStateReachable()):
            self.generateAll(isForTransitions = True)
        while(not self.isGraphMinimum()):       
            self.refactorGraph()
        
        assert self.isEveryStateReachable()
            
        self.generateGraphStr()
        return(self.graph)

    def generateTransition(self, stateNum, action, actionOut):
        self.transitions[stateNum][action] = [random.randint(0, self.numOfStates - 1), actionOut]
            
    def generateLine(self, stateNum, action, actionOut):
        self.graph += 's' + str(stateNum) + ' -> '
        self.graph += 's' + str(self.transitions[stateNum][action][0])
        self.graph += ' [label="' + action + '  /  ' + str(actionOut) + '"];\n'
        
    def generateGraphStr(self):
        self.generateAll(isForTransitions = False)
            