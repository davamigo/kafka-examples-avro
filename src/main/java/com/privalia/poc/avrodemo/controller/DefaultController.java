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
 * Default controller - Route: /
 *
 * @author david.amigo
 */
@Controller
@RequestMapping("/")
public class DefaultController {

    /**
     * Action GET / - Default action - shows the homepage
     *
     * @param model the model to send to the view
     * @return the name of the template to load (list.html)
     */
    @GetMapping({"", "/"})
    public String homepageAction(Model model) {
        return "default/homepage";
    }
}
