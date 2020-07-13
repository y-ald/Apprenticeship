package CoursesCode
//importing one or more member
import java.io.File
import java.io.IOException
import java.io.{File, IOException}
import java.io._
import java.util.Random
// renaming MEmbers in import
import java.util.{Date => JDate, HashMap => JHashMap}

object Chap7 {
  def main(args: Array[String]): Unit = {
    // ******************** Chapter 7: package and import ****************

    // Packaging with the Curly Braces Style Notation
    /*package com.acme.store {
      class Foo { override def toString =  "I am com.acme.store.Foo"}
    }

    package orderentry
    class Foo { override def toString = "I am com.acme.store.Foo"}

    package customers {
      class Foo { override def toString =  "I am com.acme.store.Foo"}
      package dataset {
        class Foo { override def toString =  "I am com.acme.dataset.Foo"}
      }
    }
    println(new orderentry.Foo)
    println(new customers.Foo)
    println(new customers.dataset.Foo)
    */


    val map = new JHashMap[String, String] // didn t compile
    import System.out.{println => p}
    p("hello")

    // Hiding a class during the import process
    import java.util.{Random => _,List => _, _}
    val r = new Random // Error



  }
}
