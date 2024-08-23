package com.zane.dao;

import lombok.Data;

import java.util.List;

/**
 * 巴西下单
 */
@Data
public class BRAOrderDetailDo {
    /**
     * 下方均为巴西下单专用的参数
     */
    private ExtraInfoDo extraInfo;
    private PackageInfoDo packageInfo;
    private List<AddressSituationDo> addressSituation;
    private String labelType;
    private String customerVip;
    private List<SkuDetailDo> skuInfos;
    private String orderType;
    private ConsigneeInfoDo consigneeInfo;
    private SenderInfoDo senderInfo;
    private ServiceInfoDo serviceInfo;
    private String orderNo;
}
