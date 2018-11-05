package com.libang.tms.service;

import com.libang.tms.entity.Permission;
import com.libang.tms.entity.Roles;

import java.util.List;

/**
 * 角色和权限的业务类
 * @author libang
 * @date 2018/8/30 20:34
 */
public interface RolePermissionService {
    /**
     * 查找所有的权限
     * @return
     */
    List<Permission> findAllPermission();

    /**
     * 查询所有角色
     * @return
     */
    List<Roles> findAllRoles();

    /**
     * 查找当前账号对应的角色
     * @param id
     * @return
     */
    List<Roles> findRolesByAccountId(Integer id);

    /**
     * 根据权限类型查询对应的权限
     * @param permissionType
     * @return
     */
    List<Permission> findPermissionByPermissionType(String permissionType);

    /**
     * 新增权限
     * @param permission
     */
    void savePermission(Permission permission);

    /**
     *
     * 根据权限Id删除权限
     * @param id
     */
    void delPermissionById(Integer id);

    /**
     * 根据权限ID进行查找对应权限对象
     * @param id
     * @return
     */
    Permission findPermissionByPermission(Integer id);

    /**
     * 修改权限
     * @param permission
     */
    void updatePermission(Permission permission);

    /**
     * 查询所有的角色和对应的权限
     * @return
     */
    List<Roles> findALlRolesWithPermission();

    /**
     * 新增角色
     * @param roles
     * @param permissionIds
     */
    void saveRoles(Roles roles, Integer[] permissionIds);

    /**
     * 根据角色ID删除角色
     * @param id
     */
    void delRolesByRolesId(Integer id);



    /**
     * 根据角色ID查找角色角色及对应的权限
     * @param id
     * @return
     */
    Roles findRolesWithPermissionByRolesId(Integer id);

    /**
     * 修改角色
     * @param roles
     * @param permissionId
     */
    void updateRoles(Roles roles, Integer[] permissionId);

    /**
     *根据rolesId查询对应的权限
     * @param rolesId
     * @return
     */
    List<Permission> findAllPermissionByRolesId(Integer rolesId);
}
