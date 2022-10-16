package learningSpark.chapter6.src

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import scala.util.Random._

object DatasetTests {

    case class Bloggers(id:Int, first:String, last:String, url:String, date:String,
        hits: Int, campaings:Array[String])

    case class Usage(uid:Int, uname:String, usage:Int)

    case class UsageCost(uid:Int, uname:String, usage:Int, cost:Double)

    def main(args: Array[String]) {
        val spark = SparkSession
        .builder()
        .appName("DatasetTests")
        .config("spark.master", "local")
        .getOrCreate()

        import  spark.implicits._
        import org.apache.spark.sql.Encoders
        val schema = Encoders.product[Bloggers].schema


        val bloggers ="../scala-at-light-speed/src/main/scala/learningSpark/chapter6/resources/bloggers.json"
        val bloggersDS = spark.read.format("json").schema(schema).option("path", bloggers).load().as[Bloggers].filter($"id".isNotNull)

        //bloggersDS.show()
        /*
            +---+------+-----+-----------------+--------+----+---------+
            | id| first| last|              url|    date|hits|campaings|
            +---+------+-----+-----------------+--------+----+---------+
            |  1| Jules|Damji|https://tinyurl.1|1/4/2016|4535|     null|
            | 87|Brooke|Wenig|https://tinyurl.2|5/5/2018|8908|     null|
            +---+------+-----+-----------------+--------+----+---------+
        */

        val r = new scala.util.Random(45)

        val data =  for (i <- 0 to 1000)
            yield (Usage(i,"user-" + r.alphanumeric.take(5).mkString(""),
            r.nextInt(1000)))

        val dsUsage = spark.createDataset(data)
        //dsUsage.show(10)
        /*
            +---+----------+-----+
            |uid|     uname|usage|
            +---+----------+-----+
            |  0|user-ZnJe1|  131|
            |  1|user-b3Grq|  719|
            |  2|user-sGqoQ|  985|
            |  3|user-6Firw|  274|
            |  4|user-AdNYv|  851|
            |  5|user-23RMo|  433|
            |  6|user-lIbAj|  361|
            |  7|user-g8d31|  393|
            |  8|user-IZniU|  862|
            |  9|user-qZAbu|  313|
            +---+----------+-----+
        */

        // Higher-order functions and functional programming
        dsUsage
            .filter(d => d.usage > 900)
            .orderBy(desc("usage"))
            //.show(5, false)
        
        def filterWithUsage(value: Usage) = value.usage > 900
        dsUsage.filter(filterWithUsage(_)).orderBy(desc("usage"))//.show(5, false)
        /*
        +---+----------+-----+
        |uid|uname     |usage|
        +---+----------+-----+
        |987|user-hRmoO|999  |
        |446|user-aXg0o|999  |
        |817|user-6C7vn|998  |
        |509|user-YQcev|997  |
        |354|user-zRM2j|994  |
        +---+----------+-----+
        */ 

        // not all lambdas function evaluate as boolean they can return compute valie
        //use map in these case 

        dsUsage.map(d => if(d.usage > 750) d.usage * 0.15 else d.usage * 0.5)//.show(10)

        def mapWithNewvalue(row: Usage) = if (row.usage > 750) row.usage * 0.15 else row.usage * 0.5

        dsUsage.map(mapWithNewvalue(_))//.show(10)
        /*
            +------------------+
            |             value|
            +------------------+
            |              65.5|
            |             359.5|
            |            147.75|
            |             137.0|
            |127.64999999999999|
            |             216.5|
            |             180.5|
            |             196.5|
            |129.29999999999998|
            |             156.5|
            +------------------+
        */
        //use method to compute coast
        def mapwithNewCol(row: Usage) = UsageCost(row.uid, row.uname, row.usage, mapWithNewvalue(row))
        dsUsage.map(mapwithNewCol(_))//.show(5)

        def computeUserCostUsage(row: Usage) = {
            val v = if(row.usage > 750) row.usage * 0.15 else row.usage * 0.50
            UsageCost(row.uid, row.uname, row.usage, v)
        }

        dsUsage.map(computeUserCostUsage(_))//.show(5)
        /*
            +---+----------+-----+------------------+
            |uid|     uname|usage|              cost|
            +---+----------+-----+------------------+
            |  0|user-ZnJe1|  131|              65.5|
            |  1|user-b3Grq|  719|             359.5|
            |  2|user-sGqoQ|  985|            147.75|
            |  3|user-6Firw|  274|             137.0|
            |  4|user-AdNYv|  851|127.64999999999999|
            +---+----------+-----+------------------+
        */

        

    }
}