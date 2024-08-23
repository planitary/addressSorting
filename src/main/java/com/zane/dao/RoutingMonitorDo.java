package com.zane.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Data
@TableName(value = "ops_full_link_monitor")
public class RoutingMonitorDo {
    private BigInteger id;
    private String waybillNo;
    private String orderType;
    private BigInteger orgId;
    private String country;
    private String province;
    private String city;
    private String trendSubNo;
    private String startDcName;
    private String startDcCode;
    private String destinationDcCode;
    private String destinationDcName;
    private String startDsCode;
    private String startDsName;
    private String actPickDsCode;
    private String actPickDsName;
    private LocalDateTime expectPickupDate;
    private LocalDateTime realityPickupDate;
    private LocalDateTime lastDcDepartureTime;
    private LocalDateTime lastDcOutboundTime;
    private String dispatchCode;
    private String dispatchName;
    private LocalDateTime dispatchInboundTime;
    private LocalDateTime dispatchOutboundTime;
    private LocalDateTime expectDeliveredDate;
    private LocalDateTime realityDeliveredDate;
    private LocalDateTime expectDsArriveTime;
    private String routerClassNo;
    private LocalDateTime firstDcStartTime;
    private String isOverTime;
    private String isChangeRouter;
    private Integer isDelete;
    private Integer recordVersion;
    private String pickUpCity;
    private LocalDateTime orderCreateTime;

}
