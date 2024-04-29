import string
import random
from typing import Final

alphabets = string.ascii_letters
numOfUnsynchActs: Final[int] = 2

def generateSyncheds(synchActions, numOfComponents):
    synchOuts = list()
    for synchAct in synchActions:
        synchOuts.append(random.randint(0,1))
    for i in range(numOfComponents):
        generateComponent(synchActions, synchOuts)
        
def generateComponent(synchActions, synchOuts):
    alphabets =  [i for i in all if i not in synchActions]
    unsynchActs = list()
    for i in range(numOfUnsynchActs):
        newAct = alphabets[random.randint(0, len(alphabets) - 1)]
        unsynchActs.append(newAct)
        alphabets.remove(newAct)
    
         
    
    
