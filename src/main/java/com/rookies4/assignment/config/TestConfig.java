package com.rookies4.assignment.config;

import com.rookies4.assignment.config.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("test")
@Configuration
public class TestConfig {
    @Bean
    public MyEnvironment myEnvironment() {
        return MyEnvironment.builder()
                .mode("테스트환경")
                .build();
    }
}