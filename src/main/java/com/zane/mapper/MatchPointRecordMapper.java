package com.zane.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zane.dao.dto.MatchPointRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchPointRecordMapper extends BaseMapper<MatchPointRecordDTO> {

    MatchPointRecordDTO getMatchPoint(@Param("businessNumber") String businessNumber);
}
