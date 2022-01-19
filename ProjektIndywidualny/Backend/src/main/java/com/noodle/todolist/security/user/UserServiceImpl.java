package com.noodle.todolist.security.user;

import com.noodle.todolist.security.role.Role;
import com.noodle.todolist.security.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(User user) {
		log.info("New User {} created.", user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Override
	public User getUser(String username) {
		log.info("Fetching user '" + username + "'");
		return userRepository.findByUsername(username);
	}
	
	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public Role createRole(Role role) {
		if (role == null) throw new UserServiceException("Cannot create role out of null value.");
		return roleRepository.save(role);
	}
	
	@Override
	public void addRoleToUser(String username, String roleName) {
		User user = getUser(username);
		Role role = getRole(roleName);
		
		log.info("Role '{}' added to user '{}'", role.getRoleName(), user.getUsername());
		user.getUserRoles().add(role);
	}
	
	@Override
	public void removeRoleFromUser(String username, String roleName) {
		User user = getUser(username);
		Role role = getRole(roleName);
		
		log.info("Deleted '" + roleName + "' role from '" + username + "' user.");
		user.getUserRoles().remove(role);
	}
	
	@Override
	public Role getRole(String roleName) {
		if (roleName == null) throw new UserServiceException("roleName cannot be null");
		return roleRepository.findByRoleName(roleName);
	}
	
	@Override
	public void deleteRole(String roleName) {
		if (roleName == null) throw new UserServiceException("Role cannot be null");
		
		Role role = getRole(roleName);
		if (role == null) {
			log.error("Cannot delete non-existing role '" + roleName + "'.");
			return;
		}
		
		log.info("Role '" + roleName + "' deleted.");
		roleRepository.delete(role);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		
		if (user == null) {
			throw new UserServiceException("User does not exist.");
		}
		
		// Grant user authorities depending on roles
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getUserRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}
}
