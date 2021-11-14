# Fractran

This is a simple implementation of a [Fractran](https://en.wikipedia.org/wiki/FRACTRAN) interpreter in scala

# Run the program

## Using the scala interpreter

`scala Fractran.scala <number> "<fractions>"`

the number can be also expressed as a power, for example: "2^3,3^4,2"

then:
scala Fractran.scala "2^3,3^4,2" "455/33,11/13,1/11,3/7,11/2,1/3"


Example:

```bash
Bash %> scala Fractran.scala 72 "455/33,11/13,1/11,3/7,11/2,1/3"
72
396
5460
4620
63700
53900
4900
2100
900
4950
68250
57750
796250
673750
61250
26250
11250
61875
853125
721875
9953125
8421875
765625
328125
140625
46875
15625
```

## Compile and run the bytecode

To compile the program:

`scalac Fractran.scala`

Run the bytecode:

```bash
Bash %> scala FractranCL 72 "455/33,11/13,1/11,3/7,11/2,1/3"
72
396
5460
4620
63700
53900
4900
2100
900
4950
68250
57750
796250
673750
61250
26250
11250
61875
853125
721875
9953125
8421875
765625
328125
140625
46875
15625
```
