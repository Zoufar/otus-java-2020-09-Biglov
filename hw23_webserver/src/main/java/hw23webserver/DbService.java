package hw23webserver;

import hw23webserver.core.dao.UserDbDao;
import hw23webserver.model.User;
import hw23webserver.core.service.DBServiceUser;
import hw23webserver.core.service.DbServiceUserImpl;
import hw23webserver.hibernate.HibernateUtils;
import hw23webserver.hibernate.dao.UserDbDaoHibernate;
import hw23webserver.hibernate.sessionmanager.SessionManagerHibernate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hw23webserver.dao.InMemoryUserDao;

import java.util.Optional;

public class DbService {
    private static final Logger logger = LoggerFactory.getLogger(DbService.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

    String dbUrl = configuration.getProperty("hibernate.connection.url");
    String dbUserName = configuration.getProperty("hibernate.connection.username");
    String dbPassword = configuration.getProperty("hibernate.connection.password");

    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration,
            new Class[]{User.class});


    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    UserDbDao userDbDao = new UserDbDaoHibernate(sessionManager);
    DBServiceUser dbServiceUser = new DbServiceUserImpl(userDbDao);

    public DBServiceUser getDbServiceUser(){
        return dbServiceUser;
    }

    public void setDb(){
        InMemoryUserDao inMemoryUserDao = new InMemoryUserDao();
        for (User user : inMemoryUserDao.getUsers().values()){
            this.dbServiceUser.saveUser(user);
        }
    }

    public void checkDbServiceUser() {

        var userJeoffry = new User("Jeoffry", "user10","101010");

        long id = dbServiceUser.saveUser(userJeoffry);
        Optional<User> mayBeCreatedUserDb = dbServiceUser.getUser(id);
        mayBeCreatedUserDb.ifPresentOrElse((client) -> outputUser("Created user", client),
                () -> logger.info("User not found"));

        Optional<User> mayBeCreatedUserDb1 = dbServiceUser.getUserByLogin("user7");
            mayBeCreatedUserDb1.ifPresentOrElse((user) -> outputUser("Created user got by login", user),
                    () -> logger.info("User login not found in DB"));

        Optional<User> mayBeCreatedUserDb2 = dbServiceUser.getRandomUser();
        mayBeCreatedUserDb2.ifPresentOrElse((user) -> outputUser("Created random user", user),
                    () -> logger.info("Random User not found in DB"));

     }

    private static void outputUser(String header, User user) {
        logger.info("-----------------------------------------------------------");
        logger.info(header);
        logger.info("user: {}", user);
    }


}
