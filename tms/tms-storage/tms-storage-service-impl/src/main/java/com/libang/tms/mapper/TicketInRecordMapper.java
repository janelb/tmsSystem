package com.libang.tms.mapper;

import com.libang.tms.entity.TicketInRecord;
import com.libang.tms.entity.TicketInRecordExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface TicketInRecordMapper {
    long countByExample(TicketInRecordExample example);

    int deleteByExample(TicketInRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TicketInRecord record);

    int insertSelective(TicketInRecord record);

    List<TicketInRecord> selectByExample(TicketInRecordExample example);

    TicketInRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TicketInRecord record, @Param("example") TicketInRecordExample example);

    int updateByExample(@Param("record") TicketInRecord record, @Param("example") TicketInRecordExample example);

    int updateByPrimaryKeySelective(TicketInRecord record);

    int updateByPrimaryKey(TicketInRecord record);
}