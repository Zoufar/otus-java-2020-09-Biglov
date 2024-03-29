package hw23webserver.core.sessionmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface DatabaseSession {

    Session getSession();

    Transaction getTransaction();

    void close();
}
