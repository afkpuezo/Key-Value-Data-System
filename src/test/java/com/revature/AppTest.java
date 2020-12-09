package com.revature;

import java.util.Hashtable;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Tests that we can create a simpleKVMem object, pass it a pre-existing Map of data,
     * and then have the Mem return an equivalent Map.
     */
    public void testSimpleKVMemGetAllData()
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        original.put("key3", "value3");

        KVMem mem = new SimpleKVMem();
        mem.setData(original);
        Map<String, String> testData = mem.getAllData();

        boolean result = original.equals(testData); // should be equal

        assertTrue(result);
    }

    /**
     * Tests that we can create a simpleKVMem object, pass it a pre-existing Map of
     * data, and then have the Mem return an equivalent Map. This one makes sure that
     * the copying process is correct, and changes made to the original parameter-Map
     * are not reflected inside the Mem object.
     */
    public void testSimpleKVMemGetAllData2()
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        original.put("key3", "value3");

        KVMem mem = new SimpleKVMem();
        mem.setData(original);
        original.put("key4", "value4");

        Map<String, String> testData = mem.getAllData();

        boolean result = !original.equals(testData); // should NOT be equal

        assertTrue(result);
    }

    /**
     * Tests that elements can be correctly added to the KVMem
     */
    public void testSimpleKVMemPut()
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        original.put("key3", "value3");

        KVMem mem = new SimpleKVMem();
        mem.setData(original);
        original.put("key4", "value4");
        mem.put("key4", "value4");

        Map<String, String> testData = mem.getAllData();

        boolean result = original.equals(testData); // should be equal

        assertTrue(result);
    }
    
    /**
     * Tests that elements can be correctly modified/overwritten to the KVMem
     */
    public void testSimpleKVMemPut2() 
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        original.put("key3", "value3");

        KVMem mem = new SimpleKVMem();
        mem.setData(original);
        original.put("key1", "value4");
        mem.put("key1", "value4");

        Map<String, String> memData = mem.getAllData();

        boolean result = original.equals(memData); // should be equal

        assertTrue(result);
    }

    /**
     * Tests that individual elements can be correctly retreived from the KVMem
     */
    public void testSimpleKVMemGetValue()
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        original.put("key3", "value3");

        KVMem mem = new SimpleKVMem();
        mem.setData(original);

        boolean result = (original.get("key1") == mem.getValue("key1")); // should be equal

        assertTrue(result);
    }

    /**
     * Tests that the KVMem behaves properly when asked to retrieve an element that does not exist
     */
    public void testSimpleKVMemGetValue2() 
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        original.put("key3", "value3");

        KVMem mem = new SimpleKVMem();
        mem.setData(original);

        boolean result = (null == mem.getValue("key4")); // should be equal

        assertTrue(result);
    }

    /**
     * Tests that the KVMem properly removes elements from the data
     */
    public void testSimpleKVMemRemove()
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        original.put("key3", "value3");

        KVMem mem = new SimpleKVMem();
        mem.setData(original);
        original.remove("key2");
        mem.remove("key2");
        Map<String, String> memData = mem.getAllData();

        boolean result = original.equals(memData); // should be equal

        assertTrue(result);
    }
    
    /**
     * Tests that the KVMem behaves properly when asked to remove a non-existant entry -
     * that is, return null
     */
    public void testSimpleKVMemRemove2() 
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        original.put("key3", "value3");

        KVMem mem = new SimpleKVMem();
        mem.setData(original);
        boolean was_removed = mem.remove("key4");

        boolean result = !was_removed;

        assertTrue(result);
    }

    /**
     * Tests that the PostgresKVDAO class can open a pre-existing table properly.
     */
    public void testPostgresKVDAOOpen()
    {
        PostgresKVDAO dao = new PostgresKVDAO();
        dao.setName("demo_table");
        Map<String, String> data = dao.open();

        // hardcoded, but it should work
        Map<String, String> original = new Hashtable<String, String>();
        original.put("demo_key1", "demo_val3");
        original.put("demo_key2", "demo_val2");

        boolean result = original.equals(data);

        assertTrue(result);
    }

    /**
     * Tests that the PostgresKVDAO class behaves properly when trying to open a table that does not exist.
     */
    public void testPostgresKVDAOOpen2() {
        PostgresKVDAO dao = new PostgresKVDAO();
        dao.setName("not_a_real_table");
        Map<String, String> data = dao.open();

        boolean result = (data == null);

        assertTrue(result);
    }

    /**
     * Tests that the PostgresKVDAO can create a new table.
     * 
     * Technically it also trusts that it can open() as well, but we've already tested that by this point.
     */
    public void testPostgresKVDAOCreate()
    {
        PostgresKVDAO dao1 = new PostgresKVDAO();
        dao1.setName("unit_test_table");
        boolean result = dao1.create(); // could fail here
        if (result)
        {
            PostgresKVDAO dao2 = new PostgresKVDAO();
            dao2.setName("unit_test_table");
            Map<String, String> data = dao2.open();
           if (data == null)
           {
               result = false; // could fail here, indicating a problem with create despite it returning true
           }
           else
           {
                // a newly created table should be empty
                Map<String, String> original = new Hashtable<String, String>();
                result = data.equals(original);
           }
        }
        
        assertTrue(result);
    }

    /**
     * Tests that the PostgresKVDAO can save data into a table.
     * 
     * Technically it also trusts that it can open() as well, but we've already tested that by this point.
     */
    public void testPostgresKVDAOSave()
    {
        Map<String, String> original = new Hashtable<String, String>();
        original.put("unit_key1", "unit_value1");
        original.put("unit_key2", "unit_value2");
        original.put("unit_key2", "unit_value3");
        
        PostgresKVDAO dao1 = new PostgresKVDAO();
        dao1.setName("unit_test_table");
        dao1.save(original);

        // a completely new dao should have no way to cheat and get the same data
        PostgresKVDAO dao2 = new PostgresKVDAO(); 
        dao2.setName("unit_test_table");
        Map<String, String> data = dao2.open();
        
        boolean result = original.equals(data);

        assertTrue(result);
    }
}
