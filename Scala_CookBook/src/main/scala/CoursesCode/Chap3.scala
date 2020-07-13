package CoursesCode

object Chap3 {
  def main(args: Array[String]): Unit = {
    // ************** Chap 3 Control Structures
    // Introduction
    //val x = if (a) y else z
    //for loop
    /*for{
      line <- get.lines
      char <- line
      if char.isLetter
    }*/
    //looping over a map
    val names = Map("fname" -> "Robert",
      "lname" -> "Goren")
    for ((k,v) <- names) println(s"key: $k, value: $v")
    (1 to 10).withFilter(i => i !=2).foreach(println)
    (1 to 10).withFilter(i => i%2 == 0).foreach(println)

    //Using loop with mutilple counter
    for(i <- 1 to 2; j <- 1 to 2) println(s"i = $i, j=$j")
    for{
      i <- 1 to 2
      j <- 1 to 2
      k <- 1 to 10
    } println(s"i = $i, j=$j, k=$k")
    // multidimensional Array
    var array = Array.ofDim[Int](2,3)
    array = Array.ofDim[Int](2,2)
    array(0)(0) = 0
    array(0)(1) = 1

    for{
      i <- 0 until 2
      j <- 0 until 3
    } array(i)(j) = 1

    // Using loop with Embedded if statements
    import util.control.Breaks._
    breakable {
      for (q <- 1 to 10) {
        println(q)
        if(q > 4) break
      }
    }

    for (q <- 1 to 10) {
      breakable {
        if(q != 1) break else println(q)
      }
    }
    // inner outer break;

      import scala.util.control._

      val Inner = new Breaks
      val Outer = new Breaks

      Outer.breakable {
        for (i <- 1 to 5) {
          Inner.breakable {
            for (j <- 'a' to 'e') {
              if (i == 1 && j == 'c') Inner.break else println(s"i: $i, j: $j")
              if (i == 2 && j == 'b') Outer.break
            }
          }
        }
      }

    // Use return instead of break
    // implement recursive tail
    def factorial(n: Int): Int = {
      if(n == 1) 1
      else n * factorial(n - 1)
    }

    import scala.annotation.tailrec

    def factorial2(n: Int): Int = {
      @tailrec def factorialAcc(acc: Int, n: Int) {
        if (n <= 1) acc
        else factorialAcc(n * acc, n - 1)
      }
      factorialAcc(1, n)
    }
    // Using @switch annotation

    /*import scala.annotation.switch
    var i = 1
    var x = (i: @switch) match {
      case 1 => "yes"
      case b: Int => "Go"
      case b: List[_] => "Go"
      case _ => "Noo" // case default => println(default) to catch the name
    }
    // Map strucuture rather than switch case statements
    val month = Map(
      1 -> "January",
      2 -> "February",
      3 -> "Mars",
      5 -> "Mai"
    )
    val monthName = month(4) // month.get(), month.apply(1)

    // Matching Multilple condition with the case statements
    x = i match {
      case 0 => "zero"
      case Nil => "an empty List"
      case List(0, _, _) => "a three element list with 0 as the first element"
      case List(1, _*) => "a List beginning with, 1 having any number of elements"
      case Vector(1, _*) => "a Vector beginning with, 1 having any number of elements"
      case (a, b) => s"got $a and $b"
      case Dog("Suka") => "goungef"
      case as: Array[String] => s"an array of string: ${as.mkString(",")}"
      case d: Dog => s"dog: ${d.name}"
      case m: Map[_, _] => m.toString
    }
    // use variable with case Pattern$
    x = ("ananas" :: "Pommes" :: "banane" :: Nil)
    x match {
      case list: List[_] => println(s"thiis $list")
      case list: List("ananas", _*) => println(s" yeah $list") // not found
      case _ => "else"
    }
    x match {
      //case list: List[_] => println(s"thiis $list")
      case list @ List("ananas", _*) => println(s" yeah $list") // not found
      case some @ Some(_) => ""
      case _ => "else"
    }
    val match1 = Some("x")
    match1 match {
      //case se: Some("x") => "some"
      case se @ Some(_) => ""
      case _ => "else"
    }
    val match2 = "stop"
    match2 match {
      case "start" | "go" => println("starting")
      case "stop" | "quit" =>
    }
    // Adding Guard to case statements
    i = 21
    i match {
      case a if 0 to 9 contains a => println("0-9 range: "+ a)
      case x if (x == 2 || x == 3) => x
      case a if 10 to 19 contains a => println("10-19 range: "+ a)
      case a if 20 to 29 contains a => println("20-29 range: "+ a)
      case _ => println("Humm ..")
    }

    def speak(p: Person) = p match {
      case Person(name) if name == "Fred" => println("Yubba dubba doo")
      case Person(name) if name == "Bam Bam" => println("bam")
      //case x if (x.symbil == "XYZ" && x.price > 50) => buy(x)
      //case Person(name) => if (name == "Fred") println("Yubba dubba doo")
      //else if (name == "Bam Bam") println("bam bam!")
    }

    //Using Match Expresion instead of isInstanceOf
    def isPerson(x: Any): Boolean = x match {
      case p: Person => true
      case _ => false
    } // o.isInstanceOf[Person]

    //working with a list in a Match Expression
    val match3 = List(1, 2, 3)
    val y = 1 :: 2 :: 3 :: Nil
    val fruits = "Apples" :: "Bananas" :: "Oranges" :: Nil
    def listToString(list: List[String]): String = list match{
      case s :: rest => s + " " + listToString(rest)
      case Nil => ""
    }
    def sum(list: List[Int]): Int = list match {
      case Nil => 1
      case n :: rest => n + sum(rest)
    }

    // matching One or more Exception
    try{

    } catch {
      case e:
      case e:
    }
    // for manipulate expcetion of any types
    try {
      val i = s.toInt
    } catch {
      case _: Throwable => println("Exception ignored") // throw _.printStackTrace()
    }*/

    //don't have to specify that methods throw exception, but you can declare the exception you want you method throw
    @throws(classOf[NumberFormatException])
    def toInt(s: String): Option[Int] =
      try{
        Some(s.toInt)
      } catch {
        case e: NumberFormatException => throw e
      }

    //Create own control strucuture
    def doubleif(test1: => Boolean)(test2: => Boolean)(codeBlock: => Unit){
      if(test1 && test2) {
        codeBlock
      }
    }
    //doubleif(age > 18)(numAc == 0) { println("Discount!")}
  }
}
