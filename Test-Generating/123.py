import string
import random
from typing import Final

alphabets = string.ascii_letters
numOfUnsynchActs: Final[int] = 2
minStates: Final[int] = 5
maxStates: Final[int] = 9

def generateSynchComponents(synchActions, numOfComponents):
    synchOuts = list()
    for synchAct in synchActions:
        synchOuts.append(random.randint(0,1))
    for i in range(numOfComponents):
        unsynchActs = generateUnsynchActs(synchActions, synchOuts)
        generateComponent(synchActions, synchOuts, unsynchActs)
        
        
def generateUnsynchActs(synchActions, synchOuts):    
    unsynchActs = list()
    for i in range(numOfUnsynchActs):
        newAct = alphabets[random.randint(0, len(alphabets) - 1)]
        unsynchActs.append(newAct)
        alphabets.remove(newAct)
    
    return(unsynchActs)

def generateComponent(synchActions, synchOuts, unsynchActs):
    numOfStates = random.randint(minStates, maxStates)
    for state in range(numOfStates):
        
         
    
    
