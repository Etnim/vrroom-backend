package com.vrrom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class VRromApplication {

	public static void main(String[] args) {
		SpringApplication.run(VRromApplication.class, args);
	}

}
