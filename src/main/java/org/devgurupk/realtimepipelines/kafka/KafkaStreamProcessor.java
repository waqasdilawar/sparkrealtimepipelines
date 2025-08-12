package org.devgurupk.realtimepipelines.kafka;

import java.util.concurrent.TimeoutException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
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
      .appName("KafkaStreamProcessor")
      .master("spark://spark-master:7077")
      .getOrCreate();

    try {
      Dataset<String> inputStream = spark.readStream()
        .format("kafka")
        .option("kafka.bootstrap.servers", "broker:29092")
        .option("subscribe", "foobar")
        .load()
        .selectExpr("CAST(value AS STRING)")
        .as(Encoders.STRING());

      StructType schema = new StructType()
        .add("name", DataTypes.StringType)
        .add("age", DataTypes.IntegerType);

      Dataset<Row> processedStream = inputStream
        .withColumn("data", functions.from_json(functions.col("value"), schema))
        .select("data.*");

      StreamingQuery query = processedStream.writeStream()
        .outputMode("append")
        .format("console")
        .start();
      query.awaitTermination();
    } finally {
      spark.stop();
    }
  }
}
