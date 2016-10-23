package agrechnev.main;

import agrechnev.dao.AbstractDao;
import agrechnev.dao.CustomerDao;
import agrechnev.dao.OrderDao;
import agrechnev.dao.SalesrepDao;
import agrechnev.dsource.DSource;
import agrechnev.ewriter.EWriter;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 */
public class Main {
    public static void main(String[] args) throws DSource.DSourceException, AbstractDao.DaoException, IOException {

        // Pre-init the datasource, to avoid the mix-up of messages in stdout
        // DsourceException, if any, is thrown here
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
        EWriter.printSortedSet(customerDao.getAll(orderDao));
//        EWriter.writeSortedSet(customerDao.getAll(orderDao), Paths.get("tables/cu_or.txt"));

        System.out.println("-------------------------------------");

        // Read all customers from the DB, left join salesreps
        EWriter.printSortedSet(customerDao.getAll(salesrepDao));
        EWriter.writeSortedSet(customerDao.getAll(salesrepDao), Paths.get("tables/cu_sr.txt"));
    }

}
