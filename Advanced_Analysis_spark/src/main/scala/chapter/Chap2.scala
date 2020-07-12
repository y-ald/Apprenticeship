package chapter

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/*
* Chap 2: Recommending Music and the AudioScrobbler Data set
* */
object Chap2 extends {
  def main(args: Array[String]) = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("SparkApp")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder
      .master("local[*]")
      //.appName("my-spark-app")
      .getOrCreate()
    //Preparing the Data
    val rawUserArtistData = spark.read.textFile("./data/chap3Data/profiledata_06-May-2005")
    rawUserArtistData.take(5).foreach(println)

    

  }
}
