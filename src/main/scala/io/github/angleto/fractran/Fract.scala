package io.github.angleto.fractran

/**
 * Represents a fraction with arbitrary precision integers.
 *
 * @param numerator   the numerator of the fraction
 * @param denominator the denominator of the fraction
 */
case class Fract(numerator: BigInt, denominator: BigInt) {

  /** Returns true if the fraction represents an integer value. */
  def isInt: Boolean = numerator % denominator == BigInt(0)

  /** Multiplies this fraction by another fraction. */
  def *(f: Fract): Fract = Fract(f.numerator * numerator, f.denominator * denominator)

  /** Returns the integer division of numerator by denominator. */
  def div: BigInt = numerator / denominator
}

object Fract {

  /**
   * Creates a Fract from a string representation.
   * Accepts formats: "n" (integer) or "n/d" (fraction).
   */
  def apply(f: String): Fract = {
    f.replaceAll(" ", "").split("/") match {
      case Array(numerator) =>
        Fract(numerator = BigInt(numerator), denominator = BigInt(1))
      case Array(numerator, denominator) =>
        Fract(numerator = BigInt(numerator), denominator = BigInt(denominator))
      case _ =>
        throw FractranException("Invalid Fraction, the input must be a simplified fraction: <numerator>/<denominator>")
    }
  }

  /** Creates a Fract from a BigInt (denominator = 1). */
  def apply(numerator: BigInt): Fract = Fract(numerator = numerator, denominator = BigInt(1))
}