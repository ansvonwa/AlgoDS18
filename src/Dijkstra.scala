import de.ummels.prioritymap.PriorityMap

import scala.annotation.tailrec

class Dijkstra(val a: Seq[Int]) {
  val cg = new CalcGains(a)
  val costs: Map[Seq[Int], Int] = ??? //cg.costs.toMap

  type Graph[N] = N => Map[N, Int]

  def dijkstra[N](g: Graph[N])(source: N): (Map[N, Int], Map[N, N]) = {
    @tailrec
    def go(active: PriorityMap[N, Int], res: Map[N, Int], pred: Map[N, N]): (Map[N, Int], Map[N, N]) =
      if (active.isEmpty) (res, pred)
      else {
        val (node, cost) = active.head
        val neighbours = for {
          (n, c) <- g(node) if !res.contains(n) &&
            cost + c < active.getOrElse(n, Int.MaxValue)
        } yield n -> (cost + c)
        val preds = neighbours mapValues (_ => node)
        go(active.tail ++ neighbours, res + (node -> cost), pred ++ preds)
      }

    go(PriorityMap(source -> 0), Map.empty, Map.empty)
  }

  def shortestPath[N](g: Graph[N])(source: N, target: N): Option[List[N]] = {
    val pred = dijkstra(g)(source)._2
    if (pred.contains(target) || source == target)
      Some(iterateRight(target)(pred.get))
    else None
  }

  def iterateRight[N](x: N)(f: N => Option[N]): List[N] = {
    @tailrec
    def go(x: N, acc: List[N]): List[N] = f(x) match {
      case None => x :: acc
      case Some(y) => go(y, x :: acc)
    }

    go(x, List.empty)
  }

  val graph: Graph[Seq[Int]] = seq =>
    (1 until seq.length - 1)
      .map(i => (seq.patch(i, Nil, 1), costs(seq.slice(i - 1, i + 2)))).toMap //.withDefaultValue(Int.MaxValue)


  println(shortestPath(graph)(a.indices, Seq(0, a.size - 1)))

}

object Dijkstra {
  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    new Dijkstra(Input.seq(22, 321))
    println(System.currentTimeMillis() - start)
  }
}