package com.zane.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zane.dao.dto.OrderSortingRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderSortingRecordMapper extends BaseMapper<OrderSortingRecordDTO> {

    OrderSortingRecordDTO getSortRes(@Param("inputBusinessNumber") String inputBusinessNumber);
}
