package com.libang.tms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.libang.tms.controller.exception.NotFindException;
import com.libang.tms.entity.Permission;
import com.libang.tms.service.RolePermissionService;
import com.libang.tms.shiro.CustomerFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 权限控制器
 *
 * @author libang
 * @date 2018/8/31 17:00
 */

@Controller
@RequestMapping("/manage/permission")
public class PermissionController {

    @Reference(version = "1.0")
    private RolePermissionService rolePermissionService;

    @Autowired
    private CustomerFilterChainDefinition customerFilterChainDefinition;

    //权限列表首页

    @GetMapping
    public String home(Model model) {
        List<Permission> permissionList = rolePermissionService.findAllPermission();
        model.addAttribute("permissionList", permissionList);
        return "/manage/permission/home";
    }

    //新增权限

    @GetMapping("/new")
    public String newPermission(Model model) {
        //查询菜单类型权限列表
        List<Permission> permissionList = rolePermissionService.findPermissionByPermissionType(Permission.MENU_TYPE);
        model.addAttribute("permissionList", permissionList);
        return "/manage/permission/new";
    }

    @PostMapping("/new")
    public String newPermission(Permission permission, RedirectAttributes redirectAttributes) {
        rolePermissionService.savePermission(permission);
        //刷新shiro权限
        customerFilterChainDefinition.updatePermission();
        redirectAttributes.addFlashAttribute("message", "新增权限成功");
        return "redirect:/manage/permission";
    }

    //删除权限
    @GetMapping("/{id:\\d+}/del")

    public String delPermission(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        rolePermissionService.delPermissionById(id);
        //刷新权限
        customerFilterChainDefinition.updatePermission();
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/manage/permission";
    }

    //修改权限
    @GetMapping("/{id:\\d+}/edit")

    public String updatePermission(@PathVariable Integer id, Model model) {
        Permission permission = rolePermissionService.findPermissionByPermission(id);

        if (permission == null) {
            throw new NotFindException();
        }
        //封装所有菜单权限列表
        List<Permission> permissionList = rolePermissionService.findPermissionByPermissionType(Permission.MENU_TYPE);

        //排除当前权限对象及其子权限对象，在修改时不能出现一已有的权限
        remove(permissionList, permission);
        model.addAttribute("permissionList", permissionList);
        model.addAttribute("permission", permission);

        return "/manage/permission/edit";
    }

    /**
     * 使用递归的方式排除自己本身和已有的对象
     *
     * @param permissionList
     * @param permission
     */
    private void remove(List<Permission> permissionList, Permission permission) {
        //使用临时变量来防止漏删
        List<Permission> temp = Lists.newArrayList(permissionList);
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getParentId().equals(permission.getId())) {
                remove(permissionList, temp.get(i));
            }
        }
        permissionList.remove(permission);
    }


    @PostMapping("/{id:\\d+}/edit")
    public String updatePermission(Permission permission, RedirectAttributes redirectAttributes) {

        rolePermissionService.updatePermission(permission);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return "redirect:/manage/permission";
    }


}
