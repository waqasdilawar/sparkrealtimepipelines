package org.devgurupk.realtimepipelines.kafka;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.current_date;
import static org.apache.spark.sql.functions.current_timestamp;
import static org.apache.spark.sql.functions.from_json;
import static org.apache.spark.sql.functions.year;

import java.util.concurrent.TimeoutException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class KafkaJsonStreamSchemaProcessor {
    
    public static void main(String[] args) throws StreamingQueryException, TimeoutException {
        SparkSession spark = SparkSession.builder()
          .appName("StructuredKafkaStream")
          .master("spark://spark-master:7077")
          .getOrCreate();
        
        spark.sparkContext().setLogLevel("TRACE");
        
        // Define JSON schema (adjust according to your JSON structure)
        StructType jsonSchema = DataTypes.createStructType(new StructField[]{
            DataTypes.createStructField("id", DataTypes.StringType, false),
            DataTypes.createStructField("name", DataTypes.StringType, true),
            DataTypes.createStructField("age", DataTypes.IntegerType, true),
            DataTypes.createStructField("email", DataTypes.StringType, true),
            DataTypes.createStructField("timestamp", DataTypes.TimestampType, true)
        });
        
        // Read from Kafka
        Dataset<Row> kafkaStream = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "broker:29092")
                .option("subscribe", "foo")
                .option("startingOffsets", "latest")
                .load();
        
        // Parse JSON and extract fields
        Dataset<Row> parsedStream = kafkaStream
                .select(
                    col("key").cast("string"),
                    from_json(col("value").cast("string"), jsonSchema).alias("data"),
                    col("topic"),
                    col("partition"),
                    col("offset"),
                    col("timestamp").alias("kafka_timestamp")
                )
                .select(
                    col("key"),
                    col("data.*"), // Expand all fields from JSON
                    col("topic"),
                    col("partition"),
                    col("offset"),
                    col("kafka_timestamp")
                );
        
        // Apply transformations
        Dataset<Row> transformedStream = parsedStream
                .filter(col("age").gt(18)) // Example filter
                .withColumn("processed_at", current_timestamp())
                .withColumn("year_of_birth", year(current_date()).minus(col("age")));
        
        // Output to console
        StreamingQuery query = transformedStream
                .writeStream()
                .outputMode("append")
                .format("console")
                .option("truncate", false)
                .trigger(org.apache.spark.sql.streaming.Trigger.ProcessingTime("10 seconds"))
                .start();
        
        query.awaitTermination();
    }
}
