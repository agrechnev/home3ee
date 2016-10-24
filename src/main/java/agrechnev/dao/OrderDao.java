package agrechnev.dao;

import agrechnev.models.Customer;
import agrechnev.models.Order;
import agrechnev.models.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/22/2016.
 * Hand-written Dao for Order
 * This can be used as slave Dao
 */
public class OrderDao extends AbstractDao<Order> {

    public OrderDao() {
        super(Order.class, "");
    }


    /**
     * Convert a line of SQL results into a bean of class Order
     * ass proper fields only, links are treated separately in convertLinks
     *
     * @param resultSet SQL results set (current line)
     * @return A generated bean
     */
    @Override
    public Order convertResult(ResultSet resultSet) {
        Order order = new Order(); // Create a new Order bean

        try {
            order.setOrder_num(resultSet.getInt(tablePrefix + "ORDER_NUM"));
            order.setOrder_date(resultSet.getDate(tablePrefix + "ORDER_DATE").toLocalDate());
            order.setQty(resultSet.getInt(tablePrefix + "QTY"));
            order.setAmount(resultSet.getBigDecimal(tablePrefix + "AMOUNT"));
        } catch (SQLException | NullPointerException e) {
            // Return null bean silently
            // This is not an error when working with left joins
            return null;
        }

        return order;
    }

    @Override
    public String getJoinString(Class masterClass, String masterTablePrefix) {
        if (masterClass == Customer.class) {
            // Make a join line
            return " LEFT JOIN ORDERS ON CUSTOMERS.CUST_NUM=ORDERS.CUST";
        } else {
            return "";
        }
    }

    @Override
    protected void convertLinks(Order bean, ResultSet resultSet, Set<? extends Entity> linkSet, AbstractDao<?> linkedDao) {

    }

    @Override
    protected String getSqlQuery(AbstractDao<?> linkedDao) {
        return "SELECT * FROM ORDERS;";
    }
}
