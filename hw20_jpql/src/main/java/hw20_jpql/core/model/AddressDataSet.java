package hw20_jpql.core.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tAddressDataSet")
public class AddressDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;


    @Column(name = "address")
    private String street;

    public AddressDataSet (){
    }

    public AddressDataSet (String street) {
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDataSet addressDataSet = (AddressDataSet) o;
        return Objects.equals(id, addressDataSet.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
