package com.privalia.poc.avrodemo.repository;

import com.privalia.poc.avrodemo.avro.Customer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository to store customers
 *
 * As this is a PoC, no database is used. The customers are stored in a hash map in memory.
 *
 * @author david.amigo
 */
@Component
public class CustomerRepository implements RepositoryInterface<String, Customer> {

    private static Map<String, Customer> customers = new HashMap<>();

    /**
     * Finds a value by key
     *
     * @param key the key to find
     * @return the value or null if not found
     */
    @Override
    public Customer find(String key) {
        return customers.get(key);
    }

    /**
     * Finds all the values
     *
     * @return the Collection of values
     */
    @Override
    public Collection<Customer> findAll() {
        return customers.values();
    }

    /**
     * Inserts/updates a key, value pair
     *
     * @param key   the key to persist
     * @param value the value to persist
     * @return The value
     */
    @Override
    public Customer persist(String key, Customer value) {
        return customers.put(key, value);
    }

    /**
     * Deletes a key, value pair
     *
     * @param key the key to delete
     * @return the deleted value or null if not found
     */
    @Override
    public Customer delete(String key) {
        return customers.remove(key);
    }
}
