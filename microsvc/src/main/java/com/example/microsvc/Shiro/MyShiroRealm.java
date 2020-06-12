//package com.example.microsvc.Shiro;
//
//import com.example.microsvc.Utils.MD5Util;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//
//import javax.servlet.annotation.WebFilter;
//import java.util.HashSet;
//
//@WebFilter(filterName = "shiroFilter",urlPatterns = {"*.jhtml","*.json"})
//public class MyShiroRealm extends AuthorizingRealm {
//
//    private final String USER_NAME = "root";
//    private final String PASS_WORD = "1109";
//
//    /*
//     * 授权
//     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        HashSet<String> roleNames = new HashSet<>();
//        HashSet<String> permissions = new HashSet<>();
//        roleNames.add("admin");
//        permissions.add("newPage.jhtml");
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setStringPermissions(permissions);
//        return info;
//    }
//    /*
//     * 登录验证
//     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        UsernamePasswordToken upt = (UsernamePasswordToken) authenticationToken;
//        if (upt.getUsername().equals(USER_NAME)){
//            return new SimpleAuthenticationInfo(USER_NAME,MD5Util.md5Encrypt32Upper(PASS_WORD),getName());
//        }
//        return null;
//    }
//}
