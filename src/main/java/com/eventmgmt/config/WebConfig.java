package com.eventmgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all endpoints
                        .allowedOrigins("http://localhost:3000") // React app frontend URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // All allowed HTTP methods
                        .allowedHeaders("*") // Allow any headers (e.g., Authorization)
                        .allowCredentials(true); // Allow cookies or authorization headers
            }
        };
    }
}
