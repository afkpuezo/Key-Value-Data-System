This is a (relatively) simple key-value data system, which I have creatively called KVDS.

Features:
    -Create a new database table
    -Open a prexisting database table
    -View, put, and remove data entries (key-value pairs)
    -Save data back to the database table (or quit without saving changes)
    
Technologies:
    - Java
    - Maven
    - Junit
    - JDBC
    - PostgreSQL

Instructions to Use:
From the command line, type "java KVDS"...
    (from the maven project root, type "mvn exec:java")
    (to store/retrieve data in a plain-text file, use the command-line argument "file" eg "java KVDS -file")

...then the program will prompt you for input with a >. You can:
    "create <name>" to create a new table, named <name> (eg "create demo_table"). If a table with that name
        already exists, it will be dropped and re-created.
    "open <name>" to open a pre-existing table, named <name> (eg "open demo_table"). 

If either create or save were completed successfully, you will be brought to the interaction loop.
Prompted by another >, you can:
    "view" to view all data.
        -If there are currently no entries, nothing will happen.
    "view <key>" to view only the entry associated with <key> (eg "view key1")
        -If there is no existing entry with <key>, nothing will happen.
    "put <key> <value> to add an entry with key <key> and value <value>. (eg, "put key1 value1")
        -If there is an existing entry with <key>, the value will be overwritten.
        -Changes to the data will not be comitted to the database table until you save.
    "rmv <key>" to remove the entry associated with <key> (eg "rmv key1")
        -If there is no existing entry with <key>, nothing will happen.
        -Changes to the data will not be comitted to the database table until you save.
    "save" to commit the data to the database table. If successful, execution will end.
    "quit" to end execution without saving data to the table.
        -The table will be left in its initial state before the interaction loop began.
        -There will be a confirmation dialog.
