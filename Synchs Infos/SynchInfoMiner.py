import os

# def decodeAct(phrase, sign):
#     signVisited = False
#     content = ""
#     for i in phrase:
#         if i != sign and signVisited:
#             content += i
#         elif i == sign and (not signVisited):
#             signVisited = True
#     return content
        
# def decodeOut(phrase, sign):
#     content = ""
#     for i in phrase:
#         if i != sign:
#             content += i
#         else:
#             return content
        
def getActAndOut(line):
    labelVisited = False
    slashVisited = False
    doubleQuote = False
    
    action = ''
    out = ''
    
    for i in range(len(line)):
        if (not labelVisited) and line[i : (i+5)] == "label":
            labelVisited = True
            i = i + 5
        elif labelVisited and line[i] == '/':
            slashVisited = True
        elif ((labelVisited and (not slashVisited)) and (not doubleQuote)) and line[i] == '"':
            doubleQuote = True
        elif ((labelVisited and (not slashVisited)) and doubleQuote) and line[i] != '/':
            action += line[i]
        elif ((labelVisited and (not slashVisited)) and doubleQuote) and line[i] == '/':
            slashVisited = True
        elif ((labelVisited and slashVisited) and doubleQuote) and line[i] != '"':
            out += line[i]
        elif ((labelVisited and slashVisited) and doubleQuote) and line[i] == '"':
            action = action.strip()
            out = out.strip()
            return action, out
        
def saveInfo(file, info):
    file.write(info + '\n')
    print(info)
    
        
    

actionsInfo = {}
actions = []
synchs = []
severalOutSynchs = []
severalOuts = []

currentComponentId = 0
synchRepeatCounter = 0

theDir = "experiment_models/"

inputDir = "Inputs/" + theDir
outDir = "Outputs/" + theDir
 
for file in os.listdir(inputDir):
    if file.endswith(".dot"):
        with open(os.path.join(inputDir, file), 'r') as file:
            currentComponentId += 1
            print("Reading the file: ", file)
            while True:
                line = file.readline()
                if not line:
                    break
                line = line.strip()
                if len(line) == 0:
                    continue;
                
                # if inputDir == "Complete_FSM_files/":
                #     if line[0] != 's':
                #         continue;
                    
                #     splittedLine = line.split()
                #     if len(splittedLine) != 6:
                #         continue;
                    
                #     currentAct = decodeAct(splittedLine[3], '"')
                #     currentOut = decodeOut(splittedLine[5], '"')
                    
                # elif inputDir == "experiment_models/":
                if not ("label" in line and "/" in line):
                    continue;
                
                currentAct, currentOut = getActAndOut(line)

                    
                if not(currentAct in actionsInfo):
                    actionsInfo[currentAct] = [currentComponentId, False, currentOut, False]
                    actions.append(currentAct)
                    
                elif (actionsInfo[currentAct][0] != currentComponentId) and actionsInfo[currentAct][1]:
                    synchRepeatCounter += 1
                    actionsInfo[currentAct][0] = currentComponentId
                    
                elif (actionsInfo[currentAct][0] != currentComponentId) and (not actionsInfo[currentAct][1]):
                    actionsInfo[currentAct][1] = True
                    synchRepeatCounter += 1
                    synchs.append(currentAct)
                    actionsInfo[currentAct][0] = currentComponentId
                    if actionsInfo[currentAct][3]:
                        severalOutSynchs.append(currentAct)
                
                elif (actionsInfo[currentAct][2] != currentOut) and (not actionsInfo[currentAct][3]):
                    actionsInfo[currentAct][3] = True
                    severalOuts.append(currentAct)
                    if actionsInfo[currentAct][1]:
                        severalOutSynchs.append(currentAct)

sameOutActs = [i for i in actions if i not in severalOuts]
sameOutSynchs = [i for i in synchs if i not in severalOutSynchs]

numOfSynchs =  len(synchs)
numOfseveralOutSynchs = len(severalOutSynchs)          
numOfActions = len(actions)
numOfSeveralOuts = len(severalOuts)

outFile = open(outDir + "Actions Info.txt", "w")
if numOfSynchs == 0:
    saveInfo(outFile, "Percentage of synch action with single output: " + "The machine has no synch actions")
else:
    saveInfo(outFile, "Percentage of synch action with single output: " + str(float(((numOfSynchs - numOfseveralOutSynchs) * 100) / numOfSynchs)))
saveInfo(outFile, "Percentage of Actions with single output: " + str(float(((numOfActions - numOfSeveralOuts) * 100) / numOfActions)))
saveInfo(outFile, '\n')

saveInfo(outFile, "Number of components: " + str(currentComponentId))
saveInfo(outFile, '\n')

saveInfo(outFile, "Number of actions: " + str(numOfActions))
saveInfo(outFile, "Actions: " + str(actions))
saveInfo(outFile, "Number of actions with single output: " + str(len(sameOutActs)))
saveInfo(outFile, "Actions with single output: " + str(sameOutActs))
saveInfo(outFile, '\n')
saveInfo(outFile, "Number of synch actions: " + str(numOfSynchs))
saveInfo(outFile, "Synch actions: " + str(synchs))
saveInfo(outFile, "Number of synch actions with single output: " + str(len(sameOutSynchs)))
saveInfo(outFile, "Synch actions with single output: " + str(sameOutSynchs))

outFile.close()
