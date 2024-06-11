package org.mutagen.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MutagenBackendMainApplication {
	public static void main(String[] args) {
		SpringApplication.run(MutagenBackendMainApplication.class, args);
	}
}
