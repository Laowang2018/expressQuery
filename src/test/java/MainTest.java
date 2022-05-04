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
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainTest {
    @Test
    public void MainTest() {
        long start = System.currentTimeMillis();
        ArrayList<SellOrder> sellOrders = SellOrderExcelRead.readExcel("C:\\Users\\wsj60\\Desktop\\韵达快递费账单\\导出\\9月韵达.xlsx");
        SellOrderDao.createTable();
        SellOrderDao.batchInsert(sellOrders);

        ArrayList<ExpressOrder> expressOrders = ExpressOrderExcelRead.readExcel("C:\\Users\\wsj60\\Desktop\\韵达快递费账单\\韵达黄林峰对账单 2021-09.xlsx");
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
                int i = update.executeUpdate();
                System.out.println("update sql expressNo=" + expressNo + ",and effect rows=" + i);

                PreparedStatement delete = conn.prepareStatement(sql4);
                delete.setString(1, expressNo);
                int count = delete.executeUpdate();
                deleteCount += count;
                System.out.println("delete the express_no=" + expressNo + ",result:" + count);
            }
            System.out.println("delete the all count:" + deleteCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "select * from express_order where express_no not in(select express_no from sell_order where express_no is not null);";
        ArrayList<ExpressOrder> exprsOrders = new ArrayList<>();
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet resultSet = pstm.executeQuery();
            int i = 0;
            while(resultSet.next()) {
                String expressNo = resultSet.getString(2);
                ExpressOrder exprsOrder = new ExpressOrder();
                assembleSellOrder(resultSet, exprsOrder);
                exprsOrders.add(exprsOrder);
                i++;
            }
            System.out.println("重复的有多少笔:" + i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String fileName = "C:\\Users\\wsj60\\Desktop\\韵达快递费账单\\"+System.currentTimeMillis()+".xlsx";
        ExcelWriter writeeExcel = EasyExcel.write(fileName, ExpressOrder.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("result1").build();
        writeeExcel.write(exprsOrders, writeSheet);
        writeeExcel.finish();
        long end = System.currentTimeMillis();
        System.out.println("TimeCost:" + (end - start) +"ms");
    }

    private void assembleSellOrder(ResultSet resultSet, ExpressOrder expressOrder) throws SQLException {
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
