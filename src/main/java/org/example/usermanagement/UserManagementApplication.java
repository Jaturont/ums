package org.example.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class UserManagementApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

}
