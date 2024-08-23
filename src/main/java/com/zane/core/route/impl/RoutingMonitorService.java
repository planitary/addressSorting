package com.zane.core.route.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zane.core.route.RoutingMonitor;
import com.zane.dao.RoutingMonitorDo;
import com.zane.mapper.RoutingMonitorMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoutingMonitorService  implements RoutingMonitor {
    @Resource
    private RoutingMonitorMapper routingMonitorMapper;
    @Override
    // @DS注解指明当前使用哪种数据源
    @DS("tms_hub_test")
    public String getRoutingMonitor(String date,String timeType) {
        LambdaQueryWrapper<RoutingMonitorDo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (Objects.equals(timeType,"pickUp")) {
            lambdaQueryWrapper.ge(RoutingMonitorDo::getRealityPickupDate,date);
        }
        if (Objects.equals(timeType,"expectDelivered")){
            lambdaQueryWrapper.ge(RoutingMonitorDo::getExpectDeliveredDate,date);
        }
        if (Objects.equals(timeType,"delivered")){
            lambdaQueryWrapper.ge(RoutingMonitorDo::getRealityDeliveredDate,date);
        }
        List<RoutingMonitorDo> routingMonitorDoList = routingMonitorMapper.selectList(lambdaQueryWrapper);
        int totalCount = routingMonitorDoList.size();
        return routingMonitorDoList.toString();

    }
}
