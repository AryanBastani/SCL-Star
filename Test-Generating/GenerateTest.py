import string
import random
from typing import Final
import pydot
import GenerateComponent as gc
from itertools import product
from string import ascii_lowercase
import os, shutil

class GenerateTest:
    def __init__(self):
        self.alphabets = [''.join(i) for i in product(ascii_lowercase, repeat = 3)]
        self.numOfEachActs: Final[int] = 2
        self.minStates: Final[int] = 5
        self.maxStates: Final[int] = 9
        self.componentCounter = 0
        
        self.POINT_TO_POINT: Final[string] = 'Point-To-Point'
        self.MESH: Final[string] = 'Mesh'
        self.STAR: Final[string] = 'Star'
        self.RING: Final[string] = 'Ring'
        self.TREE: Final[string] = 'Tree'
        self.BUS: Final[string] = 'Bus'
        self.HYBRID: Final[string] = 'Hybrid'
        
    def generateSynchComponents(self, synchActions, numOfComponents, type):
        synchOuts = list()
        for synchAct in synchActions:
            synchOuts.append(random.randint(0,1))
            
        for i in range(numOfComponents):
            self.componentCounter += 1
            unsynchActs = self.generateActs()
            numOfStates = random.randint(self.minStates, self.maxStates)
            
            componentGenerator = gc.ComponentGenerator(synchActions, synchOuts, unsynchActs, numOfStates)
            graphString = componentGenerator.generate()
            
            currentFolder = 'resources/Generated/' + type
            with open(currentFolder + '/Component' + str(self.componentCounter) + '.dot', 'w') as dotFile:
                dotFile.write(graphString) 
                dotFile.close()

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
        for twoComponents in range(0, possibleNums[numOfComponents], 2):
            synchActs = self.generateActs()
            self.generateSynchComponents(synchActs, 2, self.POINT_TO_POINT)
     
    def resetVars(self, type):
        self.clearFolder('resources/Generated/' + type)
        self.alphabets = [''.join(i) for i in product(ascii_lowercase, repeat = 3)]
        self.componentCounter = 0  
            
    def generateAllTests(self):
        self.resetVars(self.POINT_TO_POINT)
        self.generatePointTPoint()
        
        
    def clearFolder(self, folder):
        for filename in os.listdir(folder):
            file_path = os.path.join(folder, filename)
            try:
                if os.path.isfile(file_path) or os.path.islink(file_path):
                    os.unlink(file_path)
                elif os.path.isdir(file_path):
                    shutil.rmtree(file_path)
            except Exception as e:
                print('Failed to delete %s. Reason: %s' % (file_path, e))
        
            
gt = GenerateTest()
gt.generateAllTests()
    

         
    
    
