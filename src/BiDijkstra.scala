import de.ummels.prioritymap.PriorityMap

import scala.annotation.tailrec

class BiDijkstra(val a: Seq[Int]) {
  val cg = new CalcGains(a)
  import cg.costs

  type Graph[N] = N => Map[N, Int]

  def dijkstra[N](gFwd: Graph[N], gRev: Graph[N])(source: N, goal: N): (Map[N, Int], Map[N, Int], Map[N, N], Map[N, N], Set[N]) = {
    @tailrec
    def go(activeFwd: PriorityMap[N, Int], activeRev: PriorityMap[N, Int],
           resFwd: Map[N, Int], resRev: Map[N, Int],
           predFwd: Map[N, N], predRev: Map[N, N],
           upperBound: Int, lowerBndFwd: Int, lowerBndRev: Int): (Map[N, Int], Map[N, Int], Map[N, N], Map[N, N], Set[N]) =
      if (activeFwd.isEmpty || activeRev.isEmpty)
        (resFwd, resRev, predFwd, predRev, resFwd.keySet intersect resRev.keySet)
//      else if (upperBound == Int.MaxValue && (resFwd.keySet intersect resRev.keySet).nonEmpty) {
      else if (upperBound == Int.MaxValue && resFwd.keySet.exists(resRev.contains)) {//TODO improve (add lengths of longest paths) and measure performance
        go(activeFwd, activeRev, resFwd, resRev, predFwd, predRev, {
          val any = (resFwd.keySet intersect resRev.keySet).head
          println("intersections = "+(resFwd.keySet intersect resRev.keySet))
          println("new UPPERBOUND = " + (resFwd(any) + resRev(any)))
          resFwd(any) + resRev(any)
        }, activeFwd.values.min, activeRev.values.min)
      } else if (resFwd.size <= resRev.size) {// is better than (lowerBndFwd <= lowerBndRev) bc it prefers small fronts {
//        println(upperBound + "   "+lowerBndFwd+"   "+lowerBndRev)
        val fwdBound = upperBound - lowerBndRev
        val (node, cost) = activeFwd.head
        val neighbours = for {
            (n, c) <- gFwd(node) if !resFwd.contains(n) &&
              cost + c < activeFwd.getOrElse(n, fwdBound)
          } yield n -> (cost + c)
        val preds = neighbours mapValues (_ => node)
        go(activeFwd.tail ++ neighbours, activeRev, resFwd + (node -> cost), resRev, predFwd ++ preds, predRev, {
          if (upperBound == Int.MaxValue) upperBound else math.min(upperBound, cost+resRev.getOrElse(node, Int.MaxValue/2))
        }, cost, lowerBndRev)
      } else {
//        println(upperBound + "   "+lowerBndFwd+"   "+lowerBndRev)
        val revBound = upperBound - lowerBndFwd
        val (node, cost) = activeRev.head
        val neighbours = for {
          (n, c) <- gRev(node) if !resRev.contains(n) &&
            cost + c < activeRev.getOrElse(n, revBound)
        } yield n -> (cost + c)
        val preds = neighbours mapValues (_ => node)
        go(activeFwd, activeRev.tail ++ neighbours, resFwd, resRev + (node -> cost), predFwd, predRev ++ preds, {
          if (upperBound == Int.MaxValue) upperBound else math.min(upperBound, cost+resFwd.getOrElse(node, Int.MaxValue/2))
        }, lowerBndFwd, cost)
      }

    go(PriorityMap(source -> 0), PriorityMap(goal -> 0), Map.empty, Map.empty, Map.empty, Map.empty, Int.MaxValue, 0, 0)
  }


  def gain(gFwd: Graph[Seq[Int]], gRev: Graph[Seq[Int]])(source: Seq[Int], target: Seq[Int]): Int = {
    println("dijkstra")
    val (_, _, predFwd, predRev, intersection) = dijkstra(gFwd, gRev)(source, target)
    println("gaincalc")
//    println("cost = "+(cg.maxByLen.sum + shortestPath(predFwd, predRev, intersection)(source, target).map(transitions).get.map(costs(_, 0)).sum))
    println("buffersize: "+(predFwd.size + predRev.size))
    gain(predFwd, predRev, intersection)(source, target)
  }

  def gain(predFwd: Map[Seq[Int], Seq[Int]], predRev: Map[Seq[Int], Seq[Int]], intersection: Set[Seq[Int]])
          (source: Seq[Int], target: Seq[Int]): Int = {
    val path = shortestPath(predFwd, predRev, intersection)(source, target)
    println(path)
    path.get.sliding(2).map(pair => {
      val j = pair.head.indexWhere(!pair.last.contains(_))
      pair.head.slice(j - 1, j + 2)
    }).map(cg.gains).sum
  }

  def transitions(path: List[Seq[Int]]): List[Seq[Int]] =
    path.sliding(2).map(pair => {
      val j = pair.head.indexWhere(!pair.last.contains(_))
      pair.head.slice(j - 1, j + 2)
    }).toList

  def shortestPath(gFwd: Graph[Seq[Int]], gRev: Graph[Seq[Int]])(source: Seq[Int], target: Seq[Int]): Option[List[Seq[Int]]] = {
    val (_, _, predFwd, predRev, intersection) = dijkstra(gFwd, gRev)(source, target)
    shortestPath(predFwd, predRev, intersection)(source, target)
  }

  def shortestPath(predFwd: Map[Seq[Int], Seq[Int]], predRev: Map[Seq[Int], Seq[Int]], intersection: Set[Seq[Int]])(source: Seq[Int], target: Seq[Int]): Option[List[Seq[Int]]] = {
    if (source == target)
      Some(List(source, target))
    else if (intersection.nonEmpty)
      Some({
        intersection.map(mid =>
          iterateRight(mid)(predFwd.get).dropRight(1) ++ iterateRight(mid)(predRev.get).reverse
        ).maxBy(p => transitions(p).map(cg.gains).sum)
      })
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

  lazy val graphFwd: Graph[Seq[Int]] = seq =>
    (1 until seq.length - 1)
      .map(i => (seq.patch(i, Nil, 1), costs(seq.slice(i - 1, i + 2), a.length - seq.length+1))).toMap //.withDefaultValue(Int.MaxValue)
  lazy val graphRev: Graph[Seq[Int]] = seq =>
    (0 until seq.length - 1)
      .flatMap(j => seq(j) + 1 until seq(j + 1) map (i => (seq.patch(j + 1, Seq(i), 0), costs(Seq(seq(j), i, seq(j + 1)), a.length - seq.length)))).toMap
  //    seq.sliding(2).flatMap(reg => reg.head+1 until reg.last map())
  //    (1 until a.length - 1).filterNot(seq.contains)
  //      .map(i => (seq.patch(i, Seq(i), 0), costs(Seq(seq(i-1))))).toMap //.withDefaultValue(Int.MaxValue)

  //  lazy val (_, _, predFwd, predRev, intersection) = dijkstra(graphFwd, graphRev)(a.indices.toList, Seq(0, a.size - 1))

  println(gain(graphFwd, graphRev)(a.indices.toList, Seq(0, a.size - 1)))
}

object BiDijkstra {
  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    new BiDijkstra(Input.seq(18, 321))
    println(System.currentTimeMillis() - start)
  }
}
