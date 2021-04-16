package hw25warMvc.core.service;

import hw25warMvc.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    Optional<User> getUserByLogin (String login);

    Optional<User> getRandomUser ();

    List<User> getAllUsers ();

}
