package io.github.angleto.fractran

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MainSpec extends AnyFlatSpec with Matchers {

  "parseInput" should "parse a simple integer" in {
    Main.parseInput("42") shouldBe BigInt(42)
  }

  it should "parse a power expression" in {
    Main.parseInput("2^3") shouldBe BigInt(8)
    Main.parseInput("3^4") shouldBe BigInt(81)
    Main.parseInput("10^6") shouldBe BigInt(1000000)
  }

  it should "parse multiple comma-separated values as products" in {
    Main.parseInput("2,3,5") shouldBe BigInt(30)
    Main.parseInput("2,2,2") shouldBe BigInt(8)
  }

  it should "parse mixed powers and integers" in {
    Main.parseInput("2^3,3^4,2") shouldBe BigInt(1296)  // 8 * 81 * 2
    Main.parseInput("2^2,5") shouldBe BigInt(20)        // 4 * 5
  }

  it should "handle spaces in input" in {
    Main.parseInput(" 2 ^ 3 , 3 ") shouldBe BigInt(24)  // 8 * 3
  }

  it should "handle power of 0" in {
    Main.parseInput("5^0") shouldBe BigInt(1)
  }

  it should "handle power of 1" in {
    Main.parseInput("7^1") shouldBe BigInt(7)
  }

  it should "handle large numbers" in {
    Main.parseInput("2^100") shouldBe BigInt(2).pow(100)
  }

  it should "handle the classic FRACTRAN input 72 = 2^3 * 3^2" in {
    Main.parseInput("2^3,3^2") shouldBe BigInt(72)
  }
}
