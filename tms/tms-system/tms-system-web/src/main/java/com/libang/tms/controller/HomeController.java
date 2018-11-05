package com.libang.tms.controller;

import com.libang.tms.dto.ResponseBean;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author libang
 * @date 2018/8/30 20:59
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        Subject subject = SecurityUtils.getSubject();

        //判断当前是否有已经认证的账号，如果有，则退出该账号
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        if (subject.isRemembered()) {
            //如果当前为被记住（通过rememberMe认证），则直接跳转到登录成功页面 home
            return "redirect:/home";
        }
        return "index";
    }


    @PostMapping("/")
    @ResponseBody
    public ResponseBean login(String accountMobile,
                              String password,
                              String rememberMe,
                              HttpServletRequest request) {

        Subject subject = SecurityUtils.getSubject();
        //根据账号和密码进行登录
        String requestIp = request.getRemoteAddr();

        System.out.println(accountMobile);
        System.out.println(password);
        UsernamePasswordToken usernamePasswordToken =
                new UsernamePasswordToken(accountMobile, DigestUtils.md5Hex(password), rememberMe != null, requestIp);

        try{

            subject.login(usernamePasswordToken);
            //登录成功后跳转目标的判断
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            String url = "/home";
            if (savedRequest != null) {
                url = savedRequest.getRequestUrl();
            }

            System.out.println(url);
            return ResponseBean.success(url);


        }catch (UnknownAccountException | IncorrectCredentialsException ex) {
            ex.printStackTrace();
            return ResponseBean.error("账号或密码错误");
        } catch (LockedAccountException ex) {
            ex.printStackTrace();
            return ResponseBean.error("账号被锁定");
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            return ResponseBean.error("账号或密码错误");
        }

    }


    /**
     * 登录成功进行跳转
     * @return
     */

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    //没有权限进行跳转

    @GetMapping("/401")
    public String unauthorizedUrl(){
        return "401";
    }

}
