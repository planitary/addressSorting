package com.zane.controller;

import com.zane.core.addressSortingV2.AddressSortingCoreV2;
import com.zane.dao.dto.AutoAddressSortingDTO;
import com.zane.dao.dto.PreSortingDTO;
import com.zane.dao.dto.PreSortingDTOS;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AddressSortingControllerV2 {

    @Resource
    private AddressSortingCoreV2 addressSortingCoreV2;

    @PostMapping("/test/addressSorting/AutoAddressSorting")
    public String autoAdressSorting(@RequestBody AutoAddressSortingDTO autoAddressSortingDTO){
        String sortingRecordResult = addressSortingCoreV2.addressSorting(autoAddressSortingDTO);
        if (sortingRecordResult == null){
            throw new RuntimeException("分拣记录为空");
        }
        return sortingRecordResult;
    }
}
