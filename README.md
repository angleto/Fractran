# Fractran

[![Scala](https://img.shields.io/badge/Scala-2.13-red.svg)](https://www.scala-lang.org/)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

A [FRACTRAN](https://en.wikipedia.org/wiki/FRACTRAN) interpreter implemented in Scala.

## What is FRACTRAN?

FRACTRAN is a Turing-complete esoteric programming language invented by mathematician John Conway. A FRACTRAN program is an ordered list of fractions. The execution is remarkably simple:

1. Start with an integer **n**
2. Find the first fraction **f** in the list where **n × f** is an integer
3. Replace **n** with **n × f** and repeat from step 2
4. If no fraction produces an integer, the program halts

Despite its simplicity, FRACTRAN can compute anything a regular computer can.

## Installation

### Prerequisites

- [Scala](https://www.scala-lang.org/download/) 2.13+
- [SBT](https://www.scala-sbt.org/download.html) 1.x

### Build

```bash
sbt compile
```

## Usage

### Running with SBT

```bash
sbt "run <number> <fractions>"
```

### Examples

**Simple execution:**

```bash
sbt "run 72 455/33,11/13,1/11,3/7,11/2,1/3"
```

**Using power notation for input:**

The input number can be expressed as products of powers:

```bash
sbt "run 2^3,3^4,2 455/33,11/13,1/11,3/7,11/2,1/3"
```

This computes `2^3 × 3^4 × 2 = 1296` as the starting value.

**Output:**

```
72
396
5460
4620
63700
53900
4900
2100
900
...
```

### Progress Mode

For long-running computations, use `--progress` to see live updates showing the last 3 checkpoints with timestamp, step count, steps/second, and current value:

```bash
sbt "run 2^3,3^4,2 455/33,11/13,1/11,3/7,11/2,1/3 --progress"
```

You can customize the checkpoint interval (default 10000 steps):

```bash
sbt "run 2^3,3^4,2 455/33,11/13,1/11,3/7,11/2,1/3 --progress=5000"
```

**Progress output:**

```
FRACTRAN Progress
============================================================

Time               Step      Steps/s   Value
------------------------------------------------------------
10:45:12         10,000     125000.0   8421875
10:45:13         20,000     142857.1   765625
10:45:14         30,000     138888.9   15625
```

### Building a JAR

```bash
sbt assembly
java -jar target/scala-2.13/fractran.jar 72 "455/33,11/13,1/11,3/7,11/2,1/3"
```

### Using the Scala REPL

You can also use the library interactively in the Scala REPL:

```bash
# Start REPL with the JAR
scala -cp target/scala-2.13/fractran.jar
```

Then in the REPL:

```scala
import io.github.angleto.fractran._

// Run the addition program: 2^3 * 3^4 -> 2^7
val n = Fract(BigInt(648))  // 2^3 * 3^4
val fractions = LazyList(Fract("2/3"))
Fractran(n, fractions).map(_.div).toList
// List(648, 432, 288, 192, 128)

// Take first 10 results from a longer computation
val program = LazyList(Fract("455/33"), Fract("11/13"), Fract("1/11"), Fract("3/7"), Fract("11/2"), Fract("1/3"))
Fractran(Fract(72), program).take(10).map(_.div).toList
```

## Project Structure

```
fractran/
├── src/
│   ├── main/scala/io/github/angleto/fractran/
│   │   ├── Fract.scala            # Fraction representation
│   │   ├── Fractran.scala         # Core interpreter
│   │   ├── FractranException.scala
│   │   └── Main.scala             # CLI entry point
│   └── test/scala/                # Test files
├── examples/                      # Example FRACTRAN programs
├── build.sbt
└── README.md
```

## How It Works

The interpreter uses Scala's `LazyList` for efficient lazy evaluation, allowing it to handle potentially infinite computation sequences gracefully. The core algorithm:

```scala
def apply(n: Fract, fractions: LazyList[Fract]): LazyList[Fract] = {
  def fractran(value: Fract): LazyList[Fract] = {
    fractions.map(f => value * f)
      .find(_.isInt) match {
        case Some(t) => t #:: fractran(t)
        case _       => LazyList.empty[Fract]
      }
  }
  n #:: fractran(n)
}
```

## Famous FRACTRAN Programs

### Addition (2/3)

Computes `2^a × 3^b → 2^(a+b)`:

```bash
sbt "run 2^3,3^4 2/3"
```

### Primality Testing

Conway's original FRACTRAN program that generates prime numbers (the PRIMEGAME).

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Author

Angelo Leto ([@angleto](https://github.com/angleto))
