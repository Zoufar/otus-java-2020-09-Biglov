package springbootjdbc.crm.service;

import springbootjdbc.crm.model.ClientBD;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    ClientBD saveClient(ClientBD client);

    Optional<ClientBD> getClient(long id);

    List<ClientBD> findAll();
}
