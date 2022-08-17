package learningSpark.chapter3.src

import org.apache.spark.sql.{SparkSession, Encoder, Encoders}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object CaseClassExample {

    case class DeviceIoTData (battery_level: Long, c02_level: Long,
        cca2: String, cca3: String, cn: String, device_id: Long,
        device_name: String, humidity: Long, ip: String, latitude: Double,
        lcd: String, longitude: Double, scale: String, temp: Long, timestamp: Long)

    def main(args: Array[String]) {
        val spark = SparkSession
        .builder 
        .appName("CaseClassExample")
        .config("spark.master", "local")
        .getOrCreate 

        import spark.implicits._


        // implicit val enc: Encoder[DeviceIoTData] = Encoders.product[DeviceIoTData]
        val ds = spark.read
            // .schema(schema)
            .json("../scala-at-light-speed/src/main/scala/learningSpark/chapter3/resources/iot.json")
            .as[DeviceIoTData]

        // val schema = StructType(Array(
        //     StructField("battery_level", DoubleType, true),
        //     StructField("c02_level", DoubleType, true),
        //     StructField("cca2", StringType, true),
        //     StructField("cca3", StringType, true),
        //     StructField("cn", StringType, true),
        //     StructField("device_id", StringType, true),
        //     StructField("device_name", StringType, true),
        //     StructField("humidity", DoubleType, true),
        //     StructField("ip", StringType, true),
        //     StructField("latitude", DoubleType, true),
        //     StructField("lcd", StringType, true),
        //     StructField("longitude",DoubleType, true),
        //     StructField("scale", StringType, true),
        //     StructField("temp", DoubleType, true),
        //     StructField("timestamp", DoubleType, true)
        // ))

        ds.show(5, false)
    }

}