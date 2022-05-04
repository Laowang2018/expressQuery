package com.lw.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
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
    @ExcelProperty(value="express_no")
    private String expressNo;
    @ExcelProperty(value="order_date")
    private String orderDate;
    @ExcelProperty(value="customer_no")
    private String customerNo;
    @ExcelProperty(value="province")
    private String province;
    @ExcelProperty(value="city")
    private String city;
    @ExcelProperty(value="weight")
    private BigDecimal weight;
    @ExcelProperty(value="fee")
    private BigDecimal fee;
    @ExcelProperty(value="check_cost")
    private BigDecimal checkCost;
    @ExcelProperty(value="deviation")
    private BigDecimal deviation;

}
