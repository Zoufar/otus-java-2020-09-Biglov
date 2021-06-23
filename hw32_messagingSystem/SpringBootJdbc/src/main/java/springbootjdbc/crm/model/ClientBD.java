package springbootjdbc.crm.model;

import messagesystem.client.ResultDataType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;


@Table("client")
public class ClientBD extends ResultDataType {

    @Id
    private Long id;

    private String name;

    private String login;

    private String password;

    public ClientBD (String name, String login, String password) {
        this.id = null;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    @PersistenceConstructor
    public ClientBD (Long id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public ClientBD () {
        this.id = null;
        this.name = null;
        this.login = null;
        this.password = null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void copyFrom (ClientBD client){
        this.id = client.id;
        this.login = client.login;
        this.name = client.name;
        this.password = client.password;
    }

    public void nullID (){
        this.id = null;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password +
                '}';
    }
}
