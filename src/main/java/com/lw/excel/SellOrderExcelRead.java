package com.lw.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lw.dto.SellOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SellOrderExcelRead {
    public static Logger logger = LoggerFactory.getLogger(SellOrderExcelRead.class);
    public static ArrayList<SellOrder> readExcel(String filePath) {
        ArrayList<SellOrder> result = new ArrayList<>();
        ExcelReader reader = EasyExcel.read(filePath, SellOrder.class, new AnalysisEventListener<SellOrder>() {
            @Override
            public void invoke(SellOrder sellOrder, AnalysisContext analysisContext) {
                result.add(sellOrder);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
                String formatTime = simpleDateFormat.format(new Date());
                logger.info("One of the sheet finished time:{}. Total read {} lines.", formatTime, result.size());
            }
        }).build();
        reader.readAll();
        reader.finish();
        return result;
    }
}
