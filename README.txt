JAVA EE homework assignment #3 by Oleksiy Grechnyev

A toy DAO for the SQL textbook database which demonstrates the ORM problem. Only getAll() is implemenmted.

Includes both hand-written DAOs (CustomerDao, etc.) and the universal reflection-based DAO (ORMDao).

You must edit the configuration file scripts/connection.xml, at the very least you need SQL username & password. The init scripts (run if runinit=true) create the database, they are for MySQL/MariaDB only and require mysql executable in your path, versions for both Windows and Linux/Unix are included. To run tests, execute "mvn test" in the project directory, or run ORMTest class from your IDE. You can also run the class agrechnev.main.Main to print the output of a few queries to stdout.

Idea: I created simple Dao concept (AbstractDao), with getAll() only. It can work as both master and slave DAO, to create request of the type

SELECT * FROM master LEFT JOIN slave ON <link conditions>;

Multiple joins are not implemented.

I hand-coded DAOs for Customer (master only) and Order, Salesrep (slave only) as an example.

I also created a reflection-based Dao ORMDao which works for any entity class. It uses links table from an EntityLinkTable object, which is read from scripts/link.xml. Some notation on the one-to-many links:

S (from "set") = Parent entity with a "Set<ClassL> children;" field; table has the id COLUMN_S
L (from "link") = A child entity with an "ClassS parent;" field; table has a foreign key COLUMN_L

Currently the cases of two different links between a pair of table (e.g. Office<->Salesrep) are not fully supported, DAO only processes one link.

Only a few field types are currently supported in OrmDAO, new one can be added easily.

Other notes on this project:

I've used two different XML parsers as an exercise.

I've used reflections to create a universal toString() for Entity descendants, with toShortString() used to print collections to avoid infinite toString() recursion (or at least, to make it less likely).

My unit tests validate the toString() output of sorted collections, not a very beautiful solution, but it works.