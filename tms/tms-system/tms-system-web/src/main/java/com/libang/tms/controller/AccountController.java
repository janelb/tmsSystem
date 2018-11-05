package com.libang.tms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Maps;
import com.libang.tms.controller.exception.NotFindException;
import com.libang.tms.entity.Account;
import com.libang.tms.entity.Roles;
import com.libang.tms.service.AccountService;
import com.libang.tms.service.RolePermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author libang
 * @date 2018/8/30 20:02
 */
@Controller
@RequestMapping("/manage/account")
public class AccountController {

    @Reference(version = "1.0")
    private AccountService accountService;

    @Reference(version = "1.0")
    private RolePermissionService rolePermissionService;


    /**
     * 获取所有人员及对应角色,进行性查询
     *
     * @return
     */
    @GetMapping
    public String home(@RequestParam(required = false) Integer rolesId,
                       @RequestParam(required = false) String nameMobile,
                       Model model) {

        Map<String, Object> requestParam = Maps.newHashMap();
        requestParam.put("accountMobile", nameMobile);
        requestParam.put("rolesId", rolesId);
        List<Account> accountList = accountService.findAccountWithRolesByQueryParm(requestParam);
        List<Roles> rolesList = rolePermissionService.findAllRoles();

        model.addAttribute("accountList", accountList);
        model.addAttribute("rolesList", rolesList);

        return "manage/account/home";

    }

    /**
     * 新增账号
     *
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newAccount(Model model) {
        List<Roles> rolesList = rolePermissionService.findAllRoles();
        model.addAttribute("rolesList", rolesList);
        return "/manage/account/new";
    }

    @PostMapping("/new")
    public String newAccount(Account account, Integer[] rolesIds) {
        accountService.saveAccount(account, rolesIds);
        return "redirect:/manage/account";
    }

    /**
     * 修改账号
     *
     * @param id    账号id
     * @param model
     * @return
     */
    @GetMapping("/{id:\\d+}/edit")
    public String updateAccount(@PathVariable Integer id, Model model) {

        Account account = accountService.findAccountById(id);
        if (account == null) {
            throw new NotFindException();
        }

        //查询所有的角色
        List<Roles> rolesList = rolePermissionService.findAllRoles();
        //查询该账号所拥有的角色
        List<Roles> accountRolesList = rolePermissionService.findRolesByAccountId(id);

        //判断角色设否是当前账户所拥有，
        //如果是则进行打钩
            //使用map集合进行标志
        model.addAttribute("rolesList",checkRolesListIsChecked(rolesList,accountRolesList));
        model.addAttribute("account",account);

        return "/manage/account/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String updateAccount(Account account, Integer[] rolesIds, RedirectAttributes redirectAttributes){

        accountService.updateAccount(account,rolesIds);
        redirectAttributes.addFlashAttribute("message","修改成功");
        return "redirect:/manage/account";
    }


    /**
     * 转换为是否打钩的map集合
     * @param rolesList 所有角色集合
     * @param accountRolesList 当前账号所用有得角色集合
     * @return
     */
    private Object checkRolesListIsChecked(List<Roles> rolesList, List<Roles> accountRolesList) {
        //使用标志位
        Map<Roles,Boolean> map = new LinkedHashMap<>();
        for(Roles role : rolesList){
            boolean flag = false;
            for(Roles accountRoles : accountRolesList){
                if(role.equals(accountRoles)){
                    flag =true;
                }
            }
            map.put(role,flag);
        }
        return  map;
    }


}
