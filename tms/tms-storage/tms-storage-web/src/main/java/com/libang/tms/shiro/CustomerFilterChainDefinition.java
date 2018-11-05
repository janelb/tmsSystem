package com.libang.tms.shiro;

import com.alibaba.dubbo.config.annotation.Reference;
import com.libang.tms.entity.Permission;
import com.libang.tms.service.RolePermissionService;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author libang
 * @date 2018/9/1 16:00
 */
public class CustomerFilterChainDefinition {

    private Logger logger = LoggerFactory.getLogger(CustomerFilterChainDefinition.class);

    @Reference(version = "1.0")
    private RolePermissionService rolePermissionService;

    private Map<String, String> filterChainDefinition;
    private AbstractShiroFilter shiroFilter;

    public void setFilterChainDefinition(Map<String, String> filterChainDefinition) {
        this.filterChainDefinition = filterChainDefinition;
    }

    public void setShiroFilter(AbstractShiroFilter shiroFilter) {
        this.shiroFilter = shiroFilter;
    }


    /**
     * spring 容器启动时调用
     */
    @PostConstruct
    public synchronized void init() {
        logger.info("==========初始化URL权限==========");
        getFilterChainManager().getFilterChains().clear();
        load();
        logger.info("============初始化URL结束==================");
    }

    /**
     * 重新加载URL权限
     */
    @PostConstruct
    public synchronized void updateUrlPermission() {
        logger.info("=================刷新URL权限=====================");
        getFilterChainManager().getFilterChains().clear();
        load();
        logger.info("======================刷新权限结束==============================");
    }

    /**
     * 加载URL和权限的对应关系
     */
    public synchronized void load() {
        LinkedHashMap<String ,String> urlMap = new LinkedHashMap<>();
        //从数据库查找所有的权限
        List<Permission> permissionList = rolePermissionService.findAllPermission();
        //进行拼装
        for(Permission permission : permissionList){
            urlMap.put(permission.getUrl(),"perms["+permission.getPermissionCode()+"]");
        }
        urlMap.put("/**","user");
        //url和权限的关系设置到shiroFilter
        DefaultFilterChainManager defaultFilterChainManager  = getFilterChainManager();
        for(Map.Entry<String,String> entry : urlMap.entrySet()){
            defaultFilterChainManager.createChain(entry.getKey(),entry.getValue());
        }

    }

    private DefaultFilterChainManager getFilterChainManager() {
        PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) pathMatchingFilterChainResolver.getFilterChainManager();
        return defaultFilterChainManager;
    }
}
