package learningSpark.chapter5.src

import org.apache.spark.sql.{SparkSession, Encoder, Encoders}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object UdfsTest {
    def main(args: Array[String]) {
        val spark = SparkSession
        .builder 
        .appName("UdfsTest")
        .config("spark.master", "local")
        .getOrCreate 

        import spark.implicits._
        

        // Example of UDF
        val cubed = (s: Long) => {
            s *s * s
        }

        spark.udf.register("cubed", cubed) 

        spark.range(1, 9).createOrReplaceTempView("udf_test")
        spark.sql("SELECT id, cubed(id) as id_cubed FROM udf_test").show()
        /*
            +---+--------+
            | id|id_cubed|
            +---+--------+
            |  1|       1|
            |  2|       8|
            |  3|      27|
            |  4|      64|
        */

    }
}