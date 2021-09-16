package carsharing.frontend.impl;

import carsharing.Main;
import carsharing.backend.Validator;
import carsharing.backend.dao.CarDAO;
import carsharing.backend.dao.CompanyDAO;
import carsharing.backend.entities.Company;
import carsharing.frontend.AbstractMenu;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Giazitzis
 */
public final class ManagerMenu extends AbstractMenu {
    private final CompanyDAO companyDAO;
    private final CarDAO     carDAO;

    public ManagerMenu(final CompanyDAO companyDAO, final CarDAO carDAO) {
        super();
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        setOptions();
    }

    @Override
    protected void setOptions() {
        String option1 = "1. Company list";
        String option2 = "2. Create a company";
        String option0 = "0. Back";

        optionsText.addAll(Arrays.asList(option1, option2, option0));
        options.putAll(Map.of(option1, this::printCompanies, option2, this::createCompany, option0, this::exit));
    }

    private void printCompanies() {
        try {
            List<Company> companies = companyDAO.getAllCompanies();
            if (companies.isEmpty()) {
                System.out.println("The company list is empty!\n");
                return;
            }
            String input;
            while (true) {
                System.out.println("Choose the company:");
                for (int i = 0; i < companies.size(); i++) {
                    System.out.println(i + 1 + ". " + companies.get(i).getName());
                }
                System.out.println("0. Back");
                input = Main.scanner.nextLine();
                System.out.println();
                if (!Validator.isNumber(input)) {
                    System.out.println("Invalid option chosen. Please choose one from the list");
                    continue;
                }
                break;
            }
            int choice = Integer.parseInt(input);
            if (choice == 0) {
                return;
            }
            String companyName = companies.stream().filter(c -> c.toString().startsWith(String.valueOf(choice)))
                                          .findFirst().orElseThrow().getName();
            goToCompanyMenu(choice, companyName);
        } catch (SQLException e) {
            System.out.println("Could not get companies from database.");
            e.printStackTrace();
        }
    }

    private void goToCompanyMenu(final int companyId, final String companyName) {
        new CompanyMenu(companyId, companyName, carDAO).show();
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        String name = Main.scanner.nextLine();
        try {
            companyDAO.addCompany(name);
        } catch (SQLException e) {
            System.out.println("Could not create company.\n");
            e.printStackTrace();
        }
        System.out.println("The company was created!\n");
    }
}
