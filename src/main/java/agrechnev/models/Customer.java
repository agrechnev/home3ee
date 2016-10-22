package agrechnev.models;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Customer class -> CUSTOMERS table
 */
public final class Customer extends Entity  {
    // Proper fields
    private int cust_num;
    private String company;
    private BigDecimal credit_limit;

    // Links to one
    private Salesrep cust_rep;

    // Links to Many
    private Set<Order> orders;

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
    // equals() and hashCode() are id-based

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return cust_num == customer.cust_num;

    }

    @Override
    public int hashCode() {
        return cust_num;
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
