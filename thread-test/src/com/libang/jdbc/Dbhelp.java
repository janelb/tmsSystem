package com.libang.jdbc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author libang
 * @date 2018/9/4 12:43
 */
public class Dbhelp {

    private static QueryRunner queryRunner = new QueryRunner();

    public static  void update(String sql,Object... params){
        Connection connection = ConnectionJdbc.getConnection();
        try {
            queryRunner.update(connection,sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static <T> T query(String sql, ResultSetHandler<T> rsh,Object... params){
            Connection connection = ConnectionJdbc.getConnection();
        try {
            queryRunner.query(connection,sql,rsh,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
