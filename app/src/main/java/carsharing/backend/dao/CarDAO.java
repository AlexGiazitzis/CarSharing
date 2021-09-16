package carsharing.backend.dao;

import carsharing.backend.entities.Car;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Alex Giazitzis
 */
public interface CarDAO extends AutoCloseable {

    Car getCarById(final int id) throws SQLException;

    List<Car> getAllCarsByCompany(final int companyId) throws SQLException;

    boolean addCarToCompany(final String name, final int companyId) throws SQLException;

    @Override
    void close() throws SQLException;
}
