package com.example.question.Service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Hystrix implements uService {
    @Override
    public String hello() {
        return "message failed";
    }

    @Override
    public List users() {
        return null;
    }
}
