package main.scala.learningSpark

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Test {
    def main(args: Array[String]) {
        val spark = SparkSession
        .builder()
        .appName("TestScala")
        .config("spark.master", "local")
        .getOrCreate()
        
        // Get the M&M data set filename
        println("############################okay")
    }
}
