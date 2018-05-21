package com.github.mensetsu.nannmon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The spring-boot application runner.
 * 
 * @author amatsuo
 */
@EnableScheduling
@SpringBootApplication
public class Application {
    
	/**
	 * Starts spring-boot.
	 * @param args CLI args
	 */
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
