package hw18_jdbc.core.service;

public interface DBServiceMapper <T> {

        void saveObject (T objectData);

        T getObject (Object id, Class<T> clazz);

    }
