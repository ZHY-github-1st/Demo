package com.example.question.Service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "userClient",fallback = Hystrix.class)
public interface uService {
    @RequestMapping("/hello")
    String hello();
    @GetMapping("/users")
    List users();
}

