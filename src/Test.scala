object Test {
    def main(args: Array[String]): Unit = {
      val start = System.currentTimeMillis()
      new Prim(if (args.length == 0) 18 else args(0).toInt, if (args.length <= 1) 321 else args(1).toInt).run()
      print(System.currentTimeMillis()-start)
    }
//  def main(args: Array[String]): Unit = {
//    val start = System.currentTimeMillis()
////    val a = List(82, 50, 76, 89, 95, 58, 34, 37, 85, 53, 39, 26, 72, 65, 38, 49, 20, 85, 54, 16, 22)//5328
////    val a = List(82, 50, 76, 89, 95, 58, 34, 37, 85, 53, 39, 39, 26, 72, 65, 38, 49, 20, 85, 54, 16, 22)//10690
//    val a = List(82, 39, 50, 76, 89, 95, 58, 34, 37, 85, 53, 26, 72, 65, 38, 49, 20, 85, 54, 39, 16, 22)//11259
//    new Prim(a).reward
//    println(System.currentTimeMillis() - start)
////    Stream.from(3).map(len => (len, {
////      new PrimTiming(0 until len).reward
//////      Prim.reward.map(_._1.length).groupBy(identity).mapValues(_.size).toList.sortBy(_._1)
////      PrimTiming.timeUsed
////    }, System.currentTimeMillis() - start)).foreach(println)
//  }
}
