package com.hostelscout.hostel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HostelApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostelApplication.class, args);
	}

}
