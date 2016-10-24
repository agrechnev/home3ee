package agrechnev.main;

import agrechnev.dao.AbstractDao;
import agrechnev.dao.CustomerDao;
import agrechnev.dao.OrderDao;
import agrechnev.dao.SalesrepDao;
import agrechnev.dsource.DSource;
import agrechnev.ewriter.EWriter;
import agrechnev.linktable.EntityLinkTable;
import agrechnev.models.Customer;
import agrechnev.models.Order;
import agrechnev.orm.ORMDao;

import java.io.IOException;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 */
public class Main {
    public static void main(String[] args) throws DSource.DSourceException, AbstractDao.DaoException, IOException, EntityLinkTable.EntityLinkException {

        // Pre-init the datasource, to avoid the mix-up of messages in stdout
        // DsourceException, if any, is thrown here
        DSource.getInstance();

        System.out.println("-------------------------------------");
        System.out.println("Fun with hand-written DAOs:");
        System.out.println("-------------------------------------");
        System.out.println("");

        // Set up DAOs
        CustomerDao customerHandDao = new CustomerDao();
        OrderDao orderHandDao = new OrderDao();
        SalesrepDao salesrepHandDao = new SalesrepDao();

        // Read all customers from the DB, left join orders
//        EWriter.printSortedSet(customerHandDao.getAll(orderHandDao));
//        EWriter.writeSortedSet(customerHandDao.getAll(orderHandDao), Paths.get("tables/cu_or.txt"));

        System.out.println("-------------------------------------");

        // Read all customers from the DB, left join salesreps
//        EWriter.printSortedSet(customerHandDao.getAll(salesrepHandDao));
//        EWriter.writeSortedSet(customerHandDao.getAll(salesrepHandDao), Paths.get("tables/cu_sr.txt"));

        System.out.println("-------------------------------------");
        System.out.println("Fun with ORM DAO:");
        System.out.println("-------------------------------------");
        System.out.println("");

        // Load the links table
        EntityLinkTable linkTable=new EntityLinkTable("scripts/links.xml");

        // Create the ORM DAOs
        ORMDao<Customer> customerORMDao=new ORMDao<>(Customer.class,"","",linkTable);
        ORMDao<Order> orderORMDao=new ORMDao<>(Order.class,"","",linkTable);

        EWriter.printSortedSet(customerORMDao.getAll(orderORMDao));
    }

}
