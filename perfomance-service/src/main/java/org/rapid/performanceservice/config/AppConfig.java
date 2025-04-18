package org.rapid.performanceservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rapid.performanceservice.util.CalculatePerformanceUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public CalculatePerformanceUtil calculatePerformanceUtil(){
        return new CalculatePerformanceUtil();
    }
}
