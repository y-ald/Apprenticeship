package CoursesCode

object Chap8 {
  def main(args: Array[String]): Unit = {
    //************************ Chapter 8: Traits *************
    // object extends trait
    trait Debugger {
      def log(message: String) {
        //
      }
    }
    //val child = new child with Debugger
    // Ensuring Trait be added to to type that has specific MEthods
    trait WarpCore {
      this: { def ejec(passwd: String)
        def star: Unit
      } =>
    }
  }
}
