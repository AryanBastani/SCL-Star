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
        self.numOfUnsynchActs: Final[int] = 2
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
            unsynchActs = self.generateUnsynchActs(synchActions, synchOuts)
            numOfStates = random.randint(self.minStates, self.maxStates)
            
            componentGenerator = gc.ComponentGenerator(synchActions, synchOuts, unsynchActs, numOfStates)
            graphString = componentGenerator.generate()
            with open('Test-Generating/Component' + str(self.componentCounter) + '.dot', 'w') as dotFile:
                dotFile.write(graphString)
                dotFile.close()
            print(graphString)

        
    def generateUnsynchActs(self, synchActions, synchOuts):    
        unsynchActs = list()
        for i in range(self.numOfUnsynchActs):
            newAct = self.alphabets[random.randint(0, len(self.alphabets) - 1)]
            unsynchActs.append(newAct)
            self.alphabets.remove(newAct)
        
        return(unsynchActs)
    
gt = GenerateTest()
gt.generateSynchComponents(['asd', 'ads'], 2)
    

         
    
    
