package hw20_jpql.core.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tPhoneDataSet")
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "phone_number", nullable = false)
    private String number;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public PhoneDataSet() {
    }

    public PhoneDataSet (String number, Client client) {
        this.number = number;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getPerson() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", personId=" + client.getId() +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDataSet phone = (PhoneDataSet) o;
        return Objects.equals(id, phone.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
