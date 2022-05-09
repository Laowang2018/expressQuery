package com.lw.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lw.dto.ExpressOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpressOrderExcelRead {
    public static Logger logger = LoggerFactory.getLogger(ExpressOrderExcelRead.class);
    public static ArrayList<ExpressOrder> readExcel(String filePath) {
        ArrayList<ExpressOrder> readResult = new ArrayList();
        ExcelReader reader = EasyExcel.read(filePath, ExpressOrder.class, new AnalysisEventListener<ExpressOrder>() {
            @Override
            public void invoke(ExpressOrder expressOrder, AnalysisContext analysisContext) {
                readResult.add(expressOrder);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
                String formatTime = simpleDateFormat.format(new Date());
                logger.info("----------【快递的】一个表单读取完成，当前时间是:{}. 当前总共读取{}条----------", formatTime, readResult.size());
            }
        }).build();
        reader.readAll();
        reader.finish();
        return readResult;
    }

}
