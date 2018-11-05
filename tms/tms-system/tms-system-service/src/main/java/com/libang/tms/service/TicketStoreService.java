package com.libang.tms.service;

import com.github.pagehelper.PageInfo;
import com.libang.tms.entity.StoreAccount;
import com.libang.tms.entity.TicketStore;

import java.util.Map;

/**
 * @author libang
 * @date 2018/8/30 20:35
 */
public interface TicketStoreService {
    /**
     * 根据页码，条件进行查询
     * @param pageNo 页码
     * @param queryMap 条件
     * @return
     */
    PageInfo<TicketStore> findAllTicketStoreByPage(Integer pageNo, Map<String,Object> queryMap);

    /**
     * 新增售票网点
     * @param ticketStore
     */
    void saveNewTickStore(TicketStore ticketStore);

    /**
     * 通过售票点id查找售票点信息
     * @param id
     * @return
     */
    TicketStore findTicketStoreByTicketStoreId(Integer id);

    /**
     * 通过售票点id进行查询售票点信息
     * @param id
     * @return
     */
    StoreAccount findStoreAccountById(Integer id);

    /**
     * 修改售票点信息
     * @param ticketStore
     */
    void updateTicketStore(TicketStore ticketStore);
}
