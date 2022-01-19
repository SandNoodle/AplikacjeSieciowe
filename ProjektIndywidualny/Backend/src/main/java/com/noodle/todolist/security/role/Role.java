package com.noodle.todolist.security.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noodle.todolist.security.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String roleName;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "userRoles")
	private List<User> users;
}
