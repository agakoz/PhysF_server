package com.agakoz.physf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class PhysfApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhysfApplication.class, args);
	}

}
