package hw18_jdbc.core.dao;

public class ClientDaoException extends RuntimeException {
    public ClientDaoException (Exception ex) {
        super(ex);
    }
}
