package com.privalia.poc.avrodemo.controller;

import com.privalia.poc.avrodemo.avro.Customer;
import com.privalia.poc.avrodemo.kafka.producer.CustomerKafkaAvroProducer;
import com.privalia.poc.avrodemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Controller for Customer V1 - Route: /customers
 *
 * @author david.amigo
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    /** The Kafka AVRO customers producer */
    private CustomerKafkaAvroProducer customerKafkaAvroProducer;

    /** The customers repository */
    private CustomerRepository customerRepository;

    /**
     * Default constructor
     *
     * @param customerKafkaAvroProducer the Kafka AVRO customers producer
     * @param customerRepository        the customers repository
     */
    @Autowired
    public CustomerController(
            CustomerKafkaAvroProducer customerKafkaAvroProducer,
            CustomerRepository customerRepository
    ) {
        this.customerKafkaAvroProducer = customerKafkaAvroProducer;
        this.customerRepository = customerRepository;
    }

    /**
     * Action GET / - Default action - shows the homepage
     *
     * @param model the model to send to the view
     * @return the name of the template to load (list.html)
     */
    @GetMapping({"", "/"})
    public String loistCustomersAction(Model model) {
        Collection<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    /**
     * Action GET /{id} - Shows the customer data
     *
     * @param model the model to send to the view
     * @param id    the id of the customer
     * @return the name of the template to load (list.html)
     */
    @GetMapping("/{id}")
    public String showCustomerAction(Model model, @PathVariable("id") String id) {
        Customer customer = customerRepository.find(id);
        model.addAttribute("customer", customer);
        return "customers/view";
    }

    /**
     * Action GET /create - Shows the form for creating a cutomer
     *
     * @param model the model to send to the view
     * @return the name of the template to load (create.html)
     */
    @GetMapping("/create")
    public String createCustomerAction(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/create";
    }

    /**
     * Action POST /create - Creates the customer - sends the customer to the Kafka topic
     *
     * @param customer the customer data
     * @return Redirecto to another page
     */
    @PostMapping("/create")
    public ModelAndView postCustomerAction(
            @ModelAttribute Customer customer,
            @Value("${sprint.kafka.topic.customers.topic-name}") String topic
    ) {
        try {
            customer.setUuid(UUID.randomUUID().toString());
            customerKafkaAvroProducer.send(topic, customer);
        } catch (ExecutionException | InterruptedException exc) {
            exc.printStackTrace();
        }
        return new ModelAndView("redirect:/customers");
    }
}
