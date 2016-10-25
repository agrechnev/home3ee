package agrechnev.main;

import agrechnev.dao.AbstractDao;
import agrechnev.dao.CustomerDao;
import agrechnev.dao.OrderDao;
import agrechnev.dao.SalesrepDao;
import agrechnev.dsource.DSource;
import agrechnev.ewriter.EWriter;
import agrechnev.linktable.EntityLinkTable;
import agrechnev.models.*;
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
        EWriter.printSortedSet(customerHandDao.getAll(orderHandDao));

        System.out.println("-------------------------------------");

        // Read all customers from the DB, left join salesreps
        EWriter.printSortedSet(customerHandDao.getAll(salesrepHandDao));

        System.out.println("-------------------------------------");
        System.out.println("Fun with ORM DAO:");
        System.out.println("-------------------------------------");
        System.out.println("");

        // Load the links table
        EntityLinkTable linkTable=new EntityLinkTable("scripts/links.xml");

        // Create the ORM DAOs
        ORMDao<Customer> customerORMDao=new ORMDao<>(Customer.class,"","",linkTable);
        ORMDao<Order> orderORMDao=new ORMDao<>(Order.class,"","",linkTable);
        ORMDao<Office> officeORMDao=new ORMDao<>(Office.class,"","",linkTable);
        ORMDao<Product> productORMDao=new ORMDao<>(Product.class,"","",linkTable);
        // Two copies of Salesrep DAO with different prefixes are needed for self-link
        ORMDao<Salesrep> salesrepORMDao1=new ORMDao<>(Salesrep.class,"","sr1",linkTable);
        ORMDao<Salesrep> salesrepORMDao2=new ORMDao<>(Salesrep.class,"","sr2",linkTable);

        EWriter.printSortedSet(customerORMDao.getAll(orderORMDao));
        System.out.println("-------------------------------------");
        EWriter.printSortedSet(customerORMDao.getAll(salesrepORMDao1));
        System.out.println("-------------------------------------");
        EWriter.printSortedSet(salesrepORMDao1.getAll(salesrepORMDao2));

        EWriter.printSortedSet(productORMDao.getAll(orderORMDao));
        System.out.println("-------------------------------------");
        EWriter.printSortedSet(officeORMDao.getAll(salesrepORMDao1));


    }

}
