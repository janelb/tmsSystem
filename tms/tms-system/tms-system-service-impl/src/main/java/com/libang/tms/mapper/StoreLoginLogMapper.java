package com.libang.tms.mapper;

import com.libang.tms.entity.StoreLoginLog;
import com.libang.tms.entity.StoreLoginLogExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper

public interface StoreLoginLogMapper {
    long countByExample(StoreLoginLogExample example);

    int deleteByExample(StoreLoginLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(StoreLoginLog record);

    int insertSelective(StoreLoginLog record);

    List<StoreLoginLog> selectByExample(StoreLoginLogExample example);

    StoreLoginLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") StoreLoginLog record, @Param("example") StoreLoginLogExample example);

    int updateByExample(@Param("record") StoreLoginLog record, @Param("example") StoreLoginLogExample example);

    int updateByPrimaryKeySelective(StoreLoginLog record);

    int updateByPrimaryKey(StoreLoginLog record);
}