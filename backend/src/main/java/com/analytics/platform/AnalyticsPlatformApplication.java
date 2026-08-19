package com.analytics.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AnalyticsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsPlatformApplication.class, args);
    }
}
