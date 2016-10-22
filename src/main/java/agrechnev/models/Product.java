package agrechnev.models;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Product class ->  PRODUCTS table
 */
public final class Product extends Entity  {
    // Proper fields
    private String mfr_id;
    private String product_id;
    private String description;
    private BigDecimal price;
    private int qty_on_hand;

    // Links to one

    // Links to Many
    private Set<Order> orders;

    //-----------------------------------------------------------------
    // equals() and hashCode() are id-based

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!mfr_id.equals(product.mfr_id)) return false;
        return product_id.equals(product.product_id);

    }

    @Override
    public int hashCode() {
        int result = mfr_id.hashCode();
        result = 31 * result + product_id.hashCode();
        return result;
    }


    //-----------------------------------------------------------------
    // Empty construcor, getters, setters

    public Product() {
    }

    public String getMfr_id() {
        return mfr_id;
    }

    public void setMfr_id(String mfr_id) {
        this.mfr_id = mfr_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQty_on_hand() {
        return qty_on_hand;
    }

    public void setQty_on_hand(int qty_on_hand) {
        this.qty_on_hand = qty_on_hand;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
