package learningSpark.chapter2.src

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object MnMCount {
    def main(args: Array[String]) {
        val spark = SparkSession
        .builder()
        .appName("MnMCount")
        .config("spark.master", "local")
        .getOrCreate()
        
        // Get the M&M data set filename
        println("########################### MnMCount ##########################")

        // if (args.length < 1) {
        //     print("Usage: MnMcount <mnm_file_dataset>")
        //     sys.exit(1)
        // }
        // // Get the M&M data set filename
        // val mnmFile = args(0)


        val dataframe_mnm = spark.read.option("header",true)
            .csv("""E:\YALD\Project\y-ald\Apprenticeship\Rock the JVM\scala-at-light-speed\src\main\scala\learningSpark\chapter2\resources\mnm_dataset.csv""")
        dataframe_mnm.cache()
        val agg_dataframe = dataframe_mnm
            .groupBy("State", "Color")
            .agg(
                sum(col("Count")).alias("Total_Sum"),
                count(col("Count")).alias("Total_Count"))
            .orderBy(col("Total_Count").desc)
            
        agg_dataframe.cache()
        agg_dataframe.show(30)

        
        println("######################################## print CA part ##############################")
        agg_dataframe
            .filter(col("State") === "CA")
            .show
        dataframe_mnm  
            .select("State", "Color", "Count")
            .where(col("State")==="CA")
            .groupBy("State", "Color")
            .agg(count("Count").alias("Total"))
            .orderBy(desc("Total"))
            .show()

        println(s"The number of line in the dataframe is ${agg_dataframe.count()}")
        spark.stop()
        

    }
}

