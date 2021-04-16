package hw23webserver;

import hw23webserver.core.dao.UserDbDao;
import hw23webserver.core.service.DbServiceUserImpl;
import hw23webserver.core.sessionmanager.SessionManager;
import hw23webserver.hibernate.HibernateUtils;
import hw23webserver.hibernate.dao.UserDbDaoHibernate;
import hw23webserver.hibernate.sessionmanager.SessionManagerHibernate;
import hw23webserver.model.User;
import hw23webserver.server.UsersWebServer;
import hw23webserver.server.UsersWebServerWithFilterBasedSecurity;
import hw23webserver.services.*;
import hw23webserver.core.service.DBServiceUser;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hw23webserver.flyway.MigrationsExecutorFlyway;


/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080


    // Страница администрирования пользователей
    http://localhost:8080/admin


*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
/*
        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        MigrationsExecutorFlyway migration = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migration.cleanDb();
        migration.executeMigrations();
*/
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                new Class[]{User.class});

        SessionManager sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDbDao userDbDao = new UserDbDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDbDao);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(dbServiceUser);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceUser, templateProcessor);

        new InitDbService(dbServiceUser).initDbData(new User("user1", "user1", "11111"));

        usersWebServer.start();
        usersWebServer.join();

    }

}