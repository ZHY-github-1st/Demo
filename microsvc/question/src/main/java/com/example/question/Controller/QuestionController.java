package com.example.question.Controller;

import com.example.question.Service.uService;
import com.example.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class QuestionController {
    @RequestMapping(value = "/test")
    public String hello() {
        return "hello from QuestionController";
    }

    @Autowired
    private uService helloService;

    @Autowired
    private QuestionRepository questionRepository;

    @RequestMapping("/invoker-question/hello")
    public String questionhello() {
        return helloService.hello();
    }

    @RequestMapping("/invoker-question/users")
    public List users() {
        return helloService.users();
    }

    @RequestMapping("/list")
    public void list() {
        questionRepository.findAll();
    }
}
