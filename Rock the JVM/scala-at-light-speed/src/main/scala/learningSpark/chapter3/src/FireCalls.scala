package learningSpark.chapter3.src

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object FireCalls {
    def main(args: Array[String]) {
        val spark = SparkSession
        .builder 
        .appName("FireCalls")
        .config("spark.master", "local")
        .getOrCreate 

        // If you don’t want to specify the schema, Spark can infer schema
        // from a sample at a lesser cost. For example, you can use the
        // samplingRatio option:

        // val sampleDF = spark.read
        // .option("samplingRatio", 0.001)
        // .option("header", true)
        // .csv("""E:/YALD/Project/y-ald/Apprenticeship/Rock the JVM/scala-at-light-speed/src/main/scala/learningSpark/chapter3/resources/sf-fire-calls.csv""")

        // sampleDF.show 

        val fire_shema = StructType(Array(StructField("CallNumber", IntegerType, true),
            StructField("UnitID", StringType , true),
            StructField("IncidentNumber", IntegerType , true),
            StructField("CallType", StringType , true),
            StructField("CallDate", StringType , true),
            StructField("WatchDate", StringType , true),
            StructField("CallFinalDisposition", StringType , true),
            StructField("AvailableDtTm", StringType , true),
            StructField("Address", StringType , true),
            StructField("City", StringType , true),
            StructField("Zipcode", IntegerType , true),
            StructField("Battalion", StringType , true),
            StructField("StationArea", StringType , true),
            StructField("Box", StringType , true),
            StructField("OriginalPriority", StringType , true),
            StructField("Priority", StringType , true),
            StructField("FinalPriority", IntegerType , true),
            StructField("ALSUnit", BooleanType , true),
            StructField("CallTypeGroup", StringType , true),
            StructField("NumAlarms", IntegerType , true),
            StructField("UnitType", StringType , true),
            StructField("UnitSequenceInCallDispatch", IntegerType , true),
            StructField("FirePreventionDistrict", StringType , true),
            StructField("SupervisorDistrict", StringType , true),
            StructField("Neighborhood", StringType , true),
            StructField("Location", StringType , true),
            StructField("RowID", StringType , true),
            StructField("Delay", FloatType , true)))

        val dataFrame =  spark.read
            .schema(fire_shema)
            .csv("../scala-at-light-speed/src/main/scala/learningSpark/chapter3/resources/sf-fire-calls.csv")
        // dataFrame.show()


        //# return number of distinct types of calls using countDistinct()
        // dataFrame.select("callType").distinct().count()
        // dataFrame.select("CallType")
        //     .where(col("CallType").isNotNull())
        //     .agg(countDistinct("CallType").alias("CallTypeCountDistinct"))
            //.show()

        // Column Renamed 
        // dataFrame.withColumnRenamed("Delay", "RespondTimeDelayInMin")



        //# convert CallData ,WatchDate and AlarmDtTm to date 

        // dataFrame
        //     .withColumn("IncidentDate", to_date(col("CallDate"), "MM/dd/yyyy"))
        //     .withColumn("OnWatchDate", to_date(col("WatchDate"), "MM/dd/yyyy"))
        //     .withColumn("AvailableDtTS", to_date(col("AvailableDtTm"), "MM/dd/yyyy hh:mm:ss a"))
        //     .select("IncidentDate", "OnWatchDate", "AvailableDtTS")
        //     .show()

        //     +------------+-----------+-------------+
        //     |IncidentDate|OnWatchDate|AvailableDtTS|
        //     +------------+-----------+-------------+
        //     |        null|       null|         null|
        //     5
        //     |  2002-01-11| 2002-01-10|   2002-01-11|
        //     14
        //     |  2002-01-11| 2002-01-11|   2002-01-11|
        //     +------------+-----------+-------------+

        // dataFrame
        //     .withColumn("IncidentDate", to_timestamp(col("CallDate"), "MM/dd/yyyy"))
        //     .withColumn("OnWatchDate", to_timestamp(col("WatchDate"), "MM/dd/yyyy"))
        //     .withColumn("AvailableDtTS", to_timestamp(col("AvailableDtTm"), "MM/dd/yyyy hh:mm:ss a"))
        //     .select("IncidentDate", "OnWatchDate", "AvailableDtTS")
        //     .show()

        // +-------------------+-------------------+-------------------+
        // |       IncidentDate|        OnWatchDate|      AvailableDtTS|
        // +-------------------+-------------------+-------------------+
        // |               null|               null|               null|
        // |2002-01-11 00:00:00|2002-01-10 00:00:00|2002-01-11 01:51:44|
        // |2002-01-11 00:00:00|2002-01-10 00:00:00|2002-01-11 03:01:18|
        // |2002-01-11 00:00:00|2002-01-10 00:00:00|2002-01-11 02:39:50|
        // |2002-01-11 00:00:00|2002-01-10 00:00:00|2002-01-11 04:16:46|
        // |2002-01-11 00:00:00|2002-01-10 00:00:00|2002-01-11 06:01:58|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 08:03:26|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 09:46:44|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 09:58:53|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 12:06:57|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 13:08:40|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 15:31:02|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 14:59:04|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 16:22:49|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 16:18:33|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 16:09:08|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 16:34:23|
        // |2002-01-11 00:00:00|2002-01-11 00:00:00|2002-01-11 16:51:31|
        // +-------------------+-------------------+-------------------+

        val dataFrame_date = dataFrame
            .withColumn("IncidentDate", to_timestamp(col("CallDate"), "MM/dd/yyyy"))
            .withColumn("OnWatchDate", to_timestamp(col("WatchDate"), "MM/dd/yyyy"))
            .withColumn("AvailableDtTS", to_timestamp(col("AvailableDtTm"), "MM/dd/yyyy hh:mm:ss a"))
            .select("IncidentDate", "OnWatchDate", "AvailableDtTS")
        
        // dataFrame_date
        //     .select(year(col("IncidentDate")).alias("YearIncidentDate"))
        //     .distinct()
        //     .orderBy(col("YearIncidentDate"))
        //     .show(10)

        dataFrame
            .select("CallType")
            .where(col("CallType").isNotNull)
            .groupBy("CallType")
            .count()
            .orderBy(col("count").desc)
            .show(10)
        

    }
}