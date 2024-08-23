package com.zane.core.order.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.zane.base.GlobalVariables;
import com.zane.base.baseEnum.OrderType;
import com.zane.base.baseEnum.SortingType;
import com.zane.base.handler.DoExecutor;
import com.zane.core.order.CreateOrderService;
import com.zane.dao.*;
import com.zane.dao.dto.CreateOrderDTO;
import com.zane.dao.dto.OrderTMSInfo;
import com.zane.utils.UniqueIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class CreateOrderServiceImpl implements CreateOrderService {

    @Resource
    private DoExecutor doExecutor;

    @Override
    public String createOrder(CreateOrderDTO createOrderDTO) {
        UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator();
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";

        // 非巴西下单
        if (!StringUtils.equals("BRA", createOrderDTO.getCountry())) {
            OrderDetailDo orderDetailDo = new OrderDetailDo();
            url = "https://test-api.52imile.cn/openapi/client/order/createOrder";
            // 封装下单参数
            // skuDetailList
            List<SkuDetailDo> skuDetailDos = new ArrayList<>();
            SkuDetailDo skuDetailDo = new SkuDetailDo();
            String skuNo = "sw" + RandomStringUtils.randomNumeric(8);
            skuDetailDo.setSkuNo(skuNo);
            skuDetailDo.setSkuName("skirt");
            skuDetailDo.setSkuDesc("100% SILK OMBRE WRAP SKIRT");
            skuDetailDo.setSkuQty(1);
            skuDetailDo.setSkuUrl("http://www.shein.com/MOTF-X-KANDINSKY-INSPIRED-1--p-10303866.html");
            skuDetailDo.setSkuGoodsValue(55.01);
            skuDetailDo.setSkuWeight(0.316);
            skuDetailDo.setSkuHsCode("640620200022");
            skuDetailDos.add(skuDetailDo);

            orderDetailDo.setDispatchDate("2024/01/24");
            orderDetailDo.setOrderCode(uniqueIdGenerator.idGenerator());
            if (Objects.equals(createOrderDTO.getIsFMOrder(), "true")) {
                orderDetailDo.setOrderType(OrderType.FM_ORDER.getOrderTypeCode());
            } else {
                orderDetailDo.setOrderType(OrderType.COMMON_ORDER.getOrderTypeCode());
            }
            orderDetailDo.setDeliveryType("Delivery");
            orderDetailDo.setConsignor("AutoCHN");
            orderDetailDo.setCurrency("USD");
            orderDetailDo.setConsigneeContact(RandomStringUtils.randomAlphabetic(4));
            String phoneNumber = RandomStringUtils.randomNumeric(12);
            orderDetailDo.setConsigneeMobile("+" + phoneNumber);
            orderDetailDo.setConsigneePhone("+" + phoneNumber);
            orderDetailDo.setConsigneeEmail("asdasj.zhang@imile.me");
            orderDetailDo.setConsigneeCountry(createOrderDTO.getCountry());
            orderDetailDo.setConsigneeProvince("Makkah Province");
            orderDetailDo.setConsigneeCity("Makkah City");
            orderDetailDo.setConsigneeAddress(RandomStringUtils.randomAlphabetic(7));
            orderDetailDo.setConsigneestationcode("");
            orderDetailDo.setGoodsValue(2);
            orderDetailDo.setCollectingMoney(7);
            orderDetailDo.setPaymentMethod("100");
            orderDetailDo.setPickType(1);
            orderDetailDo.setPickDate("2023/12/30");
            orderDetailDo.setTotalCount(1);
            orderDetailDo.setTotalWeight(8.0);
            orderDetailDo.setTotalVolume(44363);
            orderDetailDo.setSkuTotal(27);
            orderDetailDo.setSkuName("testSku");
            orderDetailDo.setSkuDetailList(skuDetailDos);
            orderDetailDo.setBuyerId("1027808680");
            if (Objects.equals(createOrderDTO.getIsGeoCode(), "true")) {
                orderDetailDo.setConsigneeLatitude("21.36954243455747");
                orderDetailDo.setConsigneeLongitude("39.21107560187266");
            }
            paramMap.put("param", orderDetailDo);
            paramMap.put("sign", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrRFB4KAEyIx43d");
            paramMap.put("customerId", "C21016277");
            paramMap.put("format", "json");
            paramMap.put("version", "1.0.0");
            paramMap.put("signMethod", "simpleKey");
            paramMap.put("timestamp", System.currentTimeMillis());
            paramMap.put("accessToken", "3a848037-3546-4479-afbf-c7b9dc7f4ceb");
        }
        // 巴西下单
        else {
            ExtraInfoDo extraInfoDo = new ExtraInfoDo();
            PackageInfoDo packageInfoDo = new PackageInfoDo();
            ConsigneeInfoDo consigneeInfoDo = new ConsigneeInfoDo();
            SenderInfoDo senderInfoDo = new SenderInfoDo();
            ServiceInfoDo serviceInfoDo = new ServiceInfoDo();
            List<SkuDetailDo> skuDetailDos = new ArrayList<>();
            SkuDetailDo skuDetailDo = new SkuDetailDo();
            BRAOrderDetailDo braOrderDetailDo = new BRAOrderDetailDo();
            url = "https://test-openapi.52imile.cn/client/order/v2/createOrder";
            braOrderDetailDo.setLabelType("");
            braOrderDetailDo.setOrderNo(RandomStringUtils.randomAlphabetic(3) + uniqueIdGenerator.idGenerator().substring(5,8));
            braOrderDetailDo.setCustomerVip("1");


            extraInfoDo.setBagCount(0);
            extraInfoDo.setBagNo("001");
            extraInfoDo.setClientScanNo("");
            extraInfoDo.setClientSortCode("");

            packageInfoDo.setCodCurrency("");
            packageInfoDo.setProductValue("1");
            packageInfoDo.setClientDeclaredValue("5");
            packageInfoDo.setClientDeclaredCurrency("USD");
            packageInfoDo.setProductValueCurrency("USD");
            packageInfoDo.setWidth(0);
            packageInfoDo.setGoodsType("Normal");
            packageInfoDo.setGrossWeight("1");
            packageInfoDo.setHigh(0);
            packageInfoDo.setTotalVolume(0);
            packageInfoDo.setTotalCount(1);
            packageInfoDo.setCollectingMoney("0");
            packageInfoDo.setLength(0);
            packageInfoDo.setPaymentMethod("PPD");

            AddressSituationDo addressSituationDo1 = new AddressSituationDo();
            AddressSituationDo addressSituationDo2 = new AddressSituationDo();
            List<AddressSituationDo> addressSituationDos = new ArrayList<>();
            addressSituationDo1.setSuburb("");
            addressSituationDo1.setAddressType("seller");
            addressSituationDo1.setBackupPhone("");
            addressSituationDo1.setContactCompany("sellerAddress");
            addressSituationDo1.setBusinessCode("123");
            addressSituationDo1.setIdCardBackImg("");
            addressSituationDo1.setLatitude(0.98);
            addressSituationDo1.setSituationType("sellerAddress");
            addressSituationDo1.setStreet("");
            addressSituationDo1.setTaxID("124");
            addressSituationDo1.setArea("");
            addressSituationDo1.setCountry(createOrderDTO.getCountry());
            addressSituationDo1.setEmail("");
            addressSituationDo1.setIdNo("");
            addressSituationDo1.setLongitude(0.99);
            addressSituationDo1.setProvince("Rio Province");
            addressSituationDo1.setZipCode("3789-398");
            addressSituationDo1.setAddress("TESTNG_BRA_卖家");
            addressSituationDo1.setContacts("TESTNG_BRA_卖家");
            addressSituationDo1.setPhone("123456");
            addressSituationDo1.setStateRegisterNo("9n1cchsugy");
            addressSituationDo1.setCity("Rio City");
            addressSituationDo1.setTaxIDType(1);

            addressSituationDo2.setSuburb("");
            addressSituationDo2.setAddressType("seller");
            addressSituationDo2.setBackupPhone("");
            addressSituationDo2.setContactCompany("buyerAddress");
            addressSituationDo2.setBusinessCode("123");
            addressSituationDo2.setIdCardBackImg("");
            addressSituationDo2.setLatitude(0.60);
            addressSituationDo2.setSituationType("buyerAddress");
            addressSituationDo2.setStreet("");
            addressSituationDo2.setTaxID("445");
            addressSituationDo2.setArea("");
            addressSituationDo2.setCountry(createOrderDTO.getCountry());
            addressSituationDo2.setEmail("");
            addressSituationDo2.setIdNo("");
            addressSituationDo2.setLongitude(0.99);
            addressSituationDo2.setProvince("Rio Province");
            addressSituationDo2.setZipCode("3789-398");
            addressSituationDo2.setAddress("TESTNG_BRA_卖家");
            addressSituationDo2.setContacts("TESTNG_BRA_卖家");
            addressSituationDo2.setPhone("123456");
            addressSituationDo2.setStateRegisterNo("rto18129ph");
            addressSituationDo2.setCity("Rio City");
            addressSituationDo2.setTaxIDType(1);

            skuDetailDo.setSkuQty(1);
            skuDetailDo.setSkuWeight(2.8);
            skuDetailDo.setSkuHsCode("skuHsCode1");
            skuDetailDo.setSkuDesc("skuDesc中文");
            skuDetailDo.setSkuUrl("skuUrl");
            skuDetailDo.setSkuName("skuName");
            skuDetailDo.setSkuNo("skuNo");
            skuDetailDos.add(skuDetailDo);

            addressSituationDos.add(addressSituationDo1);
            addressSituationDos.add(addressSituationDo2);

            consigneeInfoDo.setCountry(createOrderDTO.getCountry());
            consigneeInfoDo.setIdNo("888");
            consigneeInfoDo.setBirthDate("2000-01-01");
            consigneeInfoDo.setIdExpiredDate("2024-01-01");
            consigneeInfoDo.setCalendarType("");
            consigneeInfoDo.setConsignee("auto-test");
            consigneeInfoDo.setContactCompany("auto-test");
            consigneeInfoDo.setContacts("auto-test");
            consigneeInfoDo.setPhone(RandomStringUtils.randomNumeric(11));
            consigneeInfoDo.setBackupPhone(RandomStringUtils.randomNumeric(6));
            consigneeInfoDo.setConsigneeEmail("werqwe@fsdf.cd");
            consigneeInfoDo.setLa(List.of(0));
            consigneeInfoDo.setLatitude(null);
            consigneeInfoDo.setLongitude(null);
            consigneeInfoDo.setProvince("São Paulo");
            consigneeInfoDo.setCity("Taboão da Serra");
            consigneeInfoDo.setArea("Parque Jacarandá");
            consigneeInfoDo.setAddress("Dolores Romero,125 Rua açougue tropical");
            consigneeInfoDo.setZipCode("06774-170");

            consigneeInfoDo.setTaxID("567");
            consigneeInfoDo.setTaxIDType(1);
            consigneeInfoDo.setAddressType("warehouse");
            consigneeInfoDo.setSellerId("88899603783");

            senderInfoDo.setSellerID("88899603783");
            senderInfoDo.setCountry(createOrderDTO.getCountry());
            senderInfoDo.setContactCompany("auto-test");
            senderInfoDo.setContacts("auto-test-man");
            senderInfoDo.setBackupPhone("");
            senderInfoDo.setPhone("+" + RandomStringUtils.randomNumeric(7));
            senderInfoDo.setProvince("São Paulo");
            senderInfoDo.setCity("Taboão da Serra");
            senderInfoDo.setArea("Parque Jacarandá");
            senderInfoDo.setAddress("Dolores Romero,125 Rua açougue tropical");
            senderInfoDo.setZipCode("06774-170");
            senderInfoDo.setSuburb("");
            senderInfoDo.setAddressType("seller");
            senderInfoDo.setTaxID("123");
            senderInfoDo.setTaxIDType(2);

            serviceInfoDo.setDeliveryService("Self");
            serviceInfoDo.setExpectedPickupDate("2023-12-20");
            serviceInfoDo.setLogisticsProductCode("LP21003801");
            serviceInfoDo.setTemperatureService(0);
            serviceInfoDo.setPickupService(1);
            serviceInfoDo.setDeliveryRequirements("自动化测试");

            if (Objects.equals(createOrderDTO.getIsFMOrder(), "true")){
                braOrderDetailDo.setOrderType(OrderType.FM_ORDER.getOrderTypeCode());
                // 标准地址库且FM订单
                if (Objects.equals(SortingType.STANDARD_ADDRESS_SORTING.getSortingTypeCode(),createOrderDTO.getSortingCode())) {
                    senderInfoDo.setPhone("+" + RandomStringUtils.randomNumeric(7));
                    senderInfoDo.setProvince("São Paulo");
                    senderInfoDo.setCity("Taboão da Serra");
                    senderInfoDo.setArea("Parque Jacarandá");
                    senderInfoDo.setAddress("Dolores Romero,125 Rua açougue tropical");
                    senderInfoDo.setZipCode("12345-345");
                }
                // 个人地址库且FM订单
                else if (Objects.equals(SortingType.USER_INFO_SORTING.getSortingTypeCode(),createOrderDTO.getSortingCode())){
                    senderInfoDo.setProvince("Sao Paulo Province");
                    senderInfoDo.setCity("Sao Paulo City");
                    senderInfoDo.setArea("332");
                    senderInfoDo.setAddress("uiuif8895644331");
                    senderInfoDo.setZipCode("99999-123");
                    senderInfoDo.setPhone("+55001945945");
                }
                else {
                    // 卖家地址库
                    senderInfoDo.setProvince("Rio Province");
                    senderInfoDo.setCity("Rio City");
                    senderInfoDo.setArea("Parque Jacarandá");
                    senderInfoDo.setAddress("mmkkok9-009923223");
                    senderInfoDo.setZipCode("12345-345");
                    senderInfoDo.setPhone("+966580250383");
                }

            }
            else {
                braOrderDetailDo.setOrderType(OrderType.COMMON_ORDER.getOrderTypeCode());
                // 标准地址库且普通订单
                if (Objects.equals(SortingType.STANDARD_ADDRESS_SORTING.getSortingTypeCode(),createOrderDTO.getSortingCode())){
                    consigneeInfoDo.setProvince("São Paulo");
                    consigneeInfoDo.setCity("São Paulo");
                    consigneeInfoDo.setArea("Parque Esmeralda");
                    consigneeInfoDo.setAddress("Rua Padre José Antônio Romano 300, bloco B 128");
                    consigneeInfoDo.setZipCode("05784-120");
                }
                // 个人地址库且普通订单
                else if (Objects.equals(SortingType.USER_INFO_SORTING.getSortingTypeCode(),createOrderDTO.getSortingCode())){
                    consigneeInfoDo.setProvince("Sao Paulo Province");
                    consigneeInfoDo.setCity("Sao Paulo City");
                    consigneeInfoDo.setArea("332");
                    consigneeInfoDo.setAddress("uiuif8895644331");
                    consigneeInfoDo.setZipCode("99999-123");
                    consigneeInfoDo.setPhone("+55001945945");
                }
                else {
                    // 卖家地址库
                    consigneeInfoDo.setProvince("Rio Province");
                    consigneeInfoDo.setCity("Rio City");
                    consigneeInfoDo.setArea("Parque Jacarandá");
                    consigneeInfoDo.setAddress("mmkkok9-009923223");
                    consigneeInfoDo.setZipCode("12345-345");
                    consigneeInfoDo.setPhone("+966580250383");
                }
            }
            braOrderDetailDo.setExtraInfo(extraInfoDo);
            braOrderDetailDo.setSkuInfos(skuDetailDos);
            braOrderDetailDo.setPackageInfo(packageInfoDo);
            braOrderDetailDo.setAddressSituation(addressSituationDos);
            braOrderDetailDo.setConsigneeInfo(consigneeInfoDo);
            braOrderDetailDo.setSenderInfo(senderInfoDo);
            braOrderDetailDo.setServiceInfo(serviceInfoDo);

            paramMap.put("param",braOrderDetailDo);
            paramMap.put("timestamp",System.currentTimeMillis());
            paramMap.put("accessToken","2c6d79a1-8f34-45ca-b03b-8e4b9a17bb71");
            paramMap.put("customerId","C2101949901");
            paramMap.put("sellerId","88899605229");
            paramMap.put("timeZone","+8");
            paramMap.put("sign","MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKvUVnuWJ1nSQFV4");
            paramMap.put("signMethod","simpleKey");
            paramMap.put("version","1.0.0");
            paramMap.put("format","json");
        }
        log.info("下单参数:{}", JSON.toJSONString(paramMap));
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");


        // 发起调用
        String response = doExecutor.doCommonHttpPostJson(paramMap, headers, url);
//        logger.info("res:{}",response);
        // 拿到订单号
        String waybillNo = (String) JSONPath.read(response,"$.data.expressNo");
        log.info("拿到订单号:{}",waybillNo);
        return waybillNo;
    }



    @Override
    public String getTMSOrder(String waybillNo) {
        Map<String,Object> requestMap = new HashMap<>();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", GlobalVariables.AUTHORIZATION_CODE);
        String URL = "https://test-scs.52imile.cn/saastms/biz/waybillInfo/list";
        requestMap.put("showCount","20");
        requestMap.put("currentPage","1");
        requestMap.put("waybillNos",waybillNo);
        requestMap.put("dateSearchType","waybill");
        String resJSON = doExecutor.doCommonHttpPostForm(requestMap, headers, URL);
        return resJSON;
    }

    @Override
    public String createOrderByZipCode(CreateOrderDTO createOrderDTO) {
        UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator();
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";

        // 非巴西下单
        if (!StringUtils.equals("BRA", createOrderDTO.getCountry())) {
            OrderDetailDo orderDetailDo = new OrderDetailDo();
            url = "https://test-api.52imile.cn/openapi/client/order/createOrder";
            // 封装下单参数
            // skuDetailList
            List<SkuDetailDo> skuDetailDos = new ArrayList<>();
            SkuDetailDo skuDetailDo = new SkuDetailDo();
            String skuNo = "sw" + RandomStringUtils.randomNumeric(8);
            skuDetailDo.setSkuNo(skuNo);
            skuDetailDo.setSkuName("skirt");
            skuDetailDo.setSkuDesc("100% SILK OMBRE WRAP SKIRT");
            skuDetailDo.setSkuQty(1);
            skuDetailDo.setSkuUrl("http://www.shein.com/MOTF-X-KANDINSKY-INSPIRED-1--p-10303866.html");
            skuDetailDo.setSkuGoodsValue(55.01);
            skuDetailDo.setSkuWeight(0.316);
            skuDetailDo.setSkuHsCode("640620200022");
            skuDetailDos.add(skuDetailDo);

            orderDetailDo.setDispatchDate("2024/01/24");
            orderDetailDo.setOrderCode(uniqueIdGenerator.idGenerator());
            if (Objects.equals(createOrderDTO.getIsFMOrder(), "true")) {
                orderDetailDo.setOrderType(OrderType.FM_ORDER.getOrderTypeCode());
            } else {
                orderDetailDo.setOrderType(OrderType.COMMON_ORDER.getOrderTypeCode());
            }
            orderDetailDo.setDeliveryType("Delivery");
            orderDetailDo.setConsignor("AutoCHN");
            orderDetailDo.setCurrency("USD");
            orderDetailDo.setConsigneeContact(RandomStringUtils.randomAlphabetic(4));
            String phoneNumber = RandomStringUtils.randomNumeric(12);
            orderDetailDo.setConsigneeMobile("+" + phoneNumber);
            orderDetailDo.setConsigneePhone("+" + phoneNumber);
            orderDetailDo.setConsigneeEmail("asdasj.zhang@imile.me");
            orderDetailDo.setConsigneeCountry(createOrderDTO.getCountry());
            orderDetailDo.setConsigneeProvince("Makkah Province");
            orderDetailDo.setConsigneeCity("Makkah City");
            orderDetailDo.setConsigneeAddress(RandomStringUtils.randomAlphabetic(7));
            orderDetailDo.setConsigneestationcode("");
            orderDetailDo.setGoodsValue(2);
            orderDetailDo.setCollectingMoney(7);
            orderDetailDo.setPaymentMethod("100");
            orderDetailDo.setPickType(1);
            orderDetailDo.setPickDate("2023/12/30");
            orderDetailDo.setTotalCount(1);
            orderDetailDo.setTotalWeight(8.0);
            orderDetailDo.setTotalVolume(44363);
            orderDetailDo.setSkuTotal(27);
            orderDetailDo.setSkuName("testSku");
            orderDetailDo.setSkuDetailList(skuDetailDos);
            orderDetailDo.setBuyerId("1027808680");
            if (Objects.equals(createOrderDTO.getIsGeoCode(), "true")) {
                orderDetailDo.setConsigneeLatitude("21.36954243455747");
                orderDetailDo.setConsigneeLongitude("39.21107560187266");
            }
            paramMap.put("param", orderDetailDo);
            paramMap.put("sign", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrRFB4KAEyIx43d");
            paramMap.put("customerId", "C21016277");
            paramMap.put("format", "json");
            paramMap.put("version", "1.0.0");
            paramMap.put("signMethod", "simpleKey");
            paramMap.put("timestamp", System.currentTimeMillis());
            paramMap.put("accessToken", "3a848037-3546-4479-afbf-c7b9dc7f4ceb");
        }
        // 巴西下单
        else {
            ExtraInfoDo extraInfoDo = new ExtraInfoDo();
            PackageInfoDo packageInfoDo = new PackageInfoDo();
            ConsigneeInfoDo consigneeInfoDo = new ConsigneeInfoDo();
            SenderInfoDo senderInfoDo = new SenderInfoDo();
            ServiceInfoDo serviceInfoDo = new ServiceInfoDo();
            List<SkuDetailDo> skuDetailDos = new ArrayList<>();
            SkuDetailDo skuDetailDo = new SkuDetailDo();
            BRAOrderDetailDo braOrderDetailDo = new BRAOrderDetailDo();
            url = "https://test-openapi.52imile.cn/client/order/v2/createOrder";
            braOrderDetailDo.setLabelType("");
            braOrderDetailDo.setOrderNo(RandomStringUtils.randomAlphabetic(3) + uniqueIdGenerator.idGenerator().substring(5,8));
            braOrderDetailDo.setCustomerVip("1");


            extraInfoDo.setBagCount(0);
            extraInfoDo.setBagNo("001");
            extraInfoDo.setClientScanNo("");
            extraInfoDo.setClientSortCode("");

            packageInfoDo.setCodCurrency("");
            packageInfoDo.setProductValue("1");
            packageInfoDo.setClientDeclaredValue("5");
            packageInfoDo.setClientDeclaredCurrency("USD");
            packageInfoDo.setProductValueCurrency("USD");
            packageInfoDo.setWidth(0);
            packageInfoDo.setGoodsType("Normal");
            packageInfoDo.setGrossWeight("1");
            packageInfoDo.setHigh(0);
            packageInfoDo.setTotalVolume(0);
            packageInfoDo.setTotalCount(1);
            packageInfoDo.setCollectingMoney("0");
            packageInfoDo.setLength(0);
            packageInfoDo.setPaymentMethod("PPD");

            AddressSituationDo addressSituationDo1 = new AddressSituationDo();
            AddressSituationDo addressSituationDo2 = new AddressSituationDo();
            List<AddressSituationDo> addressSituationDos = new ArrayList<>();
            addressSituationDo1.setSuburb("");
            addressSituationDo1.setAddressType("seller");
            addressSituationDo1.setBackupPhone("");
            addressSituationDo1.setContactCompany("sellerAddress");
            addressSituationDo1.setBusinessCode("123");
            addressSituationDo1.setIdCardBackImg("");
            addressSituationDo1.setLatitude(0.98);
            addressSituationDo1.setSituationType("sellerAddress");
            addressSituationDo1.setStreet("");
            addressSituationDo1.setTaxID("124");
            addressSituationDo1.setArea("");
            addressSituationDo1.setCountry(createOrderDTO.getCountry());
            addressSituationDo1.setEmail("");
            addressSituationDo1.setIdNo("");
            addressSituationDo1.setLongitude(0.99);
            addressSituationDo1.setProvince("Rio Province");
            addressSituationDo1.setZipCode("3789-398");
            addressSituationDo1.setAddress("TESTNG_BRA_卖家");
            addressSituationDo1.setContacts("TESTNG_BRA_卖家");
            addressSituationDo1.setPhone("123456");
            addressSituationDo1.setStateRegisterNo("9n1cchsugy");
            addressSituationDo1.setCity("Rio City");
            addressSituationDo1.setTaxIDType(1);

            addressSituationDo2.setSuburb("");
            addressSituationDo2.setAddressType("seller");
            addressSituationDo2.setBackupPhone("");
            addressSituationDo2.setContactCompany("buyerAddress");
            addressSituationDo2.setBusinessCode("123");
            addressSituationDo2.setIdCardBackImg("");
            addressSituationDo2.setLatitude(0.60);
            addressSituationDo2.setSituationType("buyerAddress");
            addressSituationDo2.setStreet("");
            addressSituationDo2.setTaxID("445");
            addressSituationDo2.setArea("");
            addressSituationDo2.setCountry(createOrderDTO.getCountry());
            addressSituationDo2.setEmail("");
            addressSituationDo2.setIdNo("");
            addressSituationDo2.setLongitude(0.99);
            addressSituationDo2.setProvince("Rio Province");
            addressSituationDo2.setZipCode("3789-398");
            addressSituationDo2.setAddress("TESTNG_BRA_卖家");
            addressSituationDo2.setContacts("TESTNG_BRA_卖家");
            addressSituationDo2.setPhone("123456");
            addressSituationDo2.setStateRegisterNo("rto18129ph");
            addressSituationDo2.setCity("Rio City");
            addressSituationDo2.setTaxIDType(1);

            skuDetailDo.setSkuQty(1);
            skuDetailDo.setSkuWeight(2.8);
            skuDetailDo.setSkuHsCode("skuHsCode1");
            skuDetailDo.setSkuDesc("skuDesc中文");
            skuDetailDo.setSkuUrl("skuUrl");
            skuDetailDo.setSkuName("skuName");
            skuDetailDo.setSkuNo("skuNo");
            skuDetailDos.add(skuDetailDo);

            addressSituationDos.add(addressSituationDo1);
            addressSituationDos.add(addressSituationDo2);

            consigneeInfoDo.setCountry(createOrderDTO.getCountry());
            consigneeInfoDo.setIdNo("888");
            consigneeInfoDo.setBirthDate("2000-01-01");
            consigneeInfoDo.setIdExpiredDate("2024-01-01");
            consigneeInfoDo.setCalendarType("");
            consigneeInfoDo.setConsignee("auto-test");
            consigneeInfoDo.setContactCompany("auto-test");
            consigneeInfoDo.setContacts("auto-test");
            consigneeInfoDo.setPhone(RandomStringUtils.randomNumeric(11));
            consigneeInfoDo.setBackupPhone(RandomStringUtils.randomNumeric(6));
            consigneeInfoDo.setConsigneeEmail("werqwe@fsdf.cd");
            consigneeInfoDo.setLa(List.of(0));
            consigneeInfoDo.setLatitude(null);
            consigneeInfoDo.setLongitude(null);
            consigneeInfoDo.setProvince("São Paulo");
            consigneeInfoDo.setCity("Taboão da Serra");
            consigneeInfoDo.setArea("Parque Jacarandá");
            consigneeInfoDo.setAddress("Dolores Romero,125 Rua açougue tropical");
            consigneeInfoDo.setZipCode("06774-170");

            consigneeInfoDo.setTaxID("567");
            consigneeInfoDo.setTaxIDType(1);
            consigneeInfoDo.setAddressType("warehouse");
            consigneeInfoDo.setSellerId("88899603783");

            senderInfoDo.setSellerID("88899603783");
            senderInfoDo.setCountry(createOrderDTO.getCountry());
            senderInfoDo.setContactCompany("auto-test");
            senderInfoDo.setContacts("auto-test-man");
            senderInfoDo.setBackupPhone("");
            senderInfoDo.setPhone("+" + RandomStringUtils.randomNumeric(7));
            senderInfoDo.setProvince("São Paulo");
            senderInfoDo.setCity("Taboão da Serra");
            senderInfoDo.setArea("Parque Jacarandá");
            senderInfoDo.setAddress("Dolores Romero,125 Rua açougue tropical");
            senderInfoDo.setZipCode("06774-170");
            senderInfoDo.setSuburb("");
            senderInfoDo.setAddressType("seller");
            senderInfoDo.setTaxID("123");
            senderInfoDo.setTaxIDType(2);

            serviceInfoDo.setDeliveryService("Self");
            serviceInfoDo.setExpectedPickupDate("2023-12-20");
            serviceInfoDo.setLogisticsProductCode("LP21003801");
            serviceInfoDo.setTemperatureService(0);
            serviceInfoDo.setPickupService(1);
            serviceInfoDo.setDeliveryRequirements("自动化测试");

            if (Objects.equals(createOrderDTO.getIsFMOrder(), "true")){
                braOrderDetailDo.setOrderType(OrderType.FM_ORDER.getOrderTypeCode());
                // 标准地址库且FM订单
                if (Objects.equals(SortingType.STANDARD_ADDRESS_SORTING.getSortingTypeCode(),createOrderDTO.getSortingCode())) {
                    senderInfoDo.setPhone("+" + RandomStringUtils.randomNumeric(7));
                    senderInfoDo.setProvince("São Paulo");
                    senderInfoDo.setCity("Taboão da Serra");
                    senderInfoDo.setArea("Parque Jacarandá");
                    senderInfoDo.setAddress("Dolores Romero,125 Rua açougue tropical");
                    senderInfoDo.setZipCode("12345-345");
                }
                // 个人地址库且FM订单
                else if (Objects.equals(SortingType.USER_INFO_SORTING.getSortingTypeCode(),createOrderDTO.getSortingCode())){
                    senderInfoDo.setProvince("Sao Paulo Province");
                    senderInfoDo.setCity("Sao Paulo City");
                    senderInfoDo.setArea("332");
                    senderInfoDo.setAddress("uiuif8895644331");
                    senderInfoDo.setZipCode("99999-123");
                    senderInfoDo.setPhone("+55001945945");
                }
                else {
                    // 卖家地址库
                    senderInfoDo.setProvince("Rio Province");
                    senderInfoDo.setCity("Rio City");
                    senderInfoDo.setArea("Parque Jacarandá");
                    senderInfoDo.setAddress("mmkkok9-009923223");
                    senderInfoDo.setZipCode("12345-345");
                    senderInfoDo.setPhone("+966580250383");
                }

            }
            else {
                braOrderDetailDo.setOrderType(OrderType.COMMON_ORDER.getOrderTypeCode());
                // 标准地址库且普通订单
                if (Objects.equals(SortingType.STANDARD_ADDRESS_SORTING.getSortingTypeCode(),createOrderDTO.getSortingCode())){
                    consigneeInfoDo.setProvince("São Paulo");
                    consigneeInfoDo.setCity("São Paulo");
                    consigneeInfoDo.setArea("Parque Esmeralda");
                    consigneeInfoDo.setAddress("Rua Padre José Antônio Romano 300, bloco B 128");
                    consigneeInfoDo.setZipCode("05784-120");
                }
                // 个人地址库且普通订单
                else if (Objects.equals(SortingType.USER_INFO_SORTING.getSortingTypeCode(),createOrderDTO.getSortingCode())){
                    consigneeInfoDo.setProvince("Sao Paulo Province");
                    consigneeInfoDo.setCity("Sao Paulo City");
                    consigneeInfoDo.setArea("332");
                    consigneeInfoDo.setAddress("uiuif8895644331");
                    consigneeInfoDo.setZipCode("99999-123");
                    consigneeInfoDo.setPhone("+55001945945");
                }
                else {
                    // 卖家地址库
                    consigneeInfoDo.setProvince("Rio Province");
                    consigneeInfoDo.setCity("Rio City");
                    consigneeInfoDo.setArea("Parque Jacarandá");
                    consigneeInfoDo.setAddress("mmkkok9-009923223");
                    consigneeInfoDo.setZipCode("12345-345");
                    consigneeInfoDo.setPhone("+966580250383");
                }
            }
            braOrderDetailDo.setExtraInfo(extraInfoDo);
            braOrderDetailDo.setSkuInfos(skuDetailDos);
            braOrderDetailDo.setPackageInfo(packageInfoDo);
            braOrderDetailDo.setAddressSituation(addressSituationDos);
            braOrderDetailDo.setConsigneeInfo(consigneeInfoDo);
            braOrderDetailDo.setSenderInfo(senderInfoDo);
            braOrderDetailDo.setServiceInfo(serviceInfoDo);

            paramMap.put("param",braOrderDetailDo);
            paramMap.put("timestamp",System.currentTimeMillis());
            paramMap.put("accessToken","2c6d79a1-8f34-45ca-b03b-8e4b9a17bb71");
            paramMap.put("customerId","C2101949901");
            paramMap.put("sellerId","88899605229");
            paramMap.put("timeZone","+8");
            paramMap.put("sign","MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKvUVnuWJ1nSQFV4");
            paramMap.put("signMethod","simpleKey");
            paramMap.put("version","1.0.0");
            paramMap.put("format","json");
        }
        log.info("下单参数:{}", JSON.toJSONString(paramMap));
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");


        // 发起调用
        String response = doExecutor.doCommonHttpPostJson(paramMap, headers, url);
//        logger.info("res:{}",response);
        // 拿到订单号
        String waybillNo = (String) JSONPath.read(response,"$.data.expressNo");
        log.info("拿到订单号:{}",waybillNo);
        return waybillNo;
    }

//    public static void main(String[] args) {
//        CreateOrderServiceImpl createOrder = new CreateOrderServiceImpl();
//        String res = createOrder.createOrder("BRA", false, true,"3");
//        System.out.println(res);
//    }
}
