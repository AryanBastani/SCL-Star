import os

def decodeAct(phrase, sign):
    signVisited = False
    content = ""
    for i in phrase:
        if i != sign and signVisited:
            content += i
        elif i == sign and (not signVisited):
            signVisited = True
    return content
        
def decodeOut(phrase, sign):
    content = ""
    for i in phrase:
        if i != sign:
            content += i
        else:
            return content
    

actions = {}
synchsCounter = 0
synchs = []
differentOutCounter = 0
differentOuts = []

currentComponentId = 0

dir = "Complete_FSM_files/"
 
for file in os.listdir(dir):
    if file.endswith(".dot"):
        with open(os.path.join(dir, file), 'r') as file:
            currentComponentId += 1
            while True:
                line = file.readline()
                if not line:
                    break
                line = line.strip()
                if len(line) == 0 or line[0] != 's':
                    continue;
                
                splittedLine = line.split()
                if len(splittedLine) != 6:
                    continue;
                currentAct = decodeAct(splittedLine[3], '"')
                currentOut = decodeOut(splittedLine[5], '"')
                
                if not(currentAct in actions):
                    actions[currentAct] = [currentComponentId, False, currentOut, False]
                    
                elif (actions[currentAct][0] != currentComponentId) and (not actions[currentAct][1]):
                    actions[currentAct][1] = True
                    synchsCounter += 1
                    synchs.append(currentAct)
                    if actions[currentAct][3]:
                        differentOutCounter += 1
                        differentOuts.append(currentAct)
                
                elif (actions[currentAct][2] != currentOut) and (not actions[currentAct][3]):
                    actions[currentAct][3] = True
                    if actions[currentAct][1]:
                        differentOutCounter += 1
                        differentOuts.append(currentAct)
                        
print(float((synchsCounter - differentOutCounter) / (synchsCounter * 100)))
                    