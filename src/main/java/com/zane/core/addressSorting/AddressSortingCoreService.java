package com.zane.core.addressSorting;

import com.zane.dao.dto.PageDTO;
import com.zane.dao.dto.PreSortingDTO;

import java.util.Map;

public interface AddressSortingCoreService {

    /**
     * 獲取分揀規則配置
     * @param country           分揀國家
     * @param isActive          規則啟用狀態
     * @return                  分揀配置結果
     */
    String getAddressSortingConfig(PageDTO pageDTO);

    String getOrderSortingRecord(String waybillNo);

    String isSortSuccess(String waybillNo);

    Map<String,Object> isSortSuccessV2(String wayillNo);

}
