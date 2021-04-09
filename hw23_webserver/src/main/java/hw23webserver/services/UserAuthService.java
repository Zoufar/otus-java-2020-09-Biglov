package hw23webserver.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
