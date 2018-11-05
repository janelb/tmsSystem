package com.libang.tms.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libang.tms.entity.Account;
import com.libang.tms.entity.TicketInRecord;
import com.libang.tms.entity.TicketInRecordExample;
import com.libang.tms.entity.Tickets;
import com.libang.tms.exception.ServiceException;
import com.libang.tms.mapper.TicketInRecordMapper;
import com.libang.tms.mapper.TicketsMapper;
import com.libang.tms.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author libang
 * @date 2018/9/1 14:59
 */
@Service(version = "1.0", timeout = 5000)
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketInRecordMapper ticketInRecordMapper;

    @Autowired
    private TicketsMapper ticketsMapper;

    /**
     * 查看入库年票
     *
     * @param pageNo
     * @return
     */
    @Override
    public PageInfo<TicketInRecord> findTicketRecoredByPageNo(Integer pageNo) {
        PageHelper.startPage(pageNo, 15);
        TicketInRecordExample ticketInRecordExample = new TicketInRecordExample();
        ticketInRecordExample.setOrderByClause(" id desc");
        List<TicketInRecord> ticketInRecordList = ticketInRecordMapper.selectByExample(ticketInRecordExample);
        return new PageInfo<>(ticketInRecordList);
    }

    /**
     * 新增年票入库
     *
     * @param ticketInRecord 年票
     * @param currentAccount 当前登录账户
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveTicketInStorage(TicketInRecord ticketInRecord, Account currentAccount) {
        //开始编号-结束编号
        BigInteger start = new BigInteger(ticketInRecord.getBeginTicketNum());
        BigInteger end = new BigInteger(ticketInRecord.getEndTicketNum());
        //按照字典顺序来比较两个对象 如果 小于 等于 大于 分别返回 负整数  0  正整数
        if (start.compareTo(end) > 0) {
            throw new ServiceException("开始编号必须小等于截止票号");
        }
        //判断入库的票号是否和之前已入库的票号重合，如果重合则不能添加
        List<TicketInRecord> ticketInRecordList = ticketInRecordMapper.selectByExample(new TicketInRecordExample());
        for (TicketInRecord record : ticketInRecordList) {
            BigInteger recordStart = new BigInteger(record.getBeginTicketNum());
            BigInteger recordEnd = new BigInteger((record.getEndTicketNum()));
            if (recordStart.compareTo(start) <= 0 && recordEnd.compareTo(start) >= 0 || recordStart.compareTo(end) <= 0 && recordEnd.compareTo(end) >= 0) {
                throw new ServiceException("票号区间重复，添加失败");
            }
        }

        //设置添加时间
        ticketInRecord.setCreateTime(new Date());
        //获取票据总数量（截止票号-开始票号+1）
        //以下是声明java.math.BigDecimal.subtract()方法
        BigInteger totalNum = end.subtract(start).add(new BigInteger(String.valueOf(1)));
        ticketInRecord.setTotalNum(totalNum.intValue());

        ticketInRecord.setAccountId(currentAccount.getId());
        ticketInRecord.setAccountName(currentAccount.getAccountName());

        //设置入库的内容
        ticketInRecord.setContent(ticketInRecord.getBeginTicketNum()+"-"+ticketInRecord.getEndTicketNum());
        ticketInRecordMapper.insertSelective(ticketInRecord);

        //在tickets表中添加票号记录
        List<Tickets> ticketsList  =new ArrayList<>();
        for(int i =0;i< totalNum.intValue();i++){
            Tickets tickets = new Tickets();
            tickets.setCreateTime(new Date());
            tickets.setTicketInTime(new Date());
            tickets.setTicketNum(start.add(new BigInteger(String.valueOf(i))).toString());
            tickets.setTicketState(Tickets.TICKETS_STATE_IN_STORE);
            ticketsList.add(tickets);
        }
        //批量入库
        ticketsMapper.batchInsert(ticketsList);
    }


}
