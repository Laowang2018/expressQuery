package com.lw.main;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.lw.dao.ExpressOrderDao;
import com.lw.dao.SellOrderDao;
import com.lw.dto.ExpressOrder;
import com.lw.dto.SellOrder;
import com.lw.excel.ExpressOrderExcelRead;
import com.lw.excel.SellOrderExcelRead;
import com.lw.h2.H2Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainClass {
    public static Logger logger = LoggerFactory.getLogger(MainClass.class);
    public static void main(String[] args) {
        //C:\Users\wsj60\Desktop\韵达快递费账单\韵达黄林峰对账单2021-09.xlsx
        String expressFile = args[0];
//        String expressFile = "C:\\Users\\wsj60\\Desktop\\run\\21-5月.xls";

        //C:\Users\wsj60\Desktop\韵达快递费账单\导出\9月韵达.xlsx
        String sellFile = args[1];
//        String sellFile = "C:\\Users\\wsj60\\Desktop\\run\\20210501.xlsx";

        long start = System.currentTimeMillis();
        ArrayList<SellOrder> sellOrders = SellOrderExcelRead.readExcel(sellFile);
        SellOrderDao.createTable();
        SellOrderDao.batchInsert(sellOrders);

        ArrayList<ExpressOrder> expressOrders = ExpressOrderExcelRead.readExcel(expressFile);
        ExpressOrderDao.createTable();
        ExpressOrderDao.batchInsert(expressOrders);


        Connection conn = H2Connection.getInstance();
        PreparedStatement query1 = null;
        try {
            String sql1 = "SELECT express_no,count(express_no) as number FROM `sell_order` where express_no is not null GROUP BY express_no HAVING number>=2";
            String sql2 = "select * from sell_order where express_no=?";
            String sql3 = "update sell_order set goods_nos=?,goods_names=? where express_no=? and express_cost is not null";
            String sql4 = "delete from sell_order where express_no=? and express_cost is null";
            query1 = conn.prepareStatement(sql1);
            ResultSet resultSet = query1.executeQuery();
            int deleteCount = 0;
            int bigestNo = 0;
            String bigestExpressNo = "";
            while (resultSet.next()) {
                String expressNo = resultSet.getString(1);
                PreparedStatement query2 = conn.prepareStatement(sql2);
                query2.setString(1, expressNo);
                ResultSet resultSet1 = query2.executeQuery();
                String goods = "";
                String names = "";
                while (resultSet1.next()) {
                    goods += resultSet1.getString(4) + ";";
                    names += resultSet1.getString(5) + ";";
                }
                PreparedStatement update = conn.prepareStatement(sql3);
                update.setString(1, goods);
                update.setString(2, names);
                update.setString(3, expressNo);
                update.executeUpdate();

                PreparedStatement delete = conn.prepareStatement(sql4);
                delete.setString(1, expressNo);
                int count = delete.executeUpdate();
                if(count >= bigestNo) {
                    bigestNo = count;
                    bigestExpressNo = expressNo;
                }
                deleteCount += count;
            }
            logger.info("------------单品最多的一单是{}，有{}个单品.------------", bigestExpressNo, bigestNo+1);
            logger.info("------------总计删除本公司重复快递记录{}笔.------------", deleteCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sqlSellOrder = "select s.id,s.`status`,s.order_no,s.goods_nos,s.goods_names,s.count,s.outbound_status,s.express_company,s.express_no,s.express_cost,s.approximate_weight,s.province,s.city,s.district,s.order_time,e.fee from sell_order s left outer join express_order e on e.express_no=s.express_no;";
        ArrayList<SellOrder> sellOrder = new ArrayList<>();
        try {
            PreparedStatement sellpstm = conn.prepareStatement(sqlSellOrder);
            ResultSet sellOrderResultSet = sellpstm.executeQuery();
            while(sellOrderResultSet.next()) {
                SellOrder sellOrd = new SellOrder();
                assembleSellOrder(sellOrderResultSet, sellOrd);
                sellOrder.add(sellOrd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String fileNam = "./deleteDuplicationSellOrder"+System.currentTimeMillis()+".xlsx";
        ExcelWriter writeeExc = EasyExcel.write(fileNam, SellOrder.class).build();
        WriteSheet writeShe = EasyExcel.writerSheet("result1").build();
        writeeExc.write(sellOrder, writeShe);
        writeeExc.finish();


        String sql = "select * from express_order where express_no not in(select express_no from sell_order where express_no is not null);";
        ArrayList<ExpressOrder> exprsOrders = new ArrayList<>();
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet resultSet = pstm.executeQuery();
            int i = 0;
            while(resultSet.next()) {
                ExpressOrder exprsOrder = new ExpressOrder();
                assembleExpressOrder(resultSet, exprsOrder);
                exprsOrders.add(exprsOrder);
                i++;
            }
            logger.info("------------快递公司方的有{}笔在我公司无记录.------------", i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String fileName = "./cantfoundinSellOrder"+System.currentTimeMillis()+".xlsx";
        ExcelWriter writeeExcel = EasyExcel.write(fileName, ExpressOrder.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("result1").build();
        writeeExcel.write(exprsOrders, writeSheet);
        writeeExcel.finish();
        long end = System.currentTimeMillis();
        logger.info("------------TimeCost:{}ms------------", end - start);
    }

    private static void assembleSellOrder(ResultSet sellOrderResultSet, SellOrder sellOrd) throws SQLException {
        sellOrd.setId(sellOrderResultSet.getInt(1));
        sellOrd.setStatus(sellOrderResultSet.getString(2));
        sellOrd.setOrderNo(sellOrderResultSet.getString(3));
        sellOrd.setGoodsNos(sellOrderResultSet.getString(4));
        sellOrd.setGoodsNames(sellOrderResultSet.getString(5));
        sellOrd.setCount(sellOrderResultSet.getInt(6));
        sellOrd.setOutboundStatus(sellOrderResultSet.getString(7));
        sellOrd.setExpressCompany(sellOrderResultSet.getString(8));
        sellOrd.setExpressNo(sellOrderResultSet.getString(9));
        sellOrd.setExpressCost(sellOrderResultSet.getBigDecimal(10));
        sellOrd.setApproximateWeight(sellOrderResultSet.getBigDecimal(11));
        sellOrd.setProvince(sellOrderResultSet.getString(12));
        sellOrd.setCity(sellOrderResultSet.getString(13));
        sellOrd.setDistrict(sellOrderResultSet.getString(14));
        sellOrd.setOrderTime(sellOrderResultSet.getString(15));
        sellOrd.setFee(sellOrderResultSet.getBigDecimal(16));
    }

    private static void assembleExpressOrder(ResultSet resultSet, ExpressOrder expressOrder) throws SQLException {
        expressOrder.setId(resultSet.getInt(1));
        expressOrder.setExpressNo(resultSet.getString(2));
        expressOrder.setOrderDate(resultSet.getString(3));
        expressOrder.setCustomerNo(resultSet.getString(4));
        expressOrder.setProvince(resultSet.getString(5));
        expressOrder.setCity(resultSet.getString(6));
        expressOrder.setWeight(resultSet.getBigDecimal(7));
        expressOrder.setFee(resultSet.getBigDecimal(8));
        expressOrder.setCheckCost(resultSet.getBigDecimal(9));
        expressOrder.setDeviation(resultSet.getBigDecimal(10));
    }
}
