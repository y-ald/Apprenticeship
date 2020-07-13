package CoursesCode

import com.sun.javafx.css.parser.Recognizer
import javax.sound.sampled.{DataLine, TargetDataLine}
import org.apache.log4j.lf5.viewer.configure.ConfigurationManager

object Chap6 {
  def main(args: Array[String]): Unit = {
    //******************* Chapter 6 Object Casting ***************
    // Object Casting
    /*val cm = new ConfigurationManager("config.xml")
    val recognizer = cm.lookup("recognizer").asInstanceOf[Recognizer]
    val microphone = cm.lookup("microphone").asInstanceOf[Microphone]
    val a = 10
    val b =  a.asInstanceOf[Long]*/
    val objects = Array("a", 1)
    val arrayOfObject = objects.asInstanceOf[Array[Object]]
    // scala Equivalent of java's class
    val info = new DataLine.Info(classOf[TargetDataLine], null) // won't give
    val stringClass = classOf[String]
    stringClass.getMethods
    // Determining the Class of an object
    val hello = <p>Hello, world</p>
    hello.child
    hello.child.foreach(e => println(e.getClass))
    // Launching an Application with object
    object Hello extends App {
      if(args.length == 1)
        println(s"Hello, ${args(0)}")
      else
        println("I didn't get your name")
    }

    object Hello2 {
      def main(args: Array[String]) {
        println("Hello World")
      }
    }
    // Creating Singletons with object
    import java.util.Calendar
    import java.text.SimpleDateFormat

    object DateUtils {

      // as "Thursday, November 29"
      def getCurrentDate: String = getCurrentDateTime("EEEE, MMMM d")

      // as "6:20 p.m"
      def getCurrentTime: String = getCurrentDateTime("K:m aa")

      private def getCurrentDateTime(dateTimeFormat: String): String = {
        val dateFormat = new SimpleDateFormat(dateTimeFormat)
        val cal = Calendar.getInstance()
        dateFormat.format(cal.getTime())
      }
    }
    val c  = DateUtils.getCurrentTime
    val b = DateUtils.getCurrentDate
    // create case of object for Singletons
    case object StartMessage
    case object StopMessage

    // deal with non static (instance) and static member
    class Pizza (var crustType: String) {
      override def toString = "Crust type is " + crustType
    }
    object Pizza {
      val CRUST_TYPE_THIN = "thin"
      val CRUST_TYPE_THICK = "thick"
      def getFoo = "Foo"
    }
    var p = new Pizza(Pizza.CRUST_TYPE_THICK)
    println(p)
    // object can acces private field in the class
    class Foo {
      private val secret = 2
      override def toString = " "+secret
    }
    object Foo {
      def double(foo: Foo) = foo.secret * 2
    }
    val f = new Foo
    println(Foo.double(f))

    // putting Common code in Package Object
    // creating new instance object without new keyword
    class Person {
      var name: String = _
    }

    object Person {
      def apply(name: String): Person = {
        var p = new Person
        p.name = name
        p
      }
    }
    val a = Array(Person("Dan"), Person("Elijah"))

    trait Animal {
      def speak
    }
    object Animal {
      private class Dog extends Animal {
        override def speak { println("woof") }
      }
      private class Cat extends Animal {
        override def speak  { println("meow") }
      }
      def apply(s: String): Animal = {
        if (s == "dog") new Dog
        else new Cat
      }
    }
  }
}
