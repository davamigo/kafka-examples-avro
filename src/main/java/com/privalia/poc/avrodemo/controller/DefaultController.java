package com.privalia.poc.avrodemo.controller;

import com.privalia.poc.avrodemo.avro.Customer;
import com.privalia.poc.avrodemo.kafka.producer.CustomerKafkaAvroProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/")
public class DefaultController {

    /** The Kafka producer */
    private CustomerKafkaAvroProducer kafkaAvroProducer;

    /** the name of the topic for customers */
    private String customersTopicName;

    /**
     * Default constructor
     *
     * @param kafkaAvroProducer the Kafka AVRO producer
     */
    @Autowired
    public DefaultController(CustomerKafkaAvroProducer kafkaAvroProducer, String customersTopicName) {
        this.kafkaAvroProducer = kafkaAvroProducer;
        this.customersTopicName = customersTopicName;
    }

    /**
     * Action GET / - Default action - shows the homepage
     *
     * @return the name of the template to load (homepage.html)
     */
    @GetMapping("/")
    public String homepageAction() {
        return "homepage";
    }

    /**
     * Action GET /create - Shows the form for creating a cutomer
     *
     * @return the name of the template to load (create.html)
     */
    @GetMapping("/create")
    public String createCustomerAction(Model model) {
        model.addAttribute("customer", new Customer());
        return "create";
    }

    /**
     * Action POST /create - Creates the customer - sends the customer to the Kafka topic
     *
     * @param customer the customer data
     * @return Redirecto to another page
     */
    @PostMapping("/create")
    public ModelAndView createCustomerSubmitAction(@ModelAttribute Customer customer) {
        try {
            customer.setUuid(UUID.randomUUID().toString());
            kafkaAvroProducer.send(customersTopicName, customer);
        } catch (ExecutionException | InterruptedException exc) {
            exc.printStackTrace();
        }
        return new ModelAndView("redirect:/");
    }
}
