package hw21cache.jdbc.dao;


import hw21cache.core.dao.ClientDao;
import hw21cache.core.dao.ClientDaoException;
import hw21cache.core.model.Client;
import hw21cache.core.sessionmanager.SessionManager;
import hw21cache.jdbc.DbExecutor;
import hw21cache.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

// этот класс не должен быть в домашней работе
public class ClientDaoJdbc implements ClientDao {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<Client> dbExecutor;

    public ClientDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor <Client> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<Client> findById(long id) {
        try {
            return dbExecutor.executeSelect(getConnection(), "select id, name from client where id  = ?",
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                return new Client(rs.getLong("id"), rs.getString("name"));
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insert(Client client) {
        try {
            return dbExecutor.executeInsert(getConnection(), "insert into client(name) values (?)",
                    Collections.singletonList(client.getName()));
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
