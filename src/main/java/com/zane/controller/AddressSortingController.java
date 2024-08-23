package com.zane.controller;

import com.zane.core.addressSorting.AddressSortingCoreService;
import com.zane.core.main.AddressSortingMain;
import com.zane.dao.dto.PageDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
public class AddressSortingController {

    @Resource
    private AddressSortingCoreService addressSortingCoreService;

    @Resource
    private AddressSortingMain addressSortingMain;

    @PostMapping("/addressSorting/getConfigList")
    public String getConfigList(@RequestBody PageDTO pageDTO) {
        String addressSortingConfig = addressSortingCoreService.getAddressSortingConfig(pageDTO);
        return Objects.requireNonNullElse(addressSortingConfig, "Not Found!");
    }

    @RequestMapping("/addressSorting/getDataByDb")
    public String getDataByDb(String waybillNo){
        String orderSortingRecord = addressSortingCoreService.getOrderSortingRecord(waybillNo);
        if (!Objects.equals(orderSortingRecord, "")) {
            return orderSortingRecord;
        }
        return "Not Found!";
    }

    @RequestMapping("/addressSorting/getMatchPointRecord")
    public String getMatchPointRecord(String waybillNo){
        String sortSuccess = addressSortingCoreService.isSortSuccess(waybillNo);
        if (!Objects.equals(sortSuccess,"")){
            return sortSuccess;
        }
        return "Not Found!";
    }


    @RequestMapping("/addressSorting/main")
    public String doAddressSorting(String country){
        return addressSortingMain.addressSortingMain(country);
    }
}
