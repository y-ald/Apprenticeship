import org.apache.spark.sql.{SparkSession, Encoder, Encoders}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object HighOrderfunctions {
    def main(args: Array[String]) {
        val spark = SparkSession
        .builder 
        .appName("HighOrderfunctions")
        .config("spark.master", "local")
        .getOrCreate 

        import spark.implicits._

        // Create DataFrame with two rows of two arrays (tempc1, tempc)
        val t1 = Array(35,36, 32, 30, 40, 42, 38)
        val t2 = Array(31, 32, 34, 55, 56)
        val tC = Seq(t1, t2).toDF("celsius")

        tC.createOrReplaceTempView("tC")
        tC.show
        // +--------------------+
        // |             celsius|
        // +--------------------+
        // |[35, 36, 32, 30, ...|
        // |[31, 32, 34, 55, 56]|
        // +--------------------+
        
        // transform()
        // transform(array<T>, function<T, U>): array<U>
        // The transform() function produces an array by applying a function to each element
        // of the input array (similar to a map() function):
        spark.sql("""
        SELECT celsius,
        transform(celsius, t -> ((t*9) div 5) + 35) as fahrenheit
        FROM tC
        """).show()
        // +--------------------+--------------------+
        // |             celsius|          fahrenheit|
        // +--------------------+--------------------+
        // |[35, 36, 32, 30, ...|[98, 99, 92, 89, ...|
        // |[31, 32, 34, 55, 56]|[90, 92, 96, 134,...|
        // +--------------------+--------------------+

        // filter()
//         filter(array<T>, function<T, Boolean>): array<T>
        // The filter() function produces an array consisting of only the elements of the input 
        // array for which the Boolean function is true:
        spark.sql("""
        SELECT celsius,
        filter(celsius, t -> t > 38) as high
        FROM tC
        """).show()
        // +--------------------+--------+
        // |             celsius|    high|
        // +--------------------+--------+
        // |[35, 36, 32, 30, ...|[40, 42]|
        // |[31, 32, 34, 55, 56]|[55, 56]|
        // +--------------------+--------+

        // exists()
        // exists(array<T>, function<T, V, Boolean>): Boolean
        // The exists() function returns true if the Boolean function holds for any element in
        // the input array:
        spark.sql("""
        SELECT celsius, 
        exists(celsius, t -> t = 38) as threshold
        FROM tC
        """).show()
        // +--------------------+---------+
        // |             celsius|threshold|
        // +--------------------+---------+
        // |[35, 36, 32, 30, ...|     true|
        // |[31, 32, 34, 55, 56]|    false|
        // +--------------------+---------+

        // reduce()
        // reduce(array<T>, B, function<B, T, B>, function<B, R>)
        // The reduce() function reduces the elements of the array to a single value by merging
        // the elements into a buffer B using

        spark.sql("""
            SELECT celsius,
            reduce(
                celsius,
                0,
                (t, acc) -> t + acc,
                acc -> (acc div size(celsius) * 9 div 5) + 32
            ) as avgFahrenheit
            FROM tC
        """).show()
        // +--------------------+-------------+
        // | celsius|avgFahrenheit|
        // +--------------------+-------------+
        // |[35, 36, 32, 30, ...| 96|
        // |[31, 32, 34, 55, 56]| 105|
        // +--------------------+-------------+

    }
}