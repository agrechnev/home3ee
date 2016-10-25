package agrechnev.dao;

import agrechnev.models.Customer;
import agrechnev.models.Entity;
import agrechnev.models.Order;
import agrechnev.models.Salesrep;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/22/2016.
 * Hand-written Dao for Customer
 * This can be used as master Dao
 */
public class CustomerDao extends AbstractDao<Customer> {

    public CustomerDao() {
        super(Customer.class, "");
    }


    /**
     * Convert a line of SQL results into a bean of class Customer
     * ass proper fields only, links are treated separately in convertLinks
     *
     * @param resultSet SQL results set (current line)
     * @return A generated bean
     */
    @Override
    public Customer convertResult(ResultSet resultSet) {
        Customer customer = new Customer(); // Create a new Customer bean

        try {
            customer.setCust_num(resultSet.getInt(tablePrefix + "CUST_NUM"));
            customer.setCompany(resultSet.getString(tablePrefix + "COMPANY"));
            customer.setCredit_limit(resultSet.getBigDecimal(tablePrefix + "CREDIT_LIMIT"));
        } catch (SQLException | NullPointerException e) {
            // Return null bean silently
            // This is not an error when working with left joins
            return null;
        }

        return customer;
    }

    /**
     * Add any links to the bean, if any
     * Creates new beans of the linked class
     *
     * @param bean      The bean to work with
     * @param resultSet SQL results set (current line)
     * @param slaveSet   The set of linked beans
     * @param slaveDao The Dao for the link type object
     */
    @Override
    protected void convertLinks(Customer bean, ResultSet resultSet, Set<? extends Entity> slaveSet, AbstractDao<?> slaveDao) {
        // If linked to Order
        if (slaveDao != null && slaveDao.getTClass() == Order.class) {
            // Get the slave Order bean
            Order orderBean = (Order) slaveDao.convertResult(resultSet);

            if (orderBean == null) return; // Check for null bean: nothing to do in that case


            // Add to set or find an equal one if exists
            orderBean = (Order) addToSet(slaveSet, orderBean);

            // Link the orderBean to customer bean
            if (bean.getOrders() == null) {
                bean.setOrders(new HashSet<Order>());
            }
            bean.getOrders().add(orderBean);
            orderBean.setCust(bean);

        }

        // If linked to Salesrep
        if (slaveDao != null && slaveDao.getTClass() == Salesrep.class) {
            // Get the slave Salesrep bean
            Salesrep salesrepBean = (Salesrep) slaveDao.convertResult(resultSet);
            if (salesrepBean == null) return;  // Check for null bean: nothing to do in that case

            // Add to set or find an equal one if exists
            salesrepBean = (Salesrep) addToSet(slaveSet, salesrepBean);

            // Link salesrepBean to customer bean
            if (salesrepBean.getCustomers() == null) {
                salesrepBean.setCustomers(new HashSet<Customer>());
            }
            salesrepBean.getCustomers().add(bean);
            bean.setCust_rep(salesrepBean);

        }
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
        if (linkedDao != null) {
            return "SELECT * FROM CUSTOMERS " + linkedDao.getJoinString(tClass,"CUSTOMERS") + ";";
        } else {
            return "SELECT * FROM CUSTOMERS;";
        }
    }
}
