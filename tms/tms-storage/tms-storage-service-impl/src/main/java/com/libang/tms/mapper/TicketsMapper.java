package com.libang.tms.mapper;

import com.libang.tms.entity.Tickets;
import com.libang.tms.entity.TicketsExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface TicketsMapper {
    long countByExample(TicketsExample example);

    int deleteByExample(TicketsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Tickets record);

    int insertSelective(Tickets record);

    List<Tickets> selectByExample(TicketsExample example);

    Tickets selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Tickets record, @Param("example") TicketsExample example);

    int updateByExample(@Param("record") Tickets record, @Param("example") TicketsExample example);

    int updateByPrimaryKeySelective(Tickets record);

    int updateByPrimaryKey(Tickets record);

    void batchInsert(List<Tickets> ticketsList);
}