package org.devgurupk.realtimepipelines;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class QueueProcessingApp {
  public static void main(String[] args) throws InterruptedException {
    JavaStreamingContext ssc = null;
    try {
      // For local testing; adjust/remove master for cluster runs
      SparkConf conf = new SparkConf()
        .setAppName("QueueStream")
        .setMaster("spark://localhost:7077")
        .set("spark.driver.host", "localhost")

      // Avoid Java 17 module-access issues when running from main()/IDE
        // by disabling process tree metrics. Alternatively, run with:
        // --add-exports=java.base/jdk.internal.platform=ALL-UNNAMED
        .set("spark.executor.processTreeMetrics.enabled", "false")
        .set("spark.driver.processTreeMetrics.enabled", "false");

      // Create the context with a 1 second batch size
      ssc = new JavaStreamingContext(conf, Durations.seconds(1));

      // Create and push some RDDs into the queue
      List<Integer> list = new ArrayList<>();
      for (int i = 0; i < 1000; i++) {
        list.add(i);
      }

      Queue<JavaRDD<Integer>> rddQueue = new LinkedList<>();
      for (int i = 0; i < 30; i++) {
        rddQueue.add(ssc.sparkContext().parallelize(list));
      }

      // Create the QueueInputDStream and use it to do some processing
      JavaDStream<Integer> inputStream = ssc.queueStream(rddQueue);
      JavaPairDStream<Integer, Integer> mappedStream = inputStream.mapToPair(i -> new Tuple2<>(i % 10, 1));
      JavaPairDStream<Integer, Integer> reducedStream = mappedStream.reduceByKey(Integer::sum);

      reducedStream.print();

      ssc.start();
      ssc.awaitTermination();
    } finally {
      // Stop gracefully
      if (ssc != null) {
        ssc.stop(true, true);
      }
    }
  }
}
