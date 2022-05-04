package com.lw.h2;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;

public class H2Connection {

    private static JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:mem:test;MODE=MYSQL;", "sa", "");
    static{
        try {
            Class.forName("org.h2.Driver");
            pool.setMaxConnections(20);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        try {
            return pool.getConnection();
        } catch (Exception e) {
            System.out.println("获取链接异常");
        }
        return null;
    }
}
