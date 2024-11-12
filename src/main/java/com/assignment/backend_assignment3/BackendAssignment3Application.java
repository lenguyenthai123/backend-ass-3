package com.assignment.backend_assignment3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendAssignment3Application {

    public static void main(String[] args) {
        SpringApplication.run(BackendAssignment3Application.class, args);
    }

    @Value("${allow.origin}")
    private String ALLOWED_ORIGINS;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                System.out.println("ALLOWED_ORIGINS: " + ALLOWED_ORIGINS);
                registry.addMapping("/**") // Áp dụng cho tất cả các endpoint
                        .allowedOrigins(ALLOWED_ORIGINS) // Lấy giá trị từ application.properties hoặc application.yml
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các HTTP method được phép
                        .allowedHeaders("*") // Cho phép mọi header
                        .allowCredentials(true) // Cho phép gửi cookie (nếu cần)
                        .maxAge(3600); // Cache thời gian preflight trong 1 giờ
            }
        };
    }
}
