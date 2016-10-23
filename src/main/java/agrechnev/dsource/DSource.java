package agrechnev.dsource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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
 * A simple singleton data source for my application
 * No need to use pools for such a simple program
 * Reads configuration from file scripts/connection.xml
 * And runs the script scripts/init.bat or scripts/init.sh
 * Opens up a new SQL connection on demand
 * Throws DSourceException if anything goes wrong
 */
public class DSource {

    /**
     * Exception class for DSource
     */
    public static class DSourceException extends Exception {
        public DSourceException(String message) {
            super(message);
        }
        public DSourceException(String message, Throwable cause) {
            super(message, cause);
        }
    }


    private static DSource instance;


    // The SQL configuration
    // Loaded from file scripts/connection.xml by readConfig()
    private String DB_DRIVER;
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;
    private boolean DB_RUNINIT;

    /**
     * Gets the instance of DSource
     *
     * @return the instance of DSource
     */
    public static DSource getInstance() throws DSourceException {
        if (instance == null) {
            instance=new DSource();
        }
        return instance;
    }

    /**
     * Opens a new SQL connection with parameters from the config cile
     * @return  a new SQL connection
     * Exits on exception
     */
    public Connection getConnection() throws DSourceException {

        try {
            // Load the DB driver if provided
            // Not needed nowadays, but wouldn't hurt
            if (DB_DRIVER.length()>0) Class.forName(DB_DRIVER);

            // Open SQL connection, exception if wrong parameters
            return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);

        } catch (SQLException|ClassNotFoundException e) {
            // Any exception = rethrow as DSourceException
            throw new DSourceException("Exception in DSource.getConnection()",e);
        }
    }

    private DSource() throws DSourceException {
        try {
            // Read the config file
            System.out.println("Reading file scripts/connection.xml");
            readConfig();

            System.out.println("driver=" + DB_DRIVER);
            System.out.println("url=" + DB_URL);
            System.out.println("user=" + DB_USER);
            System.out.println("password=" + DB_PASSWORD);
            System.out.println("runinit=" + DB_RUNINIT);

            // Run the init script
            System.out.println("Running the init script ...");
            if (DB_RUNINIT) runInitScript();

        } catch (Exception e) { // Any exception = rethrow as DSourceException
            throw new DSourceException("Exception in DSource()",e);
        }

    }

    /**
     * Read connection parameters from the config file scripts/connection.xml
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private void readConfig() throws ParserConfigurationException, IOException, SAXException, DSourceException {

        // Load the XML document using Java DOM parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("scripts/connection.xml"));

        // The connection root element
        Element connection = document.getDocumentElement();
        if (! connection.getTagName().equals("connection")) {
            throw new DSourceException("Root tag in scripts/connection.xml must be <connection>");
        }
//                (Element) document.getElementsByTagName("connection").item(0);

        // Read the configuration parameters
        // Empty strings allowed, no checks here
        // If anything goes wrong it's an SQLException -> DSourceException in getConnection()

        DB_DRIVER = getTag(connection,"driver");
        DB_URL = getTag(connection,"url");
        DB_USER = getTag(connection,"user");
        DB_PASSWORD = getTag(connection,"password");
        DB_RUNINIT = Boolean.parseBoolean(getTag(connection,"runinit"));

    }

    private static String getTag(Element connection, String tagName) throws DSourceException {
        NodeList elements = connection.getElementsByTagName(tagName);

        if (elements.getLength()==0) {
            throw new DSourceException(String.format("Cannot find XML tag <%s>",tagName));
        }

        return elements.item(0).getTextContent().trim();
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
        // Do not quit or re-trow on exception here, only print the error message
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

