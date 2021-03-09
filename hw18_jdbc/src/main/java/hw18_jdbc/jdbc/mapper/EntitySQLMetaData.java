package hw18_jdbc.jdbc.mapper;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData <T> {
    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();

}
