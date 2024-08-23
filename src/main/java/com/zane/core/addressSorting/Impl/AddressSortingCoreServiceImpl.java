package com.zane.core.addressSorting.Impl;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zane.base.GlobalVariables;
import com.zane.base.handler.DoExecutor;
import com.zane.core.addressSorting.AddressSortingCoreService;
import com.zane.core.order.CreateOrderService;
import com.zane.dao.dto.*;
import com.zane.mapper.MatchPointRecordMapper;
import com.zane.mapper.OrderSortingRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.Introspection;
import org.springframework.stereotype.Service;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import javax.xml.transform.sax.SAXResult;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class AddressSortingCoreServiceImpl implements AddressSortingCoreService {

    @Resource
    private DoExecutor doExecutor;

    @Resource
    private CreateOrderService createOrderService;

    /**
     * 国家分拣配置ID
     */
    private static String COUNTRY_CONFIG_ID;

    @Resource
    private OrderSortingRecordMapper orderSortingRecordMapper;
    @Resource
    private MatchPointRecordMapper matchPointRecordMapper;

    // 配置列表
    private final static String GET_CONFIG_LIST_URL = "https://test-scs.52imile.cn/cornerstone/address/country/config/configList";
    // 配置内容
    private final static String GET_CONFIG_DETAIL = "https://test-scs.52imile.cn/cornerstone/address/country/config/configDetail";

    @Override
    public String getAddressSortingConfig(PageDTO pageDTO) {
        String configCode = "";

        // 封装参数
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("country", pageDTO.getCountry());
        requestMap.put("currentPage", pageDTO.getCurrentPage());
        requestMap.put("showCount", pageDTO.getShowCount());
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", GlobalVariables.AUTHORIZATION_CODE);
        headers.put("Content-Type", "application/json");
        log.info("请求头:{}", headers);

        String resBodyJSON = doExecutor.doCommonHttpPostJson(requestMap, headers, GET_CONFIG_LIST_URL);
        log.info(resBodyJSON);
        Object configListObj = JSONPath.read(resBodyJSON, "$.resultObject.results");
        String configListJSON = JSON.toJSONString(configListObj);
        List<String> configList = JSON.parseArray(configListJSON, String.class);

        // 国家维度配置且状态被启用
        for (String config : configList) {
            if (Objects.equals(JSONPath.read(config, "$.configLevel"), 1) && Objects.equals(JSONPath.read(config, "$.status"), "ACTIVE")) {
                Object writeBackItemListJSON = JSONPath.read(config, "$.writeBackItemList");
                Object sortTypeJSON = JSONPath.read(config, "$.sortType");
//                if (writeBackItemListJSON instanceof ArrayList<?>) {
//                    GlobalVariables.WRITE_BACK_ITEM_LIST = (List<?>) writeBackItemListJSON;
//                }
                if (sortTypeJSON instanceof ArrayList<?>) {
                    GlobalVariables.SORT_TYPE_LIST = (List<?>) sortTypeJSON;
                }
                COUNTRY_CONFIG_ID = JSONPath.read(config, "$.id").toString();
                configCode = JSONPath.read(config,"$.configCode").toString();
            }
        }
        String configDetail = this.getConfigDetail(configCode);

        return GlobalVariables.SORT_TYPE_LIST.toString() + configDetail;
    }

    @Override
    public String getOrderSortingRecord(String waybillNo) {
        String orderSortingRecord = "";
        if (waybillNo != null) {
            LambdaQueryWrapper<OrderSortingRecordDTO> orderSortingRecordDTOLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderSortingRecordDTOLambdaQueryWrapper.eq(OrderSortingRecordDTO::getInputBusinessNumber, waybillNo)
                    .eq(OrderSortingRecordDTO::getSortCountryConfigId, Long.parseLong(COUNTRY_CONFIG_ID));
            List<OrderSortingRecordDTO> orderSortingRecordDTO = orderSortingRecordMapper.selectList(orderSortingRecordDTOLambdaQueryWrapper);
            if (orderSortingRecordDTO != null) {
                orderSortingRecord = JSON.toJSONString(orderSortingRecordDTO);
            }
        }
        return orderSortingRecord;
    }

    @Override
    public Map<String,Object> isSortSuccessV2(String waybillNo){
        List<OrderSortingRecordDTO> orderSortingRecords;
        Boolean sortFlag = false;

        String matchPointRecord = "";

        Map<String,Object> resMap = new HashMap<>();

        if (waybillNo != null) {
            // 获取分拣记录
            LambdaQueryWrapper<OrderSortingRecordDTO> orderSortingRecordDTOLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderSortingRecordDTOLambdaQueryWrapper.eq(OrderSortingRecordDTO::getInputBusinessNumber, waybillNo)
                    .eq(OrderSortingRecordDTO::getSortCountryConfigId, Long.parseLong(COUNTRY_CONFIG_ID));
            orderSortingRecords = orderSortingRecordMapper.selectList(orderSortingRecordDTOLambdaQueryWrapper);


            // 获取坐标匹配记录
            LambdaQueryWrapper<MatchPointRecordDTO> matchPointRecordDTOLambdaQueryWrapper = new LambdaQueryWrapper<>();
            matchPointRecordDTOLambdaQueryWrapper.eq(MatchPointRecordDTO::getBusinessNumber, waybillNo);
            MatchPointRecordDTO matchPointRecordDTO = matchPointRecordMapper.selectOne(matchPointRecordDTOLambdaQueryWrapper);

            // 第一版，如果分拣成功了，这里分拣记录会有两条，第二条记录一定是分拣成功，这里就先写死取第二条记录
            // 分拣记录取值优化（真正的分拣记录只有一条-sortType=ORDER_CREATE，其余的都是预分拣记录）

            if (matchPointRecordDTO != null && orderSortingRecords != null) {
                List<OrderSortingRecordDTO> filterOrderSortingRecord = orderSortingRecords.stream()
                        .filter(orderSortingRecordDTO -> Objects.equals(orderSortingRecordDTO.getSortType(), "ORDER_CREATE"))
                        .collect(Collectors.toList());
//                matchPointRecord = JSON.toJSONString(matchPointRecordDTO);
                OrderSortingRecordDTO orderSortingRecord = filterOrderSortingRecord.get(0);
                String sortZone = orderSortingRecord.getSortZone();
                // 查询TMS运单详情
                String tmsOrderList = createOrderService.getTMSOrder(waybillNo);

                // 解析数据
                JSONObject jsonObject1 = JSON.parseObject(tmsOrderList);
                JSONObject jsonObject2 = jsonObject1.getJSONObject("resultObject");
                JSONArray results = jsonObject2.getJSONArray("results");
                OrderTMSInfo orderTMSInfo = JSON.parseObject(results.get(0).toString(),OrderTMSInfo.class);

//                String TMSOrderInfoJSON = (String) JSONPath.read(tmsOrderList, "$.resultObject.results[0]");
                log.info("TMSOrderInfo:{}",orderTMSInfo);
                // 三段码校验
                if (Objects.equals(orderTMSInfo.getConsigneeRuta(), sortZone)) {
                    resMap.put("sortZone",sortZone);
                    resMap.put("TmsConsigneeRouteCode",orderTMSInfo.getConsigneeRuta());
                    sortFlag = true;
                    // 核心校验
                    if (this.recordMatch(orderSortingRecord, matchPointRecordDTO)) {
                        sortFlag = true;
                        resMap.put("sortProvince",orderSortingRecord.getSortProvince());
                        resMap.put("sortCity",orderSortingRecord.getSortCity());
                        resMap.put("sortAddress",orderSortingRecord.getSortAddress());
                        resMap.put("sortZipcode",orderSortingRecord.getSortZipCode());
                        resMap.put("matchStatus",matchPointRecordDTO.getIsMatchSuccess());
                        resMap.put("pointSource",matchPointRecordDTO.getPointSource());
                        resMap.put("addressMatchStrategy",matchPointRecordDTO.getAddressMatchStrategy());
                        resMap.put("standardAddressTrustValue",GlobalVariables.STANDARD_ADDRESS_TRUST_VALUE);
                        resMap.put("userAddressTrustValue",GlobalVariables.USER_ADDRESS_TRUST_VALUE);
                        resMap.put("sortStationCode",orderSortingRecord.getSortStationCode());
                        resMap.put("geoCodingSource",orderSortingRecord.getInputGeocodingSource());
                    }
                }
            }

        }
        if (sortFlag) {
            resMap.put("sortResult","分拣成功");
        }
        else {
            resMap.put("sortResult","分拣失败");

        }
        return resMap;
    }

    //


    @Override
    public String isSortSuccess(String waybillNo) {

        List<OrderSortingRecordDTO> orderSortingRecords;
        Boolean sortFlag = false;

        String matchPointRecord = "";

        Map<String,Object> resMap = new HashMap<>();

        if (waybillNo != null) {
            // 获取分拣记录
            LambdaQueryWrapper<OrderSortingRecordDTO> orderSortingRecordDTOLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderSortingRecordDTOLambdaQueryWrapper.eq(OrderSortingRecordDTO::getInputBusinessNumber, waybillNo)
                    .eq(OrderSortingRecordDTO::getSortCountryConfigId, Long.parseLong(COUNTRY_CONFIG_ID));
            orderSortingRecords = orderSortingRecordMapper.selectList(orderSortingRecordDTOLambdaQueryWrapper);


            // 获取坐标匹配记录
            LambdaQueryWrapper<MatchPointRecordDTO> matchPointRecordDTOLambdaQueryWrapper = new LambdaQueryWrapper<>();
            matchPointRecordDTOLambdaQueryWrapper.eq(MatchPointRecordDTO::getBusinessNumber, waybillNo);
            MatchPointRecordDTO matchPointRecordDTO = matchPointRecordMapper.selectOne(matchPointRecordDTOLambdaQueryWrapper);

            // 第一版，如果分拣成功了，这里分拣记录会有两条，第二条记录一定是分拣成功，这里就先写死取第二条记录
            // 分拣记录取值优化（真正的分拣记录只有一条-sortType=ORDER_CREATE，其余的都是预分拣记录）

            if (matchPointRecordDTO != null && orderSortingRecords != null) {
                List<OrderSortingRecordDTO> filterOrderSortingRecord = orderSortingRecords.stream()
                        .filter(orderSortingRecordDTO -> Objects.equals(orderSortingRecordDTO.getSortType(), "ORDER_CREATE"))
                        .collect(Collectors.toList());
//                matchPointRecord = JSON.toJSONString(matchPointRecordDTO);
                OrderSortingRecordDTO orderSortingRecord = filterOrderSortingRecord.get(0);
                String sortZone = orderSortingRecord.getSortZone();
                // 查询TMS运单详情
                String tmsOrderList = createOrderService.getTMSOrder(waybillNo);

                // 解析数据
                JSONObject jsonObject1 = JSON.parseObject(tmsOrderList);
                JSONObject jsonObject2 = jsonObject1.getJSONObject("resultObject");
                JSONArray results = jsonObject2.getJSONArray("results");
                OrderTMSInfo orderTMSInfo = JSON.parseObject(results.get(0).toString(),OrderTMSInfo.class);

//                String TMSOrderInfoJSON = (String) JSONPath.read(tmsOrderList, "$.resultObject.results[0]");
                log.info("TMSOrderInfo:{}",orderTMSInfo);
                // 三段码校验
                if (Objects.equals(orderTMSInfo.getConsigneeRuta(), sortZone)) {
                    resMap.put("sortZone",sortZone);
                    resMap.put("TmsConsigneeRouteCode",orderTMSInfo.getConsigneeRuta());
                    sortFlag = true;
                    // 核心校验
                    if (this.recordMatch(orderSortingRecord, matchPointRecordDTO)) {
                        sortFlag = true;
                        resMap.put("sortProvince",orderSortingRecord.getSortProvince());
                        resMap.put("sortCity",orderSortingRecord.getSortCity());
                        resMap.put("sortAddress",orderSortingRecord.getSortAddress());
                        resMap.put("sortZipcode",orderSortingRecord.getSortZipCode());
                        resMap.put("matchStatus",matchPointRecordDTO.getIsMatchSuccess());
                        resMap.put("pointSource",matchPointRecordDTO.getPointSource());
                        resMap.put("addressMatchStrategy",matchPointRecordDTO.getAddressMatchStrategy());
                        resMap.put("standardAddressTrustValue",GlobalVariables.STANDARD_ADDRESS_TRUST_VALUE);
                        resMap.put("userAddressTrustValue",GlobalVariables.USER_ADDRESS_TRUST_VALUE);
                        resMap.put("sortStationCode",orderSortingRecord.getSortStationCode());
                        resMap.put("addressCode",matchPointRecordDTO.getAddressCode());
                        resMap.put("geoCodingSource",orderSortingRecord.getInputGeocodingSource());
                    }
                }
            }

        }
        if (sortFlag) {
            resMap.put("sortResult","分拣成功");
        }
        else {
            resMap.put("sortResult","分拣失败");

        }
        return resMap.toString();
    }

    /**
     * 数据匹配
     *
     * @param orderSortingRecord  订单分拣记录
     * @param matchPointRecordDTO 坐标匹配记录
     * @return
     */

    private Boolean recordMatch(OrderSortingRecordDTO orderSortingRecord, MatchPointRecordDTO matchPointRecordDTO) {
        boolean globalFlag = false;
        String inputAddressCode = orderSortingRecord.getInputAddressCode();
        Integer isSortSuccess = orderSortingRecord.getIsSortSuccess();
        String sortGeofenceCode = orderSortingRecord.getSortGeofenceCode();
        Integer sortPath = orderSortingRecord.getSortPath();
        String sortStationCode = orderSortingRecord.getSortStationCode();
        String sortZone = orderSortingRecord.getSortZone();

        String addressCode = matchPointRecordDTO.getAddressCode();
        String addressMatchStrategy = matchPointRecordDTO.getAddressMatchStrategy();
        String businessNumber = matchPointRecordDTO.getBusinessNumber();
        Integer isMatchSuccess = matchPointRecordDTO.getIsMatchSuccess();
        String trustValue = matchPointRecordDTO.getTrustValue();
        String zipCode = matchPointRecordDTO.getZipCode();

        // 主要的判断语句在这里

        // 对比省市区
        if (Objects.equals(inputAddressCode, addressCode)) {
            globalFlag = true;
        }
        // 邮编和分拣区域不能为空
        if (zipCode != null && sortZone != null) {
            globalFlag = true;
        }
        // 分拣来源和电子围栏编码不能为空
        if (sortGeofenceCode != null && sortPath != null) {
            globalFlag = true;
        }
        // 坐标获取成功&分拣成功
        if (isSortSuccess == 1 && isMatchSuccess == 1) {
            globalFlag = true;
        }

        return globalFlag;
    }


    /**
     * 获取配置详情
     * @param configCode        配置编码
     * @return  回写配置json
     */
    private String getConfigDetail(String configCode){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", GlobalVariables.AUTHORIZATION_CODE);
//        headers.put("Content-Type", "application/json");
        // 获取信任分值
        Map<String,Object> requestMap2 = new HashMap<>();
        requestMap2.put("configCode",configCode);
        String configDetailJSON = doExecutor.doCommonHttpPostForm(requestMap2,headers,GET_CONFIG_DETAIL);
        log.info(configDetailJSON);
        GlobalVariables.STANDARD_ADDRESS_TRUST_VALUE = JSONPath.read(configDetailJSON,"$..standardTrustValueThreshold").toString();
        GlobalVariables.USER_ADDRESS_TRUST_VALUE = JSONPath.read(configDetailJSON,"$..userInfoTrustValueThreshold").toString();

        // 获取回写配置
        String writeBackListJson =JSONPath.read(configDetailJSON,"$..writeBackItemList").toString();
        log.info("回写配置JSON:{}",writeBackListJson);
        List<WriteBackItemDTO> writeBackItemDTOs = JSON.parseArray(writeBackListJson, WriteBackItemDTO.class);
        log.info("回写配置:{}",writeBackItemDTOs);
        //这里先默认取第一条（分拣成功的配置）
        GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS.put("isAreaReturn",writeBackItemDTOs.get(0).getIsAreaReturn());
        GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS.put("isCityReturn",writeBackItemDTOs.get(0).getIsCityReturn());
        GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS.put("isPointReturn",writeBackItemDTOs.get(0).getIsPointReturn());
        GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS.put("isProvinceReturn",writeBackItemDTOs.get(0).getIsProvinceReturn());
        GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS.put("isStationReturn",writeBackItemDTOs.get(0).getIsStationReturn());
        GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS.put("isZipCodeReturn",writeBackItemDTOs.get(0).getIsZipCodeReturn());
        GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS.put("isZoneReturn",writeBackItemDTOs.get(0).getIsZoneReturn());
        GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS.put("writeBackItem",writeBackItemDTOs.get(0).getWriteBackItem());

        //public static String toJSONString(Object object, SerializerFeature… features)：
        //该方法将实体对象转换成Json字符串时，如果不传递参数SerializerFeature.WriteMapNullValue，则忽略值为null的属性
        return JSON.toJSONString(GlobalVariables.WRITE_BACK_ITEM_WHEN_SUCCESS, SerializerFeature.WriteMapNullValue);
    }
}
