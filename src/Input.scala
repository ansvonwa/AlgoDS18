import scala.util.Random

object Input {
  def rndSeq: Seq[Int] = seqFromSeed(Random.nextInt())

  def seqFromSeed(seed: Int): Seq[Int] = {
    val random = new Random(seed)
    Stream.continually(random.nextInt(100))
  }

  def seq(n: Int, seed: Int): Seq[Int] = seqFromSeed(seed).take(n)
}
