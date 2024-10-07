//package com.nodes.demo.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    private final long MAX_AGE_SECS = 3600;
//
//    @Value("${app.cors.allowed-origins}")
//    private String allowedOrigin;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry
////                .addMapping("/api/**")
//                .addMapping("/**")
//                .allowedOrigins(allowedOrigin)
//                .allowedMethods("HEAD",  "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                .allowedHeaders("Authorization", "Content-Type")
////                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(MAX_AGE_SECS);
//    }
//}