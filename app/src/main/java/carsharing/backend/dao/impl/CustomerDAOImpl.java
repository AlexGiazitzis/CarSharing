package carsharing.backend.dao.impl;

import carsharing.backend.dao.CustomerDAO;
import carsharing.backend.database.DatabaseInitializer;
import carsharing.backend.entities.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Giazitzis
 */
public class CustomerDAOImpl implements CustomerDAO {
    private final List<Customer>    customers;
    private final PreparedStatement select;
    private final PreparedStatement selectRented;
    private final PreparedStatement insert;
    private final PreparedStatement update;
    private       ResultSet         results;

    public CustomerDAOImpl() throws SQLException {
        customers = new ArrayList<>();
        select = DatabaseInitializer.getDatabaseInit().getConnection().prepareStatement(Action.SELECT.getQuery());
        selectRented = DatabaseInitializer.getDatabaseInit().getConnection().prepareStatement(Action.SELECT_WITH_RENTED.getQuery());
        insert = DatabaseInitializer.getDatabaseInit().getConnection().prepareStatement(Action.INSERT.getQuery());
        update = DatabaseInitializer.getDatabaseInit().getConnection().prepareStatement(Action.UPDATE.getQuery());
        results = select.executeQuery();
    }

    @Override
    public List<Customer> getAllCustomers() throws SQLException {
        customers.clear();
        results = select.executeQuery();

        while (results.next()) {
            customers.add(new Customer(results.getInt(1), results.getString(2), results.getInt(3)));
        }
        return customers;
    }

    @Override
    public boolean addCustomer(final String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            System.out.println("Name can't be null or empty.");
            return false;
        }
        insert.setString(1, name);
        return !insert.execute();
    }

    @Override
    public boolean updateCustomer(final int customerId, final int carId) throws SQLException {
        if (carId == 0) {
            update.setNull(1, Types.INTEGER);
        } else {
            update.setInt(1, carId);
        }
        update.setInt(2, customerId);
        return !update.execute();
    }

    @Override
    public List<Integer> getRentedCarsId() throws SQLException {
        results = selectRented.executeQuery();
        List<Integer> carIds = new ArrayList<>();

        while (results.next()) {
            carIds.add(results.getInt(1));
        }
        return carIds;
    }

    @Override
    public void close() throws SQLException {
        if (results != null) {
            results.close();
        }
        insert.close();
        select.close();
    }

    private enum Action {
        SELECT("SELECT * FROM `customer` ORDER BY `id`;"),
        SELECT_WITH_RENTED("SELECT `rented_car_id` FROM `customer` WHERE `rented_car_id` IS NOT NULL;"),
        INSERT("INSERT INTO `customer` (`name`) VALUES (?);"),
        UPDATE("UPDATE `customer` SET `rented_car_id` = ? WHERE `id` = ? ;");

        private final String query;

        Action(final String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }

}
