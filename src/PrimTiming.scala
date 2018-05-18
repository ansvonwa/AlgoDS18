import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

class PrimTiming(val a: Seq[Int]) {
  lazy val reward = PrimTiming.reward(a)
  def this(n: Int, seed: Int) = this({
    val random = new Random(seed)
    (0 until n).map(_ => random.nextInt(100))
  })

  def run(): Unit = {
    println("a: "+a.mkString(" "))
    println("\nbestGain = "+PrimTiming.reward(a))
    println("steps: "+PrimTiming.getStepIndices(a).mkString(" "))
  }
}

object PrimTiming {
  val timeUsed = mutable.Seq(0l, 0)
  val timeStarted = mutable.Seq(0l, 0)
  def time = System.currentTimeMillis()
  def timeStart(i: Int) = timeStarted.update(i, time)
  def timeEnd(i: Int) = timeUsed.update(i, timeUsed(i) + time - timeStarted(i))


  val highestPrimeFac: mutable.Map[Int, Int] = mutable.Map(0 -> 0, 1 -> 1).withDefault(n => {
    timeStart(0)
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
    timeEnd(0)
    f
  })

  val reward: mutable.Map[Seq[Int], Long] = mutable.Map().withDefault(seq => {
    timeStart(1)
    assert(seq.length>=2)
    val rew =
      if (seq.length == 2)
        0
      else if (seq.length == 3)
        highestPrimeFac(seq.reduceLeft[Int]((cur, x) => cur * (if (x >= 10) 100 else 10) + x))
      else
        (1 until seq.length-1).map(i => reward(seq.slice(i-1, i+2)) + reward(seq.patch(i, Nil, 1))).max
    reward(seq) = rew
    timeEnd(1)
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