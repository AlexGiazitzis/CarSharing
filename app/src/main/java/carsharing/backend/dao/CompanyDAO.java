package carsharing.backend.dao;

import carsharing.backend.entities.Company;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Alex Giazitzis
 */
public interface CompanyDAO extends AutoCloseable{

    Company getCompanyById(final int id) throws SQLException;

    List<Company> getAllCompanies() throws SQLException;

    boolean addCompany(final String name) throws SQLException;

    @Override
    void close() throws SQLException;
}

