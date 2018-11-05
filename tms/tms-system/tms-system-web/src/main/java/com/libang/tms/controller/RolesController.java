package com.libang.tms.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.google.common.collect.Maps;
import com.libang.tms.controller.exception.NotFindException;
import com.libang.tms.entity.Permission;
import com.libang.tms.entity.Roles;
import com.libang.tms.service.RolePermissionService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import java.util.List;
import java.util.Map;

/**
 * @author libang
 * @date 2018/8/31 20:23
 */
@Controller
@RequestMapping("/manage/roles")
public class RolesController {

    @Reference(version = "1.0")
    private RolePermissionService rolePermissionService;

    /**
     * 角色的主界面，角色列表
     *
     * @param model
     * @return
     */
    @GetMapping
    public String home(Model model) {
        List<Roles> rolesList = rolePermissionService.findALlRolesWithPermission();
        model.addAttribute("rolesList", rolesList);
        return "/manage/roles/home";
    }

    /**
     * 新增角色
     *
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String saveRoles(Model model) {
        //查询所有的权限
        List<Permission> permissionList = rolePermissionService.findAllPermission();
        model.addAttribute("permissionList", permissionList);
        return "/manage/roles/new";
    }

    @PostMapping("/new")
    public String saveRoles(Roles roles, Integer[] permissionIds, RedirectAttributes redirectAttributes) {
        rolePermissionService.saveRoles(roles, permissionIds);
        redirectAttributes.addFlashAttribute("message", "添加成功");
        return "redirect:/manage/roles";
    }

    /**
     * 删除角色
     * @param id
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/{id:\\d+}/del")
    public String delRoles(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        rolePermissionService.delRolesByRolesId(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/manage/roles/del";
    }

    /**
     * 修改角色
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{id:\\d+}/edit")
    public String updateRoles(@PathVariable Integer id , Model model){
        Roles roles = rolePermissionService.findRolesWithPermissionByRolesId(id);
        if(roles == null){
            throw new NotFindException();
        }

        //查询所有的权限列表
        List<Permission> permissionList  = rolePermissionService.findAllPermission();
        //判断权限列表是否被checked
        Map<Permission ,Boolean> map = checkedPermissionList(roles.getPermissionList(),permissionList);
        model.addAttribute("permissionMap",map);
        model.addAttribute("roles",roles);

        return "/manage/roles/edit";
    }

    /**
     * 在编辑页面判断当前权限的复选框是否被checked
     * @param rolesPermissionList 当前角色拥有的权限
     * @param permissionList 所有的权限列表
     * @return 有顺序的map集合，如果被选择则value为true
     */
    private Map<Permission,Boolean> checkedPermissionList(List<Permission> rolesPermissionList, List<Permission> permissionList) {

        Map<Permission,Boolean> resultMap = Maps.newLinkedHashMap();
        for(Permission permission : permissionList){
            boolean flag = false;
            for(Permission rolesPermission : rolesPermissionList){
                if(permission.equals(rolesPermission)){
                    flag = true;
                    break;
                }
            }
            resultMap.put(permission,flag);
        }
        return resultMap;
    }

    @PostMapping("/{id:\\d+}/edit")
    public String updateRoles(Roles roles,Integer[] permissionId,RedirectAttributes redirectAttributes){
        rolePermissionService.updateRoles(roles,permissionId);
        redirectAttributes.addFlashAttribute("message","修改成功");
        return "redirect:/manage/roles";

    }


}
