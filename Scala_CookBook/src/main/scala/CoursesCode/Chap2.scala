package CoursesCode

object Chap2 {
  def main(args: Array[String]): Unit = {
    //*********** chapter 2 Numbers *********
    //min or max value of a type
    println("min or max value of a type")
    println(Short.MinValue)
    println(Short.MaxValue)
    println(Int.MinValue)
    println(Float.MaxValue)

    //COmplex Numbers  and dates
    /*println(DateTime.now)
    println(DateTime.now + 2.months )
    println(DateTime.nextMontg < DateTime.now + 2.days
    (2.hours + 45.minutes + 10.seconds).millis)*/
    //
    import java.util.Calendar
    val now = Calendar.getInstance().getTime()
    var dT = Calendar.getInstance()
    var currentMinute = dT.get(Calendar.MINUTE)
    var currentHour = dT.get(Calendar.HOUR_OF_DAY)

    //Parsing a Number from String
    println("100".toInt)
    println("100".toDouble)
    println("1".toLong)
    println("1".toShort)
    println("1".toByte)
    println("foo".toInt)
    // parsing string to BigInt BigDecimal
    val btest = BigInt("1")
    val btest2 = BigDecimal("3.14159")
    // Handling a base and radix
    Integer.parseInt("1", 2)
    Integer.parseInt("10", 2)
    Integer.parseInt("100", 2)
    Integer.parseInt("100", 2)
    Integer.parseInt("1", 8)
    Integer.parseInt("10", 8)
    // create implicit class and methods for implicit convertion
    implicit class StringToInt(s: String){
      def toInt(radix: Int) = Integer.parseInt(s, radix)
    }
    println("1".toInt(2))
    println("10".toInt(2))
    println("100".toInt(2))
    println("100".toInt(8))
    println("100".toInt(16))
    // Scala not require that we spacify that we going the throw and exception
    /*def toInt(s: String) = s.toInt
    @throws(classOf[NumberFormatException])
    def toInt(s: String) = s.toInt*/
    // other maner to deal with exception
    def toInt(s: String):Option[Int] = {
      try {
        Some(s.toInt)
      } catch {
        case e: NumberFormatException => None
      }
    }
    println(s"${toInt("1").getOrElse(0)}")
    println(s"${toInt("a").getOrElse(0)}")

    toInt("aString") match {
      case Some(n) => println(n)
      case None => println("BOom Error")
    }
    val result = toInt("aString") match {
      case Some(n) => n
      case None => 0
    }
    println(result
    )
    // COnverting Between Numeric types
    //val b = a.to[tab]
    19.45.toInt
    19.toFloat
    19.toDouble
    19.toLong
    // test whether the type can be convert before attempting the conversion
    val atest = 1000L
    println("atest.isValidByte ", atest.isValidByte)
    println("atest.isValidShort ", atest.isValidShort)

    //Overriding the default Numeric type
    /*val a = 1
    val a = 1f
    val a = 1d
    val a = 0: Byte
    val a = 0: Int
    val a: Int = 0
    val a: Byte = 0
    val a = 0x20 // hex number
    val a = 0x20L // save as Long type

    case class Person (nom: String, age: Int)
    val s = ("yald", 12)
    val a = s: Person // didn't give
    val a = s: Object */

    //intialiaze variables
    class Foo {
      var a: Short = 0
      var b: Short = _ // only use in class variable intialiazation
    }

    var name = null.asInstanceOf[String] // another way to initialize

    //comparing Floating point Number
    val a = 0.1 + 0.2 // 3.000000000000000004
    var b = 0.3
    println(a == b) // False
    // use method to compare with precision
    def ~= (a: Double, b: Double, precision: Double) = {
      if ((a - b).abs < precision) true else false
    }
    ~=(a, b, 0.00001)
    ~=(b, a, 0.00001)
    // add Methods in MathUtils
    object MathUtils {
      def ~= (a: Double, b: Double, precision: Double) = {
        if ((a - b).abs < precision) true else false
      }
    }
    println(MathUtils.~=(a, b, 0.000001))

    // Handling Very Large Numbers
    println("Handling Very Large Numbers")
    var b1 = BigDecimal(123456.7890)
    println("BigDecimal(123456.7890)", BigDecimal(123456.7890))
    var b2 = BigInt(1234567890)
    println("BigInt(1234567890)", BigInt(1234567890))
    // can be convert to other type, but put sanety check before
    b.toInt
    b.toLong
    b.toFloat
    b.isValidByte // false
    if (b.isValidInt) b.toInt
    // PositiveInfinity
    Double.PositiveInfinity
    Double.NegativeInfinity
    Double.MaxValue > Double.PositiveInfinity // False

    //Generating Random Numbers
    var r = scala.util.Random
    r.nextInt
    r.nextFloat
    r.nextInt(100)
    // set the seed
    r.setSeed(1000L)
    // Random Character
    r.nextPrintableChar
    // Create Random range length
    var range = 1 to r.nextInt(10)
    range = 1 to r.nextInt(10)
    // use for loop and yield to modify Number
    println(for(i <- 0 to r.nextInt(10)) yield (i * r.nextFloat))
    println(for(i <- 1 to 5) yield r.nextInt(100))

    // Creating a Range, List or Array Numbers
    println("Creating a Range, List or Array Numbers")
    val range2 = 1 to 10
    val range3 = 1 to 10 by 2
    for(i <- 1 to 9 by 2) println(i)
    for(i <- 1 until 9 by 2) println(i)
    var x = (1 to 10).toArray
    x = (1 to 10).toArray
    var x2 = (1 to 10).toList

    //Formating Number and Currency
    val pi = scala.math.Pi
    print(f"$pi%1.5f")
    print(f"$pi%07.2f")
    "%06.2f".format(pi)
    var local = new java.util.Locale("de", "DE")
    val formatter = java.text.NumberFormat.getIntegerInstance(local)
    println(formatter.format(10000000))
    val formatter2 = java.text.NumberFormat.getInstance
    println(formatter2.format(10000.33))
    val formatter3 = java.text.NumberFormat.getCurrencyInstance
    println(formatter3.format(10000.33))
  }
}
