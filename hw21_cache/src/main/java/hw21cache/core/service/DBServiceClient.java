package hw21cache.core.service;

import hw21cache.core.model.Client;

import java.util.Optional;

public interface DBServiceClient {

    long saveClient(Client client);

    Optional<Client> getClient(long id);

    //List<Client> findAll();
}
