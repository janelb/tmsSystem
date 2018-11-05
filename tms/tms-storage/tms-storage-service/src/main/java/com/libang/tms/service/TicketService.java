package com.libang.tms.service;

import com.github.pagehelper.PageInfo;
import com.libang.tms.entity.Account;
import com.libang.tms.entity.TicketInRecord;

/**
 * @author libang
 * @date 2018/9/1 14:59
 */
public interface TicketService {


    /**
     * 查看入库年票
     * @param pageNo
     * @return
     */
    PageInfo<TicketInRecord> findTicketRecoredByPageNo(Integer pageNo);

    /**
     * 新增年票入库
     * @param ticketInRecord 年票
     * @param currentAccount 当前登录账户
     */
    void saveTicketInStorage(TicketInRecord ticketInRecord, Account currentAccount);
}
