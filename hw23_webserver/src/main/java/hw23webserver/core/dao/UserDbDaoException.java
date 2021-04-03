package hw23webserver.core.dao;

public class UserDbDaoException extends RuntimeException {
    public UserDbDaoException(Exception ex) {
        super(ex);
    }
}
