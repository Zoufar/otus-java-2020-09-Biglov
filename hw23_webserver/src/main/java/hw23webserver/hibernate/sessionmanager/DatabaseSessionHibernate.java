package hw23webserver.hibernate.sessionmanager;

import hw23webserver.core.sessionmanager.DatabaseSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSessionHibernate implements DatabaseSession {
    private final Session session;
    private final Transaction transaction;

    DatabaseSessionHibernate(Session session) {
        this.session = session;
        this.transaction = session.beginTransaction();
    }

    @Override
    public Session getSession() {
        return session;
    }


    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void close() {
        if (transaction.isActive()) {
            transaction.commit();
        }
        session.close();
    }
}
