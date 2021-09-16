package carsharing.frontend.impl;

import carsharing.Main;
import carsharing.backend.dao.CarDAO;
import carsharing.backend.dao.CompanyDAO;
import carsharing.backend.dao.CustomerDAO;
import carsharing.frontend.AbstractMenu;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Alex Giazitzis
 */
public final class MainMenu extends AbstractMenu {
    private final CompanyDAO  companyDAO;
    private final CarDAO      carDAO;
    private final CustomerDAO customerDAO;

    public MainMenu(final CompanyDAO companyDAO, final CarDAO carDAO, final CustomerDAO customerDAO) {
        super();
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
        setOptions();
    }

    @Override
    protected void setOptions() {
        String option1 = "1. Log in as a manager";
        String option2 = "2. Log in as a customer";
        String option3 = "3. Create a customer";
        String option0 = "0. Exit";
        optionsText.addAll(Arrays.asList(option1, option2, option3, option0));
        options.putAll(Map.of(option1, () -> new ManagerMenu(companyDAO, carDAO).show(),
                              option2, () -> new CustomerMenu(companyDAO, carDAO, customerDAO).show(),
                              option3, this::createCustomer,
                              option0, this::exit));
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = Main.scanner.nextLine();

        try {
            customerDAO.addCustomer(name);
        } catch (SQLException e) {
            System.out.println("Could not create customer.");
            e.printStackTrace();
            return;
        }
        System.out.println("The customer was added!\n");
    }

}
