package hw20_jpql.core.dao;

public class ClientDaoException extends RuntimeException {
    public ClientDaoException(Exception ex) {
        super(ex);
    }
}
