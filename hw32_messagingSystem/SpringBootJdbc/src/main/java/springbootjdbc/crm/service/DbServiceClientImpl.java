package springbootjdbc.crm.service;

import springbootjdbc.crm.repository.ClientBDRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import springbootjdbc.crm.model.ClientBD;
import springbootjdbc.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionManager transactionManager;
    private final ClientBDRepository clientBDRepository;

    public DbServiceClientImpl(TransactionManager transactionManager, ClientBDRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientBDRepository = clientRepository;
    }

    @Override
    public ClientBD saveClient(ClientBD client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientBDRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<ClientBD> getClient(long id) {
        var clientOptional = clientBDRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<ClientBD> findAll() {
        List<ClientBD> clientList = new ArrayList<>();
        clientBDRepository.findAll().forEach(clientList::add);
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
