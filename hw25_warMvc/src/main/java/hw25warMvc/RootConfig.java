package hw25warMvc;

import hw25warMvc.core.dao.UserDbDao;
import hw25warMvc.core.service.DbServiceUserImpl;
import hw25warMvc.core.sessionmanager.SessionManager;
import hw25warMvc.flyway.MigrationsExecutorFlyway;
import hw25warMvc.hibernate.HibernateUtils;
import hw25warMvc.hibernate.dao.UserDbDaoHibernate;
import hw25warMvc.hibernate.sessionmanager.SessionManagerHibernate;
import hw25warMvc.model.User;
import hw25warMvc.core.service.DBServiceUser;
import hw25warMvc.services.InitDbService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
// import org.springframework.context.annotation.Configuration;

@org.springframework.context.annotation.Configuration
public class RootConfig {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    @Bean
    public Configuration configuration() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return new  Configuration().configure(HIBERNATE_CFG_FILE);
    }

    @Bean ( initMethod = "executeMigrations")
    public MigrationsExecutorFlyway migration (Configuration configuration) {
        MigrationsExecutorFlyway migration = new MigrationsExecutorFlyway(configuration);
        migration.cleanDb();
        return migration;
    }

    @Bean
    @DependsOn ("migration")
    public SessionFactory sessionFactory ( Configuration configuration ) {
        return HibernateUtils.buildSessionFactory(configuration, new Class<?>[]{User.class});
    }

    @Bean
    public SessionManager sessionManager ( SessionFactory sessionFactory) {
        return new SessionManagerHibernate(sessionFactory);
    }

    @Bean
    public UserDbDao userDbDao (SessionManager sessionManager) {
        return new UserDbDaoHibernate(sessionManager);
    }

    @Bean
    public DBServiceUser dbServiceUser (UserDbDao userDbDao) {
        return new DbServiceUserImpl(userDbDao);
    }

    @Bean ( initMethod = "initDbUsers")
    public InitDbService initDbService (DBServiceUser dbServiceUser) {
        return new InitDbService(dbServiceUser);
    }
}