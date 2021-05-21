package hw25warMvc.hibernate.dao;

import hw25warMvc.core.dao.UserDbDao;
import hw25warMvc.core.dao.UserDbDaoException;
import hw25warMvc.core.sessionmanager.DatabaseSession;
import hw25warMvc.model.User;
import hw25warMvc.core.sessionmanager.SessionManager;
import hw25warMvc.hibernate.sessionmanager.DatabaseSessionHibernate;
import hw25warMvc.hibernate.sessionmanager.SessionManagerHibernate;

import org.hibernate.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class UserDbDaoHibernate implements UserDbDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDbDaoHibernate.class);

    private final SessionManager sessionManager;

    public UserDbDaoHibernate(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getSession().find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<User> findRandomUser(){
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            var query = currentSession.getSession().
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
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            var query = currentSession.getSession().
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
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            throw new UserDbDaoException(e);
        }
    }

    @Override
    public void update(User user) {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            throw new UserDbDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(User user) {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getSession();
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

    public List<User> findAll() {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            var query = currentSession.getSession().
                    createQuery("select u from User u", User.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<User>();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
