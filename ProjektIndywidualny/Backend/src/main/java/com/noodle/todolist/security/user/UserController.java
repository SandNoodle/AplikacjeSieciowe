package com.noodle.todolist.security.user;

import com.noodle.todolist.api.list.TodoList;
import com.noodle.todolist.security.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("all/")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(userService.getUsers());
	}
	
	@GetMapping("get/")
	public ResponseEntity<User> getUser(@RequestBody String username) {
		if (username == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		User user = userService.getUser(username);
		if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		return ResponseEntity.ok(user);
	}
	
	@DeleteMapping("admin/delete/")
	public ResponseEntity<Void> deleteUser(@RequestBody String username) {
		if (username == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		userService.deleteUser(username);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("create/")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		URI createUserUri = URI.create(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/user/create")
						.toUriString());
		return ResponseEntity.created(createUserUri).body(userService.createUser(user));
	}
	
	@PostMapping("role/create/")
	public ResponseEntity<Role> createRole(@RequestBody Role role) {
		URI createRoleURI = URI.create(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/user/role/create")
						.toUriString());
		return ResponseEntity.created(createRoleURI).body(userService.createRole(role));
	}
	
	@PostMapping("role/assign/")
	public ResponseEntity<Void> addRoleToUser(@RequestBody RoleToUserForm assignForm) {
		userService.addRoleToUser(assignForm.getUsername(), assignForm.getRoleName());
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("role/remove/")
	public ResponseEntity<Void> removeRoleFromUser(@RequestBody RoleToUserForm assignForm) {
		userService.removeRoleFromUser(assignForm.getUsername(), assignForm.getRoleName());
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("role/get/")
	public ResponseEntity<Role> getRole(@RequestBody String roleName) {
		return ResponseEntity.ok(userService.getRole(roleName));
	}
	
	@DeleteMapping("role/delete")
	public ResponseEntity<Void> deleteRole(@RequestBody String roleName) {
		userService.deleteRole(roleName);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("list/assign/")
	public ResponseEntity<Void> addListToUser(@RequestBody String username,
											  @RequestBody String listTitle) {
		userService.addListToUser(username, listTitle);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("list/remove/")
	public ResponseEntity<Void> removeListFromUser(@RequestBody String username,
												   @RequestBody String listTitle) {
		userService.removeListFromUser(username, listTitle);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("list/remove/all/")
	public ResponseEntity<Void> removeAllListsFromUser(@RequestBody String username) {
		userService.removeAllListsFromUser(username);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("list/get/all/")
	public ResponseEntity<Collection<TodoList>> getUserLists(@RequestBody String username) {
		return ResponseEntity.ok(userService.getUserLists(username));
	}

	
	// TODO: Refresh token method
	
}