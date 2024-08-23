package com.zane.core.main.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zane.base.GlobalVariables;
import com.zane.base.baseEnum.SortingType;
import com.zane.core.addressSorting.AddressSortingCoreService;
import com.zane.core.base.BaseService;
import com.zane.core.main.AddressSortingMain;
import com.zane.core.order.CreateOrderService;
import com.zane.dao.dto.CreateOrderDTO;
import com.zane.dao.dto.LoginDTO;
import com.zane.dao.dto.PageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.sql.DataTruncation;
import java.util.Map;

@Service
@Slf4j
public class AddressSortingMainImpl implements AddressSortingMain {

    @Resource
    BaseService baseService;
    @Resource
    CreateOrderService createOrderService;

    @Resource
    AddressSortingCoreService addressSortingCoreService;
    @Override
    public String addressSortingMain(String country) {
        if (null == country){
            log.error("国家为空");
            throw new RuntimeException("国家不能为空");
        }
        // 先登录
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUserCode("2103471201");
        loginDTO.setPassword("19283746test!@#");
        loginDTO.setTimeZone("+8");
        loginDTO.setLang("zh_CN");
        baseService.login(loginDTO);
        log.info("登录token:{}", GlobalVariables.AUTHORIZATION_CODE);
        //下单
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setSortingCode(SortingType.STANDARD_ADDRESS_SORTING.getSortingTypeCode());
        createOrderDTO.setIsFMOrder("false");
        createOrderDTO.setIsGeoCode("false");
        createOrderDTO.setCountry(country);
        String orderNo = createOrderService.createOrder(createOrderDTO);
        log.info("orderNo:{}",orderNo);
        // 拿到订单后获取配置列表
        PageDTO pageDTO = new PageDTO();
        pageDTO.setCountry(country);
        pageDTO.setCurrentPage(1);
        pageDTO.setShowCount(20);
        String addressSortingConfigDetail = addressSortingCoreService.getAddressSortingConfig(pageDTO);
        // 开启分拣，并拿到分拣记录
        String orderSortingRecord = addressSortingCoreService.getOrderSortingRecord(orderNo);
        // 校验
        Map<String, Object> sortSuccessV2 = addressSortingCoreService.isSortSuccessV2(orderNo);
        log.info("分拣结果:{}",sortSuccessV2);
        sortSuccessV2.put("orderNo",orderNo);
        sortSuccessV2.put("addressSortingConfigDetail",addressSortingConfigDetail);
        sortSuccessV2.put("orderSortingRecord",orderSortingRecord);
//        return JSON.toJSONString(sortingRecord);
        return JSON.toJSONString(sortSuccessV2);
    }
}
