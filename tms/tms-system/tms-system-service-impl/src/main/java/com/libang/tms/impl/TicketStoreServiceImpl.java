package com.libang.tms.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libang.tms.entity.StoreAccount;
import com.libang.tms.entity.TicketStore;
import com.libang.tms.entity.TicketStoreExample;
import com.libang.tms.mapper.StoreAccountMapper;
import com.libang.tms.mapper.TicketStoreMapper;
import com.libang.tms.service.TicketStoreService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author libang
 * @date 2018/8/30 20:37
 */
@Service(version = "1.0", timeout = 5000)
public class TicketStoreServiceImpl implements TicketStoreService {


    @Autowired
    private TicketStoreMapper ticketStoreMapper;
    @Autowired
    private StoreAccountMapper storeAccountMapper;

    /**
     * 根据页码，条件进行查询
     *
     * @param pageNo   页码
     * @param queryMap 条件
     * @return
     */
    @Override
    public PageInfo<TicketStore> findAllTicketStoreByPage(Integer pageNo, Map<String, Object> queryMap) {
        PageHelper.startPage(pageNo, 15);
        String storeName = (String) queryMap.get("storeName");
        String storeManager = (String) queryMap.get("storeManager");
        String storeTel = (String) queryMap.get("storeTel");

        TicketStoreExample ticketStoreExample = new TicketStoreExample();
        TicketStoreExample.Criteria criteria = ticketStoreExample.createCriteria();
        if (StringUtils.isNotEmpty(storeName)) {
            criteria.andStoreNameLike("%" + storeName + "%");

        }
        if (StringUtils.isNotEmpty(storeManager)) {
            criteria.andStoreManagerLike("%" + storeManager + "%");

        }
        if (StringUtils.isNotEmpty(storeTel)) {
            criteria.andStoreTelEqualTo(storeTel);
        }
        ticketStoreExample.setOrderByClause("id desc");

        List<TicketStore> ticketStoreList = ticketStoreMapper.selectByExample(ticketStoreExample);

        /*PageInfo<TicketStore> pageInfo = new PageInfo<>(ticketStoreList);*/
        return new PageInfo<>(ticketStoreList);
    }

    /**
     * 新增售票网点
     *
     * @param ticketStore
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNewTickStore(TicketStore ticketStore) {

        ticketStore.setCreateTime(new Date());
        ticketStoreMapper.insertSelective(ticketStore);

        //创建售票点信息
        StoreAccount storeAccount = new StoreAccount();

        storeAccount.setStoreAccount(ticketStore.getStoreTel());
        //默认密码是123123
        storeAccount.setStorePassword(DigestUtils.md5Hex(StoreAccount.ACCOUNT_INIT_PASSWORD));
        storeAccount.setStoreState(StoreAccount.ACCOUNT_STATE_NORMAL);
        storeAccount.setId(ticketStore.getId());
        storeAccount.setCreateTime(new Date());
        storeAccountMapper.insertSelective(storeAccount);


    }

    /**
     * 通过售票点id查找售票点信息
     *
     * @param id
     * @return
     */
    @Override
    public TicketStore findTicketStoreByTicketStoreId(Integer id) {

        return ticketStoreMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过售票点id进行查询售票点信息
     *
     * @param id
     * @return
     */
    @Override
    public StoreAccount findStoreAccountById(Integer id) {
        return storeAccountMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改售票点信息
     *
     * @param ticketStore
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateTicketStore(TicketStore ticketStore) {
        ticketStore.setUpdateTime(new Date());
        //判断是否修改了手机号码
        StoreAccount storeAccount = storeAccountMapper.selectByPrimaryKey(ticketStore.getId());
        if (!ticketStore.getStoreTel().equals(storeAccount.getStoreAccount())) {
            //如果修改了手机号，要同步修改售票点账号
            storeAccount.setStoreAccount(ticketStore.getStoreTel());
            storeAccount.setStorePassword(DigestUtils.md5Hex(ticketStore.getStoreTel().substring(6)));
            storeAccount.setUpdateTime(new Date());

            storeAccountMapper.updateByPrimaryKeySelective(storeAccount);
        }
        ticketStoreMapper.updateByPrimaryKeySelective(ticketStore);
    }
}
