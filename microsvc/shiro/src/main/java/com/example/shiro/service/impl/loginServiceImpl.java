package com.example.shiro.service.impl;

import com.example.shiro.bean.Permission;
import com.example.shiro.bean.Role;
import com.example.shiro.bean.User;
import com.example.shiro.mapper.UserMapper;
import com.example.shiro.service.loginService;
import com.example.shiro.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class loginServiceImpl implements loginService {

//    @Autowired
//    private UserMapper userMapper;
    @Override
    public User getUserByName(String getMapByName) {
        return getMapByName(getMapByName);
    }

    /**
     * 模拟数据库查询
     * @param userName
     * @return
     */

//  创建User对象将密码加密后的密文放入数据库中 在ShiroConfig中设置验证方式和加密算法
    private User getMapByName(String userName){
        Permission permissions1 = new Permission("1","query");
        Permission permissions2 = new Permission("2","add");
        Set<Permission> permissionsSet = new HashSet<>();
        permissionsSet.add(permissions1);
        permissionsSet.add(permissions2);
        Role role = new Role("1","admin",permissionsSet);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
//        模拟用户注册密码加密存储
        User user = new User("1","wsl",MD5Util.MD5Pwd("wsl","123456"),roleSet);
        Map<String ,User> map = new HashMap<>();
//        userMapper.insert(user);
        map.put(user.getUsername(), user);
        Permission permissions3 = new Permission("3","query");
        Set<Permission> permissionsSet1 = new HashSet<>();
        permissionsSet1.add(permissions3);
        Role role1 = new Role("2","user",permissionsSet1);
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(role1);
        User user1 = new User("2","zhangsan",MD5Util.MD5Pwd("zhangsan","123456"),roleSet1);
        map.put(user1.getUsername(), user1);
        return map.get(userName);
    }

}
