package hw23webserver.core.service;

import hw23webserver.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    Optional<User> getUserByLogin (String login);

    Optional<User> getRandomUser ();

    List<Optional<User>> getAllUsers ();

}
