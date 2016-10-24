package agrechnev.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Product class ->  PRODUCTS table
 * Comparable by product_id and mfr_id
 */
public final class Product extends Entity implements Comparable<Product> {
    // Proper fields
    private String mfr_id;
    private String product_id;
    private String description;
    private BigDecimal price;
    private int qty_on_hand;

    // Links to one

    // Links to Many
    private Set<Order> orders = new HashSet<>();

    //-----------------------------------------------------------------
    // equals() and hashCode() include proper fields only

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (qty_on_hand != product.qty_on_hand) return false;
        if (mfr_id != null ? !mfr_id.equals(product.mfr_id) : product.mfr_id != null) return false;
        if (product_id != null ? !product_id.equals(product.product_id) : product.product_id != null) return false;
        if (description != null ? !description.equals(product.description) : product.description != null) return false;
        return price != null ? price.equals(product.price) : product.price == null;

    }

    @Override
    public int hashCode() {
        int result = mfr_id != null ? mfr_id.hashCode() : 0;
        result = 31 * result + (product_id != null ? product_id.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Product o) {
        int result = product_id.compareTo(o.product_id);

        return result == 0 ? mfr_id.compareTo(o.mfr_id) : result;
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
