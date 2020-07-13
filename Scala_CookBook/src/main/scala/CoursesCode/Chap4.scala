package CoursesCode

object Chap4 {
  def main(args: Array[String]): Unit = {
    // ***************** Chap 4: Classes and properties
    // Creating a primary constructor
    class Person(var firstName: String, var lastName: String) {
      println("the constructor begins")

      // some class fields
      private val HOME =  System.getProperty("user.home")
      var age = 0

      //some methods
      override def toString = s"$firstName $lastName is $age years old"
      def printHome { println(s"Home = $HOME") }
      def printFullName { println(this) }

      printHome
      printFullName
      println("Still in the constructor")
    }
    val person1 = new Person("Adan", "Meyer")
    // change argument
    person1.firstName = "Scott"
    person1.lastName = "Jones"

    println("show the person argument")
    println("person1.firstName", person1.firstName)
    println("person1.lastName", person1.lastName)
    //
    class Person(var firstName: String,var lastName: String) {
      println("the constructor begins")

      // some class fields
      private val HOME =  System.getProperty("user.home")
      var age = 0

      //some methods
      override def toString = s"$firstName $lastName is $age years old"
      def printHome { println(s"Home = $HOME") }
      def printFullName { println(this) }

      printHome
      printFullName
      println("Still in the constructor")
    }
    val person2 = new Person("Adan", "Meyer")
    // change argument
    person2.firstName = "Scott" // didn't works cause of val and var
    person2.lastName = "Jones"  // didn't works cause of val and var

    println("person2.firstName", person2.firstName) // didn't works cause of val and var
    println("person2.lastName", person2.lastName) // didn't works cause of val and var

    //controlling visibility of Constructor fields
    //var => getters and setters
    //val => getters
    //pivate => hidden
    //  case class => val by default

    //defining Auxialiary Constructor
    class Pizza (var crustSize: Int, var crustType: String) {

      // one-arg auxiliary constructor
      def this(crustSize: Int) {
        this(crustSize, Pizza.DEFAULT_CRUST_TYPE)
      }

      // one-arg auxiliary constructor
      /*def this(crustType: String) {
        this(Pizza.DEFAULT_CRUST_SIZE, crustType)
      }*/

      // Zero_arg auxiliary constructor
      def this() {
        this(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
      }
      def this(crustType: String) {
        this(Pizza.DEFAULT_CRUST_SIZE)
        this.crustType = crustType
      }

      override def toString = s"A $crustSize inch pizza with a $crustType crust"
    }

    object Pizza {
      val DEFAULT_CRUST_SIZE = 12
      val DEFAULT_CRUST_TYPE = "THIN"
    }

    val p1 = new Pizza(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
    val p2 = new Pizza(Pizza.DEFAULT_CRUST_SIZE)
    val p3 = new Pizza(Pizza.DEFAULT_CRUST_TYPE)
    val p4 = new Pizza

    // Generating Auxialiary Constructor for case Classes
    case class Person(name: String, age: Int)

    object Person{

      def apply() = new Person("No name", 0)
      def apply(age: Int) = new Person("No name", age)
    }

    val a = Person()
    val b = new Person("No name", 0) // didn't give
    val c = Person(20)

    // Defining Private primary Constructor
    class Person private (name: String)

    val p = new Person("Mercedes")

    class Brain private {
      override def toString = s"This is brain"
    }
    object Brain {
      val brain = new Brain
      def getInstance = brain
    }
    val b = Brain.getInstance

    // Create class with default value
    class Socket(val time: Int = 1000)
    val a = new Socket
    a.time
    val b = new Socket(5000)

    class Socket(val time: Int){
      def this() = this(1000)
    }

    // Overriding Default Accessors and Mutators
    class Person(private var _name: String) { // private is verry important to hide the underscore
      def name = _name // acessor
      def name_=(bme: String) { _name = bme} // Mutators
    }
    val person3 = new Person("Jonathan")
    person3.name = "Jony"
    println("person3.name", person3.name)

    // Preventing Getter and Setter MEthods from Being Generating
    class Stock {
      var delayedPrice: Double =  _
      private var currentPrice: Double = _ // didn't have acces
      private[this] value = _ // just for this value
    }

    // assignig a field to a Block or Function
    class Foo {

      val text = {
        var lines = ""
        try {
          lines = io.Source.fromFile("/etc/passwd").getLines.mkString
        } catch {
          case e: Exception => lines = "Error happened"
        }
        lines
      }
      println(text)
    }

    object Test extends App {
      val f = new Foo
      lazy val text  = io.Source.fromFile("/etc/passwd").getLines.foreach(println)
    }

    case class Address (city: String, state: String)

    class Person (var name: String, var address: Address) {
      override def toString = if (address == null) name else s"$name @ $address"
      def test = println("test person")
    }

    class Employee (var name: String, var address: Address) extends Person(name, address) {
      override def toString = if (address == null) name else s"$name @ $address"
    } // some problem preferable to don't use var because it will create another getter and setter, beter to heriter de la classe mère

    class Employee (name: String, address: Address, var nbrAnnée: Int) extends Person(name, address) {
      override def toString = if (address == null) name else s"$name @ $address"
    }

    // Calling Super class constructor
    // Use auxiliary constructor in the Super class and the Subclass to define default behaviour
    // trait didn't take argument, and be aware that class can't only acces abstract classe
    abstract class BaseController(db: Database) {
      def save {db.save}
      def update {db.update}

      // abstract methods cause the body is not define
      def connect

      // abstract method
      def getStatus: String

      // abstract methods
      def setServerName(severName: String)
    }
    // need to implement all the abstract method when we call her
    // class extends only one abstract class

    // Defining Properties in an abstract base class
    case class Person (name: String, age: Int)
    val emily = Person("Emily", 2)
    val paul = Person("Paul", 18)
    emily.name
    emily.name = "jonas"
    case class Company (var name: String)
    val c = Company("Apple")
    c.name = "yald_corp"
    emily == paul
    // extract information cause unapply methods is Create
    emily match { case Person(n, r) => println(n, r)}
    // case class have copy function, helpful to copy fields
    case class Employee (name: String, loc: String, role: String)
    val e1 = Employee("fred", "paris", "manager")
    val e2 = e1.copy()
    val e3 = e1.copy(name="Joe", role="mechanic")
    e1
    e3
    // defining equals method
    class Person (name: String, age: Int) {
      def canEqual(a: Any) = a.isInstanceOf[Person]
      override def equals(that: Any): Boolean =
        that match {
          case that: Person => that.canEqual(this) && this.hashCode == that.hashCode
          case _ => false
        }
      override def hashCode:Int ={
        val prime = 31
        var result = 1
        result = prime * result + age
        result = prime * result + (if (name == null) 0 else name.hashCode)
        return result
      }
    }

    val nimoy = new Person("LE", 82)
    val nimoy2 = new Person("LE", 82)
    val e =  new Person("LE", 20)
    nimoy == nimoy2
    assert(nimoy == e)
    // Benief its also use to comparare parent en child classes
    // Creating Inner classes
    class PandorasBox {

      case class Thing (name: String)

      var things =  new collection.mutable.ArrayBuffer[Thing]()
      things += Thing("Evil thing #1")
      things += Thing("Evil Thing #2")

      def addThing(name: String) { things += new Thing(name) }
    }
    val p = new PandorasBox
    p.things.foreach(println)
    p.addThing("Evil thing #3")
    p.things.foreach(println)

    object ClassInObject extends App {

      // inner classes are bound to the Object
      val oc1 = new OuterClass
      val oc2 = new OuterClass
      val ic1 = new oc1.InnerClass
      ic1.x = 10
      println(s"ic1.x = ${ic1.x}")

      class OuterClass {
        class InnerClass {
          var x = 1
        }
      }
    }

    println(new OuterObject.Innerclass().x)
    println(new OuterClass().InnerObject.y)

    object OuterObject {
      class InnerClass {
        var x = 1
      }
    }

    class OuterClass {
      object InnerObject {
        val y = 2
      }
    }

  }
}
