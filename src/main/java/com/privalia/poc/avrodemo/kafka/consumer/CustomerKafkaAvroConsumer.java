package com.privalia.poc.avrodemo.kafka.consumer;

import com.privalia.poc.avrodemo.avro.Customer;
import com.privalia.poc.avrodemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Component for reading customers from the Kafka topic and persisting them into a repository
 *
 * @author david.amigo
 */
@Component
public class CustomerKafkaAvroConsumer {

    private CustomerRepository repository;

    /**
     * Autowired repository
     *
     * @param repository the customers repository
     */
    @Autowired
    public CustomerKafkaAvroConsumer(CustomerRepository repository) {
        this.repository = repository;
    }

    /**
     * Kafka listener
     *
     * @param value The value of the message
     * @param ack   The acknowledgment object
     * @param key   The key of the message
     */
    @KafkaListener(
            topics="${sprint.kafka.topic.customers.topic-name}",
            groupId="${sprint.kafka.topic.customers.group-id}",
            containerFactory="customerKafkaListenerContainerFactory"
    )
    public void listen(
            Customer value,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key
    ) {
        repository.persist(key, value);
        ack.acknowledge();
    }
}
