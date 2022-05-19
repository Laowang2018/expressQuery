package com.lw.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lw.dao.SellOrderDao;
import com.lw.dto.SellOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SellOrderExcelRead {
    public static Logger logger = LoggerFactory.getLogger(SellOrderExcelRead.class);
    public static void readExcel(String filePath) {
        ArrayList<SellOrder> result = new ArrayList<>();
        ExcelReader reader = EasyExcel.read(filePath, SellOrder.class, new AnalysisEventListener<SellOrder>() {
            @Override
            public void invoke(SellOrder sellOrder, AnalysisContext analysisContext) {
                if(sellOrder.getId() == null || sellOrder.getId() == 0) {
                    logger.error("请检查【本公司】表单的id列为空的问题。");
                } else {
                    result.add(sellOrder);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                SellOrderDao.batchInsert(result);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
                String formatTime = simpleDateFormat.format(new Date());
                logger.info("----------【公司的】一个表单读取并插入完成，当前时间是:{}. 当前总共读取{}条----------", formatTime, result.size());
                result.clear();
            }
        }).build();
        reader.readAll();
        reader.finish();
    }
}
