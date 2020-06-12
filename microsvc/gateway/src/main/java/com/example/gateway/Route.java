//package com.example.gateway;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//
//public class Route {
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r ->
//                        r.path("/java/**")
//                                .filters(
//                                        f -> f.stripPrefix(1)
//                                )
//                                .uri("http://localhost:8090/helloWorld")
//                )
//                .build();
//    }
//}
