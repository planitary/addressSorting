package com.zane.controller;

import com.alibaba.fastjson2.JSON;
import com.zane.core.order.CreateOrderService;
import com.zane.dao.dto.CreateOrderDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    @Resource
    private CreateOrderService createOrderService;

    @PostMapping("/order/createOrder")
    public String createOrder(@RequestBody CreateOrderDTO createOrderDTO){
        String orderNo = createOrderService.createOrder(createOrderDTO);
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("waybillNo",orderNo);
        return JSON.toJSONString(resMap);
    }

    @RequestMapping("/order/getTMSOrder")
    public String getTMSOrder(String waybillNo){
        return createOrderService.getTMSOrder(waybillNo);
    }
}
