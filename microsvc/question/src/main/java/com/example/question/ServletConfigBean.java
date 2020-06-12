package com.example.question;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfigBean {
    @Bean
    public ServletRegistrationBean hystrixMetricsStreamServlet() {
        ServletRegistrationBean regist = new ServletRegistrationBean();
        regist.setServlet(new HystrixMetricsStreamServlet());
        regist.setName("hystrixMetricsStreamServlet");
        regist.setLoadOnStartup(1);
        regist.addUrlMappings("/hystrix.stream");
        return regist;
    }
}
