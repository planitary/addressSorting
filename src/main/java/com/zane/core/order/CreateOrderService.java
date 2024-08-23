package com.zane.core.order;

import com.zane.dao.dto.CreateOrderDTO;

// 下单
public interface CreateOrderService {

    String createOrder(CreateOrderDTO createOrderDTO);

    String getTMSOrder(String waybillNo);

    String createOrderByZipCode(CreateOrderDTO createOrderDTO);
}
