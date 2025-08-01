package com.grepp.spring.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${front-server.domain}")
    private String frontServer;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(frontServer, "http://localhost:3000", "https://studium.cedartodo.uk", "http://localhost:80","http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }

}

