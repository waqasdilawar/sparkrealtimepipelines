package org.devgurupk.realtimepipelines;

import java.util.concurrent.TimeoutException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public final class KafkaStreamProcessor {
  public static void main(String[] args) throws StreamingQueryException, TimeoutException {
    SparkSession spark = SparkSession.builder()
      .appName("StructuredKafkaStream")
      .master("spark://localhost:7077")
      .config("spark.driver.host", "10.43.202.124")
      // Your existing config to prevent Java 17 errors
      .config("spark.executor.processTreeMetrics.enabled", "false")
      .config("spark.driver.processTreeMetrics.enabled", "false")
      //.config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate();

    try {
      // Read from Kafka topic "test"
      Dataset<Row> inputStream = spark.readStream()
        .format("kafka")
        // Assuming Kafka is running at localhost:9092
        .option("kafka.bootstrap.servers", "localhost:9092")
        .option("subscribe", "test")
        .load();
      // The 'value' column from Kafka is binary, so we cast it to a STRING.
      Dataset<Row> jsonStream = inputStream.selectExpr("CAST(value AS STRING)  as json");

      // Define the schema for the JSON data based on your example
      StructType schema = new StructType()
        .add("name", DataTypes.StringType)
        .add("age", DataTypes.IntegerType);

      // Parse the JSON string from the 'value' column into structured data
      Dataset<Row> processedStream = jsonStream
        .withColumn("data", functions.from_json(functions.col("json"), schema))
        .select("data.*"); // Explode the struct to get columns 'name' and 'age'

      // Start the query to print the parsed data to the console
      StreamingQuery query = processedStream.writeStream()
        .outputMode("append")
        .format("console")
        .start();

      // Await termination to keep the application running
      query.awaitTermination();
    } finally {
      // Stop the SparkSession gracefully
      spark.stop();
    }
  }
}
