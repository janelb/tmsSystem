package com.libang.tms.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.libang.tms.entity.*;
import com.libang.tms.exception.ServiceException;
import com.libang.tms.mapper.AccountRolesMapper;
import com.libang.tms.mapper.PermissionMapper;
import com.libang.tms.mapper.RolesMapper;
import com.libang.tms.mapper.RolesPermissionMapper;
import com.libang.tms.service.RolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Predicate;
import org.springframework.transaction.annotation.Transactional;

import javax.management.ServiceNotFoundException;
import javax.sql.rowset.serial.SerialException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author libang
 * @date 2018/8/30 20:36
 */
@Service(version = "1.0", timeout = 5000)
public class RolePermissionServiceImpl implements RolePermissionService {

    private Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    private RolesPermissionMapper rolesPermissionMapper;

    @Autowired
    private AccountRolesMapper accountRolesMapper;


    /**
     * 查找所有的权限
     *
     * @return
     */
    @Override
    public List<Permission> findAllPermission() {
        PermissionExample permissionExample = new PermissionExample();
        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);
        List<Permission> resultList = new ArrayList<>();
        treeList(permissionList, resultList, 0);
        return resultList;
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public List<Roles> findAllRoles() {
        RolesExample rolesExample = new RolesExample();
        return rolesMapper.selectByExample(rolesExample);
    }

    /**
     * 查找当前账号对应的角色
     *
     * @param id
     * @return
     */
    @Override
    public List<Roles> findRolesByAccountId(Integer id) {
        return rolesMapper.findRolesByAccountId(id);
    }

    /**
     * 根据权限类型查询对应的权限
     *
     * @param permissionType
     * @return
     */
    @Override
    public List<Permission> findPermissionByPermissionType(String permissionType) {
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andPermissionTypeEqualTo(permissionType);
        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);
        return permissionList;
    }

    /**
     * 新增权限
     *
     * @param permission
     */
    @Override
    public void savePermission(Permission permission) {

        permission.setCreateTime(new Date());
        permissionMapper.insertSelective(permission);
        logger.info("添加权限 {}", permission);

    }

    /**
     * 根据权限Id删除权限
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delPermissionById(Integer id) {
        //判断当前权限是否带有子节点
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andParentIdEqualTo(id);
        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);

        if (permissionList != null && !permissionList.isEmpty()) {
            throw new ServiceException("当前权限下有子节点不能被删除");
        }
        //查询该权限是否被角色引用
        RolesPermissionExample rolesPermissionExample = new RolesPermissionExample();
        rolesPermissionExample.createCriteria().andPermissionIdEqualTo(id);
        List<RolesPermissionKey> rolesPermissionKeyList = rolesPermissionMapper.selectByExample(rolesPermissionExample);

        if (rolesPermissionKeyList != null && !rolesPermissionKeyList.isEmpty()) {
            throw new ServiceException("该权限被角色引用，不能被删除");
        }
        //如果没有被引用则进行删除
        Permission permission = permissionMapper.selectByPrimaryKey(id);
        permissionMapper.deleteByPrimaryKey(id);
        logger.info("删除权限 {}", permission);
    }

    /**
     * 根据权限ID进行查找对应权限对象
     *
     * @param id
     * @return
     */
    @Override
    public Permission findPermissionByPermission(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改权限
     *
     * @param permission
     */
    @Override
    public void updatePermission(Permission permission) {
        permission.setUpdateTime(new Date());
        permissionMapper.updateByPrimaryKeySelective(permission);
        logger.info("修改权限{}", permission);


    }

    /**
     * 查询所有的角色和对应的权限
     *
     * @return
     */
    @Override
    public List<Roles> findALlRolesWithPermission() {
        List<Roles> rolesList = rolesMapper.findAllRolesWithPermission();
        return rolesList;
    }

    /**
     * 新增角色
     *
     * @param roles
     * @param permissionIds
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveRoles(Roles roles, Integer[] permissionIds) {
        roles.setCreateTime(new Date());
        rolesMapper.insertSelective(roles);

        if (permissionIds != null) {

            for (Integer permissionId : permissionIds) {
                RolesPermissionKey rolesPermissionKey = new RolesPermissionKey();
                rolesPermissionKey.setRolesId(roles.getId());
                rolesPermissionKey.setPermissionId(permissionId);

                rolesPermissionMapper.insertSelective(rolesPermissionKey);
            }
        }
        logger.info("新增角色 {}", roles);


    }

    /**
     * 根据角色ID删除角色
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delRolesByRolesId(Integer id) {
        //判断该角色是否被账户所引用
        AccountRolesExample accountRolesExample = new AccountRolesExample();
        accountRolesExample.createCriteria().andRolesIdEqualTo(id);
        List<AccountRolesKey> accountRolesKeyList = accountRolesMapper.selectByExample(accountRolesExample);
        if (accountRolesKeyList != null && !accountRolesKeyList.isEmpty()) {
            throw new ServiceException("该角色已被账户引用，不能被删除");
        }
        //删除角色和权限的关联关系表
        RolesPermissionExample rolesPermissionExample = new RolesPermissionExample();
        rolesPermissionExample.createCriteria().andRolesIdEqualTo(id);
        rolesPermissionMapper.deleteByExample(rolesPermissionExample);

        //删除角色
        Roles roles = rolesMapper.selectByPrimaryKey(id);
        rolesMapper.deleteByPrimaryKey(id);
        logger.info("删除角色 {}", roles);
    }

    /**
     * 根据角色ID查找角色角色及对应的权限
     *
     * @param id
     * @return
     */
    @Override
    public Roles findRolesWithPermissionByRolesId(Integer id) {
        return rolesMapper.findRolesWithPermissionByRolesId(id);
    }

    /**
     * 修改角色
     *
     * @param roles
     * @param permissionId
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateRoles(Roles roles, Integer[] permissionId) {
        roles.setUpdateTime(new Date());
        rolesMapper.updateByPrimaryKeySelective(roles);

        //删除角色和权限原有的对应关系
        RolesPermissionExample rolesPermissionExample = new RolesPermissionExample();
        rolesPermissionExample.createCriteria().andRolesIdEqualTo(roles.getId());
        rolesPermissionMapper.deleteByExample(rolesPermissionExample);

        //添加新的对应关系
        if (permissionId != null) {

            for (Integer perId : permissionId) {
                RolesPermissionKey rolesPermissionKey = new RolesPermissionKey();
                rolesPermissionKey.setRolesId(roles.getId());
                rolesPermissionKey.setPermissionId(perId);
                rolesPermissionMapper.insertSelective(rolesPermissionKey);
            }
        }
        logger.info("修改角色 {}", roles);

    }

    /**
     * 根据rolesId查询对应的权限
     *
     * @param rolesId
     * @return
     */
    @Override
    public List<Permission> findAllPermissionByRolesId(Integer rolesId) {
        return permissionMapper.findAllPermissionByRolesId(rolesId);
    }


    /**
     * 使用递归的方式进行遍历
     * 将查询的数据库的权限列表转换为树形集合
     *
     * @param sourceList 数据中查询的集合
     * @param endList    转换后的集合
     * @param parentId   父ID
     */
    private void treeList(List<Permission> sourceList, List<Permission> endList, int parentId) {
        List<Permission> tempList = Lists.newArrayList(Collections2.filter(sourceList, permission -> permission.getParentId().equals(parentId)));
        for (Permission permission : tempList) {
            endList.add(permission);
            treeList(sourceList, endList, permission.getId());
        }
    }
}
