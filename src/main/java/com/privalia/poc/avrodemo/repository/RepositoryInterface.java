package com.privalia.poc.avrodemo.repository;

import java.util.Collection;

/**
 * Interface to a generic basic repository
 *
 * @param <K> the key class
 * @param <V> the value class
 * @author david.amigo
 */
public interface RepositoryInterface<K, V> {

    /**
     * Finds a value by key
     *
     * @param key the key to find
     * @return the value or null if not found
     */
    public V find(K key);

    /**
     * Finds all the values
     *
     * @return the list of values
     */
    public Collection<V> findAll();

    /**
     * Inserts/updates a key, value pair
     *
     * @param key   the key to persist
     * @param value the value to persist
     * @return The value
     */
    public V persist(K key, V value);

    /**
     * Deletes a key, value pair
     *
     * @param key the key to delete
     * @return the deleted value or null if not found
     */
    public V delete(K key);
}
