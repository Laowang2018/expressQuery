import com.lw.h2.H2Connection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;

public class H2Test {
    private Connection conn = H2Connection.getInstance();
    @Test
    public void testInit() throws SQLException{
        Assert.assertNotNull(conn);
        conn.close();
    }

    @Test
    public void testCreate() {
        String create = "CREATE TABLE IF NOT EXISTS `express_order`(" +
                "`id` bigint(20) unsigned NOT NULL," +
                "`express_no` varchar(30) DEFAULT NULL,"+
                "`order_date` varchar(30) DEFAULT NULL,"+
                "`customer_no` varchar(15) DEFAULT NULL,"+
                "`province` varchar(30) DEFAULT NULL,"+
                "`city` varchar(30) DEFAULT NULL,"+
                "`weight` DECIMAL(20,4) NOT NULL DEFAULT '0.0000',"+
                "`fee` DECIMAL(20,4) NOT NULL DEFAULT '0.0000',"+
                "`check_cost` DECIMAL(20,4) NULL DEFAULT NULL,"+
                "`deviation` DECIMAL(20,4) NULL DEFAULT NULL,"+
                "PRIMARY KEY (`id`),"+
                "INDEX `express_no_key`(`express_no`) using BTREE"+
                ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact";
        try {
            Statement statement = conn.createStatement();
            statement.execute(create);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        testCreate();
        String insert = "INSERT INTO `express_order`(`id`, `express_no`, `order_date`, `customer_no`, `province`, `city`, `weight`, `fee`, `check_cost`, `deviation`) VALUES (9999999, '222', '2021-09-30', '5525', '广东省', '深圳市', 0.18, 2.400, 2.100, 0.30);";
        try {
            PreparedStatement insertState = conn.prepareStatement(insert);
            int i = insertState.executeUpdate();
            System.out.println("insert into table: "+ i + " 条。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQuery() {
        testCreate();
        String query = "SELECT * FROM `express_order` WHERE `express_no`='222';";
        try {
            PreparedStatement queryState = conn.prepareStatement(query);
            ResultSet resultSet = queryState.executeQuery();
            while (resultSet.next()) {
                String orderDate = resultSet.getString(3);
                System.out.println(orderDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
