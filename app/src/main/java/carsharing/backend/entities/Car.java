package carsharing.backend.entities;

import java.util.Objects;

/**
 * @author Alex Giazitzis
 */
public class Car {
    private final int id;
    private final String name;
    private final int companyId;

    public Car(final int id, final String name, final int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && companyId == car.companyId && Objects.equals(name, car.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, companyId);
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}
