package com.libang.tms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.libang.tms.controller.exception.NotFindException;
import com.libang.tms.entity.StoreAccount;
import com.libang.tms.entity.TicketStore;
import com.libang.tms.fileStore.QiniuStore;
import com.libang.tms.service.TicketStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Map;

/**
 * @author libang
 * @date 2018/8/31 22:39
 */
@Controller
@RequestMapping("/ticketstore")
public class TicketStoreController {

    @Reference(version = "1.0")
    private TicketStoreService ticketStoreService;
    @Autowired
    private QiniuStore qiniuStore;

    /**
     * 售票点主页列表
     *
     * @return
     */
    @GetMapping
    public String home(Model model,
                       @RequestParam(name = "p", required = false, defaultValue = "") Integer pageNo,
                       @RequestParam(required = false, defaultValue = "") String storeName,
                       @RequestParam(required = false, defaultValue = "") String storeManager,
                       @RequestParam(required = false, defaultValue = "") String storeTel) {
        Map<String, Object> queryMap = Maps.newHashMap();
        queryMap.put("storeName", storeName);
        queryMap.put("storeManage", storeManager);
        queryMap.put("storeTel", storeTel);
        PageInfo<TicketStore> pageInfo = ticketStoreService.findAllTicketStoreByPage(pageNo, queryMap);
        model.addAttribute("pageInfo", pageInfo);
        return "store/home";

    }

    /**
     * 新增售票点信息
     *
     * @return
     */
    @GetMapping("/new")
    public String newStore(Model model) {
        //获取七牛云文件上传
        String upToken = qiniuStore.getUpLoadToken();
        model.addAttribute("upToken", upToken);
        return "store/new";
    }

    @PostMapping("/new")
    public String newStore(TicketStore ticketStore, RedirectAttributes redirectAttributes) {
        ticketStoreService.saveNewTickStore(ticketStore);
        redirectAttributes.addFlashAttribute("message", "新增网点成功");
        return "redirect:/ticketstore";
    }


    /**
     * 查看售票点的详情
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{id:\\d+}")
    public String viewTicketStore(@PathVariable Integer id, Model model) {

        TicketStore ticketStore = ticketStoreService.findTicketStoreByTicketStoreId(id);
        if (ticketStore == null) {
            throw new NotFindException();
        }
        //关联的售票点账号
        StoreAccount storeAccount = ticketStoreService.findStoreAccountById(ticketStore.getId());

        model.addAttribute("ticketStore", ticketStore);
        model.addAttribute("storeAccount", storeAccount);

        return "store/info";

    }

    /**
     * 修改售票点信息
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{id:\\d+}/edit")
    public String updateTicketStore(@PathVariable Integer id, Model model) {
        TicketStore ticketStore = ticketStoreService.findTicketStoreByTicketStoreId(id);
        if (ticketStore == null) {
            throw new NotFindException();
        }
        //从牛云获取图片信息
        String uploadToken = qiniuStore.getUpLoadToken();
        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("ticketStore", ticketStore);
        return "/store/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String updateTicketStore(TicketStore ticketStore,RedirectAttributes redirectAttributes){
            ticketStoreService.updateTicketStore(ticketStore);
            redirectAttributes.addFlashAttribute("message","修改成功");
            return "redirect:/ticketstore";
    }


}
