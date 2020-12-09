/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database. Data will be stored
 * in un-encrypted plain-text files.
 * 
 * This file contains the interface KVController, which controls the other classes/objects that make the KVDS run. It
 * will take directions from the KVIO and use those to direct the behavior of the KVDAO and the KVMem.
 */
package com.revature;

import java.util.Map;

 public interface KVController
 {
   /**
    * "Inject" the controller with the objects it will need to run. The controller will have to give the IO a reference to itself
    * 
    * :param io: a KVIO
    * :param mem: a KVMem
    * :param dao: a KVDAO
    */
   public abstract void inject(KVIO io, KVMem mem, KVDAO dao);

   /**
    * Called by the KVIO - creates a pre-existing data store, with the given name. Returns true if successful, false otherwise
    * 
    * :param name: the name of the file/database/table/whatever
    */
   public abstract boolean create(String name);

   /**
    * Called by the KVIO - opens a pre-existing data store, with the given name. Returns true if successful, false otherwise
    * 
    * :param name: the name of the file/database/table/whatever
    */
   public abstract boolean open(String name);

   /**
    * Called by the KVIO - returns the data corresponding to the given key, or null if there is no such key.
    * 
    * :param key:
    */
   public abstract String getValue(String key);

   /**
     * Returns a Map object representing every entry in the data. Called by the KVIO to access the KVMem
     */
    public abstract Map<String, String> getAllData();

   /**
    * Called by the KVIO - puts the given key/value pair into the live data. Returns true if successful, false otherwise
    * 
    * :param key:
    * :param value:
    */
   public abstract boolean put(String key, String value);

   /**
    * Called by the KVIO - removes the given key/value pair from the live data. Returns true if successful, false otherwise
    * 
    * @param key
    */
   public abstract boolean remove(String key);

   /**
    * Called by the KVIO - saves the data in the permanent data store. If successful, returns true and ends execution. If failed,
    * return false and continues execution.
    */
    public abstract boolean save();

    /**
     * Called by the KVIO - quits without saving and ends execution.
     */
    public abstract void quit();
 
    /**
     * Called by the KVIO when there is some IO problem
     */
    public abstract void reportKVIOProblem();
 } // end KVController interface