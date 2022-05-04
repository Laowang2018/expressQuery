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
public class SellOrder {
    @ExcelProperty(value="id")
    private Integer id;
    @ExcelProperty(value="status")
    private String status;
    @ExcelProperty(value="order_no")
    private String orderNo;
    @ExcelProperty(value="goods_nos")
    private String goodsNos;
    @ExcelProperty(value="goods_names")
    private String goodsNames;
    @ExcelProperty(value="count")
    private Integer count;
    @ExcelProperty(value="outbound_status")
    private String outboundStatus;
    @ExcelProperty(value="express_company")
    private String expressCompany;
    @ExcelProperty(value="express_no")
    private String expressNo;
    @ExcelProperty(value="express_cost")
    private BigDecimal expressCost;
    @ExcelProperty(value="approximate_weight")
    private BigDecimal approximateWeight;
    @ExcelProperty(value="province")
    private String province;
    @ExcelProperty(value="city")
    private String city;
    @ExcelProperty(value="district")
    private String district;
    @ExcelProperty(value="order_time")
    private String orderTime;
}
