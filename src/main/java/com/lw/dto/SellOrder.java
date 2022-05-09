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
    @ExcelProperty(value="系统订单状态")
    private String status;
    @ExcelProperty(value="订单号")
    private String orderNo;
    @ExcelProperty(value="商品编码")
    private String goodsNos;
    @ExcelProperty(value="商品名称")
    private String goodsNames;
    @ExcelProperty(value="数量")
    private Integer count;
    @ExcelProperty(value="库存状况")
    private String outboundStatus;
    @ExcelProperty(value="快递公司")
    private String expressCompany;
    @ExcelProperty(value="快递单号")
    private String expressNo;
    @ExcelProperty(value="快递成本")
    private BigDecimal expressCost;
    @ExcelProperty(value="估重")
    private BigDecimal approximateWeight;
    @ExcelProperty(value="省")
    private String province;
    @ExcelProperty(value="市")
    private String city;
    @ExcelProperty(value="区")
    private String district;
    @ExcelProperty(value="下单时间")
    private String orderTime;
    @ExcelProperty(value="快递公司计费")
    private BigDecimal fee;
}
