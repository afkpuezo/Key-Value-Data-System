/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database. 
 *
 * This file contains an interface describing the behavior needed for a class to perform input and output functions on 
 * the actual data for KVDS. The purpose is for classes implementing this interface to interact with the permanent data
 * storage and put it into a format that's easy for java to deal with (that is, a Map).
 * 
 */
package com.revature;

// imports
import java.util.Map;

public interface KVDAO
{
    /**
     * This method should be called before any of the other DataIO methods. It sets the name of the file/table/database that this DataIO 
     * object will be interacting with. Returns true if the name is set, false if it is not (EG if there was already a name set)
     * 
     * @param name: the name of the data store to be managed by this DAO
     */
    public abstract boolean setName(String name);

    /**
     * Create a new file, or table, or whatever to store data in. Returns a boolean reflecting whether or not the creation was successful.
     */
    public abstract boolean create();

    /**
     * Opens a pre-existing file/table and loads the data. Returns the data if successful, null if not.
     */
    public abstract Map<String, String> open();

    /**
     * Takes the given data and saves it into the permanent data format. Returns true if the data was saved successfully, false otheriwse.
     */
    public abstract boolean save(Map<String, String> data);
} // end DataIO interface