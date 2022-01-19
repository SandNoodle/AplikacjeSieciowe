package com.noodle.todolist.security.user;

import com.noodle.todolist.security.role.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;

@Configuration
public class UserConfig {
	
	@Bean
	CommandLineRunner userConfigRunner(UserService userService) {
		return args -> {
			
			// Roles
			userService.createRole(new Role(null, "ROLE_ADMIN", new ArrayList<>()));
			userService.createRole(new Role(null, "ROLE_USER", new ArrayList<>()));
			
			// Create Users
			User adminUser = new User(null, "admin", "admin", new ArrayList<>());
			User regularUser = new User(null, "user", "user", new ArrayList<>());
			
			// Add users to the database
			userService.createUser(adminUser);
			userService.createUser(regularUser);
			
			// Add roles to users
			userService.addRoleToUser("admin", "ROLE_ADMIN");
			userService.addRoleToUser("admin", "ROLE_USER");
			
			userService.addRoleToUser("user", "ROLE_USER");
		};
	}
	
}
