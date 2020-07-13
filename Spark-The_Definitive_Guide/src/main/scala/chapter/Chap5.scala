package chapter

package chapter

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
import org.apache.spark.sql.types.Metadata
import org.apache.spark.sql.functions._

object Chap5 {
  def main(args: Array[String]) = {
    /*
    * Chap 5: Basic Structured Operations
     */
    val conf = new SparkConf()
      .setAppName("app")
      .setMaster("local[*]")

    val sc = new SparkContext(conf)
    val spark = SparkSession.builder.
      master("local")
      .appName("spark session example")
      .getOrCreate()
    val df = spark.read.format("json")
      .load("./data/flight-data/json/2015-summary.json")
    df.show()

    //look at the schema
    df.printSchema()
    println(spark.read.format("json")
      .load("./data/flight-data/json/2015-summary.json")
      .schema)

    val myManualaSchema = StructType(Array(
      StructField("DEST_COUNTRY_NAME", StringType, true),
      StructField("ORIGIN_COUNTRY_NAME", StringType, true),
      StructField("count", LongType, false,
        Metadata.fromJson("{\"hello\":\"world\"}"))
    ))
    // explicit column references
    df.col("count")

    // columns as expressions
    //expr("someCol - 5") == col("someCol") - 5 == expr("someCol" - 5)

    // Accessing a DataFrame's Columns
    println(spark.read.format("json")
      .load("./data/flight-data/json/2015-summary.json")
      .columns)

    //Records and Rows
    println(df.first())
    // Creating Row
    val myRow =  Row("Hello", null, 1, false)
    // Accesing data in row
    println(myRow(0))
    println(myRow(0).asInstanceOf[String])
    println(myRow.getString(0))
    println(myRow.getInt(2))
    println()

    // DataFrame Transformations
    // Create a DataFrame
    val myManualSchema = new StructType(Array(
      new StructField("some", StringType, true),
      new StructField("col", StringType, true),
      new StructField("names", StringType, false)
    ))
    val myRows = Seq(Row("Gelo", null, "1L"))
    val myRDD = spark.sparkContext.parallelize(myRows)
    val myDf = spark.createDataFrame(myRDD, myManualSchema)
    myDf.show()
    // take advantage of spark implicit in scala
    //spark.implicits
    //val myDF = Seq(("Hello", 2, 1L)).toDF("col1", "col2", "col3")
    /*df.select(
      df.col("DEST_COUNTRY_NAME"),
      col("DEST_COUNTRY_NAME"),
      column("DEST_COUNTRY_NAME"),
      'DEST_COUNTRY_NAME,
      //$"DEST_COUNTRY_NAME",
      expr("DEST_COUNTRY_NAME"))
      .show(2)*/
    df.select(expr("DEST_COUNTRY_NAME AS destination")).show(2)
    df.select(expr("DEST_COUNTRY_NAME AS destination").alias("DEST_COUNTRY_NAME"))
      .show(2)
    df.selectExpr("DEST_COUNTRY_NAME as newColumnName", "DEST_COUNTRY_NAME").show(2)
    df.selectExpr("*", "(DEST_COUNTRY_Name = ORIGIN_COUNTRY_NAME) as withinCountry")
      .show(2)
    df.selectExpr("avg(count)", "count(distinct(DEST_COUNTRY_NAME))")
      .show(2)

    // Converting to Spark Types Literals
    df.select(col("*"), lit(1).as("One")).show(2)
    df.withColumn("withinCountry", expr("DEST_COUNTRY_NAME == ORIGIN_COUNTRY_NAME")).show(2)
    //Renamed columns
    df.withColumnRenamed("DEST_COUNTRY_NAME","dest").show(2)

    //Reserved Characters and Keywords
    val dfWithLongColName = df.withColumn(
      "THis Long Column-Name",
      expr("ORIGIN_COUNTRY_NAME")
    )
    dfWithLongColName.selectExpr(
      "`This Long Column-Name`",
      "`This Long Column-Name` as `new col`"
    ).show(2)

    // Case Sensitivity
    //set spark.sql.caseSensitive true  -- in Sql

    // Removing columns
    df.drop("ORIGIN_COUNTRY_NAME", "DEST_COUNTRY_NAME")

    //changing a Column's type cast
    df.withColumn("count2", col("count").cast("long"))

    //filtring spark
    df.where("count < 2")
    df.where(col("count") < 2)
      .where(col("ORIGIN_COUNTRY_NAME") =!= "Croatia")
      .show(2)

    //Getting Unique Rows
    println(df.select("ORIGIN_COUNTRY_NAME", "DEST_COUNTRY_NAME").distinct().count())

    // Random Samples
    val seed = 5
    val withReplacement = false
    val fraction = 0.5
    println(df.sample(withReplacement, fraction, seed).count())
    //Random Split 
    val dataFrames = df.randomSplit(Array(0.25, 0.75), seed)
    // Concatenating and Appeending Row
    val schema = df.schema
    val newRows = Seq(
        Row("New Country", "Other Country", 5L),
      Row("New", "Other Country 3", 1L)
    )
    val parallelizedRows = spark.sparkContext.parallelize(newRows)
    val newDF =spark.createDataFrame(parallelizedRows, schema)
    df.union(newDF)
      .where("count = 1")
      .where(col("ORIGIN_COUNTRY_NAME") =!= "United States")
      .show()

    // Sorting Rows
    df.sort("count").show(5)
    df.orderBy("count","DEST_COUNTRY_NAME").show(5)
    df.orderBy(col("count"), col("DEST_COUNTRY_NAME")).show(5)
    df.orderBy(expr("count desc")).show(2)
    df.orderBy(desc("count"), asc("DEST_COUNTRY_NAME")).show(2)
    // asc_nulls_firsy, desc_nulls_last to specify where you would like your null values to appear in an ordered DataFrame
    // sort partition at the reading for optimisation
    spark.read.format("json").load("./data/flight-data/json/*-summary.json")
      .sortWithinPartitions("count")

    // limit
    df.orderBy(expr("count desc")).limit(6).show()

    // Repartition and Coalesce
    println(df.rdd.getNumPartitions)
    df.repartition(2, col("DEST_COUNTRY_NAME"))

    // Collection Rows to the Driver
    val collectDF = df.limit(10)
    collectDF.take(5)
    collectDF.show()
    collectDF.collect()
    collectDF.toLocalIterator()

  }
}
