package com.lw.dao;

import com.lw.dto.ExpressOrder;
import com.lw.h2.H2Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ExpressOrderDao {
    public static Logger logger = LoggerFactory.getLogger(ExpressOrderDao.class);
    private static Connection conn = H2Connection.getInstance();
    private static String insert = "INSERT INTO `express_order`(`id`, `express_no`, `order_date`, `customer_no`, `province`, `city`, `weight`, `fee`, `check_cost`, `deviation`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static String create = "CREATE TABLE IF NOT EXISTS `express_order`(" +
            "`id` bigint(20) unsigned NOT NULL," +
            "`express_no` varchar(30) DEFAULT NULL,"+
            "`order_date` varchar(30) DEFAULT NULL,"+
            "`customer_no` varchar(15) DEFAULT NULL,"+
            "`province` varchar(30) DEFAULT NULL,"+
            "`city` varchar(30) DEFAULT NULL,"+
            "`weight` DECIMAL(20,4) DEFAULT '0.0000',"+
            "`fee` DECIMAL(20,4) DEFAULT '0.0000',"+
            "`check_cost` DECIMAL(20,4) NULL DEFAULT NULL,"+
            "`deviation` DECIMAL(20,4) NULL DEFAULT NULL,"+
            "PRIMARY KEY (`id`),"+
            "INDEX `order_express_no_key`(`express_no`) using BTREE"+
            ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;";

    public static void batchInsert(ArrayList<ExpressOrder> orders) {
        logger.info("------------准备 插入数据库【快递公司】快递单{}条.------------", orders.size());
        try {
            PreparedStatement pstm = conn.prepareStatement(insert);
            int canInsertCount = 0;
            for(int i=0; i<orders.size(); i++) {
                ExpressOrder expressOrder = orders.get(i);
                if(expressOrder.getId() == null) {
                    break;
                }
                setPrepareParams(pstm, expressOrder);
                pstm.addBatch();
                canInsertCount++;
            }
            pstm.executeBatch();
            pstm.clearBatch();
            logger.info("------------成功 插入数据库【快递公司】快递单{}条.------------", canInsertCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable() {
        try {
            Statement statement = conn.createStatement();
            statement.execute(create);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setPrepareParams(PreparedStatement pstm, ExpressOrder expressOrder) throws Exception{
        pstm.setInt(1, expressOrder.getId());
        pstm.setString(2, expressOrder.getExpressNo());
        pstm.setString(3, expressOrder.getOrderDate());
        pstm.setString(4, expressOrder.getCustomerNo());
        pstm.setString(5, expressOrder.getProvince());
        pstm.setString(6, expressOrder.getCity());
        pstm.setBigDecimal(7, expressOrder.getWeight());
        pstm.setBigDecimal(8, expressOrder.getFee());
        pstm.setBigDecimal(9, expressOrder.getCheckCost());
        pstm.setBigDecimal(10, expressOrder.getDeviation());
    }
}
