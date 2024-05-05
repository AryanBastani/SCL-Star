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
        self.numOfEachActs: Final[int] = 1
        self.minStates: Final[int] = 5
        self.maxStates: Final[int] = 9
        self.minComponents: Final[int] = 4
        self.maxComponents: Final[int] = 9
        self.componentCounter = 0
        self.experimentInput = ''
        
        self.POINT_TO_POINT: Final[string] = 'Point-To-Point'
        self.MESH: Final[string] = 'Mesh'
        self.STAR: Final[string] = 'Star'
        self.RING: Final[string] = 'Ring'
        self.TREE: Final[string] = 'Tree'
        self.BUS: Final[string] = 'Bus'
        self.HYBRID: Final[string] = 'Hybrid'
        
        self.TYPES: Final[list] = [self.POINT_TO_POINT, self.MESH,
                                   self.STAR, self.RING, self.TREE,
                                   self.BUS, self.HYBRID]
        self.TYPESFUNCS: Final[list] = [self.generatePointTPoint]
        
    def generateSynchComponents(self, synchActions, synchOuts, numOfComponents, type):
            
        for i in range(numOfComponents):
            self.componentCounter += 1
            unsynchActs = self.generateActs()
            numOfStates = random.randint(self.minStates, self.maxStates)
            
            componentGenerator = gc.ComponentGenerator(synchActions, synchOuts, unsynchActs, numOfStates)
            graphString = componentGenerator.generate()

            currentFile = 'resources/Generated/' + type + \
                '/Component' + str(self.componentCounter) + '.dot'
            self.writeIntoFile(currentFile, graphString)
            
            self.experimentInput += currentFile + '\n'
        
        self.writeIntoFile('data/Generated/' + type + '.txt', self.experimentInput)
                
    def writeIntoFile(self, file, content):
        with open(file, 'w') as writingfile:
            writingfile.write(content) 
            writingfile.close()   

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
            outSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)]
            self.generateSynchComponents(synchActs, outSynchs, 2, self.POINT_TO_POINT)
            
    def generateMesh(self):
        numOfComponents = random.randint(self.minComponents, self.maxComponents)
        synchsActs = [0] * numOfComponents 
        outSynchs = [0] * numOfComponents 
        for i in range(numOfComponents):
            synchsActs[i] = [0] * ((numOfComponents - 1) * self.numOfEachActs)
            outSynchs[i] = [0] * ((numOfComponents - 1) * self.numOfEachActs)
        
        for component in range(numOfComponents):
            for nextComps in range(component + 1 , numOfComponents):
                currentSynchs = self.generateActs()
                currentOutSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)]
            
                for synchNum in range(len(currentSynchs)):
                    synchsActs[component][((nextComps-1)*self.numOfEachActs) + synchNum] = currentSynchs[synchNum]
                    outSynchs[component][((nextComps-1)*self.numOfEachActs) + synchNum] = currentOutSynchs[synchNum]
                    
                    synchsActs[nextComps][(component*self.numOfEachActs) + synchNum] = currentSynchs[synchNum]
                    outSynchs[nextComps][(component*self.numOfEachActs) + synchNum] = currentOutSynchs[synchNum]
            self.generateSynchComponents(synchsActs[component], outSynchs[component], 1, self.MESH)
            
    def generateStar(self):
        numOfComponents = random.randint(self.minComponents, self.maxComponents)
        centerSynchsActs = []
        centerOutSynchs = []
        for component in range(numOfComponents - 1):
            currentSynchs = self.generateActs()
            currentOutSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)] 
            for synchNum in range(len(currentSynchs)):
                centerSynchsActs.append(currentSynchs[synchNum])
                centerOutSynchs.append(currentOutSynchs[synchNum])
            
            self.generateSynchComponents(currentSynchs, currentOutSynchs, 1, self.STAR)
        self.generateSynchComponents(centerSynchsActs, centerOutSynchs, 1, self.STAR)
        
    def generateBus(self):
        numOfComponents = random.randint(self.minComponents, self.maxComponents)
        currentSynchs = self.generateActs()
        currentOutSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)] 
        self.generateSynchComponents(currentSynchs, currentOutSynchs, numOfComponents, self.BUS)
    
    def generateRing(self):
        numOfComponents = random.randint(self.minComponents, self.maxComponents)
        synchsActs = [0] * numOfComponents 
        outSynchs = [0] * numOfComponents 
        for i in range(numOfComponents):
            synchsActs[i] = [0] * (2 * self.numOfEachActs)
            outSynchs[i] = [0] * (2 * self.numOfEachActs)
                
        for component in range(numOfComponents):
            currentSynchs = self.generateActs()
            currentOutSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)]
            for synchNum in range(len(currentSynchs)):
                if(component == (numOfComponents - 1)):
                    nextComp = 0
                else:
                    nextComp = component + 1
                synchsActs[component][self.numOfEachActs + synchNum] = currentSynchs[synchNum]
                outSynchs[component][self.numOfEachActs + synchNum] = currentOutSynchs[synchNum]
                
                synchsActs[nextComp][synchNum] = currentSynchs[synchNum]
                outSynchs[nextComp][synchNum] = currentOutSynchs[synchNum]
        
        for component in range(numOfComponents):
            self.generateSynchComponents(synchsActs[component], outSynchs[component], 1, self.RING)
        
        
    def resetVars(self, type):
        self.clearFolder('resources/Generated/' + type)
        self.alphabets = [''.join(i) for i in product(ascii_lowercase, repeat = 3)]
        self.experimentInput = ''
        self.componentCounter = 0  
            
    def generateAllTests(self):
        self.resetVars(self.POINT_TO_POINT)
        self.generatePointTPoint()
        
        self.resetVars(self.MESH)
        self.generateMesh()
        
        self.resetVars(self.STAR)
        self.generateStar()
        
        self.resetVars(self.BUS)
        self.generateBus()
        
        self.resetVars(self.RING)
        self.generateRing()
        
        
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
    

         
    
    
