package com.advertising.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AdvertisingAnalyticsApp {

    public static void main(String[] args) {
        SpringApplication.run(AdvertisingAnalyticsApp.class, args);
    }
}
