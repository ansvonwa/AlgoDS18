object Test {
//    def main(args: Array[String]): Unit = new Prim(5, 234).run()
  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    new Prim(Input.seq(18, 321)).reward
    println(System.currentTimeMillis() - start)
//    Stream.from(3).map(len => (len, {
//      new PrimTiming(0 until len).reward
////      Prim.reward.map(_._1.length).groupBy(identity).mapValues(_.size).toList.sortBy(_._1)
//      PrimTiming.timeUsed
//    }, System.currentTimeMillis() - start)).foreach(println)
  }
}
