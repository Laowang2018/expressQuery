package com.lw.dao;

import com.lw.dto.ExpressOrder;
import com.lw.dto.SellOrder;
import com.lw.h2.H2Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SellOrderDao {
    private static Connection conn = H2Connection.getInstance();
    private static String create = "CREATE TABLE IF NOT EXISTS `sell_order`  (" +
            "  `id` bigint(25) NOT NULL," +
            "  `status` varchar(30) DEFAULT NULL," +
            "  `order_no` varchar(64) DEFAULT NULL," +
            "  `goods_nos` varchar(512) DEFAULT NULL," +
            "  `goods_names` varchar(1024) DEFAULT NULL," +
            "  `count` int(8) NULL DEFAULT NULL," +
            "  `outbound_status` varchar(18) DEFAULT NULL," +
            "  `express_company` varchar(30) DEFAULT NULL," +
            "  `express_no` varchar(30) DEFAULT NULL," +
            "  `express_cost` DECIMAL(20, 4) DEFAULT NULL," +
            "  `approximate_weight` DECIMAL(20, 4) DEFAULT NULL," +
            "  `province` varchar(10) DEFAULT NULL," +
            "  `city` varchar(18) DEFAULT NULL," +
            "  `district` varchar(18) DEFAULT NULL," +
            "  `order_time` varchar(30) NULL DEFAULT NULL," +
            "  PRIMARY KEY (`id`) ," +
            "  INDEX `express_express_no_key`(`express_no`) USING BTREE " +
            ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact";
    private static String insert = "INSERT INTO `sell_order`(`id`, `status`, `order_no`, `goods_nos`, `goods_names`, `count`, `outbound_status`, `express_company`, `express_no`, `express_cost`, `approximate_weight`, `province`, `city`, `district`, `order_time`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static void batchInsert(ArrayList<SellOrder> sellOrders) {
        System.out.println("All parsed sell orders count:" + sellOrders.size());
        try {
            PreparedStatement pstm = conn.prepareStatement(insert);
            int canInsertCount = 0;
            for(int i=0; i<sellOrders.size(); i++) {
                SellOrder sellOrder = sellOrders.get(i);
                if(sellOrder.getId() == null) {
                    break;
                }
                setPrepareParams(pstm, sellOrder);
                pstm.addBatch();
                canInsertCount++;
            }
            pstm.executeBatch();
            pstm.clearBatch();
            System.out.println("Insert success count:" + canInsertCount);
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

    private static void setPrepareParams(PreparedStatement pstm, SellOrder sellOrder) throws Exception{
        pstm.setInt(1, sellOrder.getId());
        pstm.setString(2, sellOrder.getStatus());
        pstm.setString(3, sellOrder.getOrderNo());
        pstm.setString(4, sellOrder.getGoodsNos());
        pstm.setString(5, sellOrder.getGoodsNames());
        pstm.setInt(6, sellOrder.getCount());
        pstm.setString(7, sellOrder.getOutboundStatus());
        pstm.setString(8, sellOrder.getExpressCompany());
        pstm.setString(9, sellOrder.getExpressNo());
        pstm.setBigDecimal(10, sellOrder.getExpressCost());
        pstm.setBigDecimal(11, sellOrder.getApproximateWeight());
        pstm.setString(12, sellOrder.getProvince());
        pstm.setString(13, sellOrder.getCity());
        pstm.setString(14, sellOrder.getDistrict());
        pstm.setString(15, sellOrder.getOrderTime());
    }
}
