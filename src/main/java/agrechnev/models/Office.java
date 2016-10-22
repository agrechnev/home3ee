package agrechnev.models;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Office class -> OFFICES table
 */
public final class Office extends Entity  {
    // Proper fields
    private int office;
    private String city;
    private String region;
    private BigDecimal target;
    private BigDecimal sales;

    // Links to one
    private Salesrep mgr;

    // Links to Many
    private Set<Salesrep> employees;

    //-----------------------------------------------------------------
    // equals() and hashCode() are id-based

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Office office1 = (Office) o;

        return office == office1.office;

    }

    @Override
    public int hashCode() {
        return office;
    }

    //-----------------------------------------------------------------
    // Empty construcor, getters, setters

    public Office() {
    }

    public int getOffice() {
        return office;
    }

    public void setOffice(int office) {
        this.office = office;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    public Salesrep getMgr() {
        return mgr;
    }

    public void setMgr(Salesrep mgr) {
        this.mgr = mgr;
    }

    public Set<Salesrep> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Salesrep> employees) {
        this.employees = employees;
    }
}
