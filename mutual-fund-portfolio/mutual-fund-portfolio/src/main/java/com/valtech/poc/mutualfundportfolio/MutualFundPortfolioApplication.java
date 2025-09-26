package com.valtech.poc.mutualfundportfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
public class MutualFundPortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MutualFundPortfolioApplication.class, args);
	}

}
