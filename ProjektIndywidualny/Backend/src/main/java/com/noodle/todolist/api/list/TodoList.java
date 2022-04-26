package com.noodle.todolist.api.list;

import com.noodle.todolist.api.list.element.TodoElement;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "lists")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String title;
	private String description;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "lists_elements",
			joinColumns = @JoinColumn(name = "lists_id"),
			inverseJoinColumns = @JoinColumn(name = "elements_id")
	)
	private List<TodoElement> todoElements;
	
	public TodoList(TodoList copy) {
		this.id = copy.getId();
		this.title = copy.getTitle();
		this.description = copy.getDescription();
		this.todoElements = copy.getTodoElements();
	}
}
