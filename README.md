# [Kafka & AVRO Examples](https://github.com/davamigo/kafka-examples-avro)

**GitHub**: [davamigo/kafka-examples-avro](https://github.com/davamigo/kafka-examples-avro)

## AVRO contracts

### Customer

The customer is published into a Kafka topic using the Customer AVRO contract.

```
{
  "type": "record",
  "namespace": "com.privalia.poc.avrodemo.avro",
  "name": "Customer",
  "fields": [
    {
      "name": "uuid",
      "type": "string",
      "doc": "The unique identifier of the customer"
    },
    {
      "name": "first_name",
      "type": "string",
      "doc": "First name of the customer"
    },
    {
      "name": "last_name",
      "type": "string",
      "doc": "Last name of the customer"
    },
    {
      "name": "age",
      "type": [ "null", "int" ],
      "default": null,
      "doc": "Age at the time of registration"
    },
    {
      "name": "height",
      "type": [ "null", "float" ],
      "default": null,
      "doc": "Height at the time of registration in cm"
    },
    {
      "name": "weight",
      "type": [ "null", "float" ],
      "default": null,
      "doc": "Weight at the time of registration in kg"
    }
  ]
}
```

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
