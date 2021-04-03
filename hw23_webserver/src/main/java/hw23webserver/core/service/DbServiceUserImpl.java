package hw23webserver.core.service;

import hw23webserver.core.dao.UserDbDao;
import hw23webserver.model.User;
import hw23webserver.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDbDao userDbDao;

    public DbServiceUserImpl(UserDbDao userDbDao) {
        this.userDbDao = userDbDao;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDbDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = userDbDao.insertOrUpdate(user);
                sessionManager.commitSession();

                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }


    @Override
    public Optional<User> getUser(long id) {
        try (SessionManager sessionManager = userDbDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDbDao.findById(id);

                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserByLogin (String login) {
        try (SessionManager sessionManager = userDbDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDbDao.findByLogin(login);
                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getRandomUser () {
        try (SessionManager sessionManager = userDbDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDbDao.findRandomUser();
                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Optional<User>> getAllUsers () {
        try (SessionManager sessionManager = userDbDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                List<Optional<User>> usersOptional = userDbDao.findAll();
                return usersOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Collections.singletonList(Optional.empty());
        }
    }
}
