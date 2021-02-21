package com.agakoz.physf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("development")
@EnableWebMvc
public class DevCorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/auth/**")
                .allowedOrigins("http://localhost:4200");
        registry.addMapping("/patients/**")
                .allowedOrigins("http://localhost:4200");
        registry.addMapping("/file/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("POST", "GET")
                .allowedHeaders("file-name", "file-type")
                .exposedHeaders("file-name")
                .exposedHeaders("file-type")
                .exposedHeaders(HttpHeaders.AUTHORIZATION)
                .exposedHeaders(HttpHeaders.CONTENT_DISPOSITION);
    }
}
