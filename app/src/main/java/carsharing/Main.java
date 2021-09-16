package carsharing;

import carsharing.backend.dao.CarDAO;
import carsharing.backend.dao.CompanyDAO;
import carsharing.backend.dao.CustomerDAO;
import carsharing.backend.dao.impl.CarDAOImpl;
import carsharing.backend.dao.impl.CompanyDAOImpl;
import carsharing.backend.dao.impl.CustomerDAOImpl;
import carsharing.backend.database.DatabaseInitializer;
import carsharing.frontend.Menu;
import carsharing.frontend.impl.MainMenu;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * @author Alex Giazitzis
 */
public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String databaseFileName = "";
        if (args.length > 1 && "-databaseFileName".equals(args[0])) {
            databaseFileName = args[1];
        }

        try (DatabaseInitializer ignored = DatabaseInitializer.initDatabase(databaseFileName);
             CompanyDAO companyDAO = new CompanyDAOImpl();
             CarDAO carDAO = new CarDAOImpl();
             CustomerDAO customerDAO = new CustomerDAOImpl();
             scanner) {

            Menu menu = new MainMenu(companyDAO, carDAO, customerDAO);
            menu.show();

        } catch (SQLException e) {
            System.out.println("Could not connect to the database.");
            e.printStackTrace();
        }
    }
}
