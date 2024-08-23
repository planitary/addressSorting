package com.zane.core.addressSortingV2;

import com.zane.dao.ChangeOrderDO;
import com.zane.dao.DeliveredInfoDo;
import com.zane.dao.dto.AutoAddressSortingDTO;
import com.zane.dao.dto.PreSortingDTO;
import com.zane.dao.dto.PreSortingDTOS;

import java.util.List;
import java.util.Map;

public interface AddressSortingCoreV2 {

    /**
     * 分拣入口
     * @param preSortingDTO         预分拣DTO
     * @return
     */
    String addressSorting(AutoAddressSortingDTO autoAddressSortingDTO);



}
