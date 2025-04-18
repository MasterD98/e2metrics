package org.rapid.userservice.config;

import org.jasypt.encryption.StringEncryptor;
import org.rapid.userservice.util.EncryptorUtil;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public EncryptorUtil encryptorUtil(){
        return new EncryptorUtil();
    }
}
