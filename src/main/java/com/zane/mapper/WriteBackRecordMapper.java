package com.zane.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zane.dao.dto.WriteBackResultDTO;
import com.zane.dao.dto.WriteMqJsonDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WriteBackRecordMapper extends BaseMapper<WriteBackResultDTO> {
    WriteMqJsonDTO getWriteRecordResult(@Param("orderNo") String orderNo);
}
