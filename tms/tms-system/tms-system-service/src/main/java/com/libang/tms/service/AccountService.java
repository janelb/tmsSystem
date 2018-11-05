package com.libang.tms.service;

import com.libang.tms.entity.Account;
import com.libang.tms.entity.AccountLoginLog;

import java.util.List;
import java.util.Map;

/**
 * 系统账号业务类
 * @author libang
 * @date 2018/8/30 20:33
 */
public interface AccountService {

    /**
     * 通过电话查找用户
     * @param userMobile
     * @return
     */
    Account findByMobile(String userMobile);

    /**
     * 保存用户登录日志
     * @param accountLoginLog
     */
    void saveLoginLog(AccountLoginLog accountLoginLog);

    /**
     * 根据UI传来的查询参数查询所有账号并加载对应的角色列表
     * @param requestParam
     * @return
     */
    List<Account> findAccountWithRolesByQueryParm(Map<String,Object> requestParam);

    /**
     *
     * 新增账号
     * @param account
     * @param rolesIds
     */
    void saveAccount(Account account, Integer[] rolesIds);

    /**
     *
     * 根据账号id 查找账号
     * @param id
     * @return
     */
    Account findAccountById(Integer id);

    /**
     *
     * 修改账号
     * @param account
     * @param rolesIds
     */
    void updateAccount(Account account, Integer[] rolesIds);

    /**
     * 保存账户登录日志
     * @param accountLoginLog
     */
    void saveAccountLoginLog(AccountLoginLog accountLoginLog);
}
