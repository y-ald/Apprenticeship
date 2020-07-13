package chapter

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
/*
chap 6: WXorking with different data
*/


object chap6 {
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
      .load("./data/retail-data/by-day/2010-12-01.csv")
    df.printSchema()
    df.createGlobalTempView("dfTable")

    // coverting to Spark Types
    df.select(lit(5), lit("five"), lit(5.0))

    //Working with Booleans
    df.where(col("InvoiceNo").equalTo(536365))
      .select("InvoiceNo", "Description")
      .show(5, false)

    df.where(col("InvoiceNo")===536365)
      .select("InvoiceNo", "Description")
      .show(5, false)
    // expressing conditions in a string
    df.where("InvoiceNo = 536365")
        .show(5, false)
    df.where("InvoiceNo <> 536365")
      .show(5, false)
    //
    val priceFilter = col("UnitPrice") > 600
    val descripFilter = col("Description").contains("POSTAGE")
    df.where(col("StockCode").isin("DOT")).where(priceFilter.or(descripFilter))
      .show()
    // specify boolean column
    val DOTCodeFilter = col("StockCode") === "DOT"
    df.withColumn("isExpensive", DOTCodeFilter.and(priceFilter.or(descripFilter)))
      .where("isExpensive")
      .select("unitPrice", "isExpensive").show(5)

    df.withColumn("isExpensive", not(col("UnitPrice").leq(250)))
      .filter("isExpensive")
      .select("Description", "UnitPrice").show(5)
    df.withColumn("isExpensive", expr("NOT UnitPrice <= 250"))
      .filter("isExpensive")
      .select("Description", "UnitPrice").show(5)
    // to treat null data
    df.where(col("Description").eqNullSafe("hello")).show()

    //Working with Numbers
    val fabricatedQunantity = pow(col("Quantity") * col("UnitPrice"), 2) + 5
    df.select(expr("CustomerId"), fabricatedQunantity.alias("realQuantity")).show()

    df.selectExpr(
      "CustomerId",
      "(POWER((Quantity * UnitPrice), 2.0) + 5) as realQuantity"
    ).show(2)
    df.select(round(col("UnitPrice"), 1).alias("rounded"), col("UnitPrice")).show(5)

    df.select(round(lit("2.5")), bround(lit("2.5"))).show(2)
    df.stat.corr("Quantity", "UnitPrice")
    df.select(corr("Quantity", "UnitPrice")).show()
    df.describe().show()

    val colName = "UnitPrice"
    val quantileProbs = Array(0.5)
    val relError = 0.05
    df.stat.approxQuantile("UnitPrice", quantileProbs, relError)
    df.stat.crosstab("StockCode", "Quantity").show()
    df.stat.freqItems(Seq("StockCode", "Quantity")).show()
    // user randn or rand for generate random number

    //Working with String
    //regex expression
    // relplace expressions
    val regex_string = "BLACK|WHITE|RED|GREEN|BLUE"
    df.select(
      regexp_replace(col("Description"), regex_string, "COLOR").alias("Color_clean"),
      col("Description")
    ).show(2)

    // Check existence of a group of word
    val containsBlack = col("Description").contains("BLACK")
    val containsWhite = col("Description").contains("WHITE")
    df.withColumn("hasSimpleColor", containsBlack.or(containsWhite))
      .where("hasSimpleColor")
      .select("Description").show(3, false)
    val simpleColors = Seq("black", "white", "red", "green", "blue")
    val selectedColumns = simpleColors.map(color => {
      col("Description").contains(color.toUpperCase).alias(s"is_$color")
    }):+expr("*")
    df.select(selectedColumns:_*).where(col("is_white").or(col("is_red")))
      .show(5)

    // Working with Dates and Timestamps
    val dateDF = spark.range(10)
      .withColumn("today", current_date())
      .withColumn("now", current_timestamp())
    dateDF.createGlobalTempView("dateTable")
    dateDF.printSchema()
    // substract and add date
    dateDF.select(date_sub(col("today"), 5), date_add(col("today"), 5)).show(5)
    // date differences
    dateDF.withColumn("week_ago", date_sub(col("today"), 7))
      .select(datediff(col("week_ago"), col("today"))).show(1)
    dateDF.select(
      to_date(lit("2016-01-01")).alias("start"),
      to_date(lit("2017-05-22")).alias("end")
    )
      .select(months_between(col("start"), col("end"))).show(1)

    // convert String to date with a specific format
    spark.range(5).withColumn("date", lit("2017-01-01"))
      .select(to_date(col("date"))).show(1)
    // spark throw null if it didn't recognize the fromat
    dateDF.select(to_date(lit("2016-20-12")), to_date(lit("2017-12-11"))).show(1)
    val dateFormat = "yyyy-dd-MM"
    val cleanDateDF = spark.range(1).select(
      to_date(lit("2017-12-11"), dateFormat).alias("date"),
      to_date(lit("2017-20-12"), dateFormat).alias("date2")
    )
    cleanDateDF.createGlobalTempView("dateTable2")
    // using Timestamp, always use with date format to be more specific
    cleanDateDF.select(to_timestamp(col("date"), dateFormat)).show()

    cleanDateDF.filter(col("date2") > lit("2017-12-12")).show()
    cleanDateDF.filter(col("date2") > "'2017-12-12'")

    //Working with Nulls in DAta
    // coalesce to select the first non null value
    df.select(coalesce(col("Description"), col("CustomerId"))).show()
    // sql function
    // ifnull(null, "return_value") return the second if the first is null
    // nullif("value","value") return null if the 2 are not and the second argument if not
    //nvl(null, "return_value") return second value if the first is null
    //nvl2("not_null", "return_value", "else_value") return the second value if the first is non null ortherwise
    //Drop
    df.na.drop("all", Seq("StockCode", "InvoiceNo")) // any . drop if any value of the row is null, all =IF all the value of the row are null

    //fill
    df.na.fill(5, Seq("StockCode", "InvoiceNo"))
    val fillColValue = Map("StockCode" -> 5, "Description" -> "No Value" )
    df.na.fill(fillColValue)
    // replace
    df.na.replace("Description", Map("" -> "UNKNOWN"))

    //Working with Complex type
    df.selectExpr("(Description, InvoiceNo) as complex", "*").show()
    df.selectExpr("struct(Description, InvoiceNo) as complex", "*").show()
    val complexDF = df.select(struct("Description", "InvoiceNo").alias("complex"))
    complexDF.show(false)
    complexDF.createOrReplaceTempView("complexDF")

    //Querring it as a
    complexDF.select("complex.Description")
    complexDF.select(col("complex").getField("Description"))
    //Querring all the value
    complexDF.select("complex.*")

    // Arrays
    df.select(split(col("Description"), " ").alias("array_col")).show(2)
    df.select(split(col("Description"), " ").alias("array_col"))
      .selectExpr("array_col[0]").show(2)
    //Array Length
    df.select(size(split(col("Description"), " "))).show(2)
    //array Contains
    df.select(array_contains(split(col("Description"), " "), "WHITE")).show(2)

    // explode
    df.withColumn("splitted", split(col("Description"), " "))
      .withColumn("exploded", explode(col("splitted")))
      .select("Description", "InvoiceNo", "exploded").show(2)

    df.select(map(col("Description"), col("InvoiceNo")).alias("complex_map"))
      .selectExpr("complex_map['WHITE METAL LANTERN']").show(5)

    df.select(map(col("Description"), col("InvoiceNo")).alias("complex_map"))
      .selectExpr("explode(complex_map)").show(6)

    // Working with Json
    /*val jsonDF = spark.range(1).select(
      """ '{"myJSONKEY": {"myJSONValue" : [1, 2, 3]}}' as jsonString """)

    jsonDF.select(
      get_json_object(col("jsonString"), "$.myJSONKEY.myJSONValue[1]").alias( "column"),
      json_tuple(col("jsonString"), "myJSONKEY")
    ).show()
    df.selectExpr("(InvoiceNo, Description) as myStruct")
      .select(to_json(col("myStruct"))).show()*/

    // User_Defined Functions
    val udfExampleDF = spark.range(5).toDF("num")
    def power3(number: Double): Double = number * number * number
    println(power3(2))
    val power3udf = udf(power3(_:Double):Double)
    udfExampleDF.select(power3udf(col("num"))).show()
    // register the function to call it in sql expression
    spark.udf.register("power3", power3(_:Double):Double)
    udfExampleDF.selectExpr("power3(num)").show(2)









  }
}
