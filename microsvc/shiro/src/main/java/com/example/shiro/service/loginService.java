package com.example.shiro.service;

import com.example.shiro.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface loginService {
    User getUserByName(String username);
}
