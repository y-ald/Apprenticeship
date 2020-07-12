package chapter

import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._

object Chap1 {
  def main(args: Array[String]) = {
    // Command to run saprk shell
    //---spark-shell --master local[*]
    /*  * match the number of core avaibles
    * we can specify any number less than the total number of core
    * of your computer, it permit to specify the number of thread we want
    */

    // show the avaible command in the shell
    //---:help

    // SparkContext, function it's to coordinate the execution of spark job
    //---sc

    //show the SparkContext methods
    //---sc.[\t]
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("SparkApp")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder
      .master("local[*]")
      //.appName("my-spark-app")
      .getOrCreate()

    import spark.implicits._
    // create RDD in spark
    val rdd = sc.parallelize(Array(1,2,3,4), 4) // first argument is the object, the second is the number of partition
    // ---val rdd2 = sc.textFile(path)


    // ************Exemple***************
    val ramblocks = sc.textFile("./data/linkage")

    //return the first elements of the rdd
    println(ramblocks.first)
    val head = ramblocks.take(10)

    // size of the dataset
    println(head.length)

    //return all the content of the dataset
    println(ramblocks.collect)

    // use println for a best representation of the dataset
    head.foreach(println)

    //scala function
    // -- def isHeader(line: String) = line.contains("id_1")
    def isHeader(line: String): Boolean = {
      line.contains("id_1")
    }

    //test of scala function
    head.filter(isHeader).foreach(println)

    //exclude argument
    head.filterNot(isHeader).foreach(println)
    println(head.filter(x => !isHeader(x)).length)
    head.filter(!isHeader(_)).foreach(println)

    //Shipping code from the client to the cluster
    val noHeader = ramblocks.filter(x => !isHeader(x))
    noHeader.first


    // spark dataframe
    val prev = spark.read.csv("/home/yald/Documents/Scala_Spark/data/linkage")
    prev.show

    // inferSchema and set default value
    val parsed = spark.read.
      option("header", "true").
      option("nullValue", "?").
      option("inferSchema", "true").
      csv("./data/linkage")

    // see inferred type for each column
    parsed.printSchema()

    //load file from different format
    // --val d1 = spark.read.format("json").load("file.json")
    // -- val d2 = spark.read.json("file.json")

    // write dataframe API
    // -- d1.write.format("parquet").save("./data/ouput/file.parquet")
    // -- d1.write.parquet("./data/ouput/file.parquet")
    // --- d2.write.mode(SaveMode.Ignore).parquet("file.parquet") // Ignore, Append, Overwrite

    //get frequence of the values inside à column
    parsed.rdd.
      map(_.getAs[Boolean]("is_match")).
      countByValue()
    parsed.
      select("is_match").
      filter("is_match is not null").
      groupBy("is_match").
      count().
      orderBy(col("count").desc).
      show()

    //aggregation with dataframe
    parsed.
      agg(avg("cmp_sex").as("avg"), stddev("cmp_sex")).show()

    // create a tempory database table
    parsed.createOrReplaceTempView("linkage")
    spark.sql("""
  SELECT is_match, COUNT(*) cnt
  FROM linkage
  GROUP BY is_match
  ORDER BY cnt DESC
""").show()

    // Statistics for dataframe
    val summary = parsed.describe()
    summary.select("summary", "cmp_fname_c1", "cmp_fname_c2").show()

    //separate two gender
    val matches = parsed.where(col("is_match") === true)
    val matchSummary = matches.describe()
    matchSummary.show()

    val misses = parsed.where("is_match = false")
    val misseSummary = misses.describe()
    misseSummary.show()

    val schema = summary.schema
    /* ---val longForm = summary.flatMap(row => {
      val metric = row.getString(0)
      (1 until row.size).map(i => {
        Row(metric, schema(i).name, row.getString(i).toDouble)
      })
    })
    val longDF = longForm.toDF("metric", "field", "value")*/

    // function to pivot dataframe


    val matchSummaryT = Pivot.pivotSummary(matchSummary)
    val misseSummaryT = Pivot.pivotSummary(misseSummary)
    println("matchSummaryT")
    matchSummaryT.show()
    println("misseSummaryT")
    misseSummaryT.show()

    // Joining DataFrames and Selecting Features
    matchSummaryT.createOrReplaceTempView("match_desc")
    misseSummaryT.createOrReplaceTempView("miss_desc")
    spark.sql(
      """
        |SELECT a.field, a.count + b.count total, a.mean - b.mean delta
        |FROM match_desc a INNER JOIN miss_desc b ON a.field = b.field
        |WHERE a.field NOT IN ("id_1", "id_2")
        |ORDER BY delta DESC, total DESC
        |""".stripMargin).show()

    //
    case class MatchData(
      id_1: Int,
      id_2: Int,
      cmp_fname_c1: Option[Double],
      cmp_fname_c2: Option[Double],
      cmp_lname_c1: Option[Double],
      cmp_lname_c2: Option[Double],
      cmp_sex: Option[Int],
      cmp_bd: Option[Int],
      cmp_bm: Option[Int],
      cmp_by: Option[Int],
      cmp_plz: Option[Int],
      is_match: Boolean
    )
    //val matchData = parsed.as[MatchData]
    //matchData.show

    case class Score(value: Double) {
      def +(oi: Option[Int]) = {
        Score(value + oi.getOrElse(0))
      }
    }

    def scoreMatchData(md: MatchData): Double = {
      (Score(md.cmp_lname_c1.getOrElse(0.0)) + md.cmp_plz +
        md.cmp_by + md.cmp_bd + md.cmp_bm).value
    }

    /*val scored = matchData.map{ md =>
      (scoreMatchData((md)), md.is_match)
    }.toDF("score", "is_match")*/

    def crossTabs(scored: DataFrame, t: Double) = {
      scored.
        selectExpr(s"score >= $t as above", "is_match").
        groupBy("above").
        pivot("is_match", Seq("true", "false")).
        count()
    }

    //crossTabs(scored, 4.0).show()

  }
}
