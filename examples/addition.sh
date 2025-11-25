#!/bin/bash
# FRACTRAN Addition Example
#
# The simplest FRACTRAN program: computes 2^a * 3^b -> 2^(a+b)
# Using fraction 2/3: multiplies by 2, divides by 3

sbt "run \"2^3,3^4\" \"2/3\""
