package com.privalia.poc.avrodemo.config;

import com.privalia.poc.avrodemo.avro.Customer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka config class - defines beans
 *
 * @author david.amigo
 */
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

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String consumerAutoOffsetReset;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String consumerEnableAutoCommit;

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
     * Global configuration values for all Kafka consumers
     *
     * @return the default configurations for all the Kafka consumers
     */
    Map<String, Object> kafkaConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerAutoOffsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerEnableAutoCommit);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return props;
    }

    /**
     * Creates a factory for a Kafka producer for customers
     *
     * @param props the configuration for the Kafka producer
     * @return the default kafka producer factory
     */
    ProducerFactory<String, Customer> customerKafkaProducerFactory(Map<String, Object> props) {
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * Kafka consumer factory object for ErrorLog topics
     *
     * @return the default kafka consumer factory.
     */
    ConsumerFactory<String, Customer> customersKafkaConsumerFactory() {
        Map<String, Object> props = kafkaConsumerConfigs();
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Component: Kafka template for producing messages for customers
     *
     * @return a new Kafka template for producing messages
     */
    @Bean
    public KafkaTemplate<String, Customer> customerKafkaProducerTemplate() {
        return new KafkaTemplate<>(customerKafkaProducerFactory(kafkaProducerConfigs()));
    }

    /**
     * Kafka listener container factory bean for ErrorLog topics
     *
     * @return the kafka listener container factory for consumer.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Customer>> customerKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Customer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(customersKafkaConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
