package com.epam.esm.api.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.hateoas.config.EnableHypermediaSupport;
/**
 * It's a Spring Boot application that uses the `@SpringBootApplication` annotation to enable auto-configuration and
 * component scanning
 */
@SpringBootApplication(scanBasePackages = "com.epam.esm")
@EntityScan("com.epam.esm.core.model")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ApiApplication {

	/**
	 * The main function is the entry point of the application. It uses Spring Boot's SpringApplication.run() method to launch
	 * an application
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
