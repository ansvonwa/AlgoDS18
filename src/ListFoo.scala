import scala.collection.mutable
import scala.util.Random

object ListFoo {
  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    println(new ListFoo(10, Input.seqFromSeed(321)).maxGain)
    (20 to 100)
      .map(_*20)
      .foreach(n => {
        val rnd = Random.nextInt(10000)
        println(s"$n,$rnd,"+new ListFoo(n, Input.seqFromSeed(rnd)).maxGain)
      })


    println("time: "+(System.currentTimeMillis()-start))
  }
}

class ListFoo(val n: Int, a: Seq[Int]) {
  val cache: mutable.Seq[mutable.Seq[Long]] = mutable.Seq.fill(n)(mutable.Seq.fill(n)(0l))
//  val cache = Array.ofDim[Long](n, n)

  lazy val maxGain = gain(0, n-1)

  def gain(from: Int, to: Int): Long = {
    if (to-from == 1)
      return 0
    val cached = cache(from)(to)
    if (cached != 0)
      return cached
    val max: Long = (from+1 until to).map(mid => gain(from, mid) + Prim.highestPrimeFacOfSeq(Seq(from, mid, to).map(a)) + gain(mid, to)).max
    cache(from)(to) = max
    max
  }
}
