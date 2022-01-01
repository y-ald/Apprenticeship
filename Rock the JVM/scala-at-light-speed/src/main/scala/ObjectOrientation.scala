object ObjectOrientation extends App {

    //class and instance
    class Animal {

        //define fields
        val age: Int = 0

        //define methods
        def eat() = println("I'm eating")
    
    }

    val anAnimal = new Animal
    class Dog(val name:String) extends Animal // constructor definition
    val aDog = new Dog("Lassie")

    // constructor arguments are Not fields // need to put val before constructeur argument not see outside f
    aDog.name 

    //subtype polymorphism
    vam aDeclaredAnimal: Animal = new Dog("Hachi")
    aDeclaredAnimal.eat()

    // abstract class 
    abstract class WalkingAnimal {
        val hasLegs = true // by default public, can restrict by private val hasLeg
        // procteted class and descendent have acces to method of the class
        // private .. only the class have access
        def walk(): Unit // not instatiate just override after
    }

    // "interface" = ultimate abstract type 
    trait Canivore {
        def eat(animal: Animal): Unit
    }

    class Crocodile extends Animal with Canivore { //mixed many trait

    }

}