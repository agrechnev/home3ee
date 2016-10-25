package agrechnev.dao;

import agrechnev.dsource.DSource;
import agrechnev.models.Entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/21/2016.
 * An abstract DAO for the entity class
 * DAO might create entities for a linked objects if specified
 * Reads all objects only
 * Uses DSource as the SQL connection source
 * <p>
 * Idea: Dao can process links actively (master) by method convertLinks()
 * and passively (slave) by providing methods getJoinString() and convertResults()
 * This simple implementation can process inly 1 link and 2 entity classes:
 * Dao which we run getAll() on =master
 * and one linked Dao=slave
 * The link can be 1 to many in any direction
 * This ogic emulates SELECT with one LEFT JOIN
 */

public abstract class AbstractDao<T extends Entity> {
    protected Class<T> tClass; // The class of T
    protected String tablePrefix; // The table prefix for column names, like "" or "ORDERS."

    public static class DaoException extends Exception {
        public DaoException(String message) {
            super(message);
        }

        public DaoException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Search the set for an alement
     * Return the old element (duplicate ) equal to the new one if found
     * Add the new element and return it if not
     *
     * @param set  The set to add to
     * @param item The item to seek and add
     * @param <U>  The set element type
     * @param <U>  The item type
     * @return The old or the new element
     */
    protected static <U, V> U addToSet(Set<U> set, V item) {
        if (!set.contains(item)) {
            // Not in set: add the new element to the set and return it
            set.add((U) item);
            return (U) item;
        }

        // Find an element equal to item in the set
        for (U elem : set) {
            if (elem.equals(item)) {
                // Return existing element elem (NOT the new one item !) if found
                return elem;
            }
        }

        throw new Error("Internal error in AbstractDao.addToSet()");
    }


    /**
     * Constructor
     *
     * @param tClass
     * @param tablePrefix
     */
    protected AbstractDao(Class<T> tClass, String tablePrefix) {
        this.tClass = tClass;
        this.tablePrefix = tablePrefix;

        if (tablePrefix == null) {
            tablePrefix = "";
        }
    }


    public Class<T> getTClass() {
        return tClass;
    }

    /**
     * Get all elements from a database table as a set
     *
     * @param slaveDao The Dao object linked with this Dao
     * @return set of all elements
     */
    public Set<T> getAll(AbstractDao<?> slaveDao) throws DaoException {
        Set<T> set = new HashSet<>(); // The set of type-T beans

        // The set of slave beans
        // Note: linkSet is not returned and not kept
        // but we use it internally to avoid duplicates in links
        Set<? extends Entity> slaveSet;

        // We use existing set if the master and slave classes coincide
        // This means not creating the second copy of beans of the same class for self-reference
        // Otherwise we create a new one for a different entity class
        if (tClass == slaveDao.getTClass()) {
            slaveSet = set;
        } else {
            slaveSet = new HashSet<Entity>();
        }

        String sqlQuery = getSqlQuery(slaveDao);

        // Print the query
        System.out.println(sqlQuery);

        try (Connection connection = DSource.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            // Process each line of the SQL results
            while (resultSet.next()) {
                // Create a new bean with proper fields only (not links)
                T bean = convertResult(resultSet);

                // Check if the line already exists in the set (or cache), use equals by ids
                // Add to set if a new bean
                // The whole construct is needed if we are at the "many" side of the link, e.g.
                // SELECT * FROM CUSTOMERS LEFT JOIN ORDERS ON CUSTOMERS.CUST_NUM=ORDERS.CUST;
                // We do not want to create repeated beans, we want to add new orders
                // to the orders (Set) field of the existing Customer bean
                // The logic must be implemented in convertLinks()
                bean = addToSet(set, bean);

                // Add links, if any to either the new or the old bean
                // from the current resultSet line
                convertLinks(bean, resultSet, slaveSet, slaveDao);
            }


        } catch (SQLException | DSource.DSourceException e) {
            // Re-throw as DaoException
            throw new DaoException("Exception in AbstractDao.getAll", e);
        }

        return set;

    }

    /**
     * Create a join string e.g. " LEFT JOIN ORDERS ON CUST_NUM=CUST"
     * when called in the Order DAO (slave) from the Customer DAO (master)
     *
     * @param masterClass       the class of the beans of calling DAO (i.e. Customer.class in our example)
     * @param masterTablePrefix The table prefix of the Master DAO calling this slave routine
     * @return The join string
     */
    public String getJoinString(Class masterClass, String masterTablePrefix) {
        return "";
    }


    /**
     * Convert a line of SQL results into a bean of class T
     * ass proper fields only, links are treated separately in convertLinks
     *
     * @param resultSet SQL results set (current line)
     * @return A generated bean
     */
    public abstract T convertResult(ResultSet resultSet);

    /**
     * Add any links to the bean, if any
     * Creates new beans of the linked class
     *
     * @param bean      The bean to work with
     * @param resultSet SQL results set (current line)
     * @param slaveSet  The set of linked beans
     * @param slaveDao  The Dao for the link type object
     */
    protected abstract void convertLinks(T bean, ResultSet resultSet, Set<? extends Entity> slaveSet, AbstractDao<?> slaveDao) throws DaoException;


    /**
     * Get SQL query for getAll
     * e.g. "SELECT * FROM CUSTOMERS JOIN ORDERS ON CUST_NUM=CUST;"
     *
     * @param linkedDao The Dao for the link type object (e.g. OrderDao)
     * @return SQL query
     */
    protected abstract String getSqlQuery(AbstractDao<?> linkedDao);

}
