package hw28W_springDataJdbc;

import hw28W_springDataJdbc.crm.repository.ClientBDRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import hw28W_springDataJdbc.crm.model.ClientBD;
import hw28W_springDataJdbc.crm.service.DBServiceClient;


@Component("initBD")

public class InitBD {

    private static final Logger log = LoggerFactory.getLogger(InitBD.class);

    private final ClientBDRepository clientBDRepository;
    private final DBServiceClient dbServiceClient;

    public InitBD (ClientBDRepository clientBDRepository, DBServiceClient dbServiceClient) {
        this.clientBDRepository = clientBDRepository;
        this.dbServiceClient = dbServiceClient;
    }

    void action() {

        dbServiceClient.saveClient(new ClientBD("Client1",  "login1", "11111"));
        dbServiceClient.saveClient(new ClientBD("Client2",  "login2", "22222"));
        dbServiceClient.saveClient(new ClientBD("Client3",  "login3", "33333"));

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));

    }

}
