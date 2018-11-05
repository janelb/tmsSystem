package com.libang.tms.shiro;

import com.alibaba.dubbo.config.annotation.Reference;
import com.libang.tms.entity.Account;
import com.libang.tms.entity.AccountLoginLog;
import com.libang.tms.entity.Permission;
import com.libang.tms.entity.Roles;
import com.libang.tms.service.AccountService;

import com.libang.tms.service.RolePermissionService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author libang
 * @date 2018/9/1 15:27
 */
public class ShiroRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Reference(version = "1.0")
    private AccountService accountService;
    @Reference(version = "1.0")
    private RolePermissionService rolePermissionService;

    /**
     * 权限认证
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登录账号
        Account account = (Account) principalCollection.getPrimaryPrincipal();
        //获取当前账号的角色
        List<Roles> rolesList = rolePermissionService.findRolesByAccountId(account.getId());
        //获取当前账号的权限
        List<Permission> permissionList = new ArrayList<>();
        for (Roles roles : rolesList) {
            List<Permission> rolesPermissionList = rolePermissionService.findAllPermissionByRolesId(roles.getId());
            permissionList.addAll(rolesPermissionList);
        }
        Set<String> rolesNameSet = new HashSet<>();
        for (Roles roles : rolesList) {
            rolesNameSet.add(roles.getRolesCode());
        }
        Set<String> permissionNameSet = new HashSet<>();
        for (Permission permission : permissionList) {
            permissionNameSet.add(permission.getPermissionCode());
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //当前用户拥有的角色（code）
        simpleAuthorizationInfo.setRoles(rolesNameSet);
        //当前账户拥有的权限（code）
        simpleAuthorizationInfo.setStringPermissions(permissionNameSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 登录认证权限
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userMobile = usernamePasswordToken.getUsername();
        if (userMobile != null) {
            Account account = accountService.findByMobile(userMobile);
            if (account == null) {
                throw new UnknownAccountException("找不到账号" + userMobile);
            } else {
                if (Account.STATE_NORMAL.equals(account.getAccountState())) {
                    logger.info("{} 登录成功:{}", account, usernamePasswordToken.getHost());

                    //保存日志
                    AccountLoginLog accountLoginLog = new AccountLoginLog();
                    accountLoginLog.setLoginIp(usernamePasswordToken.getHost());
                    accountLoginLog.setLoginTime(new Date());
                    accountLoginLog.setAccountId(account.getId());
                    accountService.saveAccountLoginLog(accountLoginLog);
                    return new SimpleAuthenticationInfo(account, account.getAccountPassword(), getName());

                } else {
                    throw new LockedAccountException("账号被锁定" + account.getAccountState());

                }

            }
        }


        return null;
    }
}
