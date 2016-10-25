package agrechnev;

import agrechnev.dao.AbstractDao;
import agrechnev.ewriter.EWriter;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Oleksiy Grechnyev on 10/23/2016.
 * Simple routines for unit-testing DAO queries
 * for both hand-written DAO and the reflection ORM DAO
 * <p>
 * Testing is done by comparing array of to_string()-generated Strings
 * Not very beautiful, but it gets the job done
 */
public class Tester {

    private Tester() {
    }

    /**
     * Test a query to a couple of DAOs
     * Compares results to a text file
     *
     * @param dao1     Master DAO
     * @param dao2     Slave DAO
     * @param fileName File name containig results
     */
    public static void testDao(AbstractDao dao1, AbstractDao dao2, String fileName) {
        try {
            // Get from DAO
            String[] actual = EWriter.sortedSetToArray(dao1.getAll(dao2));

            // Read from file
            String[] expected =
                    Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8).toArray(new String[]{});

            // Compare
            Assert.assertArrayEquals(expected, actual);

        } catch (Exception e) {
            // Any exception = test failure
            e.printStackTrace();
            Assert.fail();
        }
    }


}
