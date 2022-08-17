package learningSpark.chapter4.src

import org.apache.spark.sql.{SparkSession, Encoder, Encoders}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object AirData {
    def main(args: Array[String]) {
        val spark = SparkSession
        .builder 
        .appName("Airdata")
        .config("spark.master", "local")
        .getOrCreate 

        import spark.implicits._

        val csvfile = "../scala-at-light-speed/src/main/scala/learningSpark/chapter4/resources/DelayedFlights.csv"

        val df = spark.read
            //.format(csv)
            .option("inferSchema", "true")
            .option("samplingRation", 0.1)
            .option("header", "true")
            .csv(csvfile)
        
        /*
        df.show(5)
    
         ---+----+-----+----------+---------+-------+----------+-------+----------+-------------+---------+-------+-----------------+--------------+-------+--------+--------+------+----+--------+------+-------+---------+----------------+--------+------------+------------+--------+-------------+-----------------+
        |_c0|Year|Month|DayofMonth|DayOfWeek|DepTime|CRSDepTime|ArrTime|CRSArrTime|UniqueCarrier|FlightNum|TailNum|ActualElapsedTime|CRSElapsedTime|AirTime|ArrDelay|DepDelay|Origin|Dest|Distance|TaxiIn|TaxiOut|Cancelled|CancellationCode|Diverted|CarrierDelay|WeatherDelay|NASDelay|SecurityDelay|LateAircraftDelay|
        +---+----+-----+----------+---------+-------+----------+-------+----------+-------------+---------+-------+-----------------+--------------+-------+--------+--------+------+----+--------+------+-------+---------+----------------+--------+------------+------------+--------+-------------+-----------------+
        |  0|2008|    1|         3|        4| 2003.0|      1955| 2211.0|      2225|           WN|      335| N712SW|            128.0|         150.0|  116.0|   -14.0|     8.0|   IAD| TPA|     810|   4.0|    8.0|        0|               N|       0|        null|        null|    null|         null|             null|
        |  1|2008|    1|         3|        4|  754.0|       735| 1002.0|      1000|           WN|     3231| N772SW|            128.0|         145.0|  113.0|     2.0|    19.0|   IAD| TPA|     810|   5.0|   10.0|        0|               N|       0|        null|        null|    null|         null|             null|
        |  2|2008|    1|         3|        4|  628.0|       620|  804.0|
        */

        val final_df = df
            .sample(0.2, 123)
            .withColumn("date", concat(col("Year"), lit("-"), col("Month"), lit("-"), col("DayofMonth")))
            //.withColumn("date", to_date(concat(col("Year"), lit("-"), col("Month"), lit("-"), col("DayofMonth"))))
            .withColumn("delay", col("depDelay") + col("ArrDelay"))
            .select(
                "date",
                "delay",
                "distance",
                "origin",
                "Dest"
            )
        
        final_df.cache()


        final_df.createOrReplaceTempView("us_delay_flights_tbl")

        spark.sql("SELECT * from us_delay_flights_tbl")
            //.show(5)

        // Fligth whose distance is greater than 1000 miles
        spark.sql("SELECT * " +
            "FROM us_delay_flights_tbl " +
            "WHERE distance > 1000  " +
            "ORDER BY distance DESC")
            //.show(5)
            /*
            df.select("distance", "origin", "destination")
                .where(col("distance") > 1000)
                .orderBy(desc("distance"))).show(10)
            +---------+-----+--------+------+----+
            |     date|delay|distance|origin|Dest|
            +---------+-----+--------+------+----+
            | 2008-1-3| 64.0|    1910|   LAS| PIT|
            | 2008-1-3| -9.0|    1891|   RDU| PHX|
            | 2008-6-8| null|    1846|   SFO| ORD|
            |2008-6-14| 84.0|    1846|   SFO| ORD|
            |2008-6-20|124.0|    1846|   SFO| ORD|
            +---------+-----+--------+------+----+
            */

        // Fligth between San fransico (SFO) and chicago (ORD) with a least 2 hours of delay 
        spark.sql("SELECT * " +
          "FROM us_delay_flights_tbl " +
          "WHERE Dest='ORD' and origin='SFO' and delay >= 120 " +
          "ORDER BY delay DESC")
          //.show(5)
        /*  
        +---------+-----+--------+------+----+
        |     date|delay|distance|origin|Dest|
        +---------+-----+--------+------+----+
        |2008-6-20|124.0|    1846|   SFO| ORD|
        +---------+-----+--------+------+----+
            only showing top 5 rows*/

        // Convert the date into readable format and 
        //find the days or months when these delays were most common 
        spark.sql("""
            SELECT sum(delay) as delay,
                DATE_FORMAT(to_date(date), 'MM') as month

            FROM us_delay_flights_tbl
            where Dest='ORD' and origin='SFO'
            Group By month 
            ORDER BY delay DESC
        """)
        //.show(5)

        /*
        +-----+-----+
        |delay|month|
        +-----+-----+
        |296.0|   06|
        +-----+-----+
        */

        // Create column which indicate very long delay, long delay etc

        spark.sql("""
            SELECT *,
                CASE 
                    WHEN  delay >= 360 THEN 'very long delay'
                    WHEN delay > 120 AND delay < 360 THEN 'Long delay'
                    ELSE 'delay'
                END as Flight_delays

            FROM us_delay_flights_tbl
        """)
        .show()
        /*
        val df4 = df.select(col("*"), when(col("gender") === "M","Male")
        .when(col("gender") === "F","Female")
        .otherwise("Unknown").alias("new_gender"))

        +--------+-----+--------+------+----+-------------+
        |    date|delay|distance|origin|Dest|Flight_delays|
        +--------+-----+--------+------+----+-------------+
        |2008-1-3| -6.0|     810|   IAD| TPA|        delay|
        |2008-1-3| 36.0|     688|   IND| JAX|        delay|
        |2008-1-3|146.0|     972|   ISP| MCO|   Long delay|
        |2008-1-3| 85.0|    1052|   ISP| PBI|        delay|
        |2008-1-3| 48.0|     666|   JAN| MDW|        delay|
        */




    }
}
