import string
import random
from typing import Final
import pydot
from itertools import product
from string import ascii_lowercase
import os, shutil

COMPONENTS = [
                 "4_1_BCS_MPW.dot", "4_2_BCS_APW.dot",
                 "4_3_BCS_FP.dot", "4_5_BCS_LED_FP.dot",
                 "4_6_BCS_CLS.dot", "4_7_BCS_EM.dot",
                 "4_9_BCS_LED_CLS.dot", "4_10_BCS_LED_MPW.dot",
                 "4_11_BCS_LED_APW.dot", "4_12_BCS_LED_EMT.dot",
                 "4_13_MCS_LED_EML.dot", "4_14_BCS_LED_EMB.dot",
                 "4_15_BCS_LED_EMR.dot", "4_16_BCS_LED_EMH.dot",
                 "4_17_BCS_LED_AS_Active.dot", "4_18_BCS_LED_AS_Alarm.dot",
                 "4_19_BCS_LED_AS_Alarm_D.dot", "4_20_MCS_LED_AS_IMA.dot",
                 "4_21_BCS_AS.dot", "BCS_PW_4.dot"
             ]
SYNCHS = [
            [
                "4_1_BCS_MPW.dot", "4_2_BCS_APW.dot",
                "4_3_BCS_FP.dot", "BCS_PW_4.dot"
            ]
            ,
            [
                "4_6_BCS_CLS.dot", "4_21_BCS_AS.dot"
            ]
            ,
            [
                "4_2_BCS_APW.dot", "4_5_BCS_LED_FP.dot"
            ]
            ,
            [
                "4_1_BCS_MPW.dot", "4_5_BCS_LED_FP.dot"
            ]
            ,
            [
                "4_12_BCS_LED_EMT.dot", "4_14_BCS_LED_EMB.dot"
            ]
            ,
            [
                "4_13_MCS_LED_EML.dot", "4_15_BCS_LED_EMR.dot"
            ]
            ,
            [
                "4_5_BCS_LED_FP.dot", "BCS_PW_4.dot"
            ]
         ]

TESTS_FOLDER = "Real-Tests/resources/"

numOfComponents = random.randint(2, 9)
numOfSynchs = random.randint(1, min(int(numOfComponents/2), 4))

chosen = ''
biggestSynch = 4
while(numOfSynchs):
    if((2 * numOfSynchs) < biggestSynch):
        SYNCHS.pop(0)
        biggestSynch = 0
    currentSynchIndex = random.randint(0, len(SYNCHS) - 1)
    currentSynch = SYNCHS[currentSynchIndex]
    for component in currentSynch:
        if(component in COMPONENTS):
            COMPONENTS.remove(component)
            chosen += TESTS_FOLDER + "Synchs/" + component + '\n'
            numOfComponents -= 1
            
            if biggestSynch != 0 and component in SYNCHS[0]:
                biggestSynch -= 1 

    SYNCHS.remove(currentSynch)
    
    numOfSynchs -= 1

while(numOfComponents):
    componentIndex = random.randint(0, len(COMPONENTS) - 1)
    chosen += TESTS_FOLDER + COMPONENTS[componentIndex] + '\n'
    COMPONENTS.pop(componentIndex)
    numOfComponents -= 1

with open("data/Reals.txt", 'w') as writingfile:
            writingfile.write(chosen) 
            writingfile.close()  
    