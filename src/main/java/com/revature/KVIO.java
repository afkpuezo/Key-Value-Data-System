/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database. 
 * 
 * This file contains an interface that describes methods for getting input, and displaying output, in order to drive
 * the KVDS system.
 * 
 * Also I'm drowning in acronyms now
 */
package com.revature;

public interface KVIO 
{
   /**
    * Sets the controller of this KVIO to the given controller.
    * 
    * :param controller: the controller object
    */
   public void setController(KVController controller);

   /**
    * Called by the Controller in order to indicate it is ready to ask if the
    * user/client wants to create or open a data store. May or may not lead to the
    * IO calling an operations method on the Controller.
    */
   public abstract void promptSetup();

   /**
    * Called by the Controller in order to indicate it is ready to ask if the
    * user/client wants to interact with a data store that has been opened - EG,
    * put or remove entries.. May or may not lead to the IO calling an operations
    * method on the Controller.
    */
   public abstract void promptDataInteraction();
} // end KeyValueIO