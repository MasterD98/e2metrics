package com.rapid.apigateway.config;

import com.rapid.apigateway.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public JwtUtil jwtUtil(){
        return  new JwtUtil();
    }
}
