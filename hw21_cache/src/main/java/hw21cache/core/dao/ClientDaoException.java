package hw21cache.core.dao;

public class ClientDaoException extends RuntimeException {
    public ClientDaoException (Exception ex) {
        super(ex);
    }
}
