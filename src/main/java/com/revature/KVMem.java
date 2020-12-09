/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database. 
 * 
 * In contrast to KVDAO, the KVMem class stores data that is 'live' - that is, that is in a temporary state that the
 * user can modify or view.
 */
package com.revature;

// import java.util.Set;
import java.util.Map;

public interface KVMem
{
    /**
     * Use the given Map as the data for this KVMem (should this just be given in the constructor? idk programming is hard)
     * 
     * @param data
     */
    public abstract void setData(Map<String, String> data);

    /**
     * Returns a Map object representing every entry in the data
     */
    public abstract Map<String, String> getAllData();

    /**
     * Returns the data corresponding to the given key, or null if there is no such key.
     * 
     * @param key
     */
    public abstract String getValue(String key);

    /**
     * Puts the given key/value pair into the live data. Returns true if successful, false otherwise
     * 
     * @param key
     * @param value
     */
    public abstract boolean put(String key, String value);

    /**
     * Removes the given key/value pair from the live data. Returns true if successful, false otherwise
     * 
     * @param key
     */
    public abstract boolean remove(String key);
} // end KVMem class