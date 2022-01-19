package com.noodle.todolist.security.user;

import com.noodle.todolist.security.role.Role;

import java.util.List;

public interface UserService {
	
	// Users
	User createUser(User user);
	
	User getUser(String username);
	
//	void deleteUser(String user);
	
	List<User> getUsers();
	
	// Roles
	Role createRole(Role role);
	
	Role getRole(String name);
	
	void deleteRole(String roleName);
	
	void addRoleToUser(String username, String roleName);
	
	void removeRoleFromUser(String user, String roleName);
	
}
