package com.libang.tms.mapper;

import com.libang.tms.entity.RolesPermissionExample;
import com.libang.tms.entity.RolesPermissionKey;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RolesPermissionMapper {
    long countByExample(RolesPermissionExample example);

    int deleteByExample(RolesPermissionExample example);

    int deleteByPrimaryKey(RolesPermissionKey key);

    int insert(RolesPermissionKey record);

    int insertSelective(RolesPermissionKey record);

    List<RolesPermissionKey> selectByExample(RolesPermissionExample example);

    int updateByExampleSelective(@Param("record") RolesPermissionKey record, @Param("example") RolesPermissionExample example);

    int updateByExample(@Param("record") RolesPermissionKey record, @Param("example") RolesPermissionExample example);
}