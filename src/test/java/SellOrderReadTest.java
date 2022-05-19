import com.lw.dao.SellOrderDao;
import com.lw.dto.SellOrder;
import com.lw.excel.SellOrderExcelRead;
import org.junit.Test;

import java.util.ArrayList;

public class SellOrderReadTest {
    @Test
    public void testRead() {
        SellOrderExcelRead.readExcel("C:\\Users\\wsj60\\Desktop\\韵达快递费账单\\导出\\9月韵达.xlsx");
    }
}
