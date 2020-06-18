package com.example.shiro.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class TestController {

    @RequestMapping("/getCode")
    public void getCode(HttpServletRequest reqeust, HttpServletResponse response) throws IOException {

        response.setContentType("image/jpeg");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = reqeust.getSession();

        ValidateCode vCode = new ValidateCode(100, 28, 4, 100);
        session.setAttribute("1", vCode.getCode());
        vCode.write(response.getOutputStream());
    }
}
