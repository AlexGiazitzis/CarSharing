package carsharing.frontend.impl;

import carsharing.Main;
import carsharing.backend.Validator;
import carsharing.backend.dao.CarDAO;
import carsharing.backend.dao.CompanyDAO;
import carsharing.backend.dao.CustomerDAO;
import carsharing.backend.entities.Car;
import carsharing.backend.entities.Company;
import carsharing.backend.entities.Customer;
import carsharing.frontend.AbstractMenu;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Giazitzis
 */
public class SingleCustomerMenu extends AbstractMenu {
    private       Customer    customer;
    private final CompanyDAO  companyDAO;
    private final CarDAO      carDAO;
    private final CustomerDAO customerDAO;

    public SingleCustomerMenu(final Customer customer, final CompanyDAO companyDAO, final CarDAO carDAO,
                              final CustomerDAO customerDAO) {
        this.customer = customer;
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
        setOptions();
    }

    @Override
    protected void setOptions() {
        String option1 = "1. Rent a car";
        String option2 = "2. Return a rented car";
        String option3 = "3. My rented car";
        String option0 = "0. Back";
        optionsText.addAll(Arrays.asList(option1, option2, option3, option0));
        options.putAll(Map.of(option1, this::rentCarMenu,
                              option2, this::returnCar,
                              option3, this::customerCar,
                              option0, this::exit));
    }

    private void rentCarMenu() {
        if (customer.getRentedCarId() > 0) {
            System.out.println("You've already rented a car!\n");
            return;
        }
        List<Company> companies;
        try {
            companies = companyDAO.getAllCompanies();
        } catch (SQLException e) {
            System.out.println("Could not load companies.");
            e.printStackTrace();
            return;
        }

        if (companies.isEmpty()) {
            System.out.println("The company list is empty!\n");
            return;
        }

        String input;
        while (true) {
            System.out.println("Choose a company:");
            companies.forEach(System.out::println);
            System.out.println("0. Back");
            input = Main.scanner.nextLine();
            System.out.println();

            if (!Validator.isNumber(input)) {
                System.out.println("Invalid option chosen. Please choose one from the list.");
                continue;
            }
            break;
        }

        int choice = Integer.parseInt(input);
        if (choice == 0) {
            return;
        }
        getCarListByCompany(companies.get(choice - 1));

    }

    private void getCarListByCompany(final Company company) {
        List<Car> cars;
        try {
            cars = carDAO.getAllCarsByCompany(company.getId());
        } catch (SQLException e) {
            System.out.println("Could not load company's cars.");
            e.printStackTrace();
            return;
        }

        if (cars.isEmpty()) {
            System.out.println("No available cars in '" + company.getName() + "' company\n");
            return;
        }

        removeRented(cars);

        String input;
        while (true) {
            System.out.println("Choose a car:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println((i + 1) + ". " + cars.get(i).getName());
            }
            System.out.println("0. Back");
            input = Main.scanner.nextLine();

            if (!Validator.isNumber(input)) {
                System.out.println("Invalid option chosen. Please choose one from the list.");
                continue;
            }
            break;
        }
        int choice = Integer.parseInt(input);
        if (choice == 0) {
            return;
        }
        rentCar(choice, cars);
    }

    private void removeRented(List<Car> cars) {
        List<Integer> rentedCarIds = null;
        try {
            rentedCarIds = customerDAO.getRentedCarsId();
        } catch (SQLException e) {
            System.out.println("Could not load renter car IDs.");
            e.printStackTrace();
            System.exit(10);
        }

        for (int i = 0; i < cars.size(); i++) {
            if (rentedCarIds.contains(cars.get(i).getId())) {
                cars.remove(cars.get(i));
            }
        }
    }

    private void rentCar(final int choice, final List<Car> allCars) {
        try {
            customerDAO.updateCustomer(customer.getId(), allCars.get(choice - 1).getId());
            customer = new Customer(customer.getId(), customer.getName(), allCars.get(choice - 1).getId());
        } catch (SQLException e) {
            System.out.println("Could not rent car.");
            e.printStackTrace();
            return;
        }
        System.out.println("You rented '" + allCars.get(choice - 1).getName() + "'\n");
    }

    private void returnCar() {
        if (customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }

        try {
            customerDAO.updateCustomer(customer.getId(), 0);
        } catch (SQLException e) {
            System.out.println("Could not return car.");
            e.printStackTrace();
            return;
        }
        System.out.println("You've returned a rented car!\n");
    }

    private void customerCar() {
        if (customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }

        Car car;
        try {
            car = carDAO.getCarById(customer.getRentedCarId());
        } catch (SQLException e) {
            System.out.println("Could not load car in the database.");
            e.printStackTrace();
            return;
        }

        Company company;
        try {
            company = companyDAO.getCompanyById(car.getCompanyId());
        } catch (SQLException e) {
            System.out.println("Could not load company.");
            e.printStackTrace();
            return;
        }

        System.out.println("Your renter car:");
        System.out.println(car.getName());
        System.out.println("Company:");
        System.out.println(company.getName());
        System.out.println();
    }
}
