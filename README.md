# [Kafka & AVRO Examples](https://github.com/davamigo/kafka-examples-avro)

**GitHub**: [davamigo/kafka-examples-avro](https://github.com/davamigo/kafka-examples-avro)

## Development

### Docker

This PoC uses the **[landoop/fast-data-dev](https://hub.docker.com/r/landoop/fast-data-dev)** docker image because it is very complete and easy to use.

This image has a **Zookeeper** (port: `2121`), 
a **Kafka Broker** (port: `9092`), 
the Confluent **Schema Registry** (port: `8081`) to use AVRO, 
the Confluent **REST Proxy** (port: `8082`)
an d a web based *User Interface* (port: `3030`).

#### scripts

* *`docker/startKafka.sh`*
```
docker run --rm -d \
    -p 2181:2181 -p 3030:3030 \
    -p 8081:8081 -p 8082:8082 \
    -p 8083:8083 -p 9092:9092 \
    -e ADV_HOST=127.0.0.1 \
    -e SAMPLEDATA=0 \
    -e RUNTESTS=0 \
    --name docker_kafka_dev \
    landoop/fast-data-dev:2.0
```

* *`docker/stopKafka.sh`*
```
docker stop docker_kafka_dev
```
