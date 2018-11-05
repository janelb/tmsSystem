package com.libang.tms.shiro;

import com.libang.tms.entity.Account;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * @author libang
 * @date 2018/9/1 15:56
 */
@Component
public class ShiroUtil {

    /**
     *返回当期账户
     * @return
     */
    public Account getCurrentAccount() {
        Subject subject = SecurityUtils.getSubject();
        return (Account) subject.getPrincipal();
    }
}
