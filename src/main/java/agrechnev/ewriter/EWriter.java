package agrechnev.ewriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Oleksiy Grechnyev on 10/23/2016.
 * Print set of entity after sort, result as list, array, stdout or disk file
 */
public class EWriter {

    private EWriter() {
    }

    /**
     * Convert a set of entities into a sorted List<String> using internal comparator
     * fllowed by toString()
     * @param set A set of entities
     * @return The sorted list
     */
    public static List<String> sortedSet(Set<?> set){
        List<?> entityList=new ArrayList<>(set);
        entityList.sort(null); // Sort with the internal comparator

        return entityList.stream().map(Object::toString).collect(Collectors.toList());
    }

    /**
     * Convert a set of entities into a sorted List<String> using internal comparator
     * fllowed by toString() and convert to array String[]
     * @param set A set of entities
     * @return The sorted array
     */
    public static String[] sortedSetToArray(Set<?> set){
        return sortedSet(set).toArray(new String[]{});
    }

    /**
     * Convert a set of entities into a sorted List<String>  and print to stdout
     * @param set A set of entities
     */
    public static void printSortedSet(Set<?> set){
        List<String> stringList=sortedSet(set);

        System.out.println(stringList.size() + " lines");
        System.out.println("");

        for (String s: stringList) {
            System.out.println(s);
        }
    }

    /**
     * Convert a set of entities into a sorted  List<String> and writes to file
     * @param set A set of entities
     */
    public static void writeSortedSet(Set<?> set, Path path) throws IOException {
        Files.write(path, sortedSet(set),StandardCharsets.UTF_8);
    }
}
