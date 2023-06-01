#!/bin/env python
f = open("Changelog.txt", "r")
g = open("Changelog-github.txt", "w")
for l in f:
    l = l.replace("\\", "\\\\")
    l = l.replace("*", "\\*")
    l = l.replace("-", "\\-")
    l = l.replace("+", "\\+")
    s = 0
    while l.startswith(" "):
        l = l[1:]
        s += 1
    l = ("&nbsp;"*s) + l
    g.write(l)
f.close();
g.close();
