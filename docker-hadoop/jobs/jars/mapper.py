#!/usr/bin/env python3
# -*-coding:utf-8 -*
import sys
import string

for line in sys.stdin: # reads from stdin
    print("your message B", file=sys.stderr)
    line = line.strip()
    for i in string.punctuation:
        line = line.replace(i,' ')
    line = line.lower()
    words = line.split()

    for word in words: # writes to stdout
        if word[0] < "a" :
            reducer = 0
        elif "a" <= word[0] and word[0] < "e":
            reducer = 1
        elif "e" <= word[0] and word[0] < "l":
            reducer = 2
        elif "l" <= word[0] and word[0] < "r":
            reducer = 3
        else:
            reducer = 4
        output = str(reducer)+"-"+word
        print("%s\t%d" % (output, 1))
