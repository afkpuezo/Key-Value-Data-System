/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database. Data will be stored
 * in un-encrypted plain-text files.
 * 
 * This file contains a class that implements the KVController interface.
 */

package com.revature;

import java.util.Map;

import com.revature.KVIO;



public class SimpleKVController implements KVController
{
    // instance variables
    private KVIO io;
    private KVMem mem;
    private KVDAO dao;

    /**
     * The constructor - currently does nothing. Make sure to call inject()
     */
    public SimpleKVController()
    {
        // yep
    } // end constructor

    // KVController stuff -----------------

    /**
    * "Inject" the controller with the objects it will need to run. This will begin execution / interaction with the user, as well.
    * The controller will have to give the IO a reference to itself
    * 
    * :param io: a KVIO
    * :param mem: a KVMem
    * :param dao: a KVDAO
    */
    @Override
    public void inject(KVIO the_io, KVMem the_mem, KVDAO the_dao)
    {
        io = the_io;
        mem = the_mem;
        dao = the_dao;

        io.setController(this);
        io.promptSetup();
    } // end inject method

    /**
    * Called by the KVIO - creates a pre-existing data store, with the given name. 
    * If the operation fails, this will return false; otherwise it will begin interaction
    * 
    * :param name: the name of the file/database/table/whatever
    */
    @Override
    public boolean create(String name)
    {
         if (dao.setName(name))
         {
            if (dao.create())
            {
                // TODO this block should be its own method maybe?
                Map<String, String> data = dao.open();
                if (data == null)
                {
                    return false;
                } else 
                {
                    mem.setData(data);
                    // io.promptDataInteraction();
                    return true;
                }
            }
            else
            {
                return false;
            }
         }
         else
         {
             return false;
         }
    } // end create method

    /**
    * Called by the KVIO - opens a pre-existing data store, with the given name. 
    * If the operation fails, this will return false; otherwise it will begin interaction
    * 
    * :param name: the name of the file/database/table/whatever
    */
    @Override
    public boolean open(String name)
    {
        if (dao.setName(name))
         {
            Map<String, String> data = dao.open();
            if (data == null)
            {
                return false;
            }
            else
            {
                mem.setData(data);
                // io.promptDataInteraction();
                return true;
            }
         }
         else
         {
             return false;
         }
    } // end open method

    /**
    * Called by the KVIO - returns the data corresponding to the given key, or null if there is no such key.
    * Just calls the KVMem
    * 
    * :param key:
    */
    @Override
    public String getValue(String key)
    {
        return mem.getValue(key);
    } // end getValue

    /**
     * Returns a Map object representing every entry in the data. Called by the KVIO to access the KVMem
     * Just calls the KVMem
     */
    @Override
    public Map<String, String> getAllData()
    {
        return mem.getAllData();
    } // end getAllData method

    /**
    * Called by the KVIO - puts the given key/value pair into the live data. Returns true if successful, false otherwise
    * 
    * @param key What it looks like
    * @param value What it looks like
    */
    @Override
    public boolean put(String key, String value)
    {
        return mem.put(key, value);
    } // end put method

    /**
    * Called by the KVIO - removes the given key/value pair from the live data. Returns true if successful, false otherwise
    * Just calls the KVMem
    * 
    * @param key What it looks like
    */
    @Override
    public boolean remove(String key)
    {
        return mem.remove(key);
    } // end remove method

    /**
    * Called by the KVIO - saves the data in the permanent data store. If successful, returns true and ends execution. If failed,
    * return false and continues execution.
    * Just calls the KVMem
    */
    @Override
    public boolean save()
    {
        return dao.save(mem.getAllData());
    } // end save method

    /**
    * Called by the KVIO - quits without saving and ends execution.
    */
    @Override
    public void quit()
    {
        // I think it actually does nothing?
    } // end quit method

    /**
    * Called by the KVIO when there is some IO problem
    */
    @Override
    public void reportKVIOProblem()
    {
        quit();
    } // end reportKVIOProblem method
} // end SimpleKVController