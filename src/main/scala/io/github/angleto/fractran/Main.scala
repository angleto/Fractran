package io.github.angleto.fractran

/**
 * Command-line interface for the FRACTRAN interpreter.
 */
object Main {

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("FRACTRAN Interpreter")
      println()
      println("Usage: fractran <number> <fractions>")
      println()
      println("Arguments:")
      println("  number     Initial value (can use powers: \"2^3,3^4,2\")")
      println("  fractions  Comma-separated list of fractions (e.g., \"455/33,11/13,1/11\")")
      println()
      println("Example:")
      println("  fractran 72 \"455/33,11/13,1/11,3/7,11/2,1/3\"")
      println("  fractran \"2^3,3^4,2\" \"455/33,11/13,1/11,3/7,11/2,1/3\"")
      System.exit(1)
    }

    val n = parseInput(args(0))
    val fractions = LazyList(args(1).split(",").map(f => Fract(f))).flatten

    Fractran(Fract(n), fractions)
      .map(_.div)
      .foreach(println)
  }

  /**
   * Parses input that may contain powers and products.
   * E.g., "2^3,3^4,2" becomes 2^3 * 3^4 * 2 = 1296
   */
  private[fractran] def parseInput(input: String): BigInt = {
    input.replace(" ", "").split(",").map { v =>
      if (v.contains("^")) {
        val pow = v.split("\\^")
        (pow.headOption, pow.lastOption) match {
          case (Some(num), Some(exp)) => BigInt(num).pow(exp.toInt)
          case _ => throw FractranException("Invalid power expression")
        }
      } else {
        BigInt(v)
      }
    }.foldLeft(BigInt(1))(_ * _)
  }
}