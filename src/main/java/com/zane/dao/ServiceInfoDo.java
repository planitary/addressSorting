package com.zane.dao;

import lombok.Data;

@Data
public class ServiceInfoDo {

    private String deliveryServic;
    /**
     * 截单时间，路径规划时该日期为当天往前7天内
     */
    private String expectedPickupDate;
    /**
     * 产品编码，建议这里填空，否则会拿着特定的产品编码去查询产品服务，填空则表示全部校验
     */
    private String logisticsProductCode;
    private Integer temperatureService;
    private Integer pickupService;
    private String deliveryService;
    private String deliveryRequirements;
    /**
     * FM截单时为true，普通订单为false
     */
    private Boolean isCutoff;
}
