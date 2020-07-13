object Chap1  {
  def main (args: Array[String]) = {
    println("Hello World");
    /*
    *    CookBook Scala
    */


    /*
    * Chapter 1: Strings
    */

    // *****Introduction******
    // get variables class name
    "Hello, World".getClass.getName

    // Strings methods
    var sz = "Hello, world"
    //String length
    println(sz.length)
    // concatenate String
    sz = "Hello," + " world"

    //Strin with StringOps class properties
    //foreach methods to iterate on Strings
    "hello".foreach(println)
    //treat String as a sequence an iterate on it
    for(i <- "hello") println(i)
    // treat sequence of bytes
    sz.getBytes.foreach(println)
    //filter char on sequential collection
    val result = sz.filter(_ != 'l') // "l"(String) != 'l'(char)

    //Add Methods to Closed Classes
    // add own methods to the string Class
    "scala".drop(2).take(2).capitalize

    //Testing String Equality
    val s1 = "Hello"
    var s2 = "Hello"
    val s3 = "H" + "ello"
    println(s1 == s2)
    println(s2 == s3)
    // the == method doesn't throw a NullPointerException
    val s5: String = null
    println(s3 == s5)
    println(s5 == s3)
    // compare with case sensitive manner
    s2 = "hello"
    s1 == s2
    s1.toUpperCase == s2.toUpperCase
    //call methods on null String can throw null NullPointerException
    val s4: String = null
    s1.toUpperCase == s4.toUpperCase
    // use equalsIgnoreCase() to compare Strings
    val a = "Marisa"
    val b = "marisa"
    a.equals(b)
    a.equalsIgnoreCase(b)
    /*
    * in scala == methods test null value
    * and equals methods to compare object
    */

    //*******Creating Multiline String
    val foo = """this is
    a multiline
    String"""
    //aligne the differents lines
    val speech =  """Four score and
                    |seven years ago""".stripMargin
    val speech =  """Four score and
                    #seven years ago""".stripMargin('#')
    // convert multiline in to a continue line
    val speech = """Four score and
                   |seven years ago
                   |our fathers""".stripMargin.replaceAll("\n", " ")
    val speech = """Four score and
    seven years ago
    our fathers""".replaceAll("\n", " ")
    val speech = """Four score and
seven years ago
our fathers""".replaceAll("\n", " ")
    // introduce single or double quote without having to escape them
    val s = """This is known as a
              |"multiline" string
              |or 'heredoc' syntax""".stripMargin.replaceAll("\n", " ")

    // Splitting Strings
    "hello world".split(" ").foreach(println)
    //split string on sumply character
    var sa = "eggs, milk, butter, Coco Puffs"
    sa.split(",")
    sa.split(",").map(_.trim) // remove blank above
    //split string base on a regular expression
    "hello world, this is Al".split("\\s+")
    "hello world, this is Al".split("\\a+")

    //Substituting Variables into Strings
    val name1 = "Fred"
    val age1 = 33
    val weight = 200.00
    println(s"$name1 is $age1 years old, and weighs $weight pounds.")
    // using expression in string literals
    println(s"Age next year: ${age1 + 1}")
    println(s"You are 33 years old: ${ age1 == 33}")
    //using curly braces when printing object fields
    case class Student(name: String, score: Int)
    val yald = Student("Yald", 100)
    println(s"${yald.name} has a score of ${yald.score}")
    println(s"$yald.name has a score of $yald.score") // wrong because is a methods not Variables

    //the f string interpolator
    println(f"$name1 is $age1 years old, and weighs $weight%.2f pounds")
    println(f"$name1 is $age1 years old, and weighs $weight%.0f pounds")
    // another use of String
    val out = f"$name1 is $age1 years old, and weighs $weight%.0f pounds"
    // the raw interpolator
    s"foo\nbar"
    raw"foo\nbar"
    // another Methods
    val name = "Fred"
    val age  =  33
    val s = "%s is %f years old".format(name, age)
    //override a method, override def toString: String = "%s, %s, age %d".format()
    // Processing string one character at a time
    val upper =  "hello, world".map(c => c.toUpper)
    val upper =  "hello, world".map(_.toUpper)
    val upper =  "hello, world".filter(_ != 'l').map(_.toUpper)
    // alternative to map Methods
    for(c <- "hello") println(c)
    val upper = for(c <- "hello, world") yield c.toUpper
    val result = for {
      c <- "hello, world"
      if c != 'l'
    } yield c.toUpper
    "helllo".foreach(println)

    //Understanding how map works
    "HELLO".map(c =>(c.toByte+32).toChar)
    "HELLO".map(c =>
      (c.toByte+32).toChar
    )
    //Use own function with map
    def toLower1(c: Char) = println((c.toByte+32).toChar)
    "HELLO".foreach(toLower1)
    def toLower(c: Char): Char = (c.toByte+32).toChar
    "HELLO".map(toLower)

    // Finding Pattern in Strings
    var numPattern = "[0-9]+".r
    var adress = "123 Main street Suite 101"
    var match1 = numPattern.findFirstIn(adress)
    var matches = numPattern.findAllIn(adress)
    matches.foreach(println)
    matches = numPattern.findAllIn(adress).toArray
    // use orther methods to regex expression
    import scala.util.matching.Regex
    numPattern = new Regex("[0-9]+")
    adress = "123 Main street Suite 101"
    match1 = numPattern.findFirstIn(adress)
    matches = numPattern.findAllIn(adress)
    //Deal with some
    val result = numPattern.findFirstIn(adress).getOrElse("no match")
    match1 match{
      case Some(s) => println(s"Found: $s")
      case None => "no match"
    }
    match1.foreach{e =>
      println(s"Found a match: $e")
    }

    //Replacing Patterns in Strings
    val address = "123 Main Street".replaceAll("[0-9]", "x")
    var regex = "[0-9]".r
    val newAddress = regex.replaceAllIn("123 Main Street", "x")
    val result = "123".replaceFirst("[0-9]", "x")
    regex = "H".r
    val result = regex.replaceFirstIn("Hello, World H", "J")

    // Extracting Parts of String that match pattern
    var pattern = "([0-9]+) ([A-Za-z]+)".r
    val pattern (count, fruit) = "100 Bananas"
    val pattern = "([A-Za-z]+) ([0-9]+)".r
    val pattern (count, fruit) = "Bananas 100"

    //Accessing a Character in a String
    //charAt
    "hello".charAt(0)
    "hello".charAt(1)
    "hello"(1)
    "hello".apply(1)

    // Add own methods to String class
    implicit class StringImprovements(s: String){
      def increment = s.map(c => (c + 1).toChar)
    }
    val result = "HAL".increment

    implicit class TestStringImprovements(s: String){
      def incrment = s.map(c => (c + 1).toChar)
    }
    val result = "HAL".incrment

    // Add methods more safely in an object



  }

}