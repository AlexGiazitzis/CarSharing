package carsharing.backend.dao.impl;

import carsharing.backend.dao.CarDAO;
import carsharing.backend.database.DatabaseInitializer;
import carsharing.backend.entities.Car;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Giazitzis
 */
public final class CarDAOImpl implements CarDAO {
    private final List<Car>         carList;
    private final PreparedStatement selectById;
    private final PreparedStatement selectByCompany;
    private final PreparedStatement insert;
    private       ResultSet         results;

    public CarDAOImpl() throws SQLException {
        carList = new ArrayList<>();
        selectById = DatabaseInitializer.getDatabaseInit().getConnection()
                                        .prepareStatement(Action.SELECT_BY_ID.getQuery());
        selectByCompany = DatabaseInitializer.getDatabaseInit().getConnection()
                                             .prepareStatement(Action.SELECT_BY_COMPANY.getQuery());
        insert = DatabaseInitializer.getDatabaseInit().getConnection().prepareStatement(Action.INSERT.getQuery());
    }

    @Override
    public Car getCarById(final int id) throws SQLException {
        if (id == 0) {
            System.out.println("No car found with the specified ID.");
            return null;
        }
        selectById.setInt(1, id);
        results = selectById.executeQuery();
        results.next();
        return new Car(results.getInt(1), results.getString(2), results.getInt(3));
    }

    @Override
    public List<Car> getAllCarsByCompany(final int companyId) throws SQLException {
        selectByCompany.setInt(1, companyId);
        results = selectByCompany.executeQuery();
        carList.clear();
        while (results.next()) {
            carList.add(new Car(results.getInt(1), results.getString(2), results.getInt(3)));
        }
        return carList;
    }

    @Override
    public boolean addCarToCompany(final String name, final int companyId) throws SQLException {
        insert.setString(1, name);
        insert.setInt(2, companyId);
        return !insert.execute();
    }

    @Override
    public void close() throws SQLException {
        if (results != null) {
            results.close();
        }
        insert.close();
        selectByCompany.close();
    }

    private enum Action {
        SELECT_BY_ID("SELECT * FROM `car` WHERE `id` = ? ;"),
        SELECT_BY_COMPANY("SELECT * FROM `car` WHERE `company_id` = ? ORDER BY `id`;"),
        INSERT("INSERT INTO `car` (`name`, `company_id`) VALUES (? , ?);");

        private final String query;

        Action(final String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }
}
