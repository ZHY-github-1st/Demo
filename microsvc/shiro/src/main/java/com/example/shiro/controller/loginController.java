package com.example.shiro.controller;

import com.example.shiro.bean.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.FactoryConfigurationError;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class loginController {
    Logger log = LoggerFactory.getLogger(loginController.class);
    @RequestMapping("/login")
    public String dologin(User user) {
        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getUsername(),
                user.getPassword()
        );
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
//            subject.checkRole("admin");
//            subject.checkPermissions("query", "add");

        } catch (AuthenticationException e) {
            log.error("账号或密码错误");
            log.error(e.getMessage());
            return "login";
        } catch (AuthorizationException e) {
            log.error("没有权限");
            log.error(e.getMessage());
            return "index";
        }
        return "index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String dologout(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        // 如果已经登录，则跳转到管理首页
        if(subject.getPrincipal() != null){
           subject.logout();
            System.out.println("=======退出=======");
        }
        return "index";
    }
    //注解验角色和权限
    @RequiresRoles("admin")
    @RequiresPermissions("add")
    @RequestMapping("/index")
    public String doindex() {
        return "index";
    }

    @ResponseBody
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
//    public static void main(String[] args) {
//        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
//        System.out.println("连接成功");
//        jedis.append("test","redis");
//        String test = jedis.get("test");
//        jedis.set("set","sredis");
//        String set = jedis.get("set");
//        //查看服务是否运行
//        System.out.println("服务正在运行: "+jedis.ping()+"append test"+test+"set set"+set);
//}
}
