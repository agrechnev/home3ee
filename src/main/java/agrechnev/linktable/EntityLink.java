package agrechnev.linktable;

import java.util.List;

/**
 * Link table node representing a single one-many link
 *
 *  The link has the structure
 *  Class_S, filed_S, Class_L, field_L, columns_S, columns_L
 *  Where fieldS has type Set<ClassL>
 *  And fieldL has type ClassS
 *
 *   columns_S, columns_L = names of columns to match
 */

public class EntityLink {

    private String class_S; // Name of The linked class which contains fieldS
    private String field_S;  // of type Set<ClassL>

    private String class_L; // Name of The linked class which contains fieldL
    private String field_L; // of type classS

    // Names of the database columns to match in a join statement
    private List<String> columns_S;
    private List<String> columns_L;

    /**
     * Constructor
     * @param class_S
     * @param field_S
     * @param class_L
     * @param field_L
     * @param columns_S
     * @param columns_L
     */
    public EntityLink(String class_S, String field_S, String class_L, String field_L, List<String> columns_S, List<String> columns_L) {
        this.class_S = class_S;
        this.field_S = field_S;
        this.class_L = class_L;
        this.field_L = field_L;
        this.columns_S = columns_S;
        this.columns_L = columns_L;
    }

    // Equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityLink that = (EntityLink) o;

        if (class_S != null ? !class_S.equals(that.class_S) : that.class_S != null) return false;
        if (field_S != null ? !field_S.equals(that.field_S) : that.field_S != null) return false;
        if (class_L != null ? !class_L.equals(that.class_L) : that.class_L != null) return false;
        if (field_L != null ? !field_L.equals(that.field_L) : that.field_L != null) return false;
        if (columns_S != null ? !columns_S.equals(that.columns_S) : that.columns_S != null) return false;
        return columns_L != null ? columns_L.equals(that.columns_L) : that.columns_L == null;

    }

    @Override
    public int hashCode() {
        int result = class_S != null ? class_S.hashCode() : 0;
        result = 31 * result + (field_S != null ? field_S.hashCode() : 0);
        result = 31 * result + (class_L != null ? class_L.hashCode() : 0);
        result = 31 * result + (field_L != null ? field_L.hashCode() : 0);
        return result;
    }

    // Getters

    public String getClass_S() {
        return class_S;
    }

    public String getField_S() {
        return field_S;
    }

    public String getClass_L() {
        return class_L;
    }

    public String getField_L() {
        return field_L;
    }

    public List<String> getColumns_S() {
        return columns_S;
    }

    public List<String> getColumns_L() {
        return columns_L;
    }
}
