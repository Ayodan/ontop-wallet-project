package com.ontop.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class OntopWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(OntopWalletApplication.class, args);
	}

}
