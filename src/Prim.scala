import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

class Prim(val a: Seq[Int]) {
  lazy val reward = Prim.reward(a)
  def this(n: Int, seed: Int) = this({
    val random = new Random(seed)
    (0 until n).map(_ => random.nextInt(100))
  })

  def run(): Unit = {
    println("a: "+a.mkString(" "))
    println("\nbestGain = "+Prim.reward(a))
    println("steps: "+Prim.getStepIndices(a).mkString(" "))
  }

}

object Prim {
  val highestPrimeFac: mutable.Map[Int, Int] = mutable.Map(0 -> 0, 1 -> 1).withDefault(n => {
    @tailrec
    def foo2(x: Int): Int = 4 > x match {
      case false if x % 2 == 0 => foo2(x / 2)
      case false               => foo(x, 3)
      case true                => x
    }
    @tailrec
    def foo(x: Int, a: Int): Int = a*a > x match {
      case false if x % a == 0 => foo(x / a, a)
      case false               => foo(x, a + 2)
      case true                => x
    }
    val f = foo2(n)
    highestPrimeFac(n) = f
    f
  })

  @inline
  def highestPrimeFacOfSeq(seq: Seq[Int]): Int =
    highestPrimeFac(seq.reduceLeft[Int]((cur, x) => cur * (if (x >= 10) 100 else 10) + x))

  val reward: mutable.Map[Seq[Int], Long] = mutable.Map().withDefault(seq => {
    assert(seq.length>=2)
    val rew =
      if (seq.length == 2)
        0
      else if (seq.length == 3)
        highestPrimeFacOfSeq(seq)
      else
        (1 until seq.length-1).map(i => reward(seq.slice(i-1, i+2)) + reward(seq.patch(i, Nil, 1))).max
    reward(seq) = rew
    rew
  })

  //stepshit
  def getStepIndices(seq: Seq[Int]): List[Int] =
    if (seq.size == 3) List(1)
    else {
      val i = (1 until seq.length-1).maxBy(i => reward(seq.slice(i-1, i+2)) + reward(seq.patch(i, Nil, 1)))
      i :: getStepIndices(seq.patch(i, Nil, 1))
    }

  def main(args: Array[String]): Unit = {
    assert(args.length == 2, "params: <n> <seed>")
    new Prim(args(1).toInt, args(2).toInt).run()
  }
}