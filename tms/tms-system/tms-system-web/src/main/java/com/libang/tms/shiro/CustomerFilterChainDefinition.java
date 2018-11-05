package com.libang.tms.shiro;

import com.alibaba.dubbo.config.annotation.Reference;
import com.libang.tms.entity.Permission;
import com.libang.tms.service.RolePermissionService;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author libang
 * @date 2018/8/30 23:13
 */
public class CustomerFilterChainDefinition {

    @Reference(version = "1.0")
    private RolePermissionService rolePermissionService;


    private Logger logger = LoggerFactory.getLogger(CustomerFilterChainDefinition.class);

    //静态资源
    private Map<String ,String > filterChainDefinitions;


    private AbstractShiroFilter shiroFilter;


    public void setShiroFilter(AbstractShiroFilter shiroFilter) {
        this.shiroFilter = shiroFilter;
    }

    public void setFilterChainDefinitions(Map<String, String> filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    //初始化全权限
    @PostConstruct
    public synchronized void init() {

        logger.info("---------初始URL权限--------");
        //清除原有的权限
        getFilterChainManager().getFilterChains().clear();
        //加载现有的权限
        load();
        logger.info("---------初始化URL权限为完成-----------");
    }

    //更新权限
    public synchronized void updatePermission() {
        logger.info("---------加载URL权限--------");
        //清除原有的权限
        getFilterChainManager().getFilterChains().clear();
        //加载现有的权限
        load();
        logger.info("---------刷新URL权限为完成-----------");
    }

    /**
     * 加载URL和权限的对应关系
     */
    public synchronized void load() {
        LinkedHashMap<String ,String> urlMap = new LinkedHashMap<>();
        //加载静态资源
        urlMap.putAll(filterChainDefinitions);

        //从数据库查找所有权限对象
        List<Permission> permissionList = rolePermissionService.findAllPermission();
        for(Permission permission : permissionList){
            //进行封装 /employee/add= perms[employee:add]
           urlMap.put(permission.getUrl(),"perms["+permission.getPermissionCode()+"]");
           }
           urlMap.put("/**","user");
        //url和权限设置shiro中
        DefaultFilterChainManager defaultFilterChainManager  = getFilterChainManager();
        for(Map.Entry<String,String> entry: urlMap.entrySet()){
                defaultFilterChainManager.createChain(entry.getKey(),entry.getValue());
        }
    }

    private DefaultFilterChainManager getFilterChainManager(){
        PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) this.shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) pathMatchingFilterChainResolver.getFilterChainManager();
            return defaultFilterChainManager;
    }




}
