package agrechnev;

import agrechnev.dsource.DSource;
import agrechnev.models.Customer;
import agrechnev.models.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 */
public class Main {
    public static void main(String[] args) {

        DSource.getInstance();

        Order order1=new Order();
        order1.setOrder_num(17);

        Order order2=new Order();

        order2.setOrder_num(33);
        order2.setOrder_date(LocalDate.now());
        order2.setQty(18);
        order2.setAmount(new BigDecimal("33.79"));

        Customer customer=new Customer();

        customer.setCust_num(13);
        customer.setCompany("Hell");
        customer.setCredit_limit(new BigDecimal("10.23"));
        customer.setOrders(new HashSet<Order>());
        customer.getOrders().add(order1);
        customer.getOrders().add(order2);


        System.out.println(customer.toString());


    }
}
