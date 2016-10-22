package agrechnev.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Order class -> ORDERS table
 */
public final class Order extends Entity  {
    // Proper fields
    private int order_num;
    private LocalDate order_date;
    private int qty;
    private BigDecimal amount;

    // Links to one
    private Customer cust;
    private Salesrep rep;
    private Product product;

    // Links to Many

    @Override
    public String toShortString() {
        return "{" + order_num +
                ", " + order_date +
                ", " + qty +
                ", " + amount + '}';
    }

    /**
     * Short description of an object with no recursions
     * @return
     */


    //-----------------------------------------------------------------
    // equals() and hashCode() are id-based

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return order_num == order.order_num;

    }

    @Override
    public int hashCode() {
        return order_num;
    }

    //-----------------------------------------------------------------
    // Empty construcor, getters, setters


    public Order() {
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public LocalDate getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDate order_date) {
        this.order_date = order_date;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Customer getCust() {
        return cust;
    }

    public void setCust(Customer cust) {
        this.cust = cust;
    }

    public Salesrep getRep() {
        return rep;
    }

    public void setRep(Salesrep rep) {
        this.rep = rep;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
