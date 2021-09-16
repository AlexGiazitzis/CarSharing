package carsharing.frontend.impl;

import carsharing.Main;
import carsharing.backend.dao.CarDAO;
import carsharing.backend.entities.Car;
import carsharing.frontend.AbstractMenu;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Giazitzis
 */
public class CompanyMenu extends AbstractMenu {
    private final int    companyId;
    private final String companyName;
    private final CarDAO dao;

    public CompanyMenu(final int companyId, final String companyName, final CarDAO dao) {
        super();
        this.companyId = companyId;
        this.companyName = companyName;
        this.dao = dao;
        setOptions();
    }

    @Override
    protected void setOptions() {
        System.out.println("'" + companyName + "' company");
        String option1 = "1. Car list";
        String option2 = "2. Create a car";
        String option0 = "0. Back";
        optionsText.addAll(Arrays.asList(option1, option2, option0));
        options.putAll(Map.of(option1, this::printCarListForCompany, option2, this::createCar, option0, this::exit));
    }

    private void printCarListForCompany() {
        try {
            List<Car> cars = dao.getAllCarsByCompany(companyId);
            if (cars.isEmpty()) {
                System.out.println("The car list is empty!\n");
                return;
            }

            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println(i + 1 + ". " + cars.get(i).getName());
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Could not get the cars associated with the company with id: " + companyId);
            e.printStackTrace();
        }
    }

    private void createCar() {
        System.out.println("Enter the car name:");
        String name = Main.scanner.nextLine();
        try {
            if (dao.addCarToCompany(name, companyId)) {
                System.out.println("The car was added!");
            }
        } catch (SQLException e) {
            System.out.println("Error: Could not to create a car.");
            e.printStackTrace();
        }
        System.out.println();
    }
}
