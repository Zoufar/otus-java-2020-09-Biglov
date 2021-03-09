package hw18_jdbc.core.service;

import hw18_jdbc.jdbc.mapper.EntityClassMetaDataImpl;
import hw18_jdbc.jdbc.mapper.JdbcMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DBServiceMapperImpl <T> implements DBServiceMapper {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceMapperImpl.class);

    private final JdbcMapper jdbcMapper;

    public DBServiceMapperImpl (JdbcMapper jdbcMapper) {
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public void saveObject (Object objectData) {
        var clazzMetaData = new EntityClassMetaDataImpl<>(objectData.getClass());
        try (var sessionManager = jdbcMapper.getSessionManager()) {
            sessionManager.beginSession();
            try {
                jdbcMapper.insert(objectData);
                sessionManager.commitSession();
                String objIdString = clazzMetaData.getIdField().toString();
                logger.info("created object with Id: {}", objIdString);
                return;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }

    }

    @Override
    public T getObject(Object id, Class clazz) {
        try (var sessionManager = jdbcMapper.getSessionManager()) {
            sessionManager.beginSession();
            try {
                T objData = (T) jdbcMapper.findById(id, clazz);

                logger.info("object: {}", objData.toString());
                return objData;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return null;
        }
    }

}
