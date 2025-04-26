package com.finance.manager.piggymetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PiggymetricsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiggymetricsApplication.class, args);
	}

}
