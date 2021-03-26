package hw20_jpql;

import hw20_jpql.core.model.AddressDataSet;
import hw20_jpql.core.model.PhoneDataSet;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import hw20_jpql.core.dao.ClientDao;
import hw20_jpql.core.model.Client;
import hw20_jpql.core.service.DBServiceClient;
import hw20_jpql.core.service.DbServiceClientImpl;
import hw20_jpql.flyway.MigrationsExecutorFlyway;
import hw20_jpql.hibernate.HibernateUtils;
import hw20_jpql.hibernate.dao.ClientDaoHibernate;
import hw20_jpql.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.Optional;

public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";


    public static void main(String[] args) {

        var dbServiceDemo = new DbServiceDemo();

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        // Все главное см в тестах
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                           new Class[]{Client.class, AddressDataSet.class, PhoneDataSet.class});


        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(clientDao);

        var clientJeoffry = new Client("Jeoffry", 42);
        var listPhonesJeoffry = dbServiceDemo.createPhoneDataSet (clientJeoffry, 1, 3);
        clientJeoffry.setAddress(new AddressDataSet("Lenin street"));
        clientJeoffry.setPhones(listPhonesJeoffry);
        long id = dbServiceClient.saveClient(clientJeoffry);
        Optional<Client> mayBeCreatedClient = dbServiceClient.getClient(id);
        mayBeCreatedClient.ifPresentOrElse((client) -> outputClient("Created client", client),
                () -> logger.info("Client not found"));

    /*   id = dbServiceClient.saveClient(new Client(1L, "Look it aint Jeo!"));
        Optional<Client> mayBeUpdatedClient = dbServiceClient.getClient(id);
        mayBeUpdatedClient.ifPresentOrElse((client) -> outputClient("Updated client", client),
                () -> logger.info("Client not found"));
    */

    }

    private static void outputClient(String header, Client client) {
        logger.info("-----------------------------------------------------------");
        logger.info(header);
        logger.info("client:{}", client);
    }


    private ArrayList<PhoneDataSet> createPhoneDataSet (Client client, int begin, int end) {

        var listPhone = new ArrayList<PhoneDataSet>();
        for (int idx = begin; idx <= end; idx++) {
            listPhone.add(new PhoneDataSet("+"+ client.getName()+"_" + idx, client));
        }
        return listPhone;
    }


}
