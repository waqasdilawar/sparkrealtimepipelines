package org.devgurupk.realtimepipelines;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SimpleSpark {
  public static void main(String[] args) {
    SparkSession spark = SparkSession.builder()
      .appName("SimpleSparkApp")
      .master("spark://spark-master:7077")
      .config("spark.executor.processTreeMetrics.enabled", "false")
      .config("spark.driver.processTreeMetrics.enabled", "false")
      .getOrCreate();


    System.out.println("Successfully connected to Spark master!");

    // Create a simple Dataset of numbers from 1 to 10
    Dataset<Row> numberDF = spark.range(1, 11).toDF("number");

    // Perform a simple count operation
    long count = numberDF.count();
    System.out.println("Total records in the DataFrame: " + count);

    // Print the contents of the DataFrame
    System.out.println("DataFrame content:");
    numberDF.show();

    // Stop the SparkSession
    spark.stop();

  }
}
