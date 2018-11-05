package com.libang.tms.shiro;

import com.libang.tms.entity.Account;
import com.alibaba.dubbo.config.annotation.Reference;
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
 * @date 2018/8/30 21:52
 */
public class ShiroRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

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
        //获取当前登录对象
        Account account = (Account) principalCollection.getPrimaryPrincipal();
        //获取当前登录对象所拥有的角色
        List<Roles> rolesList = rolePermissionService.findRolesByAccountId(account.getId());
        //获取当前登录对象拥有的权限
        List<Permission> permissionList = new ArrayList<>();
        for (Roles roles : rolesList) {
            List<Permission> rolesPermissionList = rolePermissionService.findAllPermissionByRolesId(roles.getId());
            permissionList.addAll(rolesPermissionList);
        }

        //获取角色的code
        Set<String> rolesNameSet = new HashSet<>();
        for (Roles roles : rolesList) {
            rolesNameSet.add(roles.getRolesCode());
        }
        //获取权限的code
        Set<String> permissionNameSet = new HashSet<>();
        for (Permission permission : permissionList) {
            permissionNameSet.add(permission.getPermissionCode());
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //当前账户所拥有的角色（code）
        simpleAuthorizationInfo.setRoles(rolesNameSet);
        //当前账户所拥有的权限(code)
        simpleAuthorizationInfo.setStringPermissions(permissionNameSet);

        return simpleAuthorizationInfo;
    }

    /**
     * 判断登录
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
                throw new UnknownAccountException("该账号不存在" + userMobile);
            } else {

                if (Account.STATE_DISABLE.equals(account.getAccountState())) {
                    logger.info("{}登录成功", account, usernamePasswordToken.getHost());
                    System.out.println("登录成功后记录日志" + usernamePasswordToken.getHost());
                    //记录日志
                    AccountLoginLog accountLoginLog = new AccountLoginLog();
                    accountLoginLog.setAccountId(account.getId());
                    accountLoginLog.setLoginTime(new Date());
                    //获取当前账号登录的ip
                    accountLoginLog.setLoginIp(usernamePasswordToken.getHost());
                    accountService.saveLoginLog(accountLoginLog);

                    return new SimpleAuthenticationInfo(account, account.getAccountPassword(), getName());
                } else {
                    throw new LockedAccountException("账号已被禁用" + account.getAccountState());
                }
            }
        }
        return null;
    }
}
