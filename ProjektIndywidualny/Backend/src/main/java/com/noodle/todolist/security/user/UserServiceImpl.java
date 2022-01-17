package com.noodle.todolist.security.user;

import com.noodle.todolist.api.list.TodoList;
import com.noodle.todolist.api.list.TodoListRepository;
import com.noodle.todolist.api.list.element.TodoElement;
import com.noodle.todolist.api.list.element.TodoElementRepository;
import com.noodle.todolist.security.role.Role;
import com.noodle.todolist.security.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	private final TodoListRepository todoListRepository;
	private final TodoElementRepository todoElementRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final int MAX_LISTS_FOR_REGULAR_USER = 5;
	private final int MAX_LISTS_FOR_ADMIN = 10;
		
	@Override
	public User createUser(User user) {
		if (doesUserExist(user))
			throw new UserServiceException("User with '" + user.getUsername() + "' username already exists.");
		
		log.info("New User {} created.", user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Override
	public User getUser(String username) {
		if (!doesUserExist(username)) throw new UserServiceException("User '" + username + "' does not exist.");
		
		return userRepository.findByUsername(username);
	}
	
	@Override
	public void deleteUser(String username) {
		if (username == null) throw new UserServiceException("Cannot delete null user.");
		
		if (!doesUserExist(username)) {
			log.error("Specified user '" + username + "' does not exist.");
			return;
		}
		
		log.info("User '" + username + "' deleted.");
		User user = getUser(username);
		userRepository.delete(user);
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
		
		if (!doesUserExist(user)) throw new UserServiceException("Cannot add role to non-existing user.");
		
		boolean doesRoleExist = roleRepository.findByRoleName(roleName) != null;
		if (!doesRoleExist) throw new UserServiceException("Cannot add non-existing role to user.");
		
		log.info("Role '{}' added to user '{}'", role.getRoleName(), user.getUsername());
		user.getRoles().add(role);
	}
	
	@Override
	public void removeRoleFromUser(String username, String roleName) {
		if (username == null) throw new UserServiceException("Cannot delete role from null user.");
		if (!doesUserExist(username)) throw new UserServiceException("Cannot delete role from non-existing user.");
		
		User user = getUser(username);
		Role role = getRole(roleName);
		
		log.info("Deleted '" + roleName + "' role from '" + username + "' user.");
		user.getRoles().remove(role);
	}
	
	@Override
	public void addListToUser(String username, String listTitle) {
		TodoList list = todoListRepository.findByTitle(listTitle);
		if (list == null) return;
		
		User user = getUser(username);
		Role adminRole = getRole("ROLE_ADMIN");
		int maxListsForUser = user.getRoles().contains(adminRole) ? MAX_LISTS_FOR_ADMIN : MAX_LISTS_FOR_REGULAR_USER;
		if (user.getLists().size() < maxListsForUser) {
			log.info("Added '" + listTitle + "' list to user '" + username + "'.");
			user.getLists().add(list);
		} else {
			log.info("List size exceeded for user '" + username + "'.");
		}
		
	}
	
	@Override
	public void removeListFromUser(String username, String listTitle) {
		User user = getUser(username);
		TodoList list = todoListRepository.findByTitle(listTitle);
		
		log.info("Removed '" + listTitle + "' list from user '" + username + "'.");
		user.getLists().remove(list);
	}
	
	@Override
	public void removeAllListsFromUser(String username) {
		if (!doesUserExist(username)) return;
		
		User user = getUser(username);
		user.getLists().clear();
		log.info("Removed all lists from '" + username + "'.");
	}
	
	@Override
	public Collection<TodoList> getUserLists(String username) {
		if (username == null) throw new UserServiceException("Username cannot be null.");
		
		User user = getUser(username);
		return user.getLists();
	}
	
	@Override
	public void createList(TodoList todoList) {
		if (todoList == null) throw new UserServiceException("Cannot create null list.");
		todoListRepository.save(todoList);
	}
	
	@Override
	public void deleteList(String listName) {
		if (listName == null) throw new UserServiceException("Cannot delete list, because provided name is null.");
		
		TodoList todoList = todoListRepository.findByTitle(listName);
		if (todoList == null) throw new UserServiceException("Cannot delete null list.");
		
		todoListRepository.delete(todoList);
	}
	
	@Override
	public void addElementToList(String listTitle, TodoElement element) {
		if (listTitle == null) throw new UserServiceException("Cannot add element to null list.");
		if (element == null) throw new UserServiceException("Cannot add null element to list.");
		
		TodoList todoList = todoListRepository.findByTitle(listTitle);
		if (todoList == null) throw new UserServiceException("Cannot add element to null list");
		
		todoList.getElements().add(element);
	}
	
	@Override
	public void removeElementFromList(String listTitle, String elementName) {
		if (listTitle == null) throw new UserServiceException("Cannot remove element from null list.");
		if (elementName == null) throw new UserServiceException("Cannot remove null element from list.");
		
		TodoList todoList = todoListRepository.findByTitle(listTitle);
		if (todoList == null) throw new UserServiceException("Cannot remove element from null list");
		
		TodoElement todoElement = todoElementRepository.findByTitle(elementName);
		
		todoList.getElements().remove(todoElement);
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
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		});
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}
	
	private boolean doesUserExist(User user) {
		if (user == null) return false;
		
		return userRepository.existsById(user.getId());
	}
	
	private boolean doesUserExist(String username) {
		if (username == null) return false;
		
		User user = getUser(username);
		return doesUserExist(user);
	}
}
