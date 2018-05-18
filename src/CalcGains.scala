import scala.collection.mutable

class CalcGains(val a: Seq[Int], val identifier: Seq[Int]) {
  def this(a: Seq[Int]) = this(a, a.indices)
  var maxByLen: mutable.Seq[Int] = mutable.Seq.fill[Int](a.size-1)(0)

  var gains: mutable.Map[Seq[Int], Int] = mutable.Map()
  var costs: mutable.Map[Seq[Int], Int] = mutable.Map()

  for (len <- 2 until a.size) {
    maxByLen(len-1) = maxByLen(len-2)
    for (left <- 0 until a.size - len) {
      for (mid <- left + 1 until left + len) {
        val gain = Prim.highestPrimeFacOfSeq(Seq(left, mid, left + len).map(a))
        gains(Seq(left, mid, left + len).map(identifier)) = gain
        if (gain > maxByLen(len-1))
          maxByLen(len-1) = gain
      }
    }
    for (left <- 0 until a.size - len)
      for (mid <- left + 1 until left + len)
        costs(Seq(left, mid, left + len).map(identifier)) = maxByLen(len-1) - gains(Seq(left, mid, left + len).map(identifier))
  }
//  2 until a.size foreach (len => {
//    maxByLen(len-1) = maxByLen(len-2)
//    0 until a.size - len foreach (left => {
//      left + 1 until left + len foreach (mid => {
////        println(left, mid, left + len)
//        val gain = Prim.highestPrimeFacOfSeq(Seq(left, mid, left + len).map(a))
//        costs(Seq(left, mid, left + len)) = gain // only temporary, to prevent re-computation; value will be replaced in next loop
//        if (gain > maxByLen(len-1))
//          maxByLen(len-1) = gain
//      })
//    })
//
//    0 until a.size - len foreach (left => {
//      left + 1 until left + len foreach (mid => {
////        println((left, mid, left + len) + " "+maxByLen(len-1) +"-"+ costs(List(left, mid, left + len)) +"="+
////          (maxByLen(len-1) - costs(List(left, mid, left + len))))
//        costs(Seq(left, mid, left + len)) = maxByLen(len-1) - costs(Seq(left, mid, left + len))
//      })
//    })
//  })

//  println(maxByLen, costs)
}

object CalcGains {
  def main(args: Array[String]): Unit = {
    println(new CalcGains(Input.seqFromSeed(234).take(5)).gains)
    println(new CalcGains(Input.seqFromSeed(234).take(5)).costs)
  }
}