package com.privalia.poc.avrodemo.config;

import com.privalia.poc.avrodemo.avro.Customer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.producer.acks}")
    private String producerAcks;

    @Value("${spring.kafka.producer.retries}")
    private String producerRetries;

    @Value("${spring.kafka.producer.linger-ms}")
    private String producerLingerMs;

    @Value("${sprint.kafka.topic.customers.topic-name}")
    private String customersTopicName;

    /**
     * Global configuration values for all Kafka producers
     *
     * @return the default configurations for all the Kafka producers
     */
    Map<String, Object> kafkaProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(ProducerConfig.ACKS_CONFIG, producerAcks);
        props.put(ProducerConfig.RETRIES_CONFIG, producerRetries);
        props.put(ProducerConfig.LINGER_MS_CONFIG, producerLingerMs);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        return props;
    }

    /**
     * Creates a factory for a Kafka producer
     *
     * @param props the configuration for the Kafka producer
     * @return the default kafka producer factory
     */
    ProducerFactory<String, Customer> kafkaProducerFactory(Map<String, Object> props) {
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * Component: Kafka template for producing messages
     *
     * @return a new Kafka template for producing messages
     */
    @Bean
    public KafkaTemplate<String, Customer> producerTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory(kafkaProducerConfigs()));
    }

    /**
     * Component: get the topic name for customers
     *
     * @return the name of the topic
     */
    @Bean
    public String customersTopicName() {
        return customersTopicName;
    }
}
