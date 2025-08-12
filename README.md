# Real-Time Data Pipelines with Apache Spark and Kafka

A comprehensive real-time data processing application built with Apache Spark Structured Streaming and Apache Kafka. This project demonstrates modern stream processing patterns for ingesting, processing, and analyzing data in real-time.

## Features

- **Real-time Stream Processing**: Process Kafka streams using Apache Spark Structured Streaming
- **JSON Schema Processing**: Handle structured JSON data with automatic schema inference
- **Scalable Architecture**: Distributed processing with configurable memory and core allocation
- **Fault Tolerance**: Built-in checkpoint and recovery mechanisms
- **Multiple Stream Processors**: Different processors for various data processing scenarios

## Prerequisites

- **Java 21** or higher
- **Apache Spark 4.0.0**
- **Apache Kafka** (for message streaming)
- **Maven 3.6+** (for building the project)
- **Docker & Docker Compose** (optional, for containerized setup)

## Project Structure
realtimepipelines/ ├── src/ │ └── main/ │ └── java/ │ └── org/devgurupk/realtimepipelines/ │ └── kafka/ │ ├── KafkaStreamProcessor.java │ └── KafkaJsonStreamSchemaProcessor.java ├── artifacts/ │ └── realtimepipelines-1.0-SNAPSHOT.jar ├── docker-compose.yml ├── pom.xml └── README.md

## Getting Started

### 1. Build the Project

```bash
# Clean and compile the project
mvn clean compile

# Package the application
mvn package

# The JAR file will be created at: target/realtimepipelines-1.0-SNAPSHOT.jar
```

### 2. Setup Infrastructure
Start Kafka and Spark services using Docker Compose:
docker-compose up -d

This will start:
- Apache Kafka broker
- Apache Spark master and worker nodes

### 3. Running the Stream Processors
#### Basic Kafka Stream Processor
Processes messages from Kafka topics with basic JSON structure:
```shell
/opt/spark/bin/spark-submit \
--class org.devgurupk.realtimepipelines.kafka.KafkaStreamProcessor \
--master spark://localhost:7077 \
--deploy-mode client \
--conf spark.jars.ivy=/tmp/.ivy2 \
--conf spark.driver.processTreeMetrics.enabled=false \
--conf spark.executor.processTreeMetrics.enabled=false \
--packages org.apache.spark:spark-sql-kafka-0-10_2.13:4.0.0 \
--driver-memory 2g \
--executor-memory 2g \
--total-executor-cores 4 \
/artifacts/realtimepipelines-1.0-SNAPSHOT.jar
```

#### JSON Schema Stream Processor
Advanced processor with schema validation and complex JSON handling:

```shell
/opt/spark/bin/spark-submit \
--class org.devgurupk.realtimepipelines.kafka.KafkaJsonStreamSchemaProcessor \
--master spark://localhost:7077 \
--deploy-mode client \
--conf spark.jars.ivy=/tmp/.ivy2 \
--conf spark.driver.processTreeMetrics.enabled=false \
--conf spark.executor.processTreeMetrics.enabled=false \
--packages org.apache.spark:spark-sql-kafka-0-10_2.13:4.0.0 \
--driver-memory 2g \
--executor-memory 2g \
--total-executor-cores 4 \
/artifacts/realtimepipelines-1.0-SNAPSHOT.jar
```

## Configuration
### Spark Configuration Parameters

| Parameter | Description | Default Value |
| --- | --- | --- |
| `--driver-memory` | Memory allocation for Spark driver | 2g |
| `--executor-memory` | Memory allocation for Spark executors | 2g |
| `--total-executor-cores` | Total CPU cores for executors | 4 |
| `--master` | Spark cluster master URL | spark://localhost:7077 |
### Kafka Configuration
The applications connect to Kafka using the following default settings:
- **Bootstrap Servers**: `broker:29092`
- **Topic**: `test`
- **Consumer Group**: Auto-generated

## Stream Processing Components
### KafkaStreamProcessor
- Basic stream processing from Kafka topics
- JSON parsing with predefined schema (name: String, age: Integer)
- Console output for processed data
- Suitable for simple streaming scenarios

### KafkaJsonStreamSchemaProcessor
- Advanced JSON schema processing
- Dynamic schema inference and validation
- Complex data transformations
- Extensible for enterprise-grade applications

## Monitoring and Debugging
### Spark Web UI
Access the Spark Web UI at: `http://localhost:8080`
### Application Logs
Monitor application logs in real-time:
# View Spark driver logs


# View application-specific logs
```shell
docker logs spark-master
tail -f working.log
```

## Technology Stack
- **Apache Spark 4.0.0**: Distributed computing framework
- **Apache Kafka**: Distributed streaming platform
- **Java 21**: Programming language
- **Maven**: Build and dependency management
- **Docker**: Containerization platform


