package carsharing.frontend.impl;

import carsharing.backend.dao.CarDAO;
import carsharing.backend.dao.CompanyDAO;
import carsharing.backend.dao.CustomerDAO;
import carsharing.backend.entities.Customer;
import carsharing.frontend.AbstractMenu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alex Giazitzis
 */
public class CustomerMenu extends AbstractMenu {
    private final CompanyDAO     companyDAO;
    private final CarDAO         carDAO;
    private final CustomerDAO    customerDAO;
    private       List<Customer> customers;


    public CustomerMenu(final CompanyDAO companyDAO, final CarDAO carDAO, final CustomerDAO customerDAO) {
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
        this.customers = new ArrayList<>();
    }

    @Override
    public void show() {
        try {
            if (customerDAO.getAllCustomers().isEmpty()) {
                System.out.println("The customer list is empty!\n");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve customer list.");
            e.printStackTrace();
        }
        setOptions();
        super.show();
    }

    @Override
    protected void setOptions() {
        optionsText.add("Choose a customer:");
        try {
            customers = customerDAO.getAllCustomers();
            optionsText.addAll(customers.stream()
                                        .map(Customer::toString)
                                        .collect(Collectors.toList()));
        } catch (SQLException e) {
            System.out.println("Unable to retrieve customer list.");
            e.printStackTrace();
        }
        optionsText.add("0. Back");
    }

    @Override
    protected void execute(final int choice) {
        if (choice == 0) {
            this.exit();
            return;
        }
        new SingleCustomerMenu(customers.get(choice - 1), companyDAO, carDAO, customerDAO).show();
    }
}
