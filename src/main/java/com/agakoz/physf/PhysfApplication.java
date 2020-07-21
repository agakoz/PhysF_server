package com.agakoz.physf;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2
@SpringBootApplication
public class PhysfApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhysfApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/").allowedOrigins("http://localhost:4200");
				registry.addMapping("/login").allowedOrigins("http://localhost:4200");
				registry.addMapping("/auth/**").allowedOrigins("http://localhost:4200");
				registry.addMapping("/patients/**").allowedOrigins("http://localhost:4200");

			}
		};
	}
}
