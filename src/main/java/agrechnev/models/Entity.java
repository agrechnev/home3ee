package agrechnev.models;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Oleksiy Grechnyev on 10/20/2016.
 * The only real reason for me to create this class was to write
 * the universal toString
 * Consider this a little funny exercise on reflections
 * <p>
 * After a while I added comparable to all children (but not Entity itself) to allow for sorting
 * This is needed for unit tests as the unsorted results depend on the SQL implementation
 */
public abstract class Entity {

    /**
     * Print all fields of an Entity object as a String using reflections
     * Prints collection elements with toShortString() if available
     * To avoid possible infinite recursion
     *
     * @return the full description of an object
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // The result
        Class myClass = this.getClass(); // Class of the object

        // Add the class simple name (e.g. "Customer")
        builder.append(myClass.getSimpleName());
        builder.append('{');

        // Scan all fields of the class
        boolean firstRun = true;
        for (Field field : myClass.getDeclaredFields()) {
            if (!firstRun) {
                builder.append(", "); // Separate by commas
            } else {
                firstRun = false;
            }

            // Print the field as a name = value
            builder.append(field.getName());
            builder.append(" = ");
            builder.append(printField(field));
        }

        builder.append('}');
        return builder.toString();
    }

    /**
     * Print a field of this as a string
     *
     * @param field as a Field object
     * @return string representation of the field
     */
    private String printField(Field field) {

        // Make a private field accessible
        // I do not see any big problem here as toString() runs for an
        // object of an Entity subclass (e.g. Customer) anyway

        field.setAccessible(true);

        // Get the value as a string
        try {
            Object value = field.get(this);

            if (value == null) {
                return "NULL";
            }

            // Check if it is a collection
            if (Collection.class.isAssignableFrom(field.getType())) {
                return printCollection((Collection<?>) value);
            }


            return value.toString();

        } catch (IllegalAccessException e) {
            return "EXCEPTION";
        }

    }

    /**
     * Print collection as a string using toShortString if available
     */
    private String printCollection(Collection<?> value) {
        // Sort the collection,
        // Exception here if not of Comparable types, but that should not happen with our entities
        List<?> sortedList = new ArrayList<>(value);

        try {
            sortedList.sort(null); // Sort w/o external comparator
        } catch (Exception e) {
            sortedList = new ArrayList<>(value); // Unsorted version
        }

        boolean firstRun = true;
        StringBuilder builder = new StringBuilder().append('[');

        for (Object o : sortedList) {
            if (!firstRun) {
                builder.append(", "); // Separate by commas
            } else {
                firstRun = false;
            }

            builder.append(printShort(o));

        }

        builder.append(']');

        return builder.toString();
    }

    /**
     * Print an object as a short string if it has the method toShortString()
     */
    private String printShort(Object o) {
        if (o == null) {
            return "NULL";
        }

        try {
            // Let us see if the object has toShortString()
            Method toShort = o.getClass().getDeclaredMethod("toShortString");
            return (String) toShort.invoke(o);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return ""; // No description
        }
    }

    /**
     * Return a very short description of an Entity object
     * Must never print any Entity members to avoid infinite recursions
     * Override for each Entity subclass
     *
     * @return short description of an Entity object
     */
    public String toShortString() {
        return "{}";
    }

}
