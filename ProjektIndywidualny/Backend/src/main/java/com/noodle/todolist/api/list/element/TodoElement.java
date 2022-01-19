package com.noodle.todolist.api.list.element;

import com.noodle.todolist.api.list.TodoList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "elements")
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class TodoElement {
	
	public static final boolean DONE = true;
	public static final boolean NOT_DONE = false;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String title;
	private String description;
	private boolean status;
}
