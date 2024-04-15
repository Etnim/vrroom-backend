package com.vrrom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VRromApplication {

	public static void main(String[] args) {
		SpringApplication.run(VRromApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
//			EmailService emailService = ctx.getBean(EmailService.class);
//			emailService.sendEmail("vrroom.leasing@gmail.com", "vrroom.leasing@gmail.com", "Test Subject", "Hello, this is a test email.");
//		};
//	}

}
