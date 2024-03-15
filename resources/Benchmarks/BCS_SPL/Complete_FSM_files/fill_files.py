import itertools
import random
list_of_names = []
with open('products/file_names.txt') as f:
    for line in f:
        if line != "":
            list_of_names.append(line)

for i in range(6, 8):
    data = list(itertools.combinations(list_of_names, i))
    num = min(len(data), 40)
    samples = random.sample(data, num)
    c = 0
    for s in samples:
        c +=1
        name = "./products2/" + str(i) + "wise_" + str(c) + ".txt"
        with open(name, 'w+') as fp:
            for item in s:
                # write each item on a new line
                fp.write("%s" % item)



