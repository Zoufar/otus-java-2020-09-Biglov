package hw23webserver.hibernate.dao;

import hw23webserver.core.dao.UserDbDao;
import hw23webserver.core.dao.UserDbDaoException;
import hw23webserver.model.User;
import hw23webserver.core.sessionmanager.SessionManager;
import hw23webserver.hibernate.sessionmanager.DatabaseSessionHibernate;
import hw23webserver.hibernate.sessionmanager.SessionManagerHibernate;

import org.hibernate.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class UserDbDaoHibernate implements UserDbDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDbDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDbDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<User> findRandomUser(){
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            var query = currentSession.getHibernateSession().
                    createQuery("select u from User u", User.class);
            List <User> users = query.getResultList();
        Random r = new Random();
        return users.stream().skip(r.nextInt(users.size() - 1)).findFirst();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<User> findByLogin(String login){
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            var query = currentSession.getHibernateSession().
                    createQuery("select u from User u where u.login = :login", User.class);
            query.setParameter("login", login);
            List <User> users = query.getResultList();
            return users.stream().filter(v -> v.getLogin().equals(login)).findFirst();
            } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insert(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            throw new UserDbDaoException(e);
        }
    }

    @Override
    public void update(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            throw new UserDbDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
            return user.getId();
        } catch (Exception e) {
            throw new UserDbDaoException(e);
        }
    }

    public List<Optional<User>> findAll() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            var query = currentSession.getHibernateSession().
                    createQuery("select u from User u", User.class);
            List <User> users = query.getResultList();

            return users.stream().map(usr -> Optional.ofNullable(usr)).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.singletonList(Optional.empty());
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
