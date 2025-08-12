```shell
 /opt/spark/bin/spark-submit   --class org.devgurupk.realtimepipelines.kafka.KafkaStreamProcessor   --master spark://localhost:7077   --deploy-mode client   --conf spark.jars.ivy=/tmp/.ivy2   --conf spark.driver.processTreeMetrics.enabled=false   --conf spark.executor.processTreeMetrics.enabled=false   --packages org.apache.spark:spark-sql-kafka-0-10_2.13:4.0.0   --driver-memory 2g   --executor-memory 2g   --total-executor-cores 4   /artifacts/realtimepipelines-1.0-SNAPSHOT.jar
```


```shell
 /opt/spark/bin/spark-submit   --class org.devgurupk.realtimepipelines.kafka.KafkaJsonStreamSchemaProcessor   --master spark://localhost:7077   --deploy-mode client   --conf spark.jars.ivy=/tmp/.ivy2   --conf spark.driver.processTreeMetrics.enabled=false   --conf spark.executor.processTreeMetrics.enabled=false   --packages org.apache.spark:spark-sql-kafka-0-10_2.13:4.0.0   --driver-memory 2g   --executor-memory 2g   --total-executor-cores 4   /artifacts/realtimepipelines-1.0-SNAPSHOT.jar
```