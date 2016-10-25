package agrechnev;

import agrechnev.dao.AbstractDao;
import agrechnev.dao.CustomerDao;
import agrechnev.dao.OrderDao;
import agrechnev.dao.SalesrepDao;
import agrechnev.dsource.DSource;
import agrechnev.linktable.EntityLinkTable;
import agrechnev.models.*;
import agrechnev.orm.ORMDao;
import org.junit.Test;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Tests for hand-written DAO and the reflection ORM DAO
 */
public class ORMTest {
    // Daos and links, class-wide non-static info, no need to @Before it

    // Hand-written DAOs
    CustomerDao customerDao = new CustomerDao();
    OrderDao orderDao = new OrderDao();
    SalesrepDao salesrepDao = new SalesrepDao();

    // Load the links table
    EntityLinkTable linkTable = new EntityLinkTable("scripts/links.xml");

    // ORM DAOs
    ORMDao<Customer> customerORMDao = new ORMDao<>(Customer.class, "", "", linkTable);
    ORMDao<Order> orderORMDao = new ORMDao<>(Order.class, "", "", linkTable);
    ORMDao<Office> officeORMDao = new ORMDao<>(Office.class, "", "", linkTable);
    ORMDao<Product> productORMDao = new ORMDao<>(Product.class, "", "", linkTable);
    // Two copies of Salesrep DAO with different prefixes are needed for self-link
    ORMDao<Salesrep> salesrepORMDao1 = new ORMDao<>(Salesrep.class, "", "sr1", linkTable);
    ORMDao<Salesrep> salesrepORMDao2 = new ORMDao<>(Salesrep.class, "", "sr2", linkTable);

    public ORMTest() throws EntityLinkTable.EntityLinkException, AbstractDao.DaoException, DSource.DSourceException {
        // Start DSource and get exceptions if any
        DSource.getInstance();
    }


    // Test Hand-written DAOs

    /**
     * Hand-written dao test: CUSTOMERS LEFT JOIN ORDERS
     */
    @Test
    public void hand_cu_or() throws Exception {
        Tester.testDao(customerDao, orderDao, "tables/cu_or.txt");
    }


    /**
     * Hand-written dao test: CUSTOMERS LEFT JOIN SALESREPS
     */
    @Test
    public void hand_cu_sr() throws Exception {
        Tester.testDao(customerDao, salesrepDao, "tables/cu_sr.txt");
    }

    // Test ORM DAOs

    /**
     * ORM dao test: CUSTOMERS LEFT JOIN ORDERS
     */
    @Test
    public void orm_cu_or() throws Exception {
        Tester.testDao(customerORMDao, orderORMDao, "tables/cu_or.txt");
    }

    /**
     * ORM dao test: CUSTOMERS LEFT JOIN SALESREPS
     */
    @Test
    public void orm_cu_sr() throws Exception {
        Tester.testDao(customerORMDao, salesrepORMDao1, "tables/cu_sr.txt");
    }


    /**
     * ORM dao test: SALESREPS LEFT JOIN SALESREPS (self-join example)
     */
    @Test
    public void orm_sr_sr() throws Exception {
        Tester.testDao(salesrepORMDao1, salesrepORMDao2, "tables/sr_sr.txt");
    }


    /**
     * ORM dao test: PRODUCTS LEFT JOIN ORDERS
     */
    @Test
    public void orm_pr_or() throws Exception {
        Tester.testDao(productORMDao, orderORMDao, "tables/pr_or.txt");
    }


}
