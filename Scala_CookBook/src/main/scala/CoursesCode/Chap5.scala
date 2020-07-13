package CoursesCode

object Chap5 {
  def main(args: Array[String]): Unit = {
    // ************************* Chapter 5 Methods *************************
    // Controlling Method Scope
    // Object private scope
    /*class Foo {
      private[this] def isFoo = true

      def doFoo(other: Foo) {
        if(other.isFoo) // didin't work
      }
    }

    // Private scope
    class Foo {
      private def isFoo = true

      def doFoo(other: Foo) {
        if(other.isFoo) {

        }
      }
    }
    // didn't avaible in Subclass
    Class Animal {
      private def heartBeat {}
    }

    Class Dog extends Animal {
      heartBeat // // won't compile cause isn't in the class , but extends works
    }
    // Proceted Scope
    class Animal {
      protected def breathe {}
    }
    class Dog extends Animal {
      breathe
    }
    //
    package world{
      class Animal {
        protected def breathe {}
      }
      class Jungle {
        val a = new animal
        a.breathe // wouldn't give cause is external class and not a subclas
      }
    }

    // Package scope
    package com.acme.coolapp.model {

      class Foo {
        private[model] def doX {}
        private def doY
      }

      class Bar {
        val f = new Foo
        f.doX // compiles
        f.doY // won't compiles
      }
    }
    // More package-level control
    package com.acme.coolapp.model {
      class Foo {
        private[model] def doX {}
        private[coolapp] def doY {}
        private[acme] def doZ {}
      }
    }

    import com.acme.coolapp.model._

    package com.acme.coolapp.view {
      class Bar {
        val f = new Foo
        f.doX // won't compile
        f.doY
        f.doZ
      }
    }
    // Calling a Method on a SuperClass
    trait Human {
      def hello = "the Human trait"
    }

    trait Mother extends Human {
      override def hello = "Mother"
    }

    trait Father extends Human {
      override def hello = "Father"
    }

    class Child extends Human with Mother with Father {
      def printSuper  = super.hello
      def printMother = super[Mother].hello
      def printFather = super[Father].hello
      def printHuman =  super[Human].hello
    }

    val c = new Child
    println(s"c.printSuper = ${c.printSuper}")
    println(s"c.printMother = ${c.printMother}")
    println(s"c.printFather = ${c.printFather}")
    println(s"c.printHuman = ${c.printHuman}")
    // this manner of call superClass works only when you exends the parent classe
    trait Animal {
      def walk { println("Animal is walking") }
    }

    class FourLeggedAnimal extends Animal {
      override def walk { println("I'm walking on all fours") }
    }

    class Dog extends FourLeggedAnimal {
      def walThenRun {
        super.walk
        super[FourLeggedAnimal].walk
        super[Animal].walk // won't compile
      }
    }*/

    // Setting Default value for method parameters
    class Connection {
      def makeConnection(timeout: Int = 5000, protocol: String = "http"){
        println("timeout = %d, protocol = %s".format(timeout, protocol))
      }
    }

    val connection =  new Connection
    connection.makeConnection()
    connection.makeConnection(2000)
    //connection.makeConnection("https") // didn't give
    connection.makeConnection(protocol="https")
    connection.makeConnection(3000, "https")
    // Using Parameters Names when Calling a Methods
    class Pizza {
      var crustSize = 12
      var crustType = "Thin"
      def update(crustSize: Int, crustType: String) {
        this.crustSize = crustSize
        this.crustType = crustType
      }
      override def toString = {
        "A %d inch %s crust pizza".format(crustSize, crustType)
      }
    }
    val pizza = new Pizza
    pizza.update(crustSize = 16, crustType = "Thick")

    // Defining a Method that return multiple Items (Tuples)
    def getStockInfo = {
      ("NFLX", 100.00, 101.00)
    }
    val (symbol, currentPrice, bidPrice) = getStockInfo
    // return value in a wrapper class
    val result = getStockInfo
    result._1
    result._2
    // Forcing Callers to Leave Parenthese off accesor
    class Pizza {
      def CrustSize = 12
    }
    val p = new Pizza
    //p.CrustSize() // wouldn't compile
    p.CrustSize
    // Creating Methods that take Variables arguement
    def printAll(strings: String*) {
      strings.foreach(println)
    }
    printAll()
    printAll("hello")
    printAll("hello", "paul")
    // Use _* to adapt a sequence
    val fruits = List("apple", "banana", "cherry")
    printAll(fruits: _*)
    // varargs fields must be the last file of the agurment
    def printAll2(nbr: Int, strings: String*) {
      println(s"int = $nbr, ${strings.foreach(print)}")
    }
    printAll2(1, "banae","heo")

    // Declaring That method can throw an Exception
    /*@throws(classOf[Exception])
    @throws(classOf[LineUnavailabeleException])
    override play {

    }*/
  }

}
