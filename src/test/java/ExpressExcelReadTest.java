import com.lw.dto.ExpressOrder;
import com.lw.excel.ExpressOrderExcelRead;
import org.junit.Test;

import java.util.ArrayList;

public class ExpressExcelReadTest {
    @Test
    public void read() {
        ExpressOrderExcelRead.readExcel("C:\\Users\\wsj60\\Desktop\\韵达快递费账单\\韵达黄林峰对账单 2021-09.xlsx");
    }
}
