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

```
realtimepipelines/
├── src/
│   └── main/
│       └── java/
│           └── org/devgurupk/realtimepipelines/
│               ├── SimpleSpark.java
│               ├── QueueProcessingApp.java
│               └── kafka/
│                   ├── KafkaStreamProcessor.java
│                   └── KafkaJsonStreamSchemaProcessor.java
├── artifacts/
│   └── realtimepipelines-1.0-SNAPSHOT.jar
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Getting Started

### 1. Build the Project

```bash
# Clean and compile the project
mvn clean compile

# Package the application
mvn package

# The JAR file will be created at: target/realtimepipelines-1.0-SNAPSHOT.jar

cp realtimepipelines-1.0-SNAPSHOT.jar ../artifacts/
```

### 2. Setup Infrastructure

Start Kafka and Spark services using Docker Compose:

```bash
docker-compose up -d
```

This will start:
- Apache Kafka broker
- Apache Spark master and worker nodes

### 3. Create Kafka Topic
* foo
* foobar

### 4. Running Applications

#### Simple Spark Application
Basic Spark connectivity test that creates a dataset and performs simple operations:

```bash
/opt/spark/bin/spark-submit \
--class org.devgurupk.realtimepipelines.SimpleSpark \
--master spark://localhost:7077 \
--driver-memory 1g \
--executor-memory 1g \
/artifacts/realtimepipelines-1.0-SNAPSHOT.jar
```

#### Queue Processing Application
Structured streaming application using rate source for continuous data processing:

```bash
/opt/spark/bin/spark-submit \
--class org.devgurupk.realtimepipelines.QueueProcessingApp \
--master spark://localhost:7077 \
--driver-memory 2g \
--executor-memory 2g \
/artifacts/realtimepipelines-1.0-SNAPSHOT.jar
```

#### Kafka Stream Processor
Processes messages from Kafka topics with basic JSON structure:

```bash
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

```bash
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

## Testing

### Send Test Messages to Kafka

```bash
# Send JSON messages to Kafka
# Produce to foobar
# Example messages to send:
{"name": "John", "age": 30}
{"name": "Jane", "age": 25}
{"name": "Bob", "age": 35}
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
- **Topic**: `foobar`
- **Consumer Group**: Auto-generated

## Application Components

### SimpleSpark
- Basic Spark application for connectivity testing
- Creates a simple dataset of numbers (1-10)
- Performs count operations and displays results
- Useful for verifying Spark cluster connectivity

### QueueProcessingApp
- Structured streaming application using rate source
- Generates continuous data streams at configurable rates
- Performs modulo operations and grouping
- Outputs running counts to console

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

## Monitoring

### Spark Web UI
Access the Spark Web UI at: `http://localhost:8080`

### Application Logs

```bash
# View Spark master logs
docker logs spark-master

# View Spark worker logs
docker logs spark-worker

# View application-specific logs
tail -f working.log

# View Kafka logs
docker logs broker
```

## Troubleshooting

### Common Issues

1. **Connection refused to Spark master**
   - Ensure Docker containers are running: `docker-compose ps`
   - Check Spark master is accessible: `curl http://localhost:8080`

2. **Kafka topic not found**
   - Create the topic first using the commands in Setup section
   - Verify topic exists: `foo` and `foobar`

3. **Out of memory errors**
   - Increase driver/executor memory in spark-submit commands
   - Monitor resource usage in Spark Web UI

## Development

### Prerequisites for Development
- IntelliJ IDEA or Eclipse IDE
- Java 21 JDK
- Maven 3.6+

## Technology Stack

- **Apache Spark 4.0.0**: Distributed computing framework
- **Apache Kafka**: Distributed streaming platform
- **Java 21**: Programming language
- **Maven**: Build and dependency management
- **Docker**: Containerization platform


