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
 */
public abstract class AbstractDao<T extends Entity> {
    protected Class<T> tClass; // The class of T
    protected  String tablePrefix; // The table prefix for column names, like "" or "ORDERS."

    /**
     * Search the set for an alement
     * Return the old element (duplicate ) equal to the new one if found
     * Add the new element and return it if not
     *
     * @param set The set to add to
     * @param item The item to seek and add
     * @param <U> The element type
     * @return The old or the new element
     */
    private static <U> U addToSet(Set<U> set, U item){
        // Try to find an element equal to item in the set
        for (U elem : set) {
            if (elem.equals(item)) {
                // Return existing element elem (NOT the new one item !) if found
                return elem;
            }
        }

        // Not found: add the new element to the set and return it
        set.add(item);
        return item;
    }


    /**
     * Constructor
     * @param tClass
     * @param tablePrefix
     */
    public AbstractDao(Class<T> tClass, String tablePrefix) {
        this.tClass = tClass;
        this.tablePrefix = tablePrefix;
    }


    /**
     * Get all elements from a database table as a set
     * @param linkedDao The Dao object linked with this Dao
     * @return  set of all elements
     */
    public Set<T> getAll(AbstractDao<?> linkedDao) {
        Set<T> set = new HashSet<>(); // The set of type-T beans
        Set<Entity> linkSet=new HashSet<>(); // The set of linked beans
        String sqlQuery = getSqlQuery(linkedDao);

        try (Connection connection= DSource.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            // Process each line of the SQL results
            while (resultSet.next()) {
                // Create a new bean with proper fields only (not links)
                T bean = convertResult(resultSet);

                // Check if the line already exists in the set (or cache), use equals by ids
                // Add to set if a new bean
                // The whole construct is needed if we are at the "many" side of the link, e.g.
                // SELECT * FROM CUSTOMERS JOIN ORDERS ON CUSTOMERS.CUST_NUM=ORDERS.CUST;
                // We do not want to create repeated beans, we want to add new orders
                // to the orders (Set) field of the existing Customer bean
                // The logic must be implemented in convertLinks()
                bean=addToSet(set,bean);

                // Add links, if any to either the new or the old bean
                // from the current resultSet line
                convertLinks(bean, resultSet, linkSet, linkedDao);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            // Do nothing else, we return the set as it is, propabbly empty
        }

        return set;

    }

    /**
     * Create a join string e.g. "JOIN ORDERS ON CUST_NUM=CUST"
     * when called in the Order DAO from the Customer DAO
     * @param fromClass the class of the beans of calling DAO (i.e. Customer.class in our example)
     * @return The join string
     */
    public String getJoinString(Class fromClass) {
        return "";
    };

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
     * @param bean  The bean to work with
     * @param resultSet  SQL results set (current line)
     * @param linkSet The set of linked beans
     * @param linkedDao The Dao for the link type object
     */
    protected abstract void convertLinks(T bean, ResultSet resultSet, Set<Entity> linkSet, AbstractDao<?> linkedDao);



    /**
     * Get SQL query for getAll
     * e.g. "SELECT * FROM CUSTOMERS JOIN ORDERS ON CUST_NUM=CUST;"
     *
     * @return SQL query
     * @param linkedDao The Dao for the link type object (e.g. OrderDao)
     */
    protected abstract String getSqlQuery(AbstractDao<?> linkedDao);

}
