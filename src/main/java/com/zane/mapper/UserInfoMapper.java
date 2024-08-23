package com.zane.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zane.dao.UserInfoDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoMapper> {
    UserInfoDo getUserInfo(@Param("addressCode") String addressCode);
}
