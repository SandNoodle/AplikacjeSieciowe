package com.noodle.todolist.api.list;

import com.noodle.todolist.api.list.element.TodoElement;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@Table
@ToString
public class TodoList {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String title;
	private String description; // OPTIONAL - TODO: Find if this can be annotated as such
	
	@OneToMany(fetch = FetchType.EAGER)
	private Collection<TodoElement> elements = new ArrayList<>();
}
