package hw20_jpql.core.dao;

import hw20_jpql.core.model.Client;
import hw20_jpql.core.sessionmanager.SessionManager;

import java.util.Optional;


public interface ClientDao {
    Optional<Client> findById(long id);
    //List<Client> findAll();

    long insert(Client client);

    void update(Client client);

    long insertOrUpdate(Client client);

    SessionManager getSessionManager();
}
