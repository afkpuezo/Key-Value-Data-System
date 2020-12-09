/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database. 
 * 
 * This file contains a class that implements the KVIO interface, interacting with the user through the command line.
 */
package com.revature;

import java.io.*; // maybe I should import each class I need specifically but this is easier
import java.util.Map;

import javax.lang.model.util.ElementScanner14; 

public class CommandLineKVIO implements KVIO
{
    // class variables
    // constant values
    private static final String NAME = "KVDS"; 
    // private static final String FILE_EXTENSION = ""; // this was creating problems

    // expected command line parameter strings
    // private static final String TEST = "test";
    private static final String HELP = "help";
    private static final String CREATE = "create";
    private static final String OPEN = "open";

    // expected during-execution input strings
    private static final String CLOSE = "save"; // renamed this for clarity
    private static final String QUIT = "quit"; 
    private static final String PUT = "put";
    private static final String REMOVE = "rmv";
    private static final String VIEW = "view";
    // private static final String ALL = "*"; // means keys can't be this

    // strings to be printed out during execution
    private static final String SETUP_PROMPT = ">";
    private static final String SETUP_IO_ERROR_MESSAGE_START = "Problem encountered:";
    
    private static final String NO_ARGS_MESSAGE = NAME + ": missing argument. (Type " 
                                                + NAME + " " + HELP + " for a list of commands.)";

    private static final String BAD_ARGS_MESSAGE_START = "Invalid arguments. \"";
    private static final String BAD_ARGS_MESSAGE_END = "\" not recognized. (Type " 
                                                + NAME + " " + HELP + " for a list of commands.)";

    private static final String FILE_CLOSE_IO_ERROR_MESSAGE_START = "Error with closing system input reader.";

    private static final String OPEN_IO_ERROR_MESSAGE_START = "Problem opening data store.";
    private static final String OPEN_SUCCESSFUL_MESSAGE_END = "Data store opened successfully."; 

    private static final String CREATE_IO_ERROR_MESSAGE_START = "Problem creating data store.";
    private static final String CREATE_SUCCESS_MESSAGE_END = "Data store created successfully.";

    private static final String LOOP_PROMPT = ">";
    private static final String LOOP_IO_ERROR_MESSAGE_START = "Problem encountered.";
    private static final String QUIT_CHECK_PROMPT = "Are you sure you want to quit without saving? (y/n): ";                               
    private static final String YES_CHECK = "y";
    private static final String LOOP_BAD_ARGS_MESSAGE = "\" not recognized. (Type " 
                                                + " " + HELP + " for a list of commands.)";

    private static final String VIEW_NOT_FOUND_MESSAGE_START = "Entry with key \"";
    private static final String VIEW_NOT_FOUND_MESSAGE_END = "\" not found.";

    private static final String CLOSE_IO_ERROR_MESSAGE_START = "Error while saving to data store.";
    private static final String CLOSE_SUCCESSFUL_MESSAGE = "Data saved successfully.";

    private static final String CONSOLE_INPUT_CLOSE_ERROR = "There was a problem closing the console input reader.";

    /**
    it may or may not be better to have these in some kind of collection 
    for printing all of the help messages at once.
     */
    private static final String HELP_CREATE_MESSAGE = "\"java KVDS " + CREATE + " <filename>\": Create"
    + " open a new database. If you save the data, it will overwrite a previously existing file.";
    private static final String HELP_OPEN_MESSAGE = "\"java KVDS " + OPEN + "<filename>\": Create "
    + "and open a pre-existing database.";

    private static final String LOOP_HELP_VIEW_MESSAGE = "\"" + VIEW + " <key>\": Show the entry "
    + "with that key. (Omit key to view all entries)";
    private static final String LOOP_HELP_PUT_MESSAGE = "\"" + PUT + "<key> <value>\": Add entry."
    + " (Keys cannot include spaces)(If an entry with that key already exists, it will be overwritten";
    private static final String LOOP_HELP_REMOVE_MESSAGE = "\"" + REMOVE + "<key>\" :Remove entry "
    + "with that key. (Safe if no such entry exists)";
    private static final String LOOP_HELP_CLOSE_MESSAGE = "\"" + CLOSE + "\": Save data, close "
    + "file, and end execution.";
    private static final String LOOP_HELP_QUIT_MESSAGE = "\"" + QUIT + "\": Close file and end "
    + "execution without saving data.";

    // instance variables
    KVController controller;
    // BufferedReader console_input;

    /**
     * The construcotr - takes no argument but performs some initialization. The controller will still need to
     * call setController()
     */
    public CommandLineKVIO()
    {
        // i guess there is nothing here right now
    } // end constructor

    /**
     * Sets the controller of this KVIO to the given controller.
     * 
     * @param controller: the controller object
     */
    @Override
    public void setController(KVController controller)
    {
        this.controller = controller;
    }

    /**
     * Called by the Controller in order to indicate it is ready to ask if the user/client wants to create or open
     * a data store. May or may not lead to the IO calling an operations method on the Controller.
     */
    @Override
    public void promptSetup()
    {
        BufferedReader consoleInput = null;
        String inputText;
        String[] inputWords;

        try
        {
            consoleInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print(SETUP_PROMPT);
            inputText = consoleInput.readLine(); 
            inputWords = inputText.split(" ", 2); // command and filename, or help and command
            // make sure the array isn't just empty
            if (inputWords.length == 0)
            {
                runNoArgs();
                return; // end execution - don't let it get to the switch statement
            }  // end if length == 0
            // use the first arg to figure out what the user is trying to do
            switch(inputWords[0])
            {
                    case HELP :
                        runHelp(inputWords);
                        break;
                    case CREATE :
                        
                        if (controller.create(inputWords[1])) //should be the name
                        {
                            System.out.println(CREATE_SUCCESS_MESSAGE_END);
                            promptDataInteraction(); // originally this was called by the controller but that caused problems
                        }
                        else
                        {
                            System.out.println(CREATE_IO_ERROR_MESSAGE_START);
                        }
                        break;
                    case OPEN :
                        if(controller.open(inputWords[1]))
                        {
                            System.out.println(OPEN_SUCCESSFUL_MESSAGE_END);
                            promptDataInteraction(); // originally this was called by the controller but that caused problems
                        }
                        else
                        {
                            System.out.println(OPEN_IO_ERROR_MESSAGE_START);
                        }
                        break;
                    default :
                        runBadArgs(inputWords);
                        break;
            } // end switch input_words[0]        
        } // end try
        catch (IOException e)
        {
            System.out.println(SETUP_IO_ERROR_MESSAGE_START + " " + e.getMessage());
            controller.reportKVIOProblem();
        } // end catch
        finally
        {
            fileClosingHelper(consoleInput);
        } // end finally

        
    } // end promptSetup()

    /**
     * Called by the Controller in order to indicate it is ready to ask if the user/client wants to interact with
     * a data store that has been opened - EG, put or remove entries.. May or may not lead to the IO calling an operations 
     * method on the Controller.
     * 
     * This will repeat on a loop until execution is ended by an error or by the user quitting
     */
    @Override
    public void promptDataInteraction()
    {
        BufferedReader consoleInput = null; // cheeky
        String inputText;
        String[] inputWords;
        boolean running = true;

        try
        {
            consoleInput = new BufferedReader(new InputStreamReader(System.in));

            while (running)
            {
                System.out.print(LOOP_PROMPT);
                inputText = consoleInput.readLine(); 
                inputWords = inputText.split(" ", 3); // command, key, and value

                switch(inputWords[0])
                {
                    case HELP:
                        runLoopHelp(inputWords);
                        break;
                    case VIEW :
                        // this method is complex enough it should have its own method I think
                        runView(inputWords);
                        break;
                    case PUT :
                        // assume key is [1], value is [2]
                        controller.put(inputWords[1], inputWords[2]);
                        break;
                    case REMOVE :
                        controller.remove(inputWords[1]);
                        break;
                    case CLOSE :
                        // (try to) save the data
                        if (controller.save())
                        {
                            System.out.println(CLOSE_SUCCESSFUL_MESSAGE);
                            running = false;
                        } // end if
                        else
                        {
                            System.out.println(CLOSE_IO_ERROR_MESSAGE_START);
                        } // end else
                        break;
                    case QUIT :
                        // make sure the user wants to quit without saving
                        // maybe this could be its own method but this seems cleaner
                        System.out.print(QUIT_CHECK_PROMPT);
                        if (YES_CHECK.equals(consoleInput.readLine()))
                        {
                            running = false;
                            controller.quit();
                        } // end if
                        break;

                    default :
                        // a little inconsistent with the message string
                        System.out.println("\"" + inputText + LOOP_BAD_ARGS_MESSAGE);
                        break;
                } // end switch
            } // end while (running) 
        } // end try
        catch (IOException e)
        {
            System.out.println(LOOP_IO_ERROR_MESSAGE_START + " " + e.getMessage());
            controller.reportKVIOProblem();
        } // end catch
        finally
        {
            fileClosingHelper(consoleInput);
        } // end finally
    } // end promptDataInteraction()

    /**
    This is a helper method for the interaction_loop method. It prints the requested data entry(s)
    on the console screen. input_words[1] should be the key to look for - if it's the ANY string,
    all entries will be printed. Otherwise, just the entries matching. If no entries are found,
    the user will be told, but there should be no errors and the app will keep executing.

    @param input_words An array of strings generated by splitting the user's input
     */
    private void runView(String[] inputWords)
    {
        if (inputWords.length == 1) // if input is just "view"
        {
            Map<String, String> data = controller.getAllData();
            for (String each_key : data.keySet())
            {
                printOneEntry(each_key, controller.getValue(each_key));
            } // end for loop
        } //end if
        else 
        {
            String key = inputWords[1];
            String value = controller.getValue(key);

            if (value != null)
            {
                printOneEntry(key, value); // maybe too many helpers
            } // end if contains
            else
            {
                System.out.println(VIEW_NOT_FOUND_MESSAGE_START + key + VIEW_NOT_FOUND_MESSAGE_END);
            } // end else (if not contains)
        } // end else
    } // end run_view method

    /**
    If the user doesn't pass any arguments, give them a little message to give them a tip.
    Some apps just print their --help message when you give them no arguments, should I?
     */
    private void runNoArgs()
    {
        System.out.println(NO_ARGS_MESSAGE);
    }

    /**
    This method prints out help messages to the terminal/console, so the user will know how to use
    the app. If (the rest of) args is empty, it will print all the messages; if args[1] matches a
    specific command, only the help message for that command will be printed.

    (I don't know if parameter documentation works this way for java)

    :param args: The user's command line parameters (so we know what help message to send)
     */

    private void runHelp(String[] args)
    {
        if (args.length < 2)
        {
            printAllHelp();
            return;
        }

        switch(args[1])
        {
            case CREATE :
                System.out.println(HELP_CREATE_MESSAGE);
                break;
            case OPEN :
                System.out.println(HELP_OPEN_MESSAGE);
                break;
            default :
                printAllHelp();
                break;
        } // end switch
    } // end run_tests method

    /**
    This handles closing files, since that needs its own try/catch and it happens a lot in the code.
    This is overloaded to handle BufferedReaders and BufferedWriters
     */
    private void fileClosingHelper(BufferedReader file)
    {
        if (file == null) return;

        // this looks like a mess but I think it's the correct way to handle closing files?
            try
            {
                file.close(); //apparently this creates the file now
            }
            catch (IOException e)
            {
                System.out.println(CONSOLE_INPUT_CLOSE_ERROR);
                controller.reportKVIOProblem();
            }
    } // end file_closing_helper method

    /**
    This method is a helper for the interaction_loop, which prints the appropriate help message
    for the user. Assumes input_words[0] is "help" and [1] is the command in question.

    (probably shouldn't be called loop anymore but whatever)

    :param input_words: An array of strings generated by splitting the user's input
     */
    private void runLoopHelp(String[] input_words)
    {
        if (input_words.length < 2)
        {
            printAllLoopHelp();
            return;
        } // end if

        switch(input_words[1])
        {
            case VIEW :
                System.out.println(LOOP_HELP_VIEW_MESSAGE);
                break;
            case PUT :
                System.out.println(LOOP_HELP_PUT_MESSAGE);
                break;
            case REMOVE :
                System.out.println(LOOP_HELP_REMOVE_MESSAGE);
                break;
            case CLOSE :
                System.out.println(LOOP_HELP_CLOSE_MESSAGE);
                break;
            case QUIT :
                System.out.println(LOOP_HELP_QUIT_MESSAGE);
                break;
            default :
                printAllLoopHelp();
                break;
        } // end switch
    } // end run_loop_help method

    /**
    This method is a helper for run_loop_help - it prints all of the in-loop help messages at once.
     */
    private void printAllLoopHelp()
    {
        System.out.println(LOOP_HELP_VIEW_MESSAGE);
        System.out.println(LOOP_HELP_PUT_MESSAGE);
        System.out.println(LOOP_HELP_REMOVE_MESSAGE);
        System.out.println(LOOP_HELP_CLOSE_MESSAGE);
        System.out.println(LOOP_HELP_QUIT_MESSAGE);
    } // end print_all_loop_help method

    /**
     * Prints out a message to the user when the input doesn't make any sense.
     * 
     * @param args the user's input
     */
    private void runBadArgs(String[] args)
    {  
        String output = BAD_ARGS_MESSAGE_START + args[0] + BAD_ARGS_MESSAGE_END;
        System.out.println(output);
    }

    /**
     * A helper method that prints out a single key/value pair
     */
    private void printOneEntry(String key, String value)
    {     
        // i could use constant variables to control this printout but it seems like overkill here
        System.out.println("KEY: " + key + "   VALUE: " + value);
    } // end print_one_entry method

    /**
    A helper method for run_help - simply prints all of the (command-line) help messages.
     */
    private void printAllHelp()
    {
        System.out.println(HELP_CREATE_MESSAGE);
        System.out.println(HELP_OPEN_MESSAGE);
    } // end print_all_help method
} // end CommandLineKVIO