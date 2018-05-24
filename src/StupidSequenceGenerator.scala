import scala.util.Random

object StupidSequenceGenerator {
  def main(args: Array[String]): Unit = {
    println(Input.seqFromSeed(560033).take(3).toList)
    println(new ListFoo(3, Input.seqFromSeed(560033)).maxGain)
    println(new ListFoo(5, Input.seqFromSeed(360183)).maxGain)
    val range = 1 to 1
    for (seed <- 0 to 10000000) {
      val rnd = new Random(seed)
      for (_ <- range) {
        if (rnd.nextInt(100) == 0)
          if (rnd.nextInt(100) == 0)
            if (rnd.nextInt(100) == 0)
              println(s"seed $seed made three zeros or 001")
      }
    }
  }
}
