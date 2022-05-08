package com.lw.h2;

import com.lw.excel.ExpressOrderExcelRead;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class H2Connection {
    public static Logger logger = LoggerFactory.getLogger(ExpressOrderExcelRead.class);
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
            logger.error("获取链接异常", e);
        }
        return null;
    }
}
