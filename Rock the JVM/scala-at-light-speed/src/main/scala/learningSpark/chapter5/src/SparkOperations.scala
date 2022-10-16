package learningSpark.chapter5.src

import org.apache.spark.sql.{SparkSession, Encoder, Encoders}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.types._

object SparkOperations {
    def main(args: Array[String]) {
        val spark = SparkSession
        .builder 
        .appName("SparkOperations")
        .config("spark.master", "local")
        .getOrCreate 

        import spark.implicits._

        // Set file paths
        val delaysPath =
        "../scala-at-light-speed/src/main/scala/learningSpark/chapter5/resources/departuredelays.csv"
        val airportsPath =
        "../scala-at-light-speed/src/main/scala/learningSpark/chapter5/resources/airport-codes-na.txt"

        //Obtain airports data set
        val airports = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .option("delimiter", "\t")
            .csv(airportsPath)
        airports.createOrReplaceTempView("airports_na")
        
        val delays = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .csv(delaysPath)
            .withColumn("delay", expr("CAST(delay as INT) as delay"))
            .withColumn("distance", expr("CAST(distance as INT) as distance"))

        delays.createOrReplaceTempView("departureDelays")
        //delays.show(5)
        //airports.show()
        // delays.printSchema()
        // root
        // |-- date: integer (nullable = true)
        // |-- delay: integer (nullable = true)
        // |-- distance: integer (nullable = true)
        // |-- origin: string (nullable = true)
        // |-- destination: string (nullable = true)

        // airports.printSchema()
        // root
        // |-- City: string (nullable = true)
        // |-- State: string (nullable = true)
        // |-- Country: string (nullable = true)
        // |-- IATA: string (nullable = true)

        //Create temporary small table fly originate from SEA and destination SFO for small time range
        // val foo = spark.sql("""
        // SELECT * FROM departureDelays
        // WHERE origin="SEA" and destination="SFO"
        // """)
        /*
        +-------+-----+--------+------+-----------+
        |   date|delay|distance|origin|destination|
        +-------+-----+--------+------+-----------+
        |1010710|   31|     590|   SEA|        SFO|
        |1012125|   -4|     590|   SEA|        SFO|
        |1011840|   -5|     590|   SEA|        SFO|
        |1010610|   -4|     590|   SEA|        SFO|
        |1011230|   -2|     590|   SEA|        SFO|
        |1010955|   -6|     590|   SEA|        SFO|
        |1011100|    2|     590|   SEA|        SFO|
        */

        val foo = delays.filter(expr("""
        origin=="SEA" and destination=="SFO" and
        CAST(date as STRING) like '1010%' and delay > 0
        """))
        foo.createOrReplaceTempView("foo")
        //foo.show(5)
        /*
        +-------+-----+--------+------+-----------+
        |   date|delay|distance|origin|destination|
        +-------+-----+--------+------+-----------+
        |1010710|   31|     590|   SEA|        SFO|
        |1010955|  104|     590|   SEA|        SFO|
        |1010730|    5|     590|   SEA|        SFO|
        +-------+-----+--------+------+-----------+
        */

        //show dataFrames
        // spark.sql("SELECT * FROM airports_na limit 5").show()
        /*
            +----------+-----+-------+----+
            |      City|State|Country|IATA|
            +----------+-----+-------+----+
            |Abbotsford|   BC| Canada| YXX|
            |  Aberdeen|   SD|    USA| ABR|
            |   Abilene|   TX|    USA| ABI|
            |     Akron|   OH|    USA| CAK|
            |   Alamosa|   CO|    USA| ALS|
            +----------+-----+-------+----+
        */

        // spark.sql("SELECT * FROM departureDelays limit 5").show()
        /*
           +-------+-----+--------+------+-----------+
            |   date|delay|distance|origin|destination|
            +-------+-----+--------+------+-----------+
            |1011245|    6|     602|   ABE|        ATL|
            |1020600|   -8|     369|   ABE|        DTW|
            |1021245|   -2|     602|   ABE|        ATL|
            |1020605|   -4|     602|   ABE|        ATL|
            |1031245|   -4|     602|   ABE|        ATL|
            +-------+-----+--------+------+-----------+
        */

        // spark.sql("SELECT * FROM foo").show()
        /*
            +-------+-----+--------+------+-----------+
            |   date|delay|distance|origin|destination|
            +-------+-----+--------+------+-----------+
            |1010710|   31|     590|   SEA|        SFO|
            |1010955|  104|     590|   SEA|        SFO|
            |1010730|    5|     590|   SEA|        SFO|
            +-------+-----+--------+------+-----------+
        */


        // Union 
        // val bar = delays.union(foo)
        // bar.createOrReplaceTempView("bar")
        // bar.filter(expr("""
        // origin=="SEA" and destination=="SFO" and 
        // date like '1010%' and delay > 0
        // """))//.show()
        // /*
        //     +-------+-----+--------+------+-----------+
        //     |   date|delay|distance|origin|destination|
        //     +-------+-----+--------+------+-----------+
        //     |1010710|   31|     590|   SEA|        SFO|
        //     |1010955|  104|     590|   SEA|        SFO|
        //     |1010730|    5|     590|   SEA|        SFO|
        //     |1010710|   31|     590|   SEA|        SFO|
        //     |1010955|  104|     590|   SEA|        SFO|
        //     |1010730|    5|     590|   SEA|        SFO|
        //     +-------+-----+--------+------+-----------+
        // */


        // // Join
        // foo.join(
        //     airports.as("air"),
        //     $"air.IATA" === $"origin"
        // ).select("city", "State", "date", "delay", "distance", "destination").show(5)
        // /*
        //     +-------+-----+-------+-----+--------+-----------+
        //     |   city|State|   date|delay|distance|destination|
        //     +-------+-----+-------+-----+--------+-----------+
        //     |Seattle|   WA|1010710|   31|     590|        SFO|
        //     |Seattle|   WA|1010955|  104|     590|        SFO|
        //     |Seattle|   WA|1010730|    5|     590|        SFO|
        //     +-------+-----+-------+-----+--------+-----------+
        // */


        // spark.sql("""
        // SELECT b.city,
        // b.State,
        // a.date,
        // a.delay,
        // a.distance,
        // a.destination
        // FROM foo a
        // JOIN airports_na b on b.IATA = a.origin
        // """).show(5)
        // /*
        //     +-------+-----+-------+-----+--------+-----------+
        //     |   city|State|   date|delay|distance|destination|
        //     +-------+-----+-------+-----+--------+-----------+
        //     |Seattle|   WA|1010710|   31|     590|        SFO|
        //     |Seattle|   WA|1010955|  104|     590|        SFO|
        //     |Seattle|   WA|1010730|    5|     590|        SFO|
        //     +-------+-----+-------+-----+--------+-----------+
        // */


        // Windows
        // TotalDelays (calculated by sum(Delay)) by flights originatig from seattle to SFO
        val window = Window.partitionBy("origin", "destination")
        // delays.filter($"origin"==="SEA" or $"origin"==="SFO" or $"origin"==="JFK")
        //     .withColumn("TotalDelys", sum("delay").over(window))
        //     .show(5)
        /*
            +-------+-----+--------+------+-----------+----------+
            |   date|delay|distance|origin|destination|TotalDelys|
            +-------+-----+--------+------+-----------+----------+
            |1011715|   18|     643|   JFK|        ORD|      5608|
            |1021715|   82|     643|   JFK|        ORD|      5608|
            |1031715|    0|     643|   JFK|        ORD|      5608|
            |1041715|   56|     643|   JFK|        ORD|      5608|
            |1051715|   99|     643|   JFK|        ORD|      5608|
            +-------+-----+--------+------+-----------+----------+
        */
        // three destination that experienced the most delays for each origin
        delays.filter('origin==="SEA" or 'origin==="SFO" or 'origin==="JFK")
            .withColumn("totalDelays", sum("delay").over(window))
            .orderBy($"totalDelays".desc)
            //.show(3)
        /*
            +-------+-----+--------+------+-----------+-----------+
            |   date|delay|distance|origin|destination|totalDelays|
            +-------+-----+--------+------+-----------+-----------+
            |1012015|   -7|     293|   SFO|        LAX|      40798|
            |1010915|   -3|     293|   SFO|        LAX|      40798|
            |1011740|   -7|     293|   SFO|        LAX|      40798|
            +-------+-----+--------+------+-----------+-----------+      
        */
        delays.filter('origin==="SEA" or 'origin==="SFO" or 'origin==="JFK")
            .groupBy("origin", "destination").agg(sum("delay").as("TotalDelays"))
            .orderBy('totalDelays.desc)
            //.show(3)
        /*
            +------+-----------+-----------+
            |origin|destination|TotalDelays|
            +------+-----------+-----------+
            |   SFO|        LAX|      40798|
            |   JFK|        LAX|      35755|
            |   JFK|        SFO|      35619|
            +------+-----------+-----------+
        */
        val window2 = Window.partitionBy("origin").orderBy("TotalDelays")
        delays.filter('origin==="SEA" or 'origin==="SFO" or 'origin==="JFK")
            .groupBy("origin", "destination").agg(sum("delay").as("TotalDelays"))
            .withColumn("rank", dense_rank().over(window2))
            .filter('rank<=3)
          //  .show(10)

        /*
            +------+-----------+-----------+----+
            |origin|destination|TotalDelays|rank|
            +------+-----------+-----------+----+
            |   SEA|        SNA|       -967|   1|
            |   SEA|        BUR|       -165|   2|
            |   SEA|        HDN|        -87|   3|
            |   SFO|        ANC|        102|   1|
            |   SFO|        LMT|        457|   2|
            |   SFO|        BZN|        591|   3|
            |   JFK|        IAH|        100|   1|
            |   JFK|        JAC|        322|   2|
            |   JFK|        PSP|        353|   3|
            +------+-----------+-----------+----+
        */


        //modification

        //adding new column
        foo.show(3)

        val foo2 = foo.withColumn("status", expr("CASE WHEN delay <= 10 THEN 'On-time' ELSE 'Delayed'"))
        foo2.show(3)

        //dropping column
        foo2.drop("status")
            .show(3)

        //renaming columns
        foo2.withColumnRenamed("status", "flight_status")
            .show(3)

        //pivoting
        
    }   
}