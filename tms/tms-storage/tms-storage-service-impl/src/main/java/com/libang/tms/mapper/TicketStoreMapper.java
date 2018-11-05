package com.libang.tms.mapper;

import com.libang.tms.entity.TicketStore;
import com.libang.tms.entity.TicketStoreExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface TicketStoreMapper {
    long countByExample(TicketStoreExample example);

    int deleteByExample(TicketStoreExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TicketStore record);

    int insertSelective(TicketStore record);

    List<TicketStore> selectByExample(TicketStoreExample example);

    TicketStore selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TicketStore record, @Param("example") TicketStoreExample example);

    int updateByExample(@Param("record") TicketStore record, @Param("example") TicketStoreExample example);

    int updateByPrimaryKeySelective(TicketStore record);

    int updateByPrimaryKey(TicketStore record);
}