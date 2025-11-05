// Scala 3 Migration

case class FractranException(message: String = "", cause: Throwable = null)
  extends Exception(message, cause)

/**
 * Fract represents fractions <n>/<d>
 */
case class Fract(
  numerator: BigInt,
  denominator: BigInt
):
  def isInt: Boolean =
    numerator % denominator == BigInt(0)

  def *(f: Fract): Fract =
    Fract(f.numerator * numerator, f.denominator * denominator)

  def div: BigInt =
    numerator / denominator

object Fract:
  def apply(f: String): Fract =
    f.replaceAll(" ", "").split("/") match
      case Array(numerator) =>
        Fract(numerator = BigInt(numerator), denominator = BigInt(1))
      case Array(numerator, denominator) =>
        Fract(numerator = BigInt(numerator), denominator = BigInt(denominator))
      case _ =>
        throw FractranException("Invalid Fraction, the input must be a simplified fraction: <num>/<denominator>")

  def apply(numerator: BigInt): Fract =
    Fract(numerator = numerator, denominator = BigInt(1))

object Fractran:
  def apply(n: Fract, fractions: LazyList[Fract]): LazyList[Fract] =
    def fractran(value: Fract): LazyList[Fract] =
      fractions
        .map(f => value * f)
        .find(f => f.isInt) match
          case Some(t) => t #:: fractran(t)
          case None => LazyList.empty[Fract]

    n #:: fractran(n)

@main def fractran(args: String*): Unit =
  if args.length < 2 then
    println("Usage: fractran <n> <fraction>")
    System.exit(0)

  val n = args(0).replace(" ", "").split(",").map: v =>
    if v.contains("^") then
      val pow = v.split("\\^")
      (pow.headOption, pow.lastOption) match
        case (Some(num), Some(exp)) =>
          BigInt(num).pow(exp.toInt)
        case _ =>
          throw FractranException("The number is not a power")
    else
      BigInt(v)
  .foldLeft(BigInt(1))((a, b) => a * b)

  val fractions = LazyList(args(1).split(",").map(f => Fract(f))*).flatten

  Fractran(Fract(n), fractions)
    .map(_.div)
    .foreach(println)
