package agrechnev.orm;

import agrechnev.dao.AbstractDao;
import agrechnev.linktable.EntityLink;
import agrechnev.linktable.EntityLinkTable;
import agrechnev.models.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Oleksiy Grechnyev on 10/24/2016.
 * Dao implementation using reflections
 * Both master and slave, universal for any class T
 */

public class ORMDao<T extends Entity> extends AbstractDao<T> {

    // Entity link table to use for this DAO
    private EntityLinkTable linkTable;

    // Private fields corresponding to class T, the bean's class
    // Basically cached here for efficiency, created once in the constructor

    private String tClassName; // The full class name
    private Set<Field> properFields; // The proper (i.e. non-link fields)
    private Set<EntityLink> linkSet; // All links of this class

    // Name of the table and the table alias
    String tableName; // Table full name
    String tableAlias; // The table alias, "" if none

    // The field to setter map
    Map<Field, Method> setters;

    // The field to column name map
    Map<Field, String> columnNames;


    /**
     * Constructor : initialize ORM DAO
     *
     * @param tClass     The bean class, e.g. Customer.class
     * @param dbPrefix   Period-ended database prefix, e.g. "mydb.", usually "" for no prefix
     * @param tableAlias Alias name for the table e.g. "mytable", or "" for no alias
     * @param linkTable  The LinkTable with all links
     * @throws DaoException If anything goes wrong
     */
    public ORMDao(Class<T> tClass, String dbPrefix, String tableAlias, EntityLinkTable linkTable) throws DaoException {
        super(tClass, "");

        if (tableAlias == null) {
            this.tableAlias = "";
        } else {
            this.tableAlias = tableAlias.trim(); // Table alias
        }

        this.linkTable = linkTable; // Link table to use
        if (linkTable == null || tClass == null || dbPrefix == null) {
            throw new DaoException("Null field in ORMDao() constructor");
        }


        // Set up the parameters for class T
        tClassName = tClass.getName();
        // Proper fields = all declared fields of the class - all link fields
        properFields = new HashSet<>(Arrays.asList(tClass.getDeclaredFields()));
        properFields.removeAll(linkTable.getFieldTable());
        // All links of this class
        linkSet = linkTable.getClassLinks(tClassName);

        // The table full name with dbPrefix
        tableName = dbPrefix + tClass.getSimpleName().toUpperCase() + "S"; // Table name from class name

        // The table prefix
        if (this.tableAlias.equals("")) {
            // No alias: use table name
            tablePrefix = tableName + ".";
        } else {
            // Use alias as the table prefix
            tablePrefix = tableAlias + ".";
        }

        // Set up setter and column name maps for the entity class T
        setters = new HashMap<>();
        columnNames = new HashMap<>();
        for (Field field : properFields) {
            setters.put(field, findGetSetter(field, "set"));
            // Column names are in the uppercase and include prefix
            columnNames.put(field, tablePrefix + field.getName().toUpperCase());
        }
    }


    /**
     * Find a setter or getter for a given field, e.g. setMyField for myField (ignores case)
     *
     * @param field  The field
     * @param prefix "get" or "set"
     * @return The setter or getter
     * @throws DaoException If not found
     */
    private static Method findGetSetter(Field field, String prefix) throws DaoException {

        String name = prefix + field.getName(); // The desired method name, ignoring case

        // Iterate over all declared methods of the entity class declaring the field
        for (Method method :
                field.getDeclaringClass().getDeclaredMethods()) {

            // Check if the method name fits, ignoring case
            // No parameter type checks, use with caution !
            if (method.getName().equalsIgnoreCase(name)) {
                return method; // Found the correct setter
            }
        }

        // Not found
        throw new DaoException("Setter for the field " + field.getName() + " not found");
    }

    /**
     * Convert a line of SQL results into a bean of class T
     * as proper fields only, links are treated separately in convertLinks
     * This version uses reflections
     *
     * @param resultSet SQL results set (current line)
     * @return A generated bean
     */
    @Override
    public T convertResult(ResultSet resultSet) {
        try {
            // Create a new bean with the default constructor
            T newBean = tClass.newInstance();

            // Iterate over all proper fields
            for (Field field :
                    properFields) {
                readField(newBean, field, resultSet);
            }

            return newBean;

        } catch (Exception e) {
            // Return null on any exception
            // This can be part of the normal LEFT JOIN logic
            return null;
        }
    }

    /**
     * Read the field of a type T bean using reflections
     * from the current line of resultSet
     * <p>
     * For now: only int, String, BigDecimal and LocalDate supporter
     * It's trivial to add more types if needed
     *
     * @param newBean   The bean
     * @param field     The field
     * @param resultSet The SQL result set
     */
    private void readField(T newBean, Field field, ResultSet resultSet) throws SQLException, InvocationTargetException, IllegalAccessException, DaoException {

        String colName = columnNames.get(field); // Column name
        Class fieldType = field.getType(); // Field type
        Method setter = setters.get(field); // Setter method

        if (fieldType == int.class) {
            // Read and int field
            int value = resultSet.getInt(colName);
            // Invoke the setter
            setter.invoke(newBean, value);
        } else if (fieldType == String.class) {
            // Read and int field
            String value = resultSet.getString(colName);
            // Invoke the setter
            setter.invoke(newBean, value);
        } else if (fieldType == LocalDate.class) {
            // Read and int field
            LocalDate value = resultSet.getDate(colName).toLocalDate();
            // Invoke the setter
            setter.invoke(newBean, value);
        } else if (fieldType == BigDecimal.class) {
            // Read and int field
            BigDecimal value = resultSet.getBigDecimal(colName);
            // Invoke the setter
            setter.invoke(newBean, value);
        } else {
            throw new DaoException("redField: wrong type of field " + field.getName());
        }

    }

    /**
     * Add any links to the bean, if any
     * Creates new beans of the linked class
     *
     * @param bean      The bean to work with
     * @param resultSet SQL results set (current line)
     * @param slaveSet  The set of linked beans
     * @param slaveDao  The Dao for the link type object (slave)
     */
    @Override
    protected void convertLinks(T bean, ResultSet resultSet, Set<? extends Entity> slaveSet, AbstractDao<?> slaveDao) throws DaoException {
        // Find the relevant link for the given slaveDao
        EntityLink link = null;
        Class slaveClass = slaveDao.getTClass();

        for (EntityLink l : linkSet) {
            if ((l.getClass_S().equals(slaveClass.getName()) && l.getClass_L().equals(tClassName)) ||
                    (l.getClass_L().equals(slaveClass.getName()) && l.getClass_S().equals(tClassName))) {
                link = l;
                break;
            }
        }

        if (link == null) return; // No links==nothing to do

        // Create a slave bean from the results set
        Entity slaveBean = (Entity) slaveDao.convertResult(resultSet);

        if (slaveBean == null) {
            return; // It's normal, nothing to do
        }

        // Add it to set or find an equal one if exists
        slaveBean = (Entity) addToSet(slaveSet, slaveBean);

        try {

            // Join the S and L beans
            // We use only ONE in case of  a self-link, this is intended
            if (link.getClass_S().equals(slaveClass.getName())) {
                // S = slave, L= master
                joinBeans(link, slaveBean, bean);
            } else if (link.getClass_L().equals(slaveClass.getName())) {
                // L = slave, S= master
                joinBeans(link, bean, slaveBean);
            }


        } catch (Exception e) {
            throw new DaoException("Exception in ORMDao.convertlinks ", e);
        }
    }

    /**
     * Join to beans with the one-to many link using reflections
     *
     * @param link  The EntityLink object
     * @param beanS The bean with the Set<> field
     * @param beanL The bean with the link field
     * @throws DaoException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static void joinBeans(EntityLink link, Entity beanS, Entity beanL) throws DaoException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        // Now add the link to beanL
        Method methodL = findGetSetter(beanL.getClass().getDeclaredField(link.getField_L()), "set"); // Setter
        methodL.invoke(beanL, beanS); // Link beanL to beanS : simple link

        // Now add the link to beanS
        Method methodS = findGetSetter(beanS.getClass().getDeclaredField(link.getField_S()), "get"); // Getter
        // Raw types are hard to avoid when using reflections
        Set beanSet = (Set) methodS.invoke(beanS); // Get the set with getter
        beanSet.add(beanL); // Link beanS to beanL via Set
    }

    /**
     * Create a join string e.g. " LEFT JOIN ORDERS ON CUST_NUM=CUST"
     * when called in the Order DAO (slave) from the Customer DAO (master)
     *
     * @param masterClass       the class of the beans of calling DAO (i.e. Customer.class in our example)
     * @param masterTablePrefix The table prefix of the Master DAO calling this slave routine
     * @return The join string
     */
    @Override
    public String getJoinString(Class masterClass, String masterTablePrefix) {
        // Start the JOIN SQL clause
        StringBuilder builder = new StringBuilder(" LEFT JOIN ");
        // Add slave table name and optionally alias
        builder.append(tableName).append(" ").append(tableAlias);

        // Find the link corresponding to masterClass
        EntityLink link = null;
        for (EntityLink l : linkSet) {
            if ((l.getClass_S().equals(masterClass.getName()) && (l.getClass_L().equals(tClassName))) ||
                    (l.getClass_L().equals(masterClass.getName()) && (l.getClass_S().equals(tClassName)))) {
                link = l;
                break;
            }
        }

        if (link != null) {
            // Found a link: process it

            // Iterate over all joined columns of the link
            for (int i = 0; i < link.getColumns_S().size(); i++) {
                // Add  ON or AND
                builder.append(i == 0 ? " ON " : " AND ");

                // Master and lave column names
                String masterCol, slaveCol;

                // Find master ans lave column names
                // Note that self-link (masterClass==tClass) would be processed only once
                // This is the intended behavior
                // The order is important for the self-link because of left join
                if (link.getClass_L().equals(masterClass.getName())) {
                    // L==master, S==slave
                    masterCol = link.getColumns_L().get(i);
                    slaveCol = link.getColumns_S().get(i);
                } else {
                    // S==master, L==slave
                    masterCol = link.getColumns_S().get(i);
                    slaveCol = link.getColumns_L().get(i);
                }

                // The join equality
                builder.append(masterTablePrefix).append(masterCol);
                builder.append(" = ");
                builder.append(tablePrefix).append(slaveCol);
            }


        }

        return builder.toString();
    }

    /**
     * Get SQL query for getAll
     * e.g. "SELECT * FROM CUSTOMERS JOIN ORDERS ON CUST_NUM=CUST;"
     *
     * @param linkedDao The Dao for the link type object (e.g. OrderDao)
     * @return SQL query
     */
    @Override
    protected String getSqlQuery(AbstractDao<?> linkedDao) {
        String joiner = ""; // The join string

        if (linkedDao != null) {
            // Get the join string
            joiner = " " + linkedDao.getJoinString(tClass, tablePrefix);
        }

        // An SQL query with an optional alias and an optional joiner
        return "SELECT * FROM " + tableName + " " + tableAlias + joiner + ";";
    }

}
