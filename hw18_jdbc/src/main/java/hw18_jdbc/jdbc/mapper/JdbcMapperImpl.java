package hw18_jdbc.jdbc.mapper;

import hw18_jdbc.core.sessionmanager.SessionManager;
import hw18_jdbc.jdbc.DbExecutor;
import hw18_jdbc.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class JdbcMapperImpl <T> implements JdbcMapper {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor <T> dbExecutor;
    private EntityClassMetaData <T> entityClassMetaData;
    private EntitySQLMetaData <T> sqlMetaData;


    public JdbcMapperImpl (SessionManagerJdbc sessionManager, DbExecutor <T> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
     public void insert (Object objectData){
        try {
            sqlMetaData = new EntitySQLMetaDataImpl<>(objectData.getClass());
            entityClassMetaData = new EntityClassMetaDataImpl<>(objectData.getClass());

           List<Object> params = new ArrayList<>(entityClassMetaData.getAllFields().stream().
                           map(field -> { field.setAccessible(true);
                               Object obj = null;
                               try{obj = field.get(objectData);}
                               catch (IllegalAccessException e){logger.error(e.getMessage(), e);}
                                return obj;})
                            .collect(Collectors.toList()));
            logger.info("params for insert: {}", params);

            dbExecutor.executeInsert(getConnection(), sqlMetaData.getInsertSql(), params);
        } catch (Exception e) { logger.error(e.getMessage(), e);}
     }


    @Override
    public void update(Object objectData) {
        try {
            sqlMetaData = new EntitySQLMetaDataImpl<>(objectData.getClass());
            entityClassMetaData = new EntityClassMetaDataImpl<>(objectData.getClass());
            Field fieldId = entityClassMetaData.getIdField();
                fieldId.setAccessible(true);
            dbExecutor.executeUpdate(getConnection(), sqlMetaData.getUpdateSql(),
                    fieldId.get(objectData),
                    entityClassMetaData.getFieldsWithoutId().stream().
                            map(field -> { field.setAccessible(true);
                                Object obj = null;
                                try { obj = field.get(objectData);}
                                catch (IllegalAccessException e){logger.error(e.getMessage(), e);}
                                           return obj;}).
                            collect(Collectors.toList()));
        }

        catch (Exception e) {
            throw new JdbcMapperException(e);
        }
    }

    @Override
    public void insertOrUpdate (Object objectData) {
        Class<T> clazz = (Class<T>) objectData.getClass();
        if(findById(objectData, clazz) ==null){insert(objectData);}
        update(objectData);
    }

    @Override
    public T findById(Object id, Class clazz) {
        sqlMetaData = new EntitySQLMetaDataImpl<>(clazz);
        var clazzMetaData = new EntityClassMetaDataImpl<T>(clazz);
        try {
            Optional<T> dbExResult = dbExecutor.executeSelect(getConnection(),
                    sqlMetaData.getSelectByIdSql(), id,
                    rs -> {
                          try {
                              if (rs.next()) {
                                  try {
                                      Object[] args = clazzMetaData.getAllFields().stream()
                                              .map(field -> {
                                                  Object obj = null;
                                                  try {
                                                      obj = rs.getObject(field.getName());
                                                  } catch (SQLException e) {
                                                      logger.error(e.getMessage(), e);
                                                  }
                                                  return obj;
                                              })
                                              .toArray(Object[]::new);
                                      T entity = (T) clazzMetaData.getConstructor().newInstance(args);
                                      return entity;
                                  } catch (InstantiationException ie) {
                                      logger.error(ie.getMessage(), ie);}
                                     catch (IllegalAccessException ie) {
                                          logger.error(ie.getMessage(), ie);}
                                     catch (InvocationTargetException ie) {
                                             logger.error(ie.getMessage(), ie); }
                              }
                          } catch (SQLException e) { logger.error(e.getMessage(), e);}
                        return null;
                    });
            if(dbExResult.isPresent()) {return dbExResult.get();}
            return null;
            } catch (Exception e) { logger.error(e.getMessage(), e);}
        return null;
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
