package hw18_jdbc.jdbc;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbExecutorImpl<T> implements DbExecutor<T> {

    @Override
    public long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        int keyStatement;
        if(params.size() > 1){
            keyStatement = Statement.NO_GENERATED_KEYS;}
        else {keyStatement = Statement.RETURN_GENERATED_KEYS;}
        try (var pst = connection.prepareStatement(sql, keyStatement)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (!(params.size() > 1)){
                rs.next();
                return rs.getInt(1);
            }
            return 0;}
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            throw ex;
        }
    }

    @Override
    public void executeUpdate(Connection connection, String sql, Object Id, List<Object> params) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (var pst = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS)) {
            for (int idx = 0; idx < params.size()-1; idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.setObject(params.size()+1, Id);
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
            }
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            throw ex;
        }
    }

    @Override
    public Optional<T> executeSelect(Connection connection, String sql, Object id,
                                     Function<ResultSet, T> rsHandler) throws SQLException {
        try (var pst = connection.prepareStatement(sql)) {
            pst.setObject(1, id);
            try (var rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

}
