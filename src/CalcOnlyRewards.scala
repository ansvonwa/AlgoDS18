import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

object CalcOnlyRewards {
  def main(args: Array[String]): Unit = {
    //assert(args.length == 2, "params: <n> <seed>")
    val n = 5//args(1).toInt
    val seed = 234//args(2).toInt
    val random = new Random(seed)
    val a = (0 until n).map(_ => random.nextInt(100))

    lazy val highestPrimeFac: mutable.Map[Int, Int] = mutable.Map(0 -> 0, 1 -> 1).withDefault(n => {
      @tailrec
      def foo(x: Int, a: Int = 2): Int = a*a > x match {
        case false if x % a == 0 => foo(x / a, a)
        case false               => foo(x, a + 1)
        case true                => x
      }
      val f = foo(n)
      highestPrimeFac(n) = f
      f
    })

    lazy val reward: mutable.Map[Seq[Int], Long] = mutable.Map().withDefault(seq => {
      println(s"foo($seq)")
      assert(seq.length>=2)
      val rew =
        if (seq.length == 2)
          0
        else if (seq.length == 3)
          highestPrimeFac(seq.reduceLeft[Int]((cur, x) => cur * (if (x >= 10) 100 else 10) + x))
        else
          (1 until seq.length-1).map(i => reward(seq.slice(i-1, i+2)) + reward(seq.patch(i, Nil, 1))).max
      reward(seq) = rew
      rew
    })

    println(reward(a))
  }
}
