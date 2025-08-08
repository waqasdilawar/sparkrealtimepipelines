package org.devgurupk.realtimepipelines;

import java.util.concurrent.TimeoutException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

public final class QueueProcessingApp {
  public static void main(String[] args) throws StreamingQueryException, TimeoutException {
    SparkSession spark = SparkSession.builder()
      .appName("StructuredQueueStream")
      .master("spark://localhost:7077")
      .config("spark.driver.host", "10.43.202.124")
      // Your existing config to prevent Java 17 errors
      .config("spark.executor.processTreeMetrics.enabled", "false")
      .config("spark.driver.processTreeMetrics.enabled", "false")
      .getOrCreate();

    try {
      // The DStream-based queueStream is for testing and has limitations.
      // A better approach in Structured Streaming is to use a robust source.
      // We'll use the "rate" source to generate a continuous stream of numbers,
      // which is a great way to test streaming logic.
      // This creates a DataFrame with 'timestamp' and 'value' (Long) columns.
      Dataset<Row> inputStream = spark.readStream()
        .format("rate")
        .option("rowsPerSecond", 10) // Adjust data rate as needed
        .load();

      // Perform the same logic as before (i % 10) using DataFrame operations
      Dataset<Row> processedStream = inputStream
        .withColumn("key", functions.col("value").mod(10))
        .groupBy("key")
        .count();

      // Start the query to print the running counts to the console
      StreamingQuery query = processedStream.writeStream()
        .outputMode("update") // Show only updated rows in the console
        .format("console")
        .option("truncate", "false") // Show full column content
        .start();

      // Await termination to keep the application running
      query.awaitTermination();
    } finally {
      // Stop the SparkSession gracefully
      spark.stop();
    }
  }
}