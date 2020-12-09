/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database.
 * 
 * This file will have the main method and set up the Controller, as well as the other classes/objects needed to get
 * things to run
 */
package com.revature;

 public class KVDS
 {
    public static void main(String[] args)
    {
        KVDAO dao = null;
        if (args.length > 0)
        {
                if (args[0].equals("-file")) dao = new TextFileDAO();
        }
        else
        {
                dao = new PostgresKVDAO();
        }
        // io mem dao
        KVIO io = new CommandLineKVIO();
        KVMem mem = new SimpleKVMem();
        KVController controller = new SimpleKVController();
        controller.inject(io, mem, dao);
    } // end main
    
 } // end KVDS class