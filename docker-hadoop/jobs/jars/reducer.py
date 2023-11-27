#!/usr/bin/env python3
# -*-coding:utf-8 -*

import sys
total = 0
lastword = None

for line in sys.stdin:
    line = line.strip()
    reducer, new_line = line.split("-")
    # recuperer la cle et la valeur et conversion de la valeur en int
    word, count = new_line.split()
    count = int(count)

    # passage au mot suivant (plusieurs cles possibles pour une même exécution de programme)
    if lastword is None:
        lastword = word
    if word == lastword:
        total += count
    else:
        print("%s\t%d occurences" % (lastword, total))
        total = count
        lastword = word

if lastword is not None:
    print("%s\t%d occurences" % (lastword, total))
