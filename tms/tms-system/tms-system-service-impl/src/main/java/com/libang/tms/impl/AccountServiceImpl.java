package com.libang.tms.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.libang.tms.entity.*;
import com.libang.tms.mapper.AccountLoginLogMapper;
import com.libang.tms.mapper.AccountMapper;
import com.libang.tms.mapper.AccountRolesMapper;
import com.libang.tms.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author libang
 * @date 2018/8/30 20:34
 */
@Service(version = "1.0", timeout = 5000)
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountLoginLogMapper accountLoginLogMapper;

    @Autowired
    private AccountRolesMapper accountRolesMapper;


    /**
     * 通过电话查找用户
     *
     * @param userMobile
     * @return
     */
    @Override
    public Account findByMobile(String userMobile) {

        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountMobileEqualTo(userMobile);
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if (accountList != null && !accountList.isEmpty()) {
            return accountList.get(0);
        }
        return null;

    }

    /**
     * 保存用户登录日志
     *
     * @param accountLoginLog
     */
    @Override
    public void saveLoginLog(AccountLoginLog accountLoginLog) {

        accountLoginLogMapper.insertSelective(accountLoginLog);

    }

    /**
     * 根据UI传来的查询参数查询所有账号并加载对应的角色列表
     *
     * @param requestParam
     * @return
     */
    @Override
    public List<Account> findAccountWithRolesByQueryParm(Map<String, Object> requestParam) {

        return accountMapper.findAccountWithRolesQueryParm(requestParam);

    }

    /**
     * 新增账号
     *
     * @param account
     * @param rolesIds
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveAccount(Account account, Integer[] rolesIds) {

        account.setCreateTime(new Date());

        //密码默认为手机号后6位
        String password;
        if (account.getAccountMobile().length() <= 6) {
            password = account.getAccountMobile();
        } else {
            password = account.getAccountMobile().substring(6);
        }
        //对密码进行加密
        password = org.apache.commons.codec.digest.DigestUtils.md5Hex(password);
        account.setAccountPassword(password);

        //账号状态默认状态
        account.setAccountState(Account.STATE_DISABLE);

        accountMapper.insertSelective(account);

        //添加账号和角色的对应关系
        for (Integer rolesId : rolesIds) {

            AccountRolesKey accountRolesKey = new AccountRolesKey();
            accountRolesKey.setRolesId(rolesId);
            accountRolesKey.setAccountId(account.getId());

            accountRolesMapper.insert(accountRolesKey);
        }
    }

    /**
     * 根据账号id 查找账号
     *
     * @param id
     * @return
     */
    @Override
    public Account findAccountById(Integer id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改账号
     *
     * @param account
     * @param rolesIds
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateAccount(Account account, Integer[] rolesIds) {


        account.setUpdateTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);

        //根据账号ID删除原有的对应关系
        AccountRolesExample accountRolesExample  = new AccountRolesExample();
        accountRolesExample.createCriteria().andAccountIdEqualTo(account.getId());
        accountRolesMapper.deleteByExample(accountRolesExample);

        //添加新的关联关系
        for(Integer rolesId : rolesIds){
            AccountRolesKey accountRoles = new AccountRolesKey();
            accountRoles.setRolesId(rolesId);
            accountRoles.setAccountId(account.getId());
            accountRolesMapper.insertSelective(accountRoles);
        }
    }

    /**
     * 保存账户登录日志
     *
     * @param accountLoginLog
     */
    @Override
    public void saveAccountLoginLog(AccountLoginLog accountLoginLog) {
        accountLoginLogMapper.insertSelective(accountLoginLog);
    }
}
