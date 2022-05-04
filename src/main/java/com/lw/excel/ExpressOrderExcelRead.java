package com.lw.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lw.dto.ExpressOrder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpressOrderExcelRead {
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
                System.out.println("One of the sheet finished time:" + formatTime + ". Total read lines:" + readResult.size());
            }
        }).build();
        reader.readAll();
        reader.finish();
        return readResult;
    }

}
