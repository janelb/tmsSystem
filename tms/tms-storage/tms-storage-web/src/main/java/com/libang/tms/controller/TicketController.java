package com.libang.tms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.libang.tms.entity.TicketInRecord;

import com.libang.tms.service.TicketService;
import com.libang.tms.shiro.ShiroUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.security.krb5.internal.Ticket;

/**
 * @author libang
 * @date 2018/9/1 14:50
 */
@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Reference(version = "1.0")
    private TicketService ticketService;

    /**
     * 年票入库首页
     *
     * @param model
     * @param pageNo
     * @return
     */
    @GetMapping("/storage")
    public String ticketIn(Model model,
                           @RequestParam(required = false, defaultValue = "1", name = "p") Integer pageNo) {
        PageInfo<TicketInRecord> pageInfo = ticketService.findTicketRecoredByPageNo(pageNo);
            model.addAttribute("pageInfo",pageInfo);
            return "ticket/storage/home";

    }

    /**
     * 新增年票入库
     * @param model
     * @return
     */
    @GetMapping("/storage/new")
    public String newTicketStorage(Model model){

        String today  = DateTime.now().toString("yyyy-MM-dd");
        model.addAttribute("today",today);
        return "ticket/storage/new";
    }

    @PostMapping("/storage/new")
    public String newTicketStorage(TicketInRecord ticketInRecord, RedirectAttributes redirectAttributes){

        ticketService.saveTicketInStorage(ticketInRecord,new ShiroUtil().getCurrentAccount());

        redirectAttributes.addFlashAttribute("message","新增成功");
        return "redirect:/ticket/storage";
    }



}
