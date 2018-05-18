import scala.collection.mutable
import scala.math.Ordering.Implicits._

class SimpleGreedy(val a: Seq[Int]) {
  val cg = new CalcGains(a)
  val costs: Map[Seq[Int], Int] = ??? //cg.costs.toMap

//  println(cg.gains.toList.sortBy(_._1.toString))
//  println(costs)
//  println(
//    costs
//      .map(seq_c =>
//        (seq_c._1, cg.maxByLen(seq_c._1.last - seq_c._1.head - 1) - seq_c._2)
//      )
//      .toList.sortBy(_._1.toString)
//  )

  // Seq of >=3 indices -> (cost, index of last removed)
//  lazy val fullCostsMap: mutable.Map[Seq[Int], (Long, Int)] = mutable.Map()//.withDefault(seq => {
//    assert(seq.length > 2)
//    val cost =
//      if (seq.length == 3)
//        costs(seq)
//      else
//        (1 until seq.length - 1).map(i => fullCosts(seq.slice(i - 1, i + 2)) + fullCosts(seq.patch(i, Nil, 1))).min
//    fullCosts(seq) = cost
//    cost
//  })
//  def fullCosts(seq: Seq[Int], lastRemoved: Int): Long = {
//    assert(seq.length > 2)
//    val cost =
//      if (seq.length == 3)
//        costs(seq)
//      else
//        (1 until seq.length - 1).map(i => costs(seq.slice(i - 1, i + 2)) + fullCosts(seq.patch(i, Nil, 1), seq(i))).min
//    fullCostsMap(seq) = (cost, lastRemoved)
//    cost
//  }

//  val priorityQueue: mutable.PriorityQueue[(Long, Seq[Int])] = new mutable.PriorityQueue()(Ordering.fromLessThan[(Long, Seq[Int])]((x,y) => x._1>y._1))
//  priorityQueue += ((0l, a.indices))
//
////  val ways
//
//  def expand(): Unit = {
//    val (cost, seq) = priorityQueue.dequeue()
//    if (seq.length == 3)
//      throw new Exception("way found, cost = "+42)
//    else
//      (1 until seq.length - 1).map(i => {
//        priorityQueue. -= null
//        costs(seq.slice(i - 1, i + 2))
//        + fullCosts(seq.patch(i, Nil, 1), seq(i))
//      }).min
//  }
//
//  def fullCosts(seq: Seq[Int]): Long = {
//    assert(seq.length > 2)
//    if (seq.length == 3)
//      costs(seq)
//    else
//      (1 until seq.length - 1).map(i => {
//        val seqi = seq.patch(i, Nil, 1)
//        val fci = fullCosts(seqi)
//        fullCostsMap.get(seqi) match {
//          case Some((oldCost, _)) if (oldCost >  =>
//          case _ =>
//        }
//        costs(seq.slice(i - 1, i + 2)) + fci
//      }).min
//  }

  def foo(): Unit = {
    println(a)
//    println(fullCosts(a.indices.toList))
//    println(fullCosts)
    println(Prim.reward(a))
    println(Prim.reward)
    println(cg.maxByLen)
    println(getReward(a.indices))
//    println(getStepIndices(a.indices))
    println(Prim.getStepIndices(a))
  }

//cg.maxByLen(seq_c._1.last - seq_c._1.head - 1) - seq_c._2
  def getReward(seq: Seq[Int]): Int = {
    //TODO
    def rew(s: Seq[Int]): Int = {
      println(s+" ==> "+(cg.maxByLen(s.last - s.head - 1) - costs(s)))
      cg.maxByLen(s.last - s.head - 1) - costs(s)
    }
    if (seq.size == 3) rew(seq)
    else {
      val i = (1 until seq.length - 1).maxBy(i => rew(seq.slice(i - 1, i + 2)) + getReward(seq.patch(i, Nil, 1)))
      rew(seq.slice(i - 1, i + 2)) + getReward(seq.patch(i, Nil, 1))
    }
  }

//  def getStepIndices(seq: Seq[Int]): List[Int] = //TODO
//    if (seq.size == 3) List(1)
//    else {
//      val i = (1 until seq.length - 1).minBy(i => fullCosts(seq.slice(i - 1, i + 2)) + fullCosts(seq.patch(i, Nil, 1)))
//      i :: getStepIndices(seq.patch(i, Nil, 1))
//    }

}

object SimpleGreedy {
  def main(args: Array[String]): Unit = {
    new SimpleGreedy(Input.seq(5, 234)).foo()
  }
}