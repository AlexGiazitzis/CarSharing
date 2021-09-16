package carsharing.backend.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Alex Giazitzis
 */
public class DatabaseInitializer implements AutoCloseable {
    private final Connection connection;
    private final Statement  statement;

    private static DatabaseInitializer databaseInit;

    private DatabaseInitializer(final Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public static DatabaseInitializer getDatabaseInit() {
        return databaseInit;
    }

    public static DatabaseInitializer initDatabase(final String databaseFileName) throws SQLException {
        databaseInit = new DatabaseInitializer(getConnection(databaseFileName));
        databaseInit.getConnection().setAutoCommit(true);
        databaseInit.getStatement().execute(DatabaseSetup.dropTable(DatabaseSetup.Tables.CAR.getTable()));
        databaseInit.getStatement().execute(DatabaseSetup.dropTable(DatabaseSetup.Tables.COMPANY.getTable()));
        databaseInit.getStatement().execute(DatabaseSetup.CREATE_COMPANY);
        databaseInit.getStatement().execute(DatabaseSetup.CREATE_CAR);
        return databaseInit;
    }

    private static Connection getConnection(final String databaseFileName) throws SQLException {
        File dir = new File(DatabaseSetup.DB_DIR);
        if (!dir.mkdir()) {
            System.out.println("Could not create the database directory.");
        }
        return DriverManager.getConnection(
                String.format(DatabaseSetup.DB_URL, databaseFileName.isBlank()
                                               ? DatabaseSetup.DB_NAME
                                               : databaseFileName));

    }

    @Override
    public void close() throws SQLException {
        statement.close();
        connection.close();
    }

    private static class DatabaseSetup {
        final static String DB_DIR         = "./db";
        final static String DB_URL         = "jdbc:h2:" + DB_DIR + "/%s";
        final static String DB_NAME        = "carsharing";

        final static String DROP_TABLE     = "DROP TABLE IF EXISTS %s;";
        final static String CREATE_COMPANY = "CREATE TABLE `company` ( " +
                                             "`id` INT DEFAULT '1' PRIMARY KEY AUTO_INCREMENT, " +
                                             "`name` VARCHAR(64) UNIQUE NOT NULL); ";
        final static String CREATE_CAR     = "CREATE TABLE `car` ( " +
                                             "`id` INT DEFAULT '1' PRIMARY KEY AUTO_INCREMENT, " +
                                             "`name` VARCHAR(40) UNIQUE NOT NULL, " +
                                             "`company_id` INT NOT NULL, " +
                                             "CONSTRAINT `fk_company` FOREIGN KEY (`company_id`) " +
                                             "REFERENCES `company`(`id`) " +
                                             "ON UPDATE CASCADE " +
                                             "ON DELETE CASCADE );";

        static String dropTable(String table) {
            return String.format(DROP_TABLE, table);
        }

        enum Tables {
            COMPANY("`company`"),
            CAR("`car`");

            private final String table;

            Tables(final String table) {
                this.table = table;
            }

            public String getTable() {
                return table;
            }
        }
    }

}
