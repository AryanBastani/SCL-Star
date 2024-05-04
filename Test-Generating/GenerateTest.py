import string
import random
from typing import Final
import pydot
import GenerateComponent as gc
from itertools import product
from string import ascii_lowercase

class GenerateTest:
    def __init__(self):
        self.alphabets = [''.join(i) for i in product(ascii_lowercase, repeat = 3)]
        self.numOfEachActs: Final[int] = 2
        self.minStates: Final[int] = 5
        self.maxStates: Final[int] = 9
        self.componentCounter = 0

    def generateSynchComponents(self, synchActions, numOfComponents):
        synchOuts = list()
        for synchAct in synchActions:
            self.alphabets.remove(synchAct)
            synchOuts.append(random.randint(0,1))
            
        for i in range(numOfComponents):
            self.componentCounter += 1
            unsynchActs = self.generateActs()
            numOfStates = random.randint(self.minStates, self.maxStates)
            
            componentGenerator = gc.ComponentGenerator(synchActions, synchOuts, unsynchActs, numOfStates)
            graphString = componentGenerator.generate()
            with open('Test-Generating/Component' + str(self.componentCounter) + '.dot', 'w') as dotFile:
                dotFile.write(graphString)
                dotFile.close()
            print(graphString)

    def generateAct(self):
        newAct = self.alphabets[random.randint(0, len(self.alphabets) - 1)]
        self.alphabets.remove(newAct)
        return(newAct)
        
    def generateActs(self):    
        acts = list()
        for i in range(self.numOfEachActs):
            acts.append(self.generateAct())
        
        return(acts)
    
    def generatePointTPoint(self):
        possibleNums = [4, 6, 8]
        numOfComponents = random.randint(0, 2)
        for twoComponents in range(0, possibleNums, 2):
            synchActs = self.generateActs()
            
            
            
        
    
gt = GenerateTest()
gt.generateSynchComponents(['asd', 'ads'], 2)
    

         
    
    
