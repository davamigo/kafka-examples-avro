package com.privalia.poc.avrodemo.kafka.producer;

import com.privalia.poc.avrodemo.avro.Customer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * Component for sending customers to a Kafka topic
 *
 * @author david.amigo
 */
@Component
public class CustomerKafkaAvroProducer {

    /**
     * Kafka template for producing messages
     */
    private final KafkaTemplate<String, Customer> producerTemplate;

    /**
     * Autowired constructor
     *
     * @param producerTemplate the Kafka template for producing messages
     */
    @Autowired
    public CustomerKafkaAvroProducer(KafkaTemplate<String, Customer> producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    /**
     * Publishes a customer to a topic
     *
     * @param topic the name of the topic
     * @param customer the customer data
     * @return the result
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted
     */
    public SendResult<String, Customer> send(String topic, Customer customer) throws ExecutionException, InterruptedException {
        String key = customer.getUuid();
        ProducerRecord<String, Customer> record = new ProducerRecord<>(topic, key, customer);
        SendResult<String, Customer> result = producerTemplate.send(record).get();
        return result;
    }
}
