package io.github.angleto.fractran

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FractSpec extends AnyFlatSpec with Matchers {

  "Fract" should "create a fraction from numerator and denominator" in {
    val f = Fract(BigInt(3), BigInt(4))
    f.numerator shouldBe BigInt(3)
    f.denominator shouldBe BigInt(4)
  }

  it should "create a fraction from a string with numerator and denominator" in {
    val f = Fract("3/4")
    f.numerator shouldBe BigInt(3)
    f.denominator shouldBe BigInt(4)
  }

  it should "create a fraction from a string with only numerator" in {
    val f = Fract("42")
    f.numerator shouldBe BigInt(42)
    f.denominator shouldBe BigInt(1)
  }

  it should "handle spaces in string input" in {
    val f = Fract(" 3 / 4 ")
    f.numerator shouldBe BigInt(3)
    f.denominator shouldBe BigInt(4)
  }

  it should "create a fraction from BigInt" in {
    val f = Fract(BigInt(100))
    f.numerator shouldBe BigInt(100)
    f.denominator shouldBe BigInt(1)
  }

  it should "throw FractranException for invalid input" in {
    an[FractranException] should be thrownBy Fract("1/2/3")
  }

  "isInt" should "return true when fraction is an integer" in {
    Fract(BigInt(6), BigInt(3)).isInt shouldBe true
    Fract(BigInt(10), BigInt(2)).isInt shouldBe true
    Fract(BigInt(0), BigInt(5)).isInt shouldBe true
  }

  it should "return false when fraction is not an integer" in {
    Fract(BigInt(5), BigInt(3)).isInt shouldBe false
    Fract(BigInt(7), BigInt(2)).isInt shouldBe false
  }

  "multiplication" should "multiply two fractions correctly" in {
    val f1 = Fract(BigInt(2), BigInt(3))
    val f2 = Fract(BigInt(5), BigInt(7))
    val result = f1 * f2
    result.numerator shouldBe BigInt(10)
    result.denominator shouldBe BigInt(21)
  }

  it should "handle multiplication with integer fractions" in {
    val f1 = Fract(BigInt(6))
    val f2 = Fract(BigInt(2), BigInt(3))
    val result = f1 * f2
    result.numerator shouldBe BigInt(12)
    result.denominator shouldBe BigInt(3)
  }

  "div" should "return integer division result" in {
    Fract(BigInt(10), BigInt(2)).div shouldBe BigInt(5)
    Fract(BigInt(15), BigInt(3)).div shouldBe BigInt(5)
    Fract(BigInt(100), BigInt(10)).div shouldBe BigInt(10)
  }

  it should "truncate non-integer divisions" in {
    Fract(BigInt(7), BigInt(2)).div shouldBe BigInt(3)
    Fract(BigInt(10), BigInt(3)).div shouldBe BigInt(3)
  }

  "Fract" should "handle large BigInt values" in {
    val large = BigInt("123456789012345678901234567890")
    val f = Fract(large, BigInt(1))
    f.numerator shouldBe large
    f.div shouldBe large
  }
}
