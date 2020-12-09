/**
 * Project 0 - Andrew Curry
 *
 * This project contains code for a simple key-value data storage system. Using a command-line interface, the user can
 * create a 'database', view the contents of a database, add & remove elements from that database.
 * 
 * This file contains another class that implements the KVDAO interface, this time using a postgre database hosted on my
 * PC in a Docker container to store the data.
 * 
 */
package com.revature;

import java.io.IOException;
import java.sql.*;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class PostgresKVDAO implements KVDAO {
    // instance variables ------
    private String dbAddress;
    private String dbUsername;
    private String dbPassword;
    private String tableName;

    private boolean nameSet;

    private Logger logger;

    /**
     * The constructor. Currently sets the server information to one pre-set. You
     * will still need to call setName to indicate which table in the database you
     * want to access.
     */
    public PostgresKVDAO() 
    {
        dbAddress = "jdbc:postgresql://localhost:5432/kvdb";
        dbUsername = "kvdb_user";
        dbPassword = "password"; // # security

        logger = java.util.logging.Logger.getLogger(this.getClass().getName());
        logger.setUseParentHandlers(false); // so it wont write to console
        try
        {
            FileHandler fh = new FileHandler("status.log");
            logger.addHandler(fh);
        }
        catch(IOException e)
        {
            //just do nothing
        }

        nameSet = false;

        logger.info("PostgresKVDAO initialized.");
    } // end constructor

    // methods from KVDAO
    /**
     * This method should be called before any of the other DataIO methods. It sets
     * the name of the file/table/database that this DataIO object will be
     * interacting with. Returns true if the name is set, false if i is not (EG if
     * there was already a name set)
     * 
     * 
     * @param name: the name for the table to be accessed/manipulated by this DAO
     */
    @Override
    public boolean setName(String name) 
    {
        
        if (nameSet)
        {   
            logger.info("Failed to set PostgresKVDAO name to " + name);
            return false;
        } 
        tableName = name;
        logger.info("Set PostgresKVDAO name to " + name);
        return true;
    } // end setName

    /**
     * Create a new file, or table, or whatever to store data in. Returns a boolean
     * reflecting whether or not the creation was successful.
     */
    @Override
    public boolean create() 
    {
        logger.info("PostgresKVDAO creating table...");

        Connection connection = getConnection();
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        boolean returnMe = true;
        
        if (connection == null) {
            // logger.info("PostgresKVDAO connection was null.");
            return false;
        }
        // so i WAS sanitizing the input but it produced a weird syntax error...I'll fix it later if I have time
        // String sql1 = "drop table if exists ?";
        // String sql2 = "create table ? (kvds_key varchar(255) PRIMARY KEY, kvds_value varchar(255))";
        String sql1 = "drop table if exists " + tableName;
        String sql2 = "create table " + tableName + "(kvdb_key varchar(255) PRIMARY KEY, kvdb_value varchar(255))";
        try {
            statement = connection.prepareStatement(sql1);
            // statement.setString(1, tableName);
            statement.executeUpdate(); // do i need the result set? is there one?
            statement2 = connection.prepareStatement(sql2);
            // statement2.setString(1, tableName);
            statement2.executeUpdate();
        } // end try
        catch (SQLException e) {
            e.printStackTrace(); // TODO remove later
            logger.info("PostgresKVDAO encountered a sql exception.");
            returnMe =  false;
        } finally {
            closeConnectionHelper(connection);
            closeStatementHelper(statement);
            closeStatementHelper(statement2);
        }
        return returnMe;
    } // end create()

    /**
     * Opens a pre-existing file/table and loads the data. Returns the data if
     * successful, null if not.
     */
    @Override
    public Map<String, String> open()
    {
        logger.info("PostgresKVDAO opening table...");
        Connection connection = getConnection();
        ResultSet rs = null;
        // so i WAS sanitizing the input but it produced a weird syntax error...I'll fix it later if I have time
        // String sql = "select * from ?";
        String sql = "select * from " + tableName;
        PreparedStatement statement = null;

        if (connection == null)
        {
            // logger.info("PostgresKVDAO connection was null.");
            return null;
        }

        try
        {
            statement = connection.prepareStatement(sql);
            // statement.setString(1, tableName);
            rs = statement.executeQuery();
        }
        catch(SQLException e)
        {
            logger.info("PostgresKVDAO encountered a sql exception.");
        }

        Map<String, String> data = new Hashtable<String, String>();

        try
        {
            if (rs != null) 
            {
                String key;
                String value;
                while (rs.next()) {
                    key = rs.getString("kvdb_key");
                    value = rs.getString("kvdb_value");
                    data.put(key, value);
                } // end while loop
            } // end if rs is not null
            else
            {
                return null;
            }
        }
        catch (SQLException e) 
        {
            logger.info("PostgresKVDAO encountered a sql exception.");
            e.printStackTrace();
            return null;
        }
        finally
        {
            closeConnectionHelper(connection);
            closeStatementHelper(statement);
        }

        return data;
    } // end open()

    /**
     * Takes the given data and saves it into the permanent data format. Will delete
     * all entries on the table and the save the data from the given Map. Returns
     * true if the data was saved successfully, false otheriwse.
     * 
     * @Param data: a map of the data to be saved
     */
    @Override
    public boolean save(Map<String, String> data)
    {
        logger.info("PostgresKVDAO saving data to table...");
        // first we delete all the rows from the table 
        // (otherwise entries deleted by the KVMEM won't be removed accordingly)
        Connection cnc = getConnection();
        String sql = "delete from " + tableName;
        PreparedStatement stm = null;

        try
        {
            stm = cnc.prepareStatement(sql);
            stm.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.info("PostgresKVDAO encountered a sql exception.");
            return false;
        }
        finally
        {
            closeConnectionHelper(cnc);
            closeStatementHelper(stm);
        }

        boolean keep_going = true;
        for (String key : data.keySet())
        {
            keep_going = saveOneEntry(key, data.get(key));
            if (!keep_going)
            {
                return false;
            }
        } // end for loop

        return true;
    } // end save()

    /**
     * This is a helper method for save that writes a single entry to the table. Returns true if the entry
     * was added successfully - false otherwise
     * 
     * @Param key
     * @Param value
     */
    private boolean saveOneEntry(String key, String value)
    {
        Connection connection = getConnection();
        PreparedStatement stm = null;

        try
        {
            /**
            String sql = "insert into ? (kvdb_key, kvdb_value) values (?, ?) on conflict (kvdb_key) do update set kvdb_value = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, tableName);
            statement.setString(2, key);
            statement.setString(3, value);
            statement.setString(4, value);
            statement.executeUpdate();
            */
            String sql = "insert into " + tableName + " values (\'" + key + "\', \'" + value + "\') on conflict (kvdb_key) do update set kvdb_value = \'" + value + "\'";
            // System.out.println("sql is " + sql);
            stm = connection.prepareStatement(sql);
            stm.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.info("PostgresKVDAO encountered a sql exception.");
            return false;
        }
        finally
        {
            closeConnectionHelper(connection);
            closeStatementHelper(stm);
        }
        return true;
    } // end saveOneEntry

    /**
     * A helper method that returns a new Connection object that is connected to the
     * server. Returns null if the connection failed.
     */
    private Connection getConnection() {
        try 
        {
            Class.forName("org.postgresql.Driver");
        } 
        catch (ClassNotFoundException e) 
        {
            logger.info("PostgresKVDAO encountered a problem getting the driver.");
            e.printStackTrace();
        }

        Connection connection = null;
        try 
        {
            connection = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } // end try
        catch (SQLException e) 
        {
            logger.info("PostgresKVDAO encountered a sql exception getting a Connection.");
            e.printStackTrace(); // TODO clean this up later
        } // end catch
        return connection;
    } // end getConnection()

    /**
     * A helper method that handles closing a connection object.
     * 
     * @Param connection: the connection object
     */
    private void closeConnectionHelper(Connection connection)
    {
        try
        {
            if (connection!=null)
            {
                connection.close();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info("PostgresKVDAO encountered a sql exception.");
        }
    } // end closeConnectionHelper

    private void closeStatementHelper(PreparedStatement stm)
    {
        try
        {
            if (stm!=null)
            {
                stm.close();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info("PostgresKVDAO encountered a sql exception.");
        }
    } // end closeStatementHelpe
} // end PostgresKVDAO class