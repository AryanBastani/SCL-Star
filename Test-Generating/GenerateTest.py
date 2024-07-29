import string
import random
from typing import Final
import GenerateComponent as gc
from itertools import product
from string import ascii_letters
import os, shutil

class GenerateTest:
    def __init__(self):
        self.alphabets = [''.join(i) for i in product(ascii_letters, repeat = 1)]
        self.numOfEachActs: Final[int] = 1
        self.minStates: Final[int] = 9
        self.maxStates: Final[int] = 11
        self.minComponents: Final[int] = 3
        self.maxComponents: Final[int] = 9
        self.numOfTests: Final[int] = 1000
        self.componentCounter = 0
        self.experimentInput = ''
        
        self.POINT_TO_POINT: Final[string] = 'Point-To-Point'
        self.MESH: Final[string] = 'Mesh'
        self.STAR: Final[string] = 'Star'
        self.RING: Final[string] = 'Ring'
        self.BUS: Final[string] = 'Bus'
        self.BIPARTITE: Final[string] = 'Bipartite'
        
        self.TYPES: Final[list] = [self.POINT_TO_POINT, self.MESH,
                                   self.STAR, self.RING, self.BUS, self.BIPARTITE]
        self.TYPE_FUNCS: Final[list] = [self.generatePointTPoint, self.generateMesh,
                                        self.generateStar, self.generateRing,
                                        self.generateBus, self.generateBipartite]
        
    def generateSynchComponents(self, synchActions, synchOuts, numOfComponents, type, testCounter, allComponentsCount):
        for i in range(numOfComponents):
            self.componentCounter += 1
            self.experimentInput += "Test-Generating/"
            unsynchActs = self.generateActs()
            if(type != self.MESH):
                numOfStates = random.randint(2, 5)
            else:
                numOfStates = random.randint(3, 6)
                
            
            componentGenerator = gc.ComponentGenerator(synchActions, synchOuts, unsynchActs, numOfStates)
            graphString = componentGenerator.generate()

            currentFile = 'resources/' + type + '/' + str(testCounter) +\
                '/Component' + str(self.componentCounter) + '.dot'
            self.writeIntoFile(currentFile, graphString)
            
            self.experimentInput += currentFile + '\n'
        
        self.writeIntoFile('data/' + type + '/' + str(testCounter) + '.txt', self.experimentInput)
                
    def writeIntoFile(self, file, content, writingType = 'w'):
        with open(file, writingType) as writingfile:
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
    
    def writeTheInput(self, testCounter, type):
        self.writeIntoFile('data/' + type + '-All-Tests.txt',\
            'Test-Generating/data/' + type + '/' + str(testCounter) + '.txt' + '\n', 'a')   
    
    def generatePointTPoint(self, testCounter):
        self.writeTheInput(testCounter, self.POINT_TO_POINT)

        numOfComponents = random.randint(self.minComponents, self.maxComponents)
        
        for twoComponents in range(0, numOfComponents - 1, 2):
            synchActs = self.generateActs()
            outSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)]
            self.generateSynchComponents(synchActs, outSynchs, 2, self.POINT_TO_POINT, testCounter, numOfComponents)
        
        if numOfComponents % 2 == 1:
            self.generateSynchComponents([], [], 1, self.POINT_TO_POINT, testCounter, numOfComponents)
            
    def generateMesh(self, testCounter):
        self.writeTheInput(testCounter, self.MESH)
        
        numOfComponents = random.randint(self.minComponents, self.maxComponents - 2)
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
            self.generateSynchComponents(synchsActs[component], outSynchs[component], 1, self.MESH, testCounter, numOfComponents)
            
    def generateStar(self, testCounter):
        self.writeTheInput(testCounter, self.STAR)
        
        numOfComponents = random.randint(self.minComponents, self.maxComponents)
        centerSynchsActs = []
        centerOutSynchs = []
        for component in range(numOfComponents - 1):
            currentSynchs = self.generateActs()
            currentOutSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)] 
            for synchNum in range(len(currentSynchs)):
                centerSynchsActs.append(currentSynchs[synchNum])
                centerOutSynchs.append(currentOutSynchs[synchNum])
            
            self.generateSynchComponents(currentSynchs, currentOutSynchs, 1, self.STAR, testCounter, numOfComponents)
        self.generateSynchComponents(centerSynchsActs, centerOutSynchs, 1, self.STAR, testCounter, numOfComponents)
        
    def generateBus(self, testCounter):
        self.writeTheInput(testCounter, self.BUS)
        
        numOfComponents = random.randint(self.minComponents, self.maxComponents)
        currentSynchs = self.generateActs()
        currentOutSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)] 
        self.generateSynchComponents(currentSynchs, currentOutSynchs, numOfComponents, self.BUS, testCounter, numOfComponents)
    
    def generateRing(self, testCounter):
        self.writeTheInput(testCounter, self.RING)
        
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
            self.generateSynchComponents(synchsActs[component], outSynchs[component], 1, self.RING, testCounter, numOfComponents)
            
    def generateBipartite(self, testCounter):
        self.writeTheInput(testCounter, self.BIPARTITE)
        
        possibleNumbers = [2, 4, 6, 8]
        numOfComponents = random.choice(possibleNumbers)
        synchsActs = [0] * numOfComponents 
        outSynchs = [0] * numOfComponents 
        for i in range(numOfComponents):
            synchsActs[i] = [0] * (int(numOfComponents / 2) * self.numOfEachActs)
            outSynchs[i] = [0] * (int(numOfComponents / 2) * self.numOfEachActs)
        
        for component in range(numOfComponents):
            if component < numOfComponents/2:
                for part2Comp in range(int(numOfComponents/2) , numOfComponents):
                    currentSynchs = self.generateActs()
                    currentOutSynchs = [random.randint(0, 1) for i in range(self.numOfEachActs)]
                
                    for synchNum in range(len(currentSynchs)):
                        synchsActs[component][((part2Comp-int(numOfComponents/2))*self.numOfEachActs) +\
                                            synchNum] = currentSynchs[synchNum]
                        outSynchs[component][((part2Comp-int(numOfComponents/2))*self.numOfEachActs) +\
                            synchNum] = currentOutSynchs[synchNum]
                        
                        synchsActs[part2Comp][(component*self.numOfEachActs) + synchNum] = currentSynchs[synchNum]
                        outSynchs[part2Comp][(component*self.numOfEachActs) + synchNum] = currentOutSynchs[synchNum]
            self.generateSynchComponents(synchsActs[component], outSynchs[component], 1, self.BIPARTITE, testCounter, numOfComponents)
        
    def resetVars(self, type, testCounter):
        self.clearFolder('resources/' + type + '/' + str(testCounter))
        self.alphabets = [''.join(i) for i in product(ascii_letters, repeat = 1)]
        self.experimentInput = ''
        self.componentCounter = 0  
            
    def generateAllTests(self):
        for i in range(self.numOfTests):
            for j in range(len(self.TYPES)):
                self.resetVars(self.TYPES[j], i + 1)
                self.TYPE_FUNCS[j](i + 1)
                
    def deletePrevTests(self):
        targetFolders = ['resources', 'data']
        for target in targetFolders:
            self.clearFolder(target)
            for CurrentType in self.TYPES:
                os.makedirs(target + '/' + CurrentType)
        
        
        
    def clearFolder(self, folder):
        if not os.path.isdir(folder):
            os.makedirs(folder)
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
gt.deletePrevTests()
gt.generateAllTests()   
    
