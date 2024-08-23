package com.zane.core.route;

import com.zane.dao.RoutingMonitorDo;

public interface RoutingMonitor {

    /**
     * 获取监控详情（按照日期）
     * @param date          日期:yyyy-MM-dd HH:mm:ss
     * @param timeType      时间类型 pickUp-揽收时间，expectDelivered-应签收时间，delivered-已签收时间
     * @return
     */
    String getRoutingMonitor(String date,String timeType);
}
