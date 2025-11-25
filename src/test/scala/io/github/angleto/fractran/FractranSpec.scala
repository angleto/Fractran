package io.github.angleto.fractran

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FractranSpec extends AnyFlatSpec with Matchers {

  "Fractran" should "compute the addition program (2/3)" in {
    // Addition: 2^a * 3^b -> 2^(a+b)
    // Starting with 2^3 * 3^4 = 8 * 81 = 648
    // Result should be 2^7 = 128
    val n = Fract(BigInt(648))  // 2^3 * 3^4
    val fractions = LazyList(Fract("2/3"))

    val result = Fractran(n, fractions).toList

    result.last.div shouldBe BigInt(128)  // 2^7
    result.map(_.div) shouldBe List(648, 432, 288, 192, 128)
  }

  it should "halt when no fraction produces an integer" in {
    val n = Fract(BigInt(5))  // Prime, won't multiply cleanly with 2/3
    val fractions = LazyList(Fract("2/3"))

    val result = Fractran(n, fractions).toList

    result shouldBe List(Fract(BigInt(5)))  // Only the initial value
  }

  it should "include the initial value in the output" in {
    val n = Fract(BigInt(72))
    val fractions = LazyList(Fract("1/2"))

    val result = Fractran(n, fractions).toList

    result.head shouldBe n
  }

  it should "compute a simple halving sequence" in {
    // 1/2 repeatedly divides by 2 until odd
    val n = Fract(BigInt(16))  // 2^4
    val fractions = LazyList(Fract("1/2"))

    val result = Fractran(n, fractions).map(_.div).toList

    result shouldBe List(16, 8, 4, 2, 1)
  }

  it should "select the first matching fraction" in {
    // With fractions 1/3, 1/2: should use 1/3 first if divisible by 3
    val n = Fract(BigInt(6))  // 2 * 3
    val fractions = LazyList(Fract("1/3"), Fract("1/2"))

    val result = Fractran(n, fractions).map(_.div).toList

    // 6 -> 6/3=2 -> 2/2=1 -> halt
    result shouldBe List(6, 2, 1)
  }

  it should "handle the multiplication example from README" in {
    // The classic example: starts with 72
    val n = Fract(BigInt(72))
    val fractions = LazyList(
      Fract("455/33"),
      Fract("11/13"),
      Fract("1/11"),
      Fract("3/7"),
      Fract("11/2"),
      Fract("1/3")
    )

    val result = Fractran(n, fractions).take(10).map(_.div).toList

    result.head shouldBe BigInt(72)
    result(1) shouldBe BigInt(396)
    result(2) shouldBe BigInt(5460)
  }

  it should "handle empty fraction list" in {
    val n = Fract(BigInt(42))
    val fractions = LazyList.empty[Fract]

    val result = Fractran(n, fractions).toList

    result shouldBe List(Fract(BigInt(42)))
  }

  it should "work with large numbers" in {
    val large = BigInt("1000000000000")
    val n = Fract(large)
    val fractions = LazyList(Fract("1/2"))

    val result = Fractran(n, fractions).take(5).map(_.div).toList

    result shouldBe List(
      BigInt("1000000000000"),
      BigInt("500000000000"),
      BigInt("250000000000"),
      BigInt("125000000000"),
      BigInt("62500000000")
    )
  }
}
