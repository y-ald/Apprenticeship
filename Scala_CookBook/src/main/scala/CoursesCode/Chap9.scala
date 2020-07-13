package CoursesCode

object Chap9 {
  def main(args: Array[String]): Unit = {

    //********************* Chapter 9: Functional Programming *********
    // Introduction
    //val greater = if (a > b) else ;
    /*val result = try {
      aString.toInt
    } catch {
      case _ => 0
    }*/
    val x = List.range(1, 10)
    // use function as variable
    val double = (i: Int) => {i * 2}
    double(3)
    x.map(double)
    // declare literals Function
    var add = (x: Int, y:Int) => {x + y}
    val add2: (Int, Int) => Int = (x, y) => x+y
    val addThenDouble: (Int, Int) => Int = (x, y) => x+y
    // USing methods like an anonymous function
    def modMethod(i: Int) = i%2 == 0
    val list = List.range(1, 10)
    list.filter(modMethod)
    // Assiging an existing function/ method to a function variable
    val c = scala.math.cos(_)
    // Defining a Method that a simple function
    /*def exec(f: () => Unit) {
      f()
    }// no argument
    def exec(f:(String) => Int): Unit = {
      f("l2")
    }*/// take argument
    def executeAndPrint(f:(Int, Int) => Int, x:Int, y: Int) {
      val result = f(x, y)
      println(result)
    }

    val sum = (x: Int, y: Int) => x + y
    executeAndPrint(sum, 2, 9)
    // User function as closure
    def exec(f:(String) => Unit, name: String){
      f(name)
    }
    var hello = "Hello"
    def sayHello(name: String) { println(s"$hello, $name")}
    exec(sayHello, "Al")

  }
}
