package hw21cache.jdbc.mapper;

import hw21cache.core.sessionmanager.SessionManager;

/**
 * Сохратяет объект в базу, читает объект из базы
 * @param <T>
 */
public interface JdbcMapper<T> {
    void insert(T objectData);

    void update(T objectData);

    void insertOrUpdate(T objectData);

    T findById(Object id, Class<T> clazz);

    SessionManager getSessionManager();

//    List<T> findAll(Class<T> clazz);
}
