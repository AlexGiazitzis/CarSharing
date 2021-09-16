package carsharing.backend.entities;

import java.util.Objects;

/**
 * @author Alex Giazitzis
 */
public class Customer {
    private final int id;
    private final String name;
    private final int rentedCarId;

    public Customer(final int id, final String name, final int rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRentedCarId() {
        return rentedCarId;
    }



    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && rentedCarId == customer.rentedCarId && Objects.equals(name, customer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rentedCarId);
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}
