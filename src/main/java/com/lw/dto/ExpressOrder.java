package com.lw.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpressOrder {
    @ExcelProperty(value="id")
    private Integer id;
    @ExcelProperty(value="运单编号")
    private String expressNo;
    @ExcelProperty(value="寄件时间")
    private String orderDate;
    @ExcelProperty(value="客户名称")
    private String customerNo;
    @ExcelProperty(value="收件省份")
    private String province;
    @ExcelProperty(value="目的地")
    private String city;
    @ExcelProperty(value="包裹计费重量")
    private BigDecimal weight;
    @ExcelProperty(value="总运费")
    private BigDecimal fee;
    @ExcelProperty(value="运单运费")
    private BigDecimal checkCost;
    @ExcelProperty(value="内部计费重量")
    private BigDecimal deviation;

}
