package com.libang.jdbc;


import com.sun.org.apache.bcel.internal.util.ClassLoaderRepository;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author libang
 * @date 2018/9/4 11:33
 */
public class ConnectionJdbc {

    private static final String DRIVER="com.mysql.jdbc.Driver";
    private static final String URL="jdbc:mysql:///carservice";
    private static final String NAME="root";
    private static final String PASSWORD="root";

    public static Connection getConnection(){
        Connection connection=null;
        try {
            //加载数据库驱动
            Class.forName(DRIVER);
            //过去数据库连接
            connection = DriverManager.getConnection(URL,NAME,PASSWORD);

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;

    }

}
