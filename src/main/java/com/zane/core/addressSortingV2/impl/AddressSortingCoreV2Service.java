package com.zane.core.addressSortingV2.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zane.base.GlobalVariables;
import com.zane.base.baseEnum.MatchFetchWay;
import com.zane.base.handler.DoExecutor;
import com.zane.core.GlobalUrlConfig;
import com.zane.core.addressSortingV2.AddressSortingCoreV2;
import com.zane.core.base.BaseService;
import com.zane.dao.ChangeOrderDO;
import com.zane.dao.DeliveredInfoDo;
import com.zane.dao.UserInfoDo;
import com.zane.dao.dto.*;
import com.zane.mapper.MatchPointRecordMapper;
import com.zane.mapper.OrderSortingRecordMapper;
import com.zane.mapper.UserInfoMapper;
import com.zane.mapper.WriteBackRecordMapper;
import com.zane.utils.UniqueIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class AddressSortingCoreV2Service implements AddressSortingCoreV2 {
    private final GlobalUrlConfig globalUrlConfig;
    //接口请求前缀
    public AddressSortingCoreV2Service(GlobalUrlConfig globalUrlConfig){
        this.globalUrlConfig = globalUrlConfig;
    }

    @Autowired
    private DoExecutor doExecutor;
    @Autowired
    private BaseService baseService;
    @Autowired
    private UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    private OrderSortingRecordMapper orderSortingRecordMapper;
    @Autowired
    private MatchPointRecordMapper matchPointRecordMapper;

    @Autowired
    private WriteBackRecordMapper writeBackRecordMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public String addressSorting(AutoAddressSortingDTO autoAddressSortingDTO) {
        String urlPrefix = globalUrlConfig.getGlobalUrlPrefix() + "cornerstone/";
        // 先登录
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUserCode(autoAddressSortingDTO.getUserCode());
        loginDTO.setPassword(autoAddressSortingDTO.getPassword());
        loginDTO.setTimeZone("+8");
        loginDTO.setLang("zh_CN");
        baseService.login(loginDTO);
        log.info("登录token:{}", GlobalVariables.AUTHORIZATION_CODE);
        // 封装请求头
        String authorizationCode = GlobalVariables.AUTHORIZATION_CODE;
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", authorizationCode);
        header.put("Content-Type", "application/json");
        String httpRes = "";
        Map<String, Object> recordInfo = new HashMap<>();

        // 根据传参构造不同的接口参数，下单0、签收1、改单2
        // 下单
        if (Objects.equals(autoAddressSortingDTO.getSortingType(), 0)) {
            List<PreSortingDTO> preSortingList = new ArrayList<>();
            PreSortingDTO preOrderCreate = getPreOrderCreate(autoAddressSortingDTO);
            preSortingList.add(preOrderCreate);
            String url = urlPrefix + "test/preOrderCreate";
            // 发起调用
            httpRes = doExecutor.doCommonHttpPostJson(preSortingList, header, url);
            // 解析结果

            recordInfo = this.parseSortingRecord(httpRes, autoAddressSortingDTO.getSortingType(), preSortingList, autoAddressSortingDTO.getWaybillNo());
        }
        // 签收
        else if (Objects.equals(autoAddressSortingDTO.getSortingType(), 1)) {
            DeliveredInfoDo deliveredInfoDo = getDeliveredOrder(autoAddressSortingDTO);
            String url = urlPrefix + "test/delivered";
            httpRes = doExecutor.doCommonHttpPostJson(deliveredInfoDo, header, url);

            recordInfo = this.parseSortingRecord(httpRes, autoAddressSortingDTO.getSortingType(), deliveredInfoDo, autoAddressSortingDTO.getWaybillNo());
        }
        // 改单
        else if (Objects.equals(autoAddressSortingDTO.getSortingType(), 2)) {
            ChangeOrderDO changeOrderDO = getChangeOrder(autoAddressSortingDTO);
            String url = urlPrefix + "test/orderChange";
            httpRes = doExecutor.doCommonHttpPostJson(changeOrderDO, header, url);

            recordInfo = this.parseSortingRecord(httpRes, autoAddressSortingDTO.getSortingType(), changeOrderDO, autoAddressSortingDTO.getWaybillNo());
        }
        return JSON.toJSONString(recordInfo);
    }

    private Map<String, Object> parseSortingRecord(String res, Integer sortType, Object o1, String waybillNo) {

        Map<String, Object> recordInfo = new HashMap<>();
        LambdaQueryWrapper<WriteBackResultDTO> writeBackResultDTOLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (waybillNo == null){
            recordInfo.put("ErrMsg","运单号为空");
            recordInfo.put("status", "failure");
            recordInfo.put("sortResult", "false");
            return recordInfo;
        }
        if (res.contains("异常") || res.contains("failure")) {
            Map<String, Object> httpsResMap = JSON.parseObject(res, Map.class);
            log.info("httpRes:{}", httpsResMap);
            log.info("code:{}", httpsResMap.get("resultCode"));
            if (Objects.equals(httpsResMap.get("resultCode"), "2000")) {
                recordInfo.put("ErrMsg", httpsResMap.get("message"));
                recordInfo.put("status", "failure");
                recordInfo.put("sortResult", "false");
                recordInfo.put("traceId",httpsResMap.get("traceId"));
                recordInfo.put("waybillNo",waybillNo);
                return recordInfo;
            }
            return httpsResMap;
        } else if (Objects.equals(sortType, 0)) {
            writeBackResultDTOLambdaQueryWrapper.eq(WriteBackResultDTO::getWaybillNo, waybillNo);
            WriteBackResultDTO writeBackResultDTO = writeBackRecordMapper.selectOne(writeBackResultDTOLambdaQueryWrapper);

            List<PreSortingRecordDTO> preSortingRecord = JSON.parseArray(res, PreSortingRecordDTO.class);
            for (PreSortingRecordDTO recordDTO : preSortingRecord) {
//                sortingRecordDTOLambdaQueryWrapper.eq(OrderSortingRecordDTO::getId, recordDTO.getSortRecordId());

                MatchPointRecordDTO matchPointRecordDTO = matchPointRecordMapper.getMatchPoint(recordDTO.getBusinessNumber());
                OrderSortingRecordDTO orderSortingRecordDTO = orderSortingRecordMapper.getSortRes(recordDTO.getBusinessNumber());

                // 对比分拣记录和坐标获取记录
                this.sortingMatchCompare(orderSortingRecordDTO, matchPointRecordDTO, recordInfo);

                if (orderSortingRecordDTO != null && matchPointRecordDTO != null) {
                    if (orderSortingRecordDTO.getIsSortSuccess() == 1) {
                        // 个人地址库
                        if (Objects.equals(orderSortingRecordDTO.getInputGeocodingFetchWay(), "12")) {
                            recordInfo.put("pointFetchWay", MatchFetchWay.USER_INFO_MATCH);
                            recordInfo.put("pointFetchWayDesc", MatchFetchWay.USER_INFO_MATCH.getSortingType());
                        }
                        // 标准地址库
                        if (Objects.equals(orderSortingRecordDTO.getInputGeocodingFetchWay(), "13")) {
                            recordInfo.put("pointFetchWay", MatchFetchWay.STANDARD_ADDRESS_MATCH);
                            recordInfo.put("pointFetchWayDesc", MatchFetchWay.STANDARD_POINT_MATCH.getSortingType());
                        }
                        // 自带坐标
                        if (Objects.equals(orderSortingRecordDTO.getInputGeocodingFetchWay(), "1")) {
                            recordInfo.put("pointFetchWay", MatchFetchWay.USER_DEFINITION);
                            recordInfo.put("pointFetchWayDesc", MatchFetchWay.USER_DEFINITION.getSortingType());
                        }
                        // 地图服务商
                        if (Objects.equals(orderSortingRecordDTO.getInputGeocodingFetchWay(), "40")) {
                            recordInfo.put("pointFetchWay", MatchFetchWay.MAP_SERVICE);
                            recordInfo.put("pointFetchWayDesc", MatchFetchWay.MAP_SERVICE.getSortingType());
                        }
                        //检查回写表，聚合数据
                        this.getWriteRecordInfo(writeBackResultDTO, orderSortingRecordDTO, recordInfo);

                    } else {
                        recordInfo.put("pointSource", -1);
                        recordInfo.put("pointSourceDesc", "分拣失败");
                        recordInfo.put("geoCoding",matchPointRecordDTO.getPoint());
                        recordInfo.put("orderNo",waybillNo);
                    }
                    // 坐标原始来源
                    if (matchPointRecordDTO.getIsMatchSuccess() == 1) {
                        this.getPointOriginalSource(orderSortingRecordDTO.getInputGeocodingSource(), recordInfo);
                    }
                    else {
                        recordInfo.put("pointFetchWay", -1);
                        recordInfo.put("pointFetchWayDesc", "坐标匹配失败");
                    }

                    // 字段聚合（db-res）
                    if (orderSortingRecordDTO.getIsSortSuccess() == 1 && recordDTO.getIsSuccess()) {
                        recordInfo.put("sortResult", "success");
                        recordInfo.put("pointFetchWay", orderSortingRecordDTO.getInputGeocodingFetchWay());
                        recordInfo.put("orderNo", recordDTO.getBusinessNumber());
                        recordInfo.put("sortStrategy", orderSortingRecordDTO.getSortStrategy());
                        recordInfo.put("sortType", orderSortingRecordDTO.getSortType());
                        recordInfo.put("inputStationCode", recordDTO.getStationInfo().getStationCode());
                        recordInfo.put("sortedStationCode", orderSortingRecordDTO.getSortStationCode());
                        recordInfo.put("sortedGeofenceCode", orderSortingRecordDTO.getSortGeofenceCode());
                        recordInfo.put("trustValue", matchPointRecordDTO.getTrustValue());
                        recordInfo.put("msg", "true");
                        recordInfo.put("matchPointResult", matchPointRecordDTO.getIsMatchSuccess());
                        recordInfo.put("addressCode", matchPointRecordDTO.getAddressCode());
                        recordInfo.put("geocode", matchPointRecordDTO.getPoint());
                    }
                }
            }
        }
        //签收
        if (Objects.equals(sortType, 1)) {
            DeliveredInfoDo deliveredInfoDo = (DeliveredInfoDo) o1;
            writeBackResultDTOLambdaQueryWrapper.eq(WriteBackResultDTO::getWaybillNo, waybillNo);
            WriteBackResultDTO writeBackResultDTO = writeBackRecordMapper.selectOne(writeBackResultDTOLambdaQueryWrapper);

            if (Objects.equals(res, "\"操作成功\"")) {
                MatchPointRecordDTO matchPointRecordDTO = matchPointRecordMapper.getMatchPoint(deliveredInfoDo.getWaybillNo());
                OrderSortingRecordDTO orderSortingRecordDTO = orderSortingRecordMapper.getSortRes(deliveredInfoDo.getWaybillNo());

                this.sortingMatchCompare(orderSortingRecordDTO, matchPointRecordDTO, recordInfo);

                if (orderSortingRecordDTO != null) {
                    // 签收订单在坐标匹配表中无记录，需要单独查询个人地址库和标准地址库（订单只要签收了，必定会落个人地址库）
                    UserInfoDo userInfo = userInfoMapper.getUserInfo(orderSortingRecordDTO.getInputAddressCode());
                    if (userInfo != null) {
                        recordInfo.put("trustValue", userInfo.getTrustValue());
                        recordInfo.put("addressCode", userInfo.getAddressCode());
                        recordInfo.put("geocode", userInfo.getPoint());
                        this.getWriteRecordInfo(writeBackResultDTO, orderSortingRecordDTO, recordInfo);
                    }
                    // 坐标原始来源

                    this.getPointOriginalSource(orderSortingRecordDTO.getInputGeocodingSource(), recordInfo);

                    // 字段聚合（db-res）
                    if (orderSortingRecordDTO.getIsSortSuccess() == 1) {
                        recordInfo.put("sortResult", "success");
                        recordInfo.put("pointFetchWay", orderSortingRecordDTO.getInputGeocodingFetchWay());
                        recordInfo.put("orderNo", orderSortingRecordDTO.getInputBusinessNumber());
                        recordInfo.put("sortStrategy", orderSortingRecordDTO.getSortStrategy());
                        recordInfo.put("sortType", orderSortingRecordDTO.getSortType());
                        recordInfo.put("inputStationCode", deliveredInfoDo.getOcCode());
                        recordInfo.put("sortedStationCode", orderSortingRecordDTO.getSortStationCode());
                        recordInfo.put("sortedGeofenceCode", orderSortingRecordDTO.getSortGeofenceCode());
                        recordInfo.put("msg", "true");
                        recordInfo.put("pointSource", MatchFetchWay.DELIVERED);
                        recordInfo.put("pointSourceDesc", MatchFetchWay.DELIVERED.getSortingType());
//                        recordInfo.put("matchPointResult", matchPointRecordDTO.getIsMatchSuccess());
                    }else {
                        recordInfo.put("pointSource", -1);
                        recordInfo.put("pointSourceDesc", "分拣失败");
                        recordInfo.put("orderNo",waybillNo);
                        recordInfo.put("geoCoding",matchPointRecordDTO.getPoint());
                        return recordInfo;
                    }
                }
            }

        }

        // 改单
        if (Objects.equals(sortType, 2)) {
            writeBackResultDTOLambdaQueryWrapper.eq(WriteBackResultDTO::getWaybillNo, waybillNo);
            WriteBackResultDTO writeBackResultDTO = writeBackRecordMapper.selectOne(writeBackResultDTOLambdaQueryWrapper);

            // 改单返回的json有额外的信息，不过基本信息都在PreSortingRecordDTO中有，但是改单不会返回orderNo，需要单独处理
            PreSortingRecordDTO recordDTO = JSON.parseObject(res, PreSortingRecordDTO.class);
            recordDTO.setBusinessNumber(waybillNo);

            ChangeOrderDO changeOrderDO = (ChangeOrderDO) o1;
            MatchPointRecordDTO matchPointRecordDTO = matchPointRecordMapper.getMatchPoint(changeOrderDO.getWaybillNO());
            OrderSortingRecordDTO orderSortingRecordDTO = orderSortingRecordMapper.getSortRes(changeOrderDO.getWaybillNO());

            // 对比分拣记录和坐标获取记录
            this.sortingMatchCompare(orderSortingRecordDTO, matchPointRecordDTO, recordInfo);

            if (orderSortingRecordDTO != null && matchPointRecordDTO != null) {
                // 坐标获取方式
                if (orderSortingRecordDTO.getIsSortSuccess() == 1) {
                    // 个人地址库
                    if (Objects.equals(orderSortingRecordDTO.getInputGeocodingFetchWay(), "12")) {
                        recordInfo.put("pointFetchWay", MatchFetchWay.USER_INFO_MATCH);
                        recordInfo.put("pointFetchWayDesc", MatchFetchWay.USER_INFO_MATCH.getSortingType());
                    }
                    // 标准地址库
                    if (Objects.equals(orderSortingRecordDTO.getInputGeocodingFetchWay(), "13")) {
                        recordInfo.put("pointFetchWay", MatchFetchWay.STANDARD_ADDRESS_MATCH);
                        recordInfo.put("pointFetchWayDesc", MatchFetchWay.STANDARD_POINT_MATCH.getSortingType());
                    }
                    // 自带坐标
                    if (Objects.equals(orderSortingRecordDTO.getInputGeocodingFetchWay(), "1")) {
                        recordInfo.put("pointFetchWay", MatchFetchWay.USER_DEFINITION);
                        recordInfo.put("pointFetchWayDesc", MatchFetchWay.USER_DEFINITION.getSortingType());
                    }
                    // 地图服务商
                    if (Objects.equals(orderSortingRecordDTO.getInputGeocodingFetchWay(), "40")) {
                        recordInfo.put("pointFetchWay", MatchFetchWay.MAP_SERVICE);
                        recordInfo.put("pointFetchWayDesc", MatchFetchWay.MAP_SERVICE.getSortingType());
                    }
                    this.getWriteRecordInfo(writeBackResultDTO, orderSortingRecordDTO, recordInfo);

                } else {
                    recordInfo.put("pointSource", -1);
                    recordInfo.put("pointSourceDesc", "分拣失败");
                    recordInfo.put("orderNo",waybillNo);
                    recordInfo.put("geoCoding",matchPointRecordDTO.getPoint());
                    return recordInfo;
                }
                // 坐标原始来源
                if (matchPointRecordDTO.getIsMatchSuccess() == 1) {
                    this.getPointOriginalSource(orderSortingRecordDTO.getInputGeocodingSource(), recordInfo);
                }
                else {
                    recordInfo.put("pointFetchWay", -1);
                    recordInfo.put("pointFetchWayDesc", "坐标匹配失败");
                }
                // 字段聚合（db-res）
                if (orderSortingRecordDTO.getIsSortSuccess() == 1 && recordDTO.getIsSuccess()) {
                    recordInfo.put("sortResult", "success");
                    recordInfo.put("pointFetchWay", orderSortingRecordDTO.getInputGeocodingFetchWay());
                    recordInfo.put("orderNo", recordDTO.getBusinessNumber());
                    recordInfo.put("sortStrategy", orderSortingRecordDTO.getSortStrategy());
                    recordInfo.put("sortType", orderSortingRecordDTO.getSortType());
                    recordInfo.put("inputStationCode", recordDTO.getStationInfo().getStationCode());
                    recordInfo.put("sortedStationCode", orderSortingRecordDTO.getSortStationCode());
                    recordInfo.put("sortedGeofenceCode", orderSortingRecordDTO.getSortGeofenceCode());
                    recordInfo.put("trustValue", matchPointRecordDTO.getTrustValue());
                    recordInfo.put("msg", "true");
                    recordInfo.put("matchPointResult", matchPointRecordDTO.getIsMatchSuccess());
                    recordInfo.put("addressCode", matchPointRecordDTO.getAddressCode());
                    recordInfo.put("geocode", matchPointRecordDTO.getPoint());
                }
            }

        }
        return recordInfo;
    }

    /**
     * 预分拣下单接口
     *
     * @param autoAddressSortingDTO
     * @return
     */
    private PreSortingDTO getPreOrderCreate(AutoAddressSortingDTO autoAddressSortingDTO) {
        // 封装参数
        PreSortingDTO preSortingDTO = new PreSortingDTO();
        preSortingDTO.setArea(autoAddressSortingDTO.getConsigneeArea());
        preSortingDTO.setCity(autoAddressSortingDTO.getConsigneeCity());
        preSortingDTO.setCountry(autoAddressSortingDTO.getConsigneeCountry());
        preSortingDTO.setDetailAddress(autoAddressSortingDTO.getConsigneeAddress());
        preSortingDTO.setOrgId(10);
        preSortingDTO.setPhone(autoAddressSortingDTO.getConsigneePhone());
        preSortingDTO.setProvince(autoAddressSortingDTO.getConsigneeProvince());
        preSortingDTO.setZipCode(autoAddressSortingDTO.getConsigneeZipCode());
        preSortingDTO.setBusinessNumber(autoAddressSortingDTO.getWaybillNo());
        //如果传了坐标
        if (autoAddressSortingDTO.getLongitude() != null && autoAddressSortingDTO.getLatitude() != null) {
            PointInfoDTO pointInfoDTO = new PointInfoDTO();
            pointInfoDTO.setLatitude(autoAddressSortingDTO.getLatitude());
            pointInfoDTO.setLongitude(autoAddressSortingDTO.getLongitude());
            preSortingDTO.setPoint(pointInfoDTO);
        }
        return preSortingDTO;
    }

    /**
     * 订单签收接口
     *
     * @param autoAddressSortingDTO
     * @return
     */
    private DeliveredInfoDo getDeliveredOrder(AutoAddressSortingDTO autoAddressSortingDTO) {
        if (autoAddressSortingDTO.getDeliverLatitude() == null && autoAddressSortingDTO.getDeliverLongitude() == null) {
            log.error("签收坐标为空");
            return null;
        }
        if (autoAddressSortingDTO.getDeliverLongitude() == null || autoAddressSortingDTO.getDeliverLatitude() == null) {
            log.error("签收坐标非法");
            return null;
        }
        DeliveredInfoDo deliveredInfoDo = new DeliveredInfoDo();
        BeanUtils.copyProperties(autoAddressSortingDTO, deliveredInfoDo);
        deliveredInfoDo.setConsigneeExternalNumber("10086");
        deliveredInfoDo.setConsigneeInternalNumber("10086");
        deliveredInfoDo.setCountry(autoAddressSortingDTO.getConsigneeCountry());
        deliveredInfoDo.setCourier(RandomStringUtils.randomAlphanumeric(7));
        deliveredInfoDo.setCourierCode("CO" + uniqueIdGenerator.idGenerator());
        deliveredInfoDo.setDaCode("DO" + uniqueIdGenerator.idGenerator());
        deliveredInfoDo.setDeliveryMethod(0);
        deliveredInfoDo.setDistanceConfirmResult("success");
        deliveredInfoDo.setOcCode("S2101391501");
        deliveredInfoDo.setWaybillNo(autoAddressSortingDTO.getWaybillNo());
        deliveredInfoDo.setLongitude(autoAddressSortingDTO.getDeliverLongitude());
        deliveredInfoDo.setLatitude(autoAddressSortingDTO.getDeliverLatitude());
        return deliveredInfoDo;
    }


    /**
     * 改单接口
     *
     * @param autoAddressSortingDTO
     * @return
     */
    private ChangeOrderDO getChangeOrder(AutoAddressSortingDTO autoAddressSortingDTO) {
        ChangeOrderDO changeOrderDO = new ChangeOrderDO();
        BeanUtils.copyProperties(autoAddressSortingDTO, changeOrderDO);
        changeOrderDO.setWaybillNO(autoAddressSortingDTO.getWaybillNo());
        changeOrderDO.setClientCode("CC" + uniqueIdGenerator.idGenerator());
        changeOrderDO.setClientName(RandomStringUtils.randomAlphanumeric(6));
        changeOrderDO.setConsignee(RandomStringUtils.randomAlphanumeric(6));
        changeOrderDO.setConsigneeExternalNumber("10086");
        changeOrderDO.setConsigneeInternalNumber("10086");
        changeOrderDO.setConsigneeLatitude(autoAddressSortingDTO.getDeliverLatitude());
        changeOrderDO.setConsigneeLongitude(autoAddressSortingDTO.getDeliverLatitude());
        changeOrderDO.setShippingMethod(0);
        return changeOrderDO;
    }

    // 分拣记录&坐标获取匹配
    private void sortingMatchCompare(OrderSortingRecordDTO orderSortingRecordDTO, MatchPointRecordDTO matchPointRecordDTO, Map<String, Object> recordInfo) {
        // 分拣记录表和坐标匹配表都为空
        if (orderSortingRecordDTO == null && matchPointRecordDTO == null) {
            log.error("不存在任何记录");
            recordInfo.put("msg", "不存在任何记录");
        }
        // 分拣记录表为空当时坐标匹配表不为空
        if (orderSortingRecordDTO == null && matchPointRecordDTO != null) {
            log.error("分拣异常");
            recordInfo.put("msg", "分拣异常");
        }
    }

    // 回写表拿数据
    private void getWriteRecordInfo(WriteBackResultDTO writeBackResultDTO, OrderSortingRecordDTO orderSortingRecordDTO, Map<String, Object> recordInfo) {
        if (writeBackResultDTO != null) {
            // 对比回写表数据和分拣记录数据
            if (Objects.equals(writeBackResultDTO.getSortRecordId(), orderSortingRecordDTO.getId())) {
                WriteMqJsonDTO writeMqJsonDTO = JSON.parseObject(writeBackResultDTO.getResult(), WriteMqJsonDTO.class);
                log.info("{}",writeMqJsonDTO);
                writeBackResultDTO.setResult("");
                recordInfo.put("writeBackInfo", writeBackResultDTO);
                recordInfo.put("writeBackJson",writeMqJsonDTO);
            }else {
                log.info("回写数据异常");
                recordInfo.put("writeBackInfo","回写数据异常");
            }
        }
    }

    // 记录坐标原始来源
    private void getPointOriginalSource(String pointSource, Map<String, Object> recordInfo) {
        switch (pointSource) {
            case "1":
                recordInfo.put("原始坐标获取方式", MatchFetchWay.USER_DEFINITION.getSortingType());
                break;
            case "6":
                recordInfo.put("原始坐标获取方式", MatchFetchWay.GOOGLE_MAPS.getSortingType());
                break;
            case "9":
                recordInfo.put("原始坐标获取方式", MatchFetchWay.USER_APPOINTMENT.getSortingType());
                break;
            case "11":
                recordInfo.put("原始坐标获取方式", MatchFetchWay.DELIVERED.getSortingType());
                break;
            case "12":
                recordInfo.put("原始坐标获取方式", MatchFetchWay.USER_INFO_MATCH.getSortingType());
                break;
            case "13":
                recordInfo.put("原始坐标获取方式", MatchFetchWay.STANDARD_POINT_MATCH.getSortingType());
                break;
        }
    }

    // DTO转为Map
    private Map<String, Object> DTO2MapConverter(Object dto) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = dto.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(dto);
                map.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                log.error("转换异常:{}", e.getMessage());
            }
        }
        return map;
    }

}
