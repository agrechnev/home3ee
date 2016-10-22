package agrechnev.dsource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Oleksiy Grechnyev on 10/22/2016.
 * A simple data source for my application
 * No need to use pools for such a simple program
 * Reads configuration from file scripts/connection.xml
 * And runs the script scripts/init.bat or scripts/init.sh
 * Opens up a new SQL connection on demand
 * Exits on exception
 */
public class DSource {
    private static DSource instance = new DSource();

    // The SQL configuration
    // Loaded from file scripts/connection.xml by readConfig()
    private String DB_DRIVER;
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;

    /**
     * Gets the instance of DSource
     *
     * @return the instance of DSource
     */
    public static DSource getInstance() {
        return instance;
    }

    /**
     * Opens a new SQL connection with parameters from the config cile
     * @return  a new SQL connection
     * Exits on exception
     */
    public Connection getConnection() {

        try {
            // Load the DB driver
            // Not needed nowadays, but wouldn't hurt
            Class.forName(DB_DRIVER);

            return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        } catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return null; // How stupid to put this after System.exit(1) !!!!
        }
    }

    private DSource() {
        try {
            // Read the config file
            System.out.println("Reading file scripts/connection.xml");
            readConfig();

            System.out.println("driver=" + DB_DRIVER);
            System.out.println("URL=" + DB_URL);
            System.out.println("user=" + DB_USER);
            System.out.println("password=" + DB_PASSWORD);

            // Run the init scripts
            System.out.println("Running init scripts ...");
            runInitScript();

        } catch (Exception e) { // Any exception
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Read connection parameters from the config file scripts/connection.xml
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private void readConfig() throws ParserConfigurationException, IOException, SAXException {

        // Load the XML document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("scripts/connection.xml"));

        // The connection element
        Element connection = (Element) document.getElementsByTagName("connection").item(0);

        // Read the configuration parameters
        DB_DRIVER = connection.getElementsByTagName("driver").item(0).getTextContent();
        DB_URL = connection.getElementsByTagName("url").item(0).getTextContent();
        DB_USER = connection.getElementsByTagName("user").item(0).getTextContent();
        DB_PASSWORD = connection.getElementsByTagName("password").item(0).getTextContent();

    }

    /**
     * Runs the init script scripts/init.bat or scripts/init.sh
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void runInitScript() throws IOException, InterruptedException {

        // Check for Windows vs Unix
        String os = System.getProperty("os.name").toLowerCase();
        boolean runsOnWindows = os.contains("win");

        // Ensure the exectutable permission of the Unix script scripts/init.sh
        // Do not quit on exception here, only print the error message
        try {
            if (!runsOnWindows) {
                Runtime.getRuntime().exec("chmod a+x scripts/init.sh").waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        // Run the scripts/init.bat or scripts/init.sh script
        // different for Win and Unix
        String command = (runsOnWindows ? "scripts\\init.bat " : "scripts/init.sh ") +
                DB_USER + " " + DB_PASSWORD;

        Runtime.getRuntime().exec(command).waitFor();

    }
}

