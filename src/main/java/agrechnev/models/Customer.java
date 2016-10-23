package agrechnev.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Customer class -> CUSTOMERS table
 * Comparable by cust_num
 */
public final class Customer extends Entity implements Comparable<Customer>  {
    // Proper fields
    private int cust_num;
    private String company;
    private BigDecimal credit_limit;

    // Links to one
    private Salesrep cust_rep;

    // Links to Many
    private Set<Order> orders=new HashSet<>();

    /**
     * Short description of an object with no recursions
     * @return
     */
    @Override
    public String toShortString() {
        return "{" + cust_num +
                ", " + company +
                ", " + credit_limit + '}';
    }

    //-----------------------------------------------------------------
    // equals() and hashCode() include proper fields only

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (cust_num != customer.cust_num) return false;
        if (company != null ? !company.equals(customer.company) : customer.company != null) return false;
        return credit_limit != null ? credit_limit.equals(customer.credit_limit) : customer.credit_limit == null;

    }

    @Override
    public int hashCode() {
        return cust_num;
    }

    @Override
    public int compareTo(Customer o) {
        return Integer.compare(cust_num,o.cust_num);
    }

    //-----------------------------------------------------------------
    // Empty construcor, getters, setters

    public Customer() {
    }

    public int getCust_num() {
        return cust_num;
    }

    public void setCust_num(int cust_num) {
        this.cust_num = cust_num;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public BigDecimal getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(BigDecimal credit_limit) {
        this.credit_limit = credit_limit;
    }

    public Salesrep getCust_rep() {
        return cust_rep;
    }

    public void setCust_rep(Salesrep cust_rep) {
        this.cust_rep = cust_rep;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
