package agrechnev.main;

import agrechnev.dao.CustomerDao;
import agrechnev.dao.OrderDao;
import agrechnev.dao.SalesrepDao;
import agrechnev.dsource.DSource;

import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 */
public class Main {
    public static void main(String[] args) {

        // Pre-init the datasource, to avoid the mix-up of messages in stdout
        DSource.getInstance();

        System.out.println("-------------------------------------");
        System.out.println("Fun with hand-written DAOs:");
        System.out.println("");
        System.out.println("-------------------------------------");

        // Set up DAOs
        CustomerDao customerDao=new CustomerDao();
        OrderDao orderDao=new OrderDao();
        SalesrepDao salesrepDao=new SalesrepDao();

        // Read all customers from the DB, left join orders
        printSet(customerDao.getAll(orderDao));

        System.out.println("-------------------------------------");

        // Read all customers from the DB, left join salesreps
        printSet(customerDao.getAll(salesrepDao));

    }

    /**
     * Print a set, with a "number of rows" header
     * @param set
     * @param <T>
     */
    public static <T> void printSet(Set<T> set) {

        System.out.println(set.size()+" row(s)");
        System.out.println("");
        set.forEach(System.out::println);
    }
}
