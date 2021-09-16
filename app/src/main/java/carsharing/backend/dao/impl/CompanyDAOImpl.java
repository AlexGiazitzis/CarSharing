package carsharing.backend.dao.impl;

import carsharing.backend.dao.CompanyDAO;
import carsharing.backend.database.DatabaseInitializer;
import carsharing.backend.entities.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Giazitzis
 */
public final class CompanyDAOImpl implements CompanyDAO, AutoCloseable {
    private final List<Company>     companies;
    private final PreparedStatement selectById;
    private final PreparedStatement select;
    private final PreparedStatement insert;
    private       ResultSet         set;

    public CompanyDAOImpl() throws SQLException {
        this.companies = new ArrayList<>();
        this.selectById = DatabaseInitializer.getDatabaseInit().getConnection()
                                             .prepareStatement(Action.SELECT_BY_ID.getQuery());
        this.select = DatabaseInitializer.getDatabaseInit().getConnection().prepareStatement(Action.SELECT.getQuery());
        this.insert = DatabaseInitializer.getDatabaseInit().getConnection().prepareStatement(Action.INSERT.getQuery());
        this.set = select.executeQuery();
    }

    @Override
    public Company getCompanyById(final int id) throws SQLException {
        selectById.setInt(1, id);
        set = selectById.executeQuery();
        set.next();
        return new Company(set.getInt(1), set.getString(2));
    }

    @Override
    public List<Company> getAllCompanies() throws SQLException {

        set = select.executeQuery();
        set.beforeFirst();
        companies.clear();
        while (set.next()) {
            companies.add(new Company(set.getInt(1), set.getString(2)));
        }
        return companies;
    }

    @Override
    public boolean addCompany(final String name) throws SQLException {
        insert.setString(1, name);
        return insert.executeUpdate() == 1;
    }

    @Override
    public void close() throws SQLException {
        set.close();
        insert.close();
        select.close();
    }

    private enum Action {
        SELECT("SELECT * FROM `company` ORDER BY `id`;"),
        SELECT_BY_ID("SELECT * FROM `company` WHERE `id` = ? ;"),
        INSERT("INSERT INTO `company` (`name`) VALUES (?);");

        private final String query;

        Action(final String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }
}
