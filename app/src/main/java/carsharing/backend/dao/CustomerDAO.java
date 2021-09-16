package carsharing.backend.dao;

import carsharing.backend.entities.Customer;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Alex Giazitzis
 */
public interface CustomerDAO extends AutoCloseable {

    List<Customer> getAllCustomers() throws SQLException;

    boolean addCustomer(final String name) throws SQLException;

    boolean updateCustomer(final int customerId, final int carId) throws SQLException;

    List<Integer> getRentedCarsId() throws SQLException;

    @Override
    void close() throws SQLException;

}
