/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database. 
 *
 * This file contains a class that implements the KeyValueDAO interface. This implementation uses BufferedReader/Wrtier to
 * save the data in plain-text files.
 * 
 * (technically to be consistent with the names of other classes, this should be TextFileKVDAO)
 */
package com.revature;
import java.util.Hashtable;
import java.util.Map; 
import java.io.*; // maybe I should import each class I need specifically but this is easier

public class TextFileDAO implements KVDAO
{
    // instance variables -----------------
    private String filename;
    private boolean name_set;

    /**
     * The constructor. You still need to call set_name before doing anything with the data.
     */
    public TextFileDAO()
    {
        name_set = false;
    } // end constructor

    // methods from DataIO ---------

    /**
     * This method should be called before any of the other DataIO methods. It sets the name of the file that this DataIO 
     * object will be interacting with. Returns true if the name is set, false if it is not (EG if there was already a name set)
     * 
     * :param args: command-line input from the player. Will be parsed to determine the file/table name.
     */
    @Override
    public boolean setName(String name)
    {
        if (name_set)
        {
            return false;
        }

        filename = name;
        name_set = true;
        return true;
    } // end set_name method

    /**
     * Create a new file to store data in. Returns a boolean reflecting whether or not the creation was successful. Will overwrite
     * a file if one already exists with the name this object was given.
     */
    @Override
    public boolean create()
    {
        BufferedWriter file;

        try
        {
            file = new BufferedWriter(new FileWriter(filename));
            file_closing_helper(file);
            return true;
        } // end try
        catch (IOException e)
        {
            return false;
        } // end catch
    } // end run_create method

    /**
     * Opens a pre-existing file/table and loads the data. Returns the data if successful, null if not.
     */
    @Override
    public Map<String, String> open()
    {
        //get the data from the given file
        try
        {
            Map<String, String> data = new Hashtable<String, String>();
            BufferedReader file = new BufferedReader(new FileReader(filename));


            String[] line_words;
            while (file.ready())
            {
                // split key and value - this will mean that keys can't have spaces, but values can?
                line_words = file.readLine().split(" ", 2);
                data.put(line_words[0], line_words[1]);
            } // end while loop

            file_closing_helper(file);
            return data;
        } // end try
        catch (IOException e)
        {
            return null; // failure
        } // end catch
    } // end run_open method

    /**
     * Takes the given data and saves it into the text file. Returns true if the data was saved successfully, false otheriwse.
     */
    @Override
    public boolean save(Map<String, String> data)
    {
        try
        {
            BufferedWriter file = new BufferedWriter(new FileWriter(filename));
            // String output;

            for (String key : data.keySet())
            {
                file.write(key + " " + data.get(key));
                file.newLine();
            } // end for

            file_closing_helper(file);
            return true;
        } // end try
        catch (IOException e)
        {
            return false;
        } // end catch

    } // end run_save method

    // helper methods ---------

    /**
    This handles closing files, since that needs its own try/catch and it happens a lot in the code.
    This is overloaded to handle BufferedReaders and BufferedWriters
     */
    private void file_closing_helper(BufferedWriter file)
    {
        // this looks like a mess but I think it's the correct way to handle closing files?
            try
            {
                file.close(); //apparently this creates the file now
            }
            catch (IOException e)
            {
                System.out.println("Error closing file: " + filename);
            }
    } // end file_closing_helper method

    /**
    This handles closing files, since that needs its own try/catch and it happens a lot in the code.
    This is overloaded to handle BufferedReaders and BufferedWriters
     */
    private void file_closing_helper(BufferedReader file)
    {
        // this looks like a mess but I think it's the correct way to handle closing files?
            try
            {
                file.close(); //apparently this creates the file now
            }
            catch (IOException e)
            {
                System.out.println("Error closing file: " + filename);
            }
    } // end file_closing_helper method
} // end TextFileDataIO class