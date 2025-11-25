package io.github.angleto.fractran

/**
 * FRACTRAN interpreter implementation.
 *
 * FRACTRAN is a Turing-complete esoteric programming language invented by John Conway.
 * A program is an ordered list of fractions. Execution proceeds by multiplying
 * an integer n by the first fraction f in the list for which nf is an integer,
 * replacing n by nf and repeating.
 *
 * @see https://en.wikipedia.org/wiki/FRACTRAN
 */
object Fractran {

  /**
   * Executes a FRACTRAN program.
   *
   * @param n         the initial value as a Fract
   * @param fractions the ordered list of fractions (the program)
   * @return a lazy list of all values in the computation sequence
   */
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
}