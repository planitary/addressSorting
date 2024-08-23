package com.zane.controller;

import com.zane.core.route.impl.RoutingMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RoutingMonitorController {

    @Resource
    private RoutingMonitorService routingMonitorService;

    @RequestMapping("/getRoutingMonitor")
    public String getRoutingMonitor(String date,String timeType){
        return routingMonitorService.getRoutingMonitor(date,timeType);
    }
}
