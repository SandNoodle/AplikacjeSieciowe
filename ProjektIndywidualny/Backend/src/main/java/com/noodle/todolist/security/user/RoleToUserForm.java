package com.noodle.todolist.security.user;

import lombok.Data;

@Data
public class RoleToUserForm {
	private String username;
	private String roleName;
}