package agrechnev.dao;

import agrechnev.models.Customer;
import agrechnev.models.Entity;
import agrechnev.models.Salesrep;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/22/2016.
 * Hand-written Dao for Salesrep
 * This can be used as slave Dao
 */
public class SalesrepDao extends AbstractDao<Salesrep> {

    public SalesrepDao() {
        super(Salesrep.class, "");
    }


    /**
     * Convert a line of SQL results into a bean of class Salesrep
     * ass proper fields only, links are treated separately in convertLinks
     *
     * @param resultSet SQL results set (current line)
     * @return A generated bean
     */
    @Override
    public Salesrep convertResult(ResultSet resultSet) {
        Salesrep salesrep = new Salesrep(); // Create a new Salesrep bean

        try {
            salesrep.setEmpl_num(resultSet.getInt(tablePrefix + "EMPL_NUM"));
            salesrep.setName(resultSet.getString(tablePrefix + "NAME"));
            salesrep.setAge(resultSet.getInt(tablePrefix + "AGE"));
            salesrep.setTitle(resultSet.getString(tablePrefix + "TITLE"));
            salesrep.setHire_date(resultSet.getDate(tablePrefix + "HIRE_DATE").toLocalDate());
            salesrep.setQuota(resultSet.getBigDecimal(tablePrefix + "QUOTA"));
            salesrep.setSales(resultSet.getBigDecimal(tablePrefix + "SALES"));
        } catch (SQLException | NullPointerException e) {
            // Return null bean silently
            // This is not an error when working with left joins
            return null;
        }

        return salesrep;
    }

    @Override
    public String getJoinString(Class masterClass, String masterTablePrefix) {
        if (masterClass == Customer.class) {
            // Make a join line
            return " LEFT JOIN SALESREPS ON customers.CUST_REP=salesreps.EMPL_NUM";
        } else {
            return "";
        }
    }

    @Override
    protected void convertLinks(Salesrep bean, ResultSet resultSet, Set<? extends Entity> linkSet, AbstractDao<?> linkedDao) {

    }

    @Override
    protected String getSqlQuery(AbstractDao<?> linkedDao) {
        return "SELECT * FROM SALESREPS;";
    }
}
