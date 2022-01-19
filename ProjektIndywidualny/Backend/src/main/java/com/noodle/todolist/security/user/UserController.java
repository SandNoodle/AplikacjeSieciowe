package com.noodle.todolist.security.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noodle.todolist.security.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.noodle.todolist.util.Globals.EXPIRE_TIME_MINUTES;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
public class UserController {
	
	private final UserService userService;
	
	@Value("${auth.encryptionSecret}")
	private String encryptionSecret;
	
	@Value("${auth.authorizationTokenExpirationTime}")
	private long authTokenExpirationTime;
	
	@GetMapping("all/")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(userService.getUsers());
	}
	
	@GetMapping("get")
	public ResponseEntity<User> getUser(@RequestParam String username) {
		if (username == null) {
			log.error("Cannot get user - username was null.");
			return ResponseEntity.badRequest().build();
		}
		
		User user = userService.getUser(username);
		if (user == null) {
			log.info("User with '{}' username was not found", username);
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(userService.getUser(username));
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
	
	@GetMapping("token/refresh")
	public void refreshToken(HttpServletRequest clientRequest,
							 HttpServletResponse serverResponse) throws IOException {
		String authorizationHeader = clientRequest.getHeader(AUTHORIZATION);
		
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refreshToken = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256(encryptionSecret.getBytes());
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
				
				String decodedUsername = decodedJWT.getSubject();
				User user = userService.getUser(decodedUsername);
				
				String accessToken = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + authTokenExpirationTime * EXPIRE_TIME_MINUTES))
						.withIssuer(clientRequest.getRequestURL().toString())
						.withClaim("roles", user.getUserRoles()
								.stream()
								.map(Role::getRoleName)
								.collect(Collectors.toList()))
						.sign(algorithm);
				
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", accessToken);
				tokens.put("refresh_token", refreshToken);
				serverResponse.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(serverResponse.getOutputStream(), tokens);
			} catch (Exception e) {
				serverResponse.setHeader("error", e.getMessage());
				serverResponse.setStatus(FORBIDDEN.value());
				
				Map<String, String> errorMap = new HashMap<>();
				errorMap.put("error-message", e.getMessage());
				serverResponse.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(serverResponse.getOutputStream(), errorMap);
			}
		} else {
			log.error("Authorization token error! Refresh token is missing");
		}
	}
	
}