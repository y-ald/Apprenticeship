package chapter

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


/*
Chapter 7: Aggregations
*/

object chap7 {
  def main(args: Array[String]) = {
    val conf = new SparkConf()
      .setAppName("app")
      .setMaster("local[*]")

    val sc = new SparkContext(conf)
    val spark = SparkSession.builder.
      master("local")
      .appName("spark session example")
      .getOrCreate()

    val df = spark.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("./data/retail-data/all/*.csv")

    df.cache()
    df.createGlobalTempView("dfTable")
    println(df.count())

    // Aggregation Functions
    df.select(count("StockCode")).show()
    df.select(count("*")).show()
    //countDisctinct
    df.select(countDistinct("StockCode")).show()




  }
}
