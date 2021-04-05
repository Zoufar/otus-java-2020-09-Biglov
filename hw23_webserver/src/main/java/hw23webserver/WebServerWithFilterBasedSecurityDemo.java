package hw23webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hw23webserver.core.dao.UserDbDao;
import hw23webserver.core.service.DbServiceUserImpl;
import hw23webserver.hibernate.HibernateUtils;
import hw23webserver.hibernate.dao.UserDbDaoHibernate;
import hw23webserver.hibernate.sessionmanager.SessionManagerHibernate;
import hw23webserver.model.User;
import hw23webserver.server.UsersWebServer;
import hw23webserver.server.UsersWebServerWithFilterBasedSecurity;
import hw23webserver.services.TemplateProcessor;
import hw23webserver.services.TemplateProcessorImpl;
import hw23webserver.services.UserAuthService;
import hw23webserver.services.UserAuthServiceImpl;
import hw23webserver.core.service.DBServiceUser;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hw23webserver.flyway.MigrationsExecutorFlyway;

import java.util.HashMap;
import java.util.Map;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // Страница администрирования пользователей
    http://localhost:8080/admin

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                new Class[]{User.class});

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDbDao userDbDao = new UserDbDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDbDao);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(dbServiceUser);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceUser, gson, templateProcessor);

        fillDbInitially(dbServiceUser);

        usersWebServer.start();
        usersWebServer.join();

    }

    public static void fillDbInitially(DBServiceUser dbServiceUser) {
        Map<Long, User> users = new HashMap<>();
        users.put(1L, new User(1L, "Крис Гир", "user1", "11111"));
        users.put(2L, new User(2L, "Ая Кэш", "user2", "11111"));
        users.put(3L, new User(3L, "Десмин Боргес", "user3", "11111"));
        users.put(4L, new User(4L, "Кетер Донохью", "user4", "11111"));
        users.put(5L, new User(5L, "Стивен Шнайдер", "user5", "11111"));
        users.put(6L, new User(6L, "Джанет Вэрни", "user6", "11111"));
        users.put(7L, new User(7L, "Брэндон Смит", "user7", "11111"));
        for (User user : users.values()) {
            dbServiceUser.saveUser(user);
        }
    }
}