package agrechnev;

import agrechnev.dao.CustomerDao;
import agrechnev.dao.OrderDao;
import agrechnev.dao.SalesrepDao;
import agrechnev.dsource.DSource;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Tests for hand-written DAO and the reflection ORM DAO
 */
public class ORMTest {
    // Hand-written DAOs
    CustomerDao customerDao = new CustomerDao();
    OrderDao orderDao = new OrderDao();
    SalesrepDao salesrepDao = new SalesrepDao();

    @Before
    public void init() throws DSource.DSourceException {
        // Start DSource and get exceptions if any
        DSource.getInstance();
    }

    /**
     * Hand-written dao test: CUSTOMERS JOIN ORDERS
     */
    @Test
    public void hand_cu_or() throws Exception {
        Tester.testHandDao(customerDao, orderDao, "tables/cu_or.txt");
    }


    /**
     * Hand-written dao test: CUSTOMERS JOIN SALESREPS
     */
    @Test
    public void hand_cu_sr() throws Exception {
        Tester.testHandDao(customerDao, salesrepDao, "tables/cu_sr.txt");
    }
}
