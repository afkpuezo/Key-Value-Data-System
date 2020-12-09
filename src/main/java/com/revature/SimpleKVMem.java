/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database. 
 * 
 * This class implements the KVMem interface, which is probably overkill
 * 
 * I don't know if this is really necessary but OOP
 */
package com.revature;

import java.util.Set;
import java.util.Map;
import java.util.Hashtable;

public class SimpleKVMem implements KVMem
{
    // instance variables
    Map<String, String> data;

    /**
     * The constructor. Currently does nothing - you need to call set_data
     */
    public SimpleKVMem()
    {
        // yep
    } // end constructor

    /**
     * _Copies_ the given map as the data for this KVMem 
     * (should this just be given in the constructor? idk programming is hard)
     * 
     * @param data (must be a map of string, string)
     */
    @Override
    public void setData(Map<String, String> data)
    {
        this.data = new Hashtable<String, String>(data);
    }

    /**
     * Returns a copy of the map representing all of the data
     */
    @Override
    public Map<String, String> getAllData()
    {
        return new Hashtable<String, String>(data); // ez
    } // end getAllData()

    /**
     * Returns the data corresponding to the given key, or null if there is no such key.
     * 
     * @param key
     */
    @Override
    public String getValue(String key)
    {
        if (data.containsKey(key))
        {
            return data.get(key);
        }
        else
        {
            return null; // sketchy
        }
    }

    /**
     * Puts the given key/value pair into the live data. Returns true if successful, false otherwise
     * 
     * @param key
     * @param value
     */
    @Override
    public boolean put(String key, String value)
    {
        data.put(key, value);
        return true; // can this even fail? What have I done
    } // end put

    /**
     * Removes the given key/value pair from the live data. Returns true if successful, false otherwise
     * 
     * @param key
     */
    @Override
    public boolean remove(String key)
    {
        if (data.containsKey(key))
        {
            data.remove(key);
            return true;
        }
        else
        {
            return false;
        }
    } // end remove
} // end SimpleKVMem