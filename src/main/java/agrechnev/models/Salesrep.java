package agrechnev.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * Salesrep class -> SALESREPS table
 * Comparable by empl_num
 */
public final class Salesrep extends Entity implements Comparable<Salesrep> {
    // Proper fields
    private int empl_num;
    private String name;
    private int age;
    private String title;
    private LocalDate hire_date;
    private BigDecimal quota;
    private BigDecimal sales;

    // Links to one
    private Salesrep manager;
    private Office rep_office;

    // Links to Many
    private Set<Customer> customers = new HashSet<>();
    private Set<Office> offices = new HashSet<>();
    private Set<Order> orders = new HashSet<>();
    private Set<Salesrep> managerTo = new HashSet<>();

    //-----------------------------------------------------------------
    // equals() and hashCode() include proper fields only

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Salesrep salesrep = (Salesrep) o;

        if (empl_num != salesrep.empl_num) return false;
        if (age != salesrep.age) return false;
        if (name != null ? !name.equals(salesrep.name) : salesrep.name != null) return false;
        if (title != null ? !title.equals(salesrep.title) : salesrep.title != null) return false;
        if (hire_date != null ? !hire_date.equals(salesrep.hire_date) : salesrep.hire_date != null) return false;
        if (quota != null ? !quota.equals(salesrep.quota) : salesrep.quota != null) return false;
        return sales != null ? sales.equals(salesrep.sales) : salesrep.sales == null;

    }

    @Override
    public int hashCode() {
        return empl_num;
    }

    @Override
    public int compareTo(Salesrep o) {
        return Integer.compare(empl_num, o.empl_num);
    }

//-----------------------------------------------------------------
    // Empty construcor, getters, setters


    public Salesrep() {
    }

    public int getEmpl_num() {
        return empl_num;
    }

    public void setEmpl_num(int empl_num) {
        this.empl_num = empl_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getHire_date() {
        return hire_date;
    }

    public void setHire_date(LocalDate hire_date) {
        this.hire_date = hire_date;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    public Salesrep getManager() {
        return manager;
    }

    public void setManager(Salesrep manager) {
        this.manager = manager;
    }

    public Office getRep_office() {
        return rep_office;
    }

    public void setRep_office(Office rep_office) {
        this.rep_office = rep_office;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Office> getOffices() {
        return offices;
    }

    public void setOffices(Set<Office> offices) {
        this.offices = offices;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Salesrep> getManagerTo() {
        return managerTo;
    }

    public void setManagerTo(Set<Salesrep> managerTo) {
        this.managerTo = managerTo;
    }
}
