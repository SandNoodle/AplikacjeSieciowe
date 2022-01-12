package com.noodle.todolist.security.user;

import com.noodle.todolist.api.list.TodoList;
import com.noodle.todolist.api.list.element.TodoElement;
import com.noodle.todolist.security.role.Role;

import java.util.Collection;
import java.util.List;

public interface UserService {
	
	// Users
	User createUser(User user);
	
	User getUser(String username);
	
	void deleteUser(String user);
	
	List<User> getUsers();
	
	// Roles
	Role createRole(Role role);
	
	Role getRole(String name);
	
	void deleteRole(String roleName);
	
	void addRoleToUser(String username, String roleName);
	
	void removeRoleFromUser(String user, String roleName);
	
	// Lists
	void addListToUser(String username, String listTitle);
	
	void removeListFromUser(String username, String listTitle);
	
	void removeAllListsFromUser(String username);
	
	Collection<TodoList> getUserLists(String username);
	
	void createList(TodoList todoList);
	
	void deleteList(String listName);
	
	// Elements
	
	void addElementToList(String listTitle, TodoElement element);
	
	void removeElementFromList(String listTitle, String elementName);
	
}
