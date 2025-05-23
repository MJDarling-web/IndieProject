package persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.*;
import jakarta.persistence.*;


/**
 * Provides access to the database
 * Created on 8/31/16.
 *
 * @author pwaite
 * @author Alex M - Fall 2019 - added multi-line sql capability
 */

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    // create an object of the class Database
    private static Database instance = new Database();

    private Properties properties;
    private Connection connection;

    /** private constructor prevents instantiating this class anywhere else
     **/
    Database() {
        loadProperties();

    }

    /** load the properties file containing the driver, connection url, userid and pwd.
     * TODO this would be improved by using properties loader interface provided in adv java
     */
    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load (this.getClass().getResourceAsStream("/database.properties"));
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Database.loadProperties()… Cannot load the properties file", ioe);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Database.loadProperties()...", e);
        }

    }

    /** get the only Database object available
     @return the single database object
     */
    public static Database getInstance() {
        return instance;
    }

    /** get the database connection
     @return the database connection
     */
    public Connection getConnection() {
        return connection;
    }

    /** attempt to connect to the database
     */
    public void connect() throws Exception {
        if (connection != null)
            return;

        try {
            Class.forName(properties.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            throw new Exception("Database.connect()... Error: MySQL Driver not found");
        }

        String url = properties.getProperty("url");
        connection = DriverManager.getConnection(url, properties.getProperty("username"),  properties.getProperty("password"));
        logger.info("Database.connect()... Connection successful");
    }

    /** close and clean up the database connection
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE,"Cannot close connection" + e);
            }
        }

        connection = null;
    }

    /**
     * Run the sql.
     *
     * @param sqlFile the sql file to be read and executed line by line
     */
    public void runSQL(String sqlFile) {

        Statement stmt = null;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(sqlFile))))  {

            connect();
            stmt = connection.createStatement();

            String sql = "";
            while (br.ready())
            {
                char inputValue = (char)br.read();

                if(inputValue == ';')
                {
                    stmt.executeUpdate(sql);
                    sql = "";
                }
                else
                    sql += inputValue;
            }

        } catch (SQLException se) {
            logger.log(Level.SEVERE,"SQL Exception..." + se);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Exception" + e);
        } finally {
            disconnect();
        }

    }
}