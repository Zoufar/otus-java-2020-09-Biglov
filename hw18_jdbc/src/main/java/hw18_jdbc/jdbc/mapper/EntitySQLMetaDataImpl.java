package hw18_jdbc.jdbc.mapper;

import java.util.Arrays;

public class EntitySQLMetaDataImpl <T> implements EntitySQLMetaData {
    private final StringBuilder sqlString = new StringBuilder();
    private final Class clazz;
    private final EntityClassMetaData <T> clazzMetaData;

    public EntitySQLMetaDataImpl (Class<T> clazz){
        this.clazz = clazz;
        this.clazzMetaData = new EntityClassMetaDataImpl<>(clazz);
    }


    @Override
    public String getSelectAllSql() {
        sqlString.delete(0,sqlString.length());
        sqlString.append("SELECT * FROM ").append(clazzMetaData.getName().toLowerCase());
        return sqlString.toString();
//        "select * from client"
    }

    @Override
    public String getSelectByIdSql() {
        sqlString.delete(0,sqlString.length());
        sqlString.append("SELECT");
        clazzMetaData.getAllFields().stream().
                map(fld -> fld.getName().toLowerCase()).forEach(str -> sqlString.append(" "+str+","));
        sqlString.replace(sqlString.lastIndexOf(","), sqlString.lastIndexOf(",") + 1, " FROM ");
        sqlString.append(clazzMetaData.getName().toLowerCase()).append(" WHERE ").
                append(clazzMetaData.getIdField().getName().toLowerCase()).append(" = ?");
        return sqlString.toString();
//        "select id, name, age from client where id  = ?"
    }

    @Override
    public String getInsertSql() {
        sqlString.delete(0,sqlString.length());
        sqlString.append("INSERT INTO ").append(clazzMetaData.getName().toLowerCase()+"(");
        clazzMetaData.getAllFields().stream().
                map(fld -> fld.getName().toLowerCase()).forEach(str -> sqlString.append(str+","));
        sqlString.replace(sqlString.lastIndexOf(","), sqlString.lastIndexOf(",") + 1, ") VALUES (");
        Arrays.asList(new String[clazzMetaData.getAllFields().size()]).stream().
                forEach(str -> sqlString.append("?,"));
        sqlString.replace(sqlString.lastIndexOf(","), sqlString.lastIndexOf(",") + 1, ")");
        return sqlString.toString();

//        "insert into client(id,name,age) values (?,?,?)"
    }

    @Override
    public String getUpdateSql() {
        sqlString.delete(0,sqlString.length());
        sqlString.append("UPDATE ");
        sqlString.append(clazzMetaData.getName().toLowerCase()).append(" SET");
        clazzMetaData.getFieldsWithoutId().stream().
                map(fld -> fld.getName().toLowerCase()).forEach(str -> sqlString.append(" "+str+"=?,"));
        sqlString.replace(sqlString.lastIndexOf(","), sqlString.lastIndexOf(",") + 1, " WHERE ");
        sqlString.append(clazzMetaData.getIdField().getName().toLowerCase()).append(" = ?");
        return sqlString.toString();

//        "update client set name=?, age=? where id=?"
    }
}
