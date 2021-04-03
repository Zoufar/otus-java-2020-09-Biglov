package hw23webserver.core.dao;

import hw23webserver.dao.UserDao;
import hw23webserver.model.User;
import hw23webserver.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;


public interface UserDbDao extends UserDao {

 //   Optional<User> findById(long id);

 //   Optional<User> findRandomUser();

  //  Optional<User> findByLogin(String login);

    List<Optional<User>> findAll();

    long insert(User user);

    void update(User user);

    long insertOrUpdate(User user);

    SessionManager getSessionManager();
}
