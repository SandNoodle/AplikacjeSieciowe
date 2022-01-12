package com.noodle.todolist.security.user;

import com.noodle.todolist.api.list.TodoList;
import com.noodle.todolist.security.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String username;
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Role> roles = new ArrayList<>();
	
	// TODO: Annotation - verify?
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<TodoList> lists = new ArrayList<>();
	
}
