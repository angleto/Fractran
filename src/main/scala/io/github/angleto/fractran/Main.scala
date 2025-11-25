package io.github.angleto.fractran

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Command-line interface for the FRACTRAN interpreter.
 */
object Main {

  private val dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("FRACTRAN Interpreter")
      println()
      println("Usage: fractran <number> <fractions> [--progress[=N]]")
      println()
      println("Arguments:")
      println("  number       Initial value (can use powers: \"2^3,3^4,2\")")
      println("  fractions    Comma-separated list of fractions (e.g., \"455/33,11/13,1/11\")")
      println()
      println("Options:")
      println("  --progress   Show progress every 10000 steps (last 3 checkpoints)")
      println("  --progress=N Show progress every N steps")
      println()
      println("Example:")
      println("  fractran 72 \"455/33,11/13,1/11,3/7,11/2,1/3\"")
      println("  fractran \"2^3,3^4,2\" \"455/33,11/13,1/11,3/7,11/2,1/3\" --progress")
      println("  fractran \"2^3,3^4,2\" \"455/33,11/13,1/11,3/7,11/2,1/3\" --progress=1000")
      System.exit(1)
    }

    val progressInterval = args.find(_.startsWith("--progress")).map { arg =>
      if (arg.contains("=")) arg.split("=")(1).toInt else 10000
    }

    val n = parseInput(args(0))
    val fractions = LazyList(args(1).split(",").map(f => Fract(f))).flatten

    progressInterval match {
      case Some(interval) => runWithProgress(Fract(n), fractions, interval)
      case None => runNormal(Fract(n), fractions)
    }
  }

  private def runNormal(n: Fract, fractions: LazyList[Fract]): Unit = {
    Fractran(n, fractions)
      .map(_.div)
      .foreach(println)
  }

  private def runWithProgress(n: Fract, fractions: LazyList[Fract], interval: Int): Unit = {
    case class Checkpoint(time: LocalDateTime, step: Long, stepsPerSec: Double, value: BigInt)

    var checkpoints = List.empty[Checkpoint]
    var step = 0L
    var lastCheckpointTime = System.nanoTime()
    var lastCheckpointStep = 0L

    def addCheckpoint(value: BigInt): Unit = {
      val now = System.nanoTime()
      val elapsed = (now - lastCheckpointTime) / 1e9
      val stepsPerSec = if (elapsed > 0) (step - lastCheckpointStep) / elapsed else 0.0

      val checkpoint = Checkpoint(LocalDateTime.now(), step, stepsPerSec, value)
      checkpoints = (checkpoint :: checkpoints).take(3)

      lastCheckpointTime = now
      lastCheckpointStep = step

      printProgress(checkpoints)
    }

    def printProgress(cps: List[Checkpoint]): Unit = {
      print("\u001b[2J\u001b[H") // Clear screen and move cursor to top
      println("FRACTRAN Progress")
      println("=" * 60)
      println()
      println(f"${"Time"}%-12s ${"Step"}%15s ${"Steps/s"}%12s   Value")
      println("-" * 60)

      cps.reverse.foreach { cp =>
        val timeStr = cp.time.format(dateFormatter)
        val valueStr = if (cp.value.toString.length > 20)
          cp.value.toString.take(17) + "..."
        else
          cp.value.toString
        println(f"$timeStr%-12s ${cp.step}%,15d ${cp.stepsPerSec}%,12.1f   $valueStr")
      }

      println()
    }

    Fractran(n, fractions).foreach { fract =>
      step += 1
      if (step % interval == 0) {
        addCheckpoint(fract.div)
      }
    }

    // Final checkpoint
    if (step % interval != 0) {
      val now = System.nanoTime()
      val elapsed = (now - lastCheckpointTime) / 1e9
      val stepsPerSec = if (elapsed > 0) (step - lastCheckpointStep) / elapsed else 0.0
      val finalCheckpoint = Checkpoint(LocalDateTime.now(), step, stepsPerSec, BigInt(0))
      checkpoints = (finalCheckpoint :: checkpoints).take(3)
    }

    println()
    println(s"Completed: $step total steps")
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
